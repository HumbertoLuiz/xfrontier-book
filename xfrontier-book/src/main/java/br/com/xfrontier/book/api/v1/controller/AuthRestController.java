package br.com.xfrontier.book.api.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.xfrontier.book.domain.services.ApiAuthService;
import br.com.xfrontier.book.dto.v1.AccountCredentialsDto;
import br.com.xfrontier.book.utils.MediaType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for Managing Authentication")
public class AuthRestController {

	@Autowired
    ApiAuthService apiAuthService;

	@PostMapping(value = "/signin", consumes = {MediaType.APPLICATION_JSON,
	MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}, produces = {
			MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML})
	@Operation(summary = "Authenticates a user and returns a token", description = "Authenticates by passing in a JSON, XML or YML!", tags = {
	"Authentication"}, responses = {
		@ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = AccountCredentialsDto.class))),
		@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
		@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
		@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),})
    public ResponseEntity<?> signin(@RequestBody @Valid AccountCredentialsDto data) {
		
		if (checkIfParamsIsNotNull(data))
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		
		var token = apiAuthService.signin(data);
		
		if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
			return token;
	}
	
	@PutMapping(value = "/refresh/{email}", consumes = {MediaType.APPLICATION_JSON,
	MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}, produces = {
			MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML})
	@Operation(summary = "Refresh token", description = "Refresh token for authenticated user and returns a token by passing in a JSON, XML or YML!", tags = {
	"Authentication"}, responses = {
		@ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = AccountCredentialsDto.class))),
		@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
		@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
		@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),})
    public ResponseEntity<?> refreshToken(@PathVariable("email") String email,
	@RequestHeader("Authorization") String refreshToken) {
		
		if (checkIfParamsIsNotNull(email, refreshToken))
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		
		var token = apiAuthService.refreshToken(email, refreshToken);
		
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
    public ResponseEntity<Void> logout(@RequestBody @Valid String refreshToken) {
    	apiAuthService.logout(refreshToken);
        return new ResponseEntity<>(HttpStatus.RESET_CONTENT);
    }
    
	private boolean checkIfParamsIsNotNull(String email, String refreshToken) {
		return refreshToken == null || refreshToken.isBlank() ||
				email == null || email.isBlank();
	}

	private boolean checkIfParamsIsNotNull(AccountCredentialsDto data) {
		return data == null || data.getEmail() == null || data.getEmail().isBlank()
				 || data.getPassword() == null || data.getPassword().isBlank();
	}

}
