package com.nofrontier.book.dto.v1;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nofrontier.book.domain.model.IdBaseEntity;
import com.nofrontier.book.domain.model.Product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
@JsonPropertyOrder({"id", "unitPrice", "totalPrice", "quantity", "observation",
		"order", "book"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemDto extends RepresentationModel<OrderItemDto>
		implements
			Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long key;

	@NotNull(message = "{not.null.message}")
	@DecimalMin(value = "0.0", inclusive = false, message = "{positive.message}")
	private BigDecimal unitPrice;
	
	private BigDecimal totalPrice;

	@NotNull(message = "{not.null.message}")
	@PositiveOrZero(message = "{positiveOrZero.message}")
	private Integer quantity;

	private String observation;

	@NotNull(message = "{not.null.message}")
	private Long orderId;
	
	private OrderDto order;

	@NotNull(message = "{not.null.message}")
	private Long bookId;
	
	private BookDto book;
	
	public IdBaseEntity getProduct() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setProduct(Product product) {
		// TODO Auto-generated method stub		
	}
}
