package com.nofrontier.book.domain.services;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

import com.nofrontier.book.core.services.PasswordResetService;
import com.nofrontier.book.core.services.email.adapters.EmailService;
import com.nofrontier.book.core.services.email.dtos.EmailParams;
import com.nofrontier.book.domain.exceptions.PasswordDoesntMatchException;
import com.nofrontier.book.dto.v1.requests.ResetPasswordConfirmationRequest;
import com.nofrontier.book.dto.v1.requests.ResetPasswordRequest;
import com.nofrontier.book.dto.v1.responses.MessageResponse;

@Service
public class ApiResetPasswordService {

	@Autowired
	private PasswordResetService passwordResetService;

	@Autowired
	private EmailService emailService;

	@Value("${com.nofrontier.book.frontend.host}")
	private String hostFrontend;

	public MessageResponse requestPasswordReset(
			ResetPasswordRequest resetPasswordRequest) {
		var passwordReset = passwordResetService
				.createPasswordReset(resetPasswordRequest.getEmail());

		if (passwordReset != null) {
			var props = new HashMap<String, Object>();
			props.put("link", hostFrontend + "/recover-password?token="
					+ passwordReset.getToken());
			var emailParams = EmailParams.builder()
					.addressee(resetPasswordRequest.getEmail())
					.subject("Password reset request")
					.template("emails/reset-password").props(props).build();
			emailService.sendMailTemplateHtml((EmailParams) emailParams);
		}

		return new MessageResponse(
				"Check your e-mail for the password reset link");
	}

	public MessageResponse confirmPasswordReset(
			ResetPasswordConfirmationRequest request) {
		validateConfirmationPassword(request);
		passwordResetService.resetPassword(request.getToken(),
				request.getPassword());
		return new MessageResponse("Password changed successfully!");
	}

	private void validateConfirmationPassword(
			ResetPasswordConfirmationRequest request) {
		var password = request.getPassword();
		var confirmationPassword = request.getPasswordConfirmation();

		if (!password.equals(confirmationPassword)) {
			var message = "The two password fields don't match";
			var fieldError = new FieldError(request.getClass().getName(),
					"confirmationPassword", request.getPasswordConfirmation(),
					false, null, null, message);
			throw new PasswordDoesntMatchException(message, fieldError);
		}
	}

}
