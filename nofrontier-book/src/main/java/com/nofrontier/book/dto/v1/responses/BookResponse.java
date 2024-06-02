package com.nofrontier.book.dto.v1.responses;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nofrontier.book.core.enums.BookStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
@JsonPropertyOrder({"id", "title", "author", "isbn", "launchDate",
		"registrationDate", "updateDate", "createdBy", "lastModifiedBy",
		"active", "bookStatus", "price", "observation", "reasonCancellation", "order", "paymentMethods", "responsible", "products"})
public class BookResponse extends RepresentationModel<BookResponse>
		implements
			Serializable {

	private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private Long key;

    private String title;
    private String author;
    private String isbn;
    private Date launchDate;
    private OffsetDateTime registrationDate;
    private OffsetDateTime updateDate;
    private Integer createdBy;
    private Integer lastModifiedBy;
    private Boolean active;
	private BookStatus bookStatus;
    private BigDecimal price;
    private String observation;
    private String reasonCancellation;
    private OrderResponse order;
    private Set<PaymentMethodResponse> paymentMethods = new HashSet<>();
    private Set<UserResponse> responsible = new HashSet<>();
    private Set<ProductResponse> products = new HashSet<>();
 
	public boolean acceptPaymentForm(PaymentMethodResponse paymentMethod) {
		return getPaymentMethods().contains(paymentMethod);
	}
	
	public boolean doesntAcceptPaymentForm(PaymentMethodResponse paymentMethod) {
		return !acceptPaymentForm(paymentMethod);
	}


	
}