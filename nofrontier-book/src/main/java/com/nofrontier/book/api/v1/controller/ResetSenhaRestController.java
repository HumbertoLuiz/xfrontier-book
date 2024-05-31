package com.nofrontier.book.api.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nofrontier.book.domain.services.ApiResetPasswordService;
import com.nofrontier.book.dto.v1.requests.ResetPasswordConfirmationRequest;
import com.nofrontier.book.dto.v1.requests.ResetPasswordRequest;
import com.nofrontier.book.dto.v1.responses.MessageResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/recover-password/v1")
public class ResetSenhaRestController {

	@Autowired
	private ApiResetPasswordService apiResetPasswordService;

	@PostMapping
	public MessageResponse requestPasswordReset(
			@RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {
		return apiResetPasswordService
				.requestPasswordReset(resetPasswordRequest);
	}

	@PostMapping("/confirm")
	public MessageResponse confirmPasswordReset(
			@RequestBody @Valid ResetPasswordConfirmationRequest request) {
		return apiResetPasswordService.confirmPasswordReset(request);
	}

}
