package com.nofrontier.book.api.v1.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nofrontier.book.domain.services.ApiAuthService;
import com.nofrontier.book.dto.v1.requests.RefreshRequest;
import com.nofrontier.book.dto.v1.requests.TokenRequest;
import com.nofrontier.book.dto.v1.responses.TokenResponse;
import com.nofrontier.book.utils.MediaType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = {"http://localhost:8080", "https://nofrontier.com.br"})
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for Managing Authentication")
public class AuthRestController {

    private final ApiAuthService apiAuthService;


	@PostMapping(value = "/token", consumes = {MediaType.APPLICATION_JSON,
	MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}, produces = {
			MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML})
	@Operation(summary = "Authenticates a user and returns a token", description = "Authenticates by passing in a JSON, XML or YML!", tags = {
	"Authentication"}, responses = {
		@ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = TokenResponse.class))),
		@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
		@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
		@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),})
    public ResponseEntity<?> authenticate(@RequestBody @Valid TokenRequest tokenRequest) {
		
		if (checkIfParamsIsNotNull(tokenRequest))
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		
		var token = apiAuthService.authenticate(tokenRequest);
		
		if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
			return token;
	}
	

	@PostMapping(value = "/refresh", consumes = {MediaType.APPLICATION_JSON,
	MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}, produces = {
			MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML})
	@Operation(summary = "Refresh token", description = "Refresh token for authenticated user and returns a token by passing in a JSON, XML or YML!", tags = {
	"Authentication"}, responses = {
		@ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = TokenResponse.class))),
		@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
		@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
		@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),})
    public ResponseEntity<?> reauthenticate(@RequestBody @Valid RefreshRequest refrehRequest) {
		
		if (checkIfParamsIsNotNull(refrehRequest))
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		
		var token = apiAuthService.reauthenticate(refrehRequest);
		
		if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
			return token;
	}	
	

	@PostMapping(value = "/logout", consumes = {MediaType.APPLICATION_JSON,
	MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}, produces = {
			MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML})
	@Operation(summary = "Logout system", description = "Logout system by passing in a JSON, XML or YML!", tags = {
	"Authentication"}, responses = {
		@ApiResponse(description = "Success", responseCode = "200", content = @Content),
		@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
		@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
		@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),})
    public ResponseEntity<Void> logout(@RequestBody @Valid RefreshRequest refrehRequest) {
    	apiAuthService.logout(refrehRequest);
        return new ResponseEntity<>(HttpStatus.RESET_CONTENT);
    }
    
	
	private boolean checkIfParamsIsNotNull(RefreshRequest refrehRequest) {
		return refrehRequest == null;				
	}

	private boolean checkIfParamsIsNotNull(TokenRequest tokenRequest) {
		return tokenRequest == null || tokenRequest.getEmail() == null || tokenRequest.getEmail().isBlank()
				 || tokenRequest.getPassword() == null || tokenRequest.getPassword().isBlank();
	}

}
