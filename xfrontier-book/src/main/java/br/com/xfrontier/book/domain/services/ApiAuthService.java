package br.com.xfrontier.book.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.xfrontier.book.core.services.TokenBlackListService;
import br.com.xfrontier.book.core.services.token.jwt.JwtTokenProvider;
import br.com.xfrontier.book.domain.repository.UserRepository;
import br.com.xfrontier.book.dto.v1.AccountCredentialsDto;
import br.com.xfrontier.book.dto.v1.TokenDto;

@Service
public class ApiAuthService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private UserRepository repository;

	@Autowired
	private TokenBlackListService tokenBlackListService;


	public ResponseEntity<?> signin(AccountCredentialsDto data) {
		try {
			var email = data.getEmail();
			var password = data.getPassword();
			authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(email, password));
			
			var user = repository.findByEmail(email);
			
			var tokenResponse = new TokenDto();
			if (user != null) {
				tokenResponse = tokenProvider.createAccessToken(email, user.get().getRoles());
			} else {
				throw new UsernameNotFoundException("Username " + email + " not found!");
			}
			return ResponseEntity.ok(tokenResponse);
			
		} catch (BadCredentialsException e) {
		    throw new BadCredentialsException("Invalid email/password supplied!");
		} catch (Exception e) {
		    // Registrar e tratar outras exceções
		    throw new RuntimeException("Ocorreu um erro inesperado");
		}
	}
	

	 public ResponseEntity<?> refreshToken(String email, String refreshToken) {
	        // Checks if the token is blacklisted
	        tokenBlackListService.checkToken(refreshToken);

	        var user = repository.findByEmail(email);

	        var tokenResponse = new TokenDto();
	        if (user != null) {
	            // Generates a new access token
	            tokenResponse = tokenProvider.refreshToken(refreshToken);

	            // Add the old token to the blacklist
	            tokenBlackListService.putTokenOnBlackList(refreshToken);
	        } else {
	            throw new UsernameNotFoundException("Username " + email + " not found!");
	        }
	        return ResponseEntity.ok(tokenResponse);
	    }	
	
	public void logout(String refreshToken) {
	    // Validar refreshToken antes de adicionar à lista negra
	    tokenProvider.validateToken(refreshToken);
	    tokenBlackListService.putTokenOnBlackList(refreshToken);
	}

}
