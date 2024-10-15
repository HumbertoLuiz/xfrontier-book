package com.nofrontier.book.dto.v1;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotBlank;
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
@JsonPropertyOrder({"id", "name", "ibgeCode", "state_id", "state", "addresses"})
public class CityDto extends RepresentationModel<CityDto>
		implements
			Serializable {

	private static final long serialVersionUID = 1L;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("id")
	private Long key;
	
	@NotBlank(message = "{not.blank.message}")
	private String name;

	@NotBlank(message = "{not.blank.message}")
	@Size(max = 7, message = "{size.message}")
	private String ibgeCode;

	@JsonProperty("state_id")
	private Long stateId;
	
	private StateDto state;
	
	@JsonIgnore
	private Set<AddressDto> addresses  = new HashSet<>();
}
