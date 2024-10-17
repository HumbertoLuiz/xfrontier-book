package br.com.xfrontier.book.dto.v1;

import java.io.Serializable;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.Email;
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
public class UpdateUserRequest implements Serializable {

    private static final long serialVersionUID = 1L;

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
