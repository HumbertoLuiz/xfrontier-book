package com.nofrontier.book.dto.v1.responses;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nofrontier.book.core.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
@JsonPropertyOrder({"id", "code", "subtotal", "shippingRate", "totalValue",
		"shippingAddress", "orderStatus", "creationDate", "confirmationDate",
		"cancellationDate", "deliveryDate", "paymentMethod", "customer",
		"items"})
public class OrderResponse extends RepresentationModel<OrderResponse>
		implements
			Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long key;
	private String code;
	private BigDecimal subtotal;
	private BigDecimal shippingRate;
	private BigDecimal totalValue;
	private AddressResponse shippingAddress;
	private OrderStatus orderStatus;
	private OffsetDateTime creationDate;
	private OffsetDateTime confirmationDate;
	private OffsetDateTime cancellationDate;
	private OffsetDateTime deliveryDate;
	private PaymentMethodResponse paymentMethod;
	private UserResponse customer;
	private List<OrderItemResponse> items = new ArrayList<>();
	
	public Long getBooks() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setBooks(BookResponse book) {
		// TODO Auto-generated method stub
		
	}

	public void setCustomer(EntityModel<UserResponse> customer2) {
		// TODO Auto-generated method stub
		
	}

	public void setShippingRate(Long books) {
		// TODO Auto-generated method stub
		
	}

	public void calculateTotalValue() {
		// TODO Auto-generated method stub
		
	}
}
