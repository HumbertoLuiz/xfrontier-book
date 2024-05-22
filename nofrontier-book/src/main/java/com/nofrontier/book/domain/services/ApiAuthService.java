package com.nofrontier.book.domain.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.nofrontier.book.core.services.TokenBlackListService;
import com.nofrontier.book.core.services.token.adapters.TokenService;
import com.nofrontier.book.dto.v1.requests.RefreshRequest;
import com.nofrontier.book.dto.v1.requests.TokenRequest;
import com.nofrontier.book.dto.v1.responses.TokenResponse;

@Service
public class ApiAuthService {

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


    public TokenResponse authenticate(TokenRequest tokenRequest) {
        var email = tokenRequest.getEmail();
        var password = tokenRequest.getPassword();

        var authentication = new UsernamePasswordAuthenticationToken(email, password);
        authenticationManager.authenticate(authentication);

        var access = tokenService.generateAccessToken(email);
        var refresh = tokenService.generateRefreshToken(email);

        return new TokenResponse(access, refresh);
    }

    public TokenResponse reauthenticate(RefreshRequest refrehRequest) {
        var token = refrehRequest.getRefresh();
        tokenBlackListService.checkToken(token);

        var email = tokenService.getSubjectDoRefreshToken(token);

        userDetailsService.loadUserByUsername(email);

        var access = tokenService.generateAccessToken(email);
        var refresh = tokenService.generateRefreshToken(email);

        tokenBlackListService.putTokenOnBlackList(token);

        return new TokenResponse(access, refresh);
    }

    public void logout(RefreshRequest refrehRequest) {
        var token = refrehRequest.getRefresh();
        tokenBlackListService.putTokenOnBlackList(token);
    }
}