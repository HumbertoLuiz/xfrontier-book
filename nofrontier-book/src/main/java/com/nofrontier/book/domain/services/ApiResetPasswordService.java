package com.nofrontier.book.domain.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;

import com.nofrontier.book.api.v1.controller.ResetPasswordRestController;
import com.nofrontier.book.core.services.PasswordResetService;
import com.nofrontier.book.core.services.email.adapters.EmailService;
import com.nofrontier.book.core.services.email.dtos.EmailParams;
import com.nofrontier.book.domain.exceptions.PasswordDoesntMatchException;
import com.nofrontier.book.dto.v1.ResetPasswordConfirmationRequest;
import com.nofrontier.book.dto.v1.ResetPasswordRequest;
import com.nofrontier.book.dto.v1.MessageResponse;

@Service
public class ApiResetPasswordService {

	private final PasswordResetService passwordResetService;
	private final EmailService emailService;
	private final String hostFrontend;

	public ApiResetPasswordService(
			PasswordResetService passwordResetService,
			EmailService emailService,
			@Value("${com.nofrontier.frontend.host}") 
			String hostFrontend) {
		this.passwordResetService = passwordResetService;
		this.emailService = emailService;
		this.hostFrontend = hostFrontend;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public EntityModel<MessageResponse> resetPasswordRequest(
			ResetPasswordRequest resetpasswordRequest) {
		var passwordReset = passwordResetService
				.createPasswordReset(resetpasswordRequest.getEmail());

		if (passwordReset != null) {
			var props = new HashMap<String, Object>();
			props.put("link", hostFrontend + "/recover-password?token="
					+ passwordReset.getContent().getToken());
			var emailParams = EmailParams.builder()
					.addressee(resetpasswordRequest.getEmail())
					.subject("Password reset request")
					.template("emails/reset-password").props(props).build();
			emailService.sendMailTemplateHtml(emailParams);

			MessageResponse response = new MessageResponse(
					"Check your e-mail for the password reset link");

			// Add links HAL
			EntityModel<MessageResponse> resource = EntityModel.of(response);
			Link selfLink = linkTo(methodOn(ResetPasswordRestController.class)
					.requestPasswordReset(resetpasswordRequest)).withSelfRel();
			resource.add(selfLink);

			return resource;
		}

		MessageResponse response = new MessageResponse("Email not found");
		EntityModel<MessageResponse> resource = EntityModel.of(response);
		Link selfLink = linkTo(methodOn(ResetPasswordRestController.class)
				.requestPasswordReset(resetpasswordRequest)).withSelfRel();
		resource.add(selfLink);

		return resource;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public EntityModel<MessageResponse> resetPasswordConfirm(
			ResetPasswordConfirmationRequest resetPasswordConfirmationRequest) {
		validatePasswordConfirmation(resetPasswordConfirmationRequest);
		passwordResetService.resetPassword(
				resetPasswordConfirmationRequest.getToken(),
				resetPasswordConfirmationRequest.getPassword());

		MessageResponse response = new MessageResponse(
				"Password changed successfully!");

		// Add links HAL
		EntityModel<MessageResponse> resource = EntityModel.of(response);
		Link selfLink = linkTo(methodOn(ResetPasswordRestController.class)
				.confirmPasswordReset(resetPasswordConfirmationRequest))
				.withSelfRel();
		resource.add(selfLink);

		return resource;
	}

	// -------------------------------------------------------------------------------------------------------------

	private void validatePasswordConfirmation(
			ResetPasswordConfirmationRequest resetPasswordConfirmationRequest) {
		var password = resetPasswordConfirmationRequest.getPassword();
		var confirmationPassword = resetPasswordConfirmationRequest
				.getPasswordConfirmation();

		if (!password.equals(confirmationPassword)) {
			var message = "The two password fields don't match";
			var fieldError = new FieldError(
					resetPasswordConfirmationRequest.getClass().getName(),
					"confirmationPassword",
					resetPasswordConfirmationRequest.getPasswordConfirmation(),
					false, null, null, message);
			throw new PasswordDoesntMatchException(message, fieldError);
		}
	}

}
