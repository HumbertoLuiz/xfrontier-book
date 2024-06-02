package com.nofrontier.book.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.domain.AbstractAggregateRoot;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nofrontier.book.core.enums.OrderStatus;
import com.nofrontier.book.core.events.OrderCancelledEvent;
import com.nofrontier.book.core.events.OrderConfirmedEvent;
import com.nofrontier.book.domain.exceptions.BusinessException;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
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
@Table(name = "order_entity")
public class Order extends AbstractAggregateRoot<Order>
		implements
			Serializable {

	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String code;

	@Column(nullable = false)
	private BigDecimal subtotal;
	
	@Column(nullable = false)
	private BigDecimal shippingRate;
	
	@Column(nullable = false)
	private BigDecimal totalValue;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shipping_address_id", nullable = false)
	private Address shippingAddress;

	@Column(name = "order_status", nullable = false)
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	@CreationTimestamp
	@Column(nullable = false, columnDefinition = "datetime")
	private OffsetDateTime creationDate;

	@Column(nullable = false, columnDefinition = "datetime")
	private OffsetDateTime confirmationDate;
	
	@Column(nullable = false, columnDefinition = "datetime")
	private OffsetDateTime cancellationDate;
	
	@Column(nullable = false, columnDefinition = "datetime")
	private OffsetDateTime deliveryDate;
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "payment_method_id", nullable = false)
	private PaymentMethod paymentMethod;

	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "order_payment_method", joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "payment_method_id"))
	private Set<PaymentMethod> paymentMethods = new HashSet<>();

	@ManyToMany(fetch = FetchType.EAGER, cascade = (CascadeType.ALL))
    @JoinTable(name = "order_book",
               joinColumns = @JoinColumn(name = "order_id"),
               inverseJoinColumns = @JoinColumn(name = "book_id"))
    private Set<Book> books = new HashSet<>();

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_customer_id", nullable = false)
	private User customer;

	@JsonManagedReference
	@OneToMany(mappedBy = "order",  fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<OrderItem> items = new ArrayList<>();

	public void calculateTotalValue() {
		getItems().forEach(OrderItem::calculateTotalPrice);

		this.subtotal = getItems().stream().map(item -> item.getTotalPrice())
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		this.totalValue = this.subtotal.add(this.shippingRate);
	}

	public void confirm() {
		setStatus(OrderStatus.CONFIRMED);
		setConfirmationDate(OffsetDateTime.now());

		registerEvent(new OrderConfirmedEvent(this));
	}

	public void deliver() {
		setStatus(OrderStatus.DELIVERED);
		setDeliveryDate(OffsetDateTime.now());
	}

	public void cancel() {
		setStatus(OrderStatus.CANCELLED);
		setCancellationDate(OffsetDateTime.now());

		registerEvent(new OrderCancelledEvent(this));
	}

	public boolean canBeConfirmed() {
		return getOrderStatus().canChangeTo(OrderStatus.CONFIRMED);
	}

	public boolean podeSerEntregue() {
		return getOrderStatus().canChangeTo(OrderStatus.DELIVERED);
	}

	public boolean canBeCanceled() {
		return getOrderStatus().canChangeTo(OrderStatus.CANCELLED);
	}

	private void setStatus(OrderStatus newStatus) {
		if (getOrderStatus().cannotChangeTo(newStatus)) {
			throw new BusinessException(String.format(
					"Order status %s cannot be changed from %s to %s",
					getCode(), getOrderStatus().getDescription(),
					newStatus.getDescription()));
		}

		this.orderStatus = newStatus;
	}

	@PrePersist
	private void generateCode() {
		setCode(UUID.randomUUID().toString());
	}

}
