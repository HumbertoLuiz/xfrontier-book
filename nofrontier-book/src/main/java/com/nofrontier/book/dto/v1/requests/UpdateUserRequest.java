package com.nofrontier.book.dto.v1.requests;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class UpdateUserRequest {

    @Size(min = 3, max = 255, message = "{size.message}")
    private String completeName;

	@Size(max = 255, message = "{size.message}")
	@Email(message = "{email.invalid}")
    private String email;

    @Size(max = 255, message = "{size.message}")
    private String password;

    @Size(max = 255, message = "{size.message}")
    private String newPassword;

    @Size(max = 255, message = "{size.message}")
    private String passwordConfirmation;


}
