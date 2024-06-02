package com.nofrontier.book.dto.v1.responses;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nofrontier.book.domain.model.IdBaseEntity;
import com.nofrontier.book.domain.model.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
@JsonPropertyOrder({"id", "unitPrice", "totalPrice", "quantity", "observation",
		"order", "book"})
public class OrderItemResponse extends RepresentationModel<OrderItemResponse>
		implements
			Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long key;

	private BigDecimal unitPrice;
	private BigDecimal totalPrice;
	private Integer quantity;
	private String observation;
	private OrderResponse order;
	private BookResponse book;
	
	public IdBaseEntity getProduct() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setProduct(Product product) {
		// TODO Auto-generated method stub
		
	}
}
