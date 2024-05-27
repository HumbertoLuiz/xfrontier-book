package com.nofrontier.book.dto.v1.requests;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class OrderItemRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull(message = "{not.null.message}")
	@DecimalMin(value = "0.0", inclusive = false, message = "{positive.message}")
	private BigDecimal unitPrice;

	@NotNull(message = "{not.null.message}")
	@PositiveOrZero(message = "{positiveOrZero.message}")
	private Integer quantity;

	private String observation;

	@NotNull(message = "{not.null.message}")
	private Long orderId;

	@NotNull(message = "{not.null.message}")
	private Long bookId;
}
