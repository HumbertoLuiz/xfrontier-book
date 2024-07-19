package com.nofrontier.book.dto.v1.requests;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nofrontier.book.core.validation.Age;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class PersonRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	@NotBlank(message = "{not.blank.message}")
	@Size(min = 3, max = 255, message = "{size.message}")
	private String firstName;

	@NotBlank(message = "{not.blank.message}")
	@Size(min = 3, max = 255, message = "{size.message}")
	private String lastName;

	@NotBlank(message = "{not.blank.message}")
	@Size(min = 4, max = 6, message = "{size.message}")
	private String gender;

	@NotNull(message = "{not.null.message}")
	@Size(min = 11, max = 11, message = "{size.message}")
	@CPF(message = "{cpf.invalid}")
	private String cpf;

	@NotNull(message = "{not.null.message}")
	@Past(message = "{past.message}")
	@Age(min = 18, max = 100, message = "{size.message}")
	@DateTimeFormat(iso = ISO.DATE, pattern = "{date.invalid}")
	private LocalDate birth;

	@NotBlank(message = "{not.blank.message}")
	@Size(min = 10, max = 11, message = "{size.message}")
	private String phoneNumber;

	@NotBlank(message = "{not.blank.message}")
	@Size(min = 11, max = 11, message = "{size.message}")
	private String mobileNumber;

	@NotBlank(message = "{not.blank.message}")
	@Size(min = 11, max = 255, message = "{size.message}")
	private String keyPix;

	@NotNull(message = "{not.null.message}")
	private Boolean enabled;

	//@NotEmpty(message = "{not.empty.message}") // Removendo a anotação @NotEmpty para tornar campo opcional
	private Set<UserRequest> users = new HashSet<>();

	//@NotEmpty(message = "{not.empty.message}") // Removendo a anotação @NotEmpty para tornar campo opcional
	private Set<AddressRequest> addresses = new HashSet<>();
	
    @Override
    public String toString() {
        return "PersonRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", cpf='" + cpf + '\'' +
                ", birth=" + birth +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", keyPix='" + keyPix + '\'' +
                ", enabled=" + enabled +
                ", users=" + users +
                ", addresses=" + addresses +
                '}';
    }
}
