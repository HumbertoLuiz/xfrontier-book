package com.nofrontier.book.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.domain.AbstractAggregateRoot;

import com.nofrontier.book.core.enums.StatusOrder;
import com.nofrontier.book.domain.event.OrderCanceledEvent;
import com.nofrontier.book.domain.event.OrderConfirmedEvent;
import com.nofrontier.book.domain.exceptions.BusinessException;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
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
public class Order extends AbstractAggregateRoot<Order>
		implements
			Serializable {

	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String code;

	private BigDecimal subtotal;
	private BigDecimal shippingRate;
	private BigDecimal totalValue;

	@Embedded
	private Address shippingAddress;

	@Enumerated(EnumType.STRING)
	private StatusOrder status = StatusOrder.CREATED;

	@CreationTimestamp
	private OffsetDateTime dateCreated;

	private OffsetDateTime dataConfirmacao;
	private OffsetDateTime dataCancelamento;
	private OffsetDateTime dataEntrega;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private PaymentMethod paymentMethod;

	@ManyToOne
	@JoinColumn(nullable = false)
	private Book book;

	@ManyToOne
	@JoinColumn(name = "user_customer_id", nullable = false)
	private User customer;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItem> items = new ArrayList<>();

	public void calculateTotalValue() {
		getItems().forEach(OrderItem::calculateTotalPrice);

		this.subtotal = getItems().stream().map(item -> item.getTotalPrice())
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		this.totalValue = this.subtotal.add(this.shippingRate);
	}

	public void confirm() {
		setStatus(StatusOrder.CONFIRMED);
		setDataConfirmacao(OffsetDateTime.now());

		registerEvent(new OrderConfirmedEvent(this));
	}

	public void deliver() {
		setStatus(StatusOrder.DELIVERED);
		setDataEntrega(OffsetDateTime.now());
	}

	public void cancel() {
		setStatus(StatusOrder.CANCELED);
		setDataCancelamento(OffsetDateTime.now());

		registerEvent(new OrderCanceledEvent(this));
	}

	public boolean canBeConfirmed() {
		return getStatus().canChangeTo(StatusOrder.CONFIRMED);
	}

	public boolean podeSerEntregue() {
		return getStatus().canChangeTo(StatusOrder.DELIVERED);
	}

	public boolean canBeCanceled() {
		return getStatus().canChangeTo(StatusOrder.CANCELED);
	}

	private void setStatus(StatusOrder newStatus) {
		if (getStatus().cannotChangeTo(newStatus)) {
			throw new BusinessException(String.format(
					"Order status %s cannot be changed from %s to %s",
					getCode(), getStatus().getDescription(),
					newStatus.getDescription()));
		}

		this.status = newStatus;
	}

	@PrePersist
	private void generateCode() {
		setCode(UUID.randomUUID().toString());
	}

}
