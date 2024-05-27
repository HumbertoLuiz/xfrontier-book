package com.nofrontier.book.dto.v1.requests;

import java.io.Serializable;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nofrontier.book.core.enums.AddressType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

	@NotBlank(message = "{not.blank.message}")
	@Size(max = 64, message = "{size.message}")
	private String street;

	@NotBlank(message = "{not.blank.message}")
	@Size(max = 10, message = "{size.message}")
	private String number;

	@NotBlank(message = "{not.blank.message}")
	@Size(max = 30, message = "{size.message}")
	private String neighborhood;

	private String complement;

	@NotBlank(message = "{not.blank.message}")
	@Size(max = 9, message = "{size.message}")
	private String zipCode;

	@NotNull(message = "{not.null.message}")
	private AddressType addressType;

	@NotNull(message = "{not.null.message}")
	private Long cityId;
	
	@NotNull(message = "{not.null.message}")
	private Long personId;	
}
