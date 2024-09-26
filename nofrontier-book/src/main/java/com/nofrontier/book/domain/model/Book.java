package com.nofrontier.book.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nofrontier.book.core.enums.BookStatus;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "books")
public class Book extends IdBaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 250)
	private String title;

	@Column(nullable = false, length = 180)
	private String author;

	@Column(nullable = false, unique = true, length = 13)
	private String isbn;

	@Column(name = "launch_date", nullable = false)
	private LocalDate launchDate;

	@CreationTimestamp
	@Column(name = "registration_date", nullable = false, columnDefinition = "datetime")
	private OffsetDateTime registrationDate;

	@UpdateTimestamp
	@Column(name = "update_date", nullable = false, columnDefinition = "datetime")
	private OffsetDateTime updateDate;

	@CreatedBy
	@Column(name = "created_by", nullable = false, updatable = false)
	private Integer createdBy;

	@LastModifiedBy
	@Column(name = "last_modified_by", insertable = false)
	private Integer lastModifiedBy;

	@Column(nullable = false)
	private Boolean active;

	@Column(name = "book_status", nullable = false)
	@Enumerated(EnumType.STRING)
	private BookStatus bookStatus;
	
	@Column(name = "shipping_rate", nullable = false)
	private BigDecimal shippingRate;
	
    @Column(nullable = false)
    private BigDecimal price;
	
    @Column(nullable = true)
    private String observation;

    @Column(name = "reason_cancellation" ,nullable = true)
    private String reasonCancellation;

	@JsonIgnore
	@ManyToMany(mappedBy = "books")
	private Set<Order> orders = new HashSet<>();

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;
	
	@ManyToMany
	@JoinTable(name = "book_payment_method", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "payment_method_id"))
	private Set<PaymentMethod> paymentMethods = new HashSet<>();

	@ManyToMany
	@JoinTable(name = "book_user_responsible", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> responsible = new HashSet<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	private Set<Product> products = new HashSet<>();

	public Boolean isWithoutPayment() {
		return bookStatus.equals(BookStatus.WITHOUT_PAYMENT);
	}

	public Boolean isPaid() {
		return bookStatus.equals(BookStatus.PAID);
	}

	public Boolean isCancelled() {
		return bookStatus.equals(BookStatus.CANCELLED);
	}

	public Boolean isRated() {
		return bookStatus.equals(BookStatus.RATED);
	}

	public void setPaymentMethods(BookStatus paid) {
		// TODO Auto-generated method stub		
	}
	
	public boolean removePaymentForm(PaymentMethod paymentMethod) {
		return getPaymentMethods().remove(paymentMethod);
	}
	
	public boolean addPaymentForm(PaymentMethod paymentMethod) {
		return getPaymentMethods().add(paymentMethod);
	}
	
	public boolean acceptPaymentForm(PaymentMethod paymentMethod) {
		return getPaymentMethods().contains(paymentMethod);
	}
	
	public boolean doesntAcceptPaymentForm(PaymentMethod paymentMethod) {
		return !acceptPaymentForm(paymentMethod);
	}
	
	public boolean removeResponsible(User user) {
		return getResponsible().remove(user);
	}
	
	public boolean addResponsible(User user) {
		return getResponsible().add(user);
	}

}
