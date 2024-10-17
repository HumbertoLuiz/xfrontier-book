package br.com.xfrontier.book.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import br.com.xfrontier.book.core.enums.BookStatus;
import br.com.xfrontier.book.core.enums.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@Table(name = "payment_method")
public class PaymentMethod extends Auditable implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	@ToString.Include
	private Long id;

	@Column(nullable = false)
	private String description;
	
    @Column(nullable = false)
    private BigDecimal value;

	@Column(name = "payment_status", nullable = false)
	@Enumerated(EnumType.STRING)
	private PaymentStatus paymentStatus;
	
	@Column(name = "book_status", nullable = false)
	@Enumerated(EnumType.STRING)
	private BookStatus bookStatus;

	@Column(name = "transaction_id", nullable = false)
	private String transactionId;

	@Builder.Default
    @ManyToMany(mappedBy = "paymentMethods")
    private Set<Book> books = new HashSet<>();
	
	public boolean isAccepted() {
		return paymentStatus.equals(PaymentStatus.ACCEPTED);
	}

}