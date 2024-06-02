package com.nofrontier.book.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
public class OrderItem extends IdBaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal unitPrice;
	private BigDecimal totalPrice;
	private Integer quantity;
	private String observation;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(nullable = false, name = "order_id")
	private Order order;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(nullable = false, name = "book_id")
	private Book book;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Product product;

	public void calculateTotalPrice() {
		BigDecimal unitPrice = this.getTotalPrice();
		Integer quantity = this.getQuantity();

		if (unitPrice == null) {
			unitPrice = BigDecimal.ZERO;
		}

		if (quantity == null) {
			quantity = 0;
		}

		this.setTotalPrice(unitPrice.multiply(new BigDecimal(quantity)));
	}

}
