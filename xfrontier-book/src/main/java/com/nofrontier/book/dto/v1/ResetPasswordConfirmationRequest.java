package com.nofrontier.book.dto.v1;

import java.io.Serializable;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class ResetPasswordConfirmationRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "{not.blank.message}")
	@Size(max = 255, message = "{size.message}")
	@Email(message = "{email.invalid}")
    private String email;

	@NotBlank(message = "{not.blank.message}")
	@Size(min = 6, message = "{size.message}")
	private String password;

	@NotBlank(message = "{not.blank.message}")
	private String passwordConfirmation;

	@NotBlank(message = "{not.blank.message}")
    private String token;

}