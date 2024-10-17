package br.com.xfrontier.book.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "order_item")
public class OrderItem extends IdBaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "unit_price", nullable = false)
	private BigDecimal unitPrice;
	
	@Column(name = "total_price", nullable = false)
	private BigDecimal totalPrice;
	
	@Column(name = "quantity", nullable = false)
	private Integer quantity;
	
	@Column(name = "observation", nullable = false)
	private String observation;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(nullable = false, name = "order_id")
	private Order order;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(nullable = false, name = "book_id")
	private Book book;
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "product_id")
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
