package com.nofrontier.book.dto.v1;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nofrontier.book.domain.model.Order;
import com.nofrontier.book.domain.model.PaymentMethod;
import com.nofrontier.book.domain.model.Product;
import com.nofrontier.book.domain.model.User;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
@JsonPropertyOrder({ "id", "author", "launchDate", "price", "title" })
public class BookDto extends RepresentationModel<BookDto> implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long key;
	
	@NotNull
	@Size(min = 3, max = 255)
	private String title;

	@NotNull
	@Size(min = 3, max = 255)
	private String author;

    @NotNull
    @Size(min = 13, max = 13)
	private String isbn;

    @NotNull
    @Future
	private Date launchDate;

	private OffsetDateTime registrationDate;

	private OffsetDateTime updateDate;

	private Integer createdBy;

	private Integer lastModifiedBy;

	private Boolean active;

	private Order order;

	private Set<PaymentMethod> paymentMethods = new HashSet<>();

	private Set<User> responsible = new HashSet<>();

	private Set<Product> products = new HashSet<>();
}
