package com.nofrontier.book.dto.v1;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nofrontier.book.core.enums.AddressType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
@JsonNaming(SnakeCaseStrategy.class)
@JsonPropertyOrder({"id", "street", "number", "neighborhood", "complement",
		"zipCode", "addressType", "city_id", "person_id"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDto extends RepresentationModel<AddressDto>
		implements
			Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long key;

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
	
	@JsonIgnore
	private CityDto city;

	@JsonProperty("city_id")
	@NotNull(message = "{not.null.message}")
	private Long cityId;
	
	@JsonProperty("person_id")
	@NotNull(message = "{not.null.message}")
	private Long personId;
	

	public Boolean isCommercial() {
		return addressType.equals(AddressType.COMMERCIAL);
	}

}
