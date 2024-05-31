package com.nofrontier.book.api.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nofrontier.book.domain.services.ApiAuthService;
import com.nofrontier.book.dto.v1.requests.RefreshRequest;
import com.nofrontier.book.dto.v1.requests.TokenRequest;
import com.nofrontier.book.dto.v1.responses.TokenResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthRestController {

    @Autowired
    private ApiAuthService service;

    @PostMapping("/token")
    public TokenResponse authenticate(@RequestBody @Valid TokenRequest tokenRequest) {
        return service.authenticate(tokenRequest);
    }

    @PostMapping("/refresh")
    public TokenResponse reauthenticate(@RequestBody @Valid RefreshRequest refrehRequest) {
        return service.reauthenticate(refrehRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody @Valid RefreshRequest refrehRequest) {
        service.logout(refrehRequest);
        return new ResponseEntity<>(HttpStatus.RESET_CONTENT);
    }

}
