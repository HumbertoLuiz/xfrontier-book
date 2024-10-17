package br.com.xfrontier.book.dto.v1;

import java.io.Serializable;

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
public class ResetPasswordRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "{not.blank.message}")
	@Size(max = 255, message = "{size.message}")
	@Email(message = "{email.invalid}")
    private String email;

}
