package com.nofrontier.book.api.v1.controller;

import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nofrontier.book.core.services.PasswordResetService;
import com.nofrontier.book.domain.services.ApiResetPasswordService;
import com.nofrontier.book.dto.v1.requests.ResetPasswordConfirmationRequest;
import com.nofrontier.book.dto.v1.requests.ResetPasswordRequest;
import com.nofrontier.book.dto.v1.responses.MessageResponse;
import com.nofrontier.book.dto.v1.responses.PasswordResetResponse;
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
@RequestMapping(path = "/api/recover-password/v1", produces = MediaType.APPLICATION_JSON)
@Tag(name = "PasswordReset", description = "Endpoints for Managing PasswordReset")
public class ResetPasswordRestController {

    private final ApiResetPasswordService apiResetPasswordService;
    
    private final PasswordResetService passwordResetService;

	// -------------------------------------------------------------------------------------------------------------

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Finds a PasswordReset", description = "Finds a PasswordReset", tags = {
            "PasswordReset"}, responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = PasswordResetResponse.class))),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),})
    public PasswordResetResponse findById(@PathVariable(value = "id") Long id) {
        return passwordResetService.findById(id);
    }

	// -------------------------------------------------------------------------------------------------------------

    @CrossOrigin(origins = {"http://localhost:8080",
            "https://nofrontier.com.br"})
    @PostMapping(consumes = {MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}, produces = {
                    MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
                    MediaType.APPLICATION_YML})
    @Operation(summary = "Adds a new RequestPasswordReset", description = "Adds a new RequestPasswordReset by passing in a JSON, XML or YML representation of the RequestPasswordReset!", tags = {
            "PasswordReset"}, responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),})
    public EntityModel<MessageResponse> requestPasswordReset(
            @RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {
        return apiResetPasswordService.resetPasswordRequest(resetPasswordRequest);
    }

	// -------------------------------------------------------------------------------------------------------------

    @CrossOrigin(origins = {"http://localhost:8080",
            "https://nofrontier.com.br"})
    @PostMapping(value = "/confirm", consumes = {MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}, produces = {
                    MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
                    MediaType.APPLICATION_YML})
    @Operation(summary = "Adds a new ResetPasswordReset", description = "Adds a new ResetPasswordReset by passing in a JSON, XML or YML representation of the ResetPasswordReset!", tags = {
            "PasswordReset"}, responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),})
    public EntityModel<MessageResponse> confirmPasswordReset(
            @RequestBody @Valid ResetPasswordConfirmationRequest resetPasswordConfirmationRequest) {
        return apiResetPasswordService.resetPasswordConfirm(resetPasswordConfirmationRequest);
    }

	// -------------------------------------------------------------------------------------------------------------

    @GetMapping("/password-reset/{id}")
    public EntityModel<PasswordResetResponse> getPasswordReset(
            @PathVariable Long id) {
        PasswordResetResponse passwordReset = passwordResetService
                .findById(id);
        return EntityModel.of(passwordReset);
    }
}
