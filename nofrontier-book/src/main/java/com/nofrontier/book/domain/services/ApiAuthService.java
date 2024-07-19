package com.nofrontier.book.domain.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nofrontier.book.core.services.TokenBlackListService;
import com.nofrontier.book.core.services.token.adapters.TokenService;
import com.nofrontier.book.dto.v1.requests.RefreshRequest;
import com.nofrontier.book.dto.v1.requests.TokenRequest;
import com.nofrontier.book.dto.v1.responses.TokenResponse;

@Service
public class ApiAuthService {

    private static final Logger log = LoggerFactory.getLogger(ApiAuthService.class);

    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final TokenBlackListService tokenBlackListService;

    public ApiAuthService(TokenService tokenService,
                          UserDetailsService userDetailsService,
                          AuthenticationManager authenticationManager,
                          TokenBlackListService tokenBlackListService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.tokenBlackListService = tokenBlackListService;
    }

    @Transactional
    public ResponseEntity<TokenResponse> authenticate(TokenRequest tokenRequest) {
        try {
            String username = tokenRequest.getEmail();
            String password = tokenRequest.getPassword();

            UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(username, password);

            // Autentica o usuário
            log.info("Trying to authenticate the user: {}", username);
            authenticationManager.authenticate(authentication);
            log.info("Successful authentication for the user: {}", username);

            // Gera tokens de acesso e atualização
            String access = tokenService.generateAccessToken(username);
            String refresh = tokenService.generateRefreshToken(username);
            log.info("Tokens generated for the user: {}", username);

            // Cria a resposta com os tokens
            TokenResponse tokenResponse = new TokenResponse(access, refresh);

            return ResponseEntity.ok(tokenResponse);
        } catch (BadCredentialsException e) {
            // Loga a exceção de credenciais inválidas
            log.error("Authentication failed for the user: " + tokenRequest.getEmail(), e);
            throw new BadCredentialsException("Invalid username/password supplied!", e);
        } catch (Exception e) {
            // Loga qualquer outra exceção que ocorra
            log.error("Unexpected error during authentication", e);
            throw new RuntimeException("Unexpected error during authentication", e);
        }
    }

    @Transactional
    public ResponseEntity<TokenResponse> reauthenticate(RefreshRequest refreshRequest) {
        try {
            var token = refreshRequest.getRefresh();
            tokenBlackListService.checkToken(token);

            var email = tokenService.getSubjectDoRefreshToken(token);

            // Carregar detalhes do usuário para confirmar que o usuário existe
            var userDetails = userDetailsService.loadUserByUsername(email);
            if (userDetails == null) {
                throw new UsernameNotFoundException("Email " + email + " not found!");
            }

            var access = tokenService.generateAccessToken(email);
            var refresh = tokenService.generateRefreshToken(email);

            tokenBlackListService.putTokenOnBlackList(token);

            var tokenResponse = new TokenResponse(access, refresh);

            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid refresh token supplied!", e);
        }
    }

    @Transactional
    public void logout(RefreshRequest refreshRequest) {
        var token = refreshRequest.getRefresh();
        tokenBlackListService.putTokenOnBlackList(token);
    }
}
