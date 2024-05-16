package com.nofrontier.book.dto.v1;

import java.io.Serializable;
import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nofrontier.book.core.validation.Age;

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
@JsonPropertyOrder({"id", "firstName", "lastName", "address", "gender",
		"enabled"})
public class PersonDto extends RepresentationModel<PersonDto>
		implements
			Serializable {

	private static final long serialVersionUID = 1L;

	// @Mapping("id")
	@JsonProperty("id")
	private Long key;

	@NotNull
	@Size(min = 3, max = 255)
	private String firstName;

	@NotNull
	@Size(min = 3, max = 255)
	private String lastName;

	@NotNull
	@Size(min = 9, max = 9)
	private String gender;

	@NotNull
	@Size(min = 11, max = 11)
	@CPF
	private String cpf;

	@NotNull
	@Past
	@Age(min = 18, max = 100)
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate birth;

	@NotNull
	@Size(min = 11, max = 11)
	private String phoneNumber;

	@NotNull
	@Size(min = 11, max = 11)
	private String mobileNumber;

	@Size(min = 11, max = 255)
	private String keyPix;

	private Boolean enabled;
}
