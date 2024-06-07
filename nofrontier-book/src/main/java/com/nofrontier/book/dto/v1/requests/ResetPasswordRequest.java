package com.nofrontier.book.dto.v1.requests;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "{not.blank.message}")
	@Size(max = 255, message = "{size.message}")
	@Email(message = "{email.invalid}")
    private String email;

}
