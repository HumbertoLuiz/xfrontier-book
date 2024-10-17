package br.com.xfrontier.book.dto.v1;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@JsonPropertyOrder({"id", "description", "format", "edition", "price", "active", "book_id"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto extends RepresentationModel<ProductDto>
		implements
			Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long key;

	@NotBlank(message = "{not.blank.message}")
	private String description;

	@NotBlank(message = "{not.blank.message}")
	private String format;

	@NotBlank(message = "{not.blank.message}")
	private String edition;

	@NotNull(message = "{not.null.message}")
	@DecimalMin(value = "0.0", inclusive = false, message = "{positive.message}")
	private BigDecimal price;

	@NotNull(message = "{not.null.message}")
	private Boolean active;
	
	@JsonProperty("book_id")
    @NotNull(message = "{not.null.message}")
	private Long bookId;
	
}
