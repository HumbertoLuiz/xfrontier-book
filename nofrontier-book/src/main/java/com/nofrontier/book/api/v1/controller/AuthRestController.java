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

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("/auth")
public class AuthRestController {

    @Autowired
    private ApiAuthService apiAuthService;

    @PostMapping("/token")
    public TokenResponse authenticate(@RequestBody @Valid TokenRequest toquenRequest) {
        return apiAuthService.authenticate(toquenRequest);
    }

    @PostMapping("/refresh")
    public TokenResponse reauthenticate(@RequestBody @Valid RefreshRequest refreshRequest) {
        return apiAuthService.reauthenticate(refreshRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody @Valid RefreshRequest refreshRequest) {
        apiAuthService.logout(refreshRequest);
        return new ResponseEntity<>(HttpStatus.RESET_CONTENT);
    }
}
