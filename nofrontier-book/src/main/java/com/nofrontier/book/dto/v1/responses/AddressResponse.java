package com.nofrontier.book.dto.v1.responses;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nofrontier.book.core.enums.AddressType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
@JsonPropertyOrder({"id", "street", "number", "neighborhood", "complement", "zipCode",
		"addressType", "city", "person"})
public class AddressResponse extends RepresentationModel<AddressResponse>
		implements
			Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long key;

	private String street;

	private String number;

	private String neighborhood;

	private String complement;

	private String zipCode;

	private AddressType addressType;

	private CityResponse city;
	
	private PersonResponse person;	

	public Boolean isResidential() {
		return addressType.equals(AddressType.RESIDENTIAL);
	}

	public Boolean isCommercial() {
		return addressType.equals(AddressType.COMMERCIAL);
	}
}
