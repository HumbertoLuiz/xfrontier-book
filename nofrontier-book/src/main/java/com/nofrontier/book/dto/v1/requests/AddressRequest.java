package com.nofrontier.book.dto.v1.requests;

import java.io.Serializable;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nofrontier.book.core.enums.AddressType;

import jakarta.validation.constraints.NotBlank;
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
public class AddressRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank
	@Size(max = 64)
	private String street;

	@NotBlank
	@Size(max = 10)
	private String number;

	@NotBlank
	@Size(max = 30)
	private String neighborhood;

	private String complement;

	@NotBlank
	@Size(max = 9)
	private String zipCode;

	@NotBlank
	private AddressType addressType;

	private Long cityId;
	
	private Long personId;	
}
