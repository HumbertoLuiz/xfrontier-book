package com.nofrontier.book.dto.v1.requests;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class ProductRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "{not.blank.message}")
	private String description;
	
	@NotBlank(message = "{not.blank.message}")
	private String format;

	@NotBlank(message = "{not.blank.message}")
	private String edition;

	@NotNull(message = "{not.null.message}")
	@DecimalMin(value = "0.0", inclusive = false, message = "{positive.message}")
	private BigDecimal price;

	@NotEmpty(message = "{not.empty.message}")
	private Boolean active;

	@NotEmpty(message = "{not.empty.message}")
	private Long bookId;

}
