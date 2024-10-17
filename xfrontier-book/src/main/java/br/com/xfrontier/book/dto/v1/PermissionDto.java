package br.com.xfrontier.book.dto.v1;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

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
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
@JsonPropertyOrder({"id", "name", "description"})
public class PermissionDto extends RepresentationModel<PermissionDto>
		implements
			Serializable {

	private static final long serialVersionUID = 1L;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("id")
	private Long key;

	@NotBlank(message = "{not.blank.message}")
	@Size(min = 3, max = 255, message = "{size.message}")
	private String name;

	@Size(max = 255, message = "{size.message}")
	private String description;

}
