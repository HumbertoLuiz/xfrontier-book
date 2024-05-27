package com.nofrontier.book.dto.v1.requests;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class UpdateUserRequest {

	@NotBlank(message = "{not.blank.message}")
    @Size(min = 3, max = 255, message = "{size.message}")
    private String completeName;

	@NotBlank(message = "{not.blank.message}")
	@Size(max = 255, message = "{size.message}")
	@Email(message = "{email.invalid}")
    private String email;

	@NotBlank(message = "{not.blank.message}")
    @Size(max = 255, message = "{size.message}")
    private String password;

	@NotBlank(message = "{not.blank.message}")
    @Size(max = 255, message = "{size.message}")
    private String newPassword;

	@NotBlank(message = "{not.blank.message}")
    @Size(max = 255, message = "{size.message}")
    private String passwordConfirmation;

}
