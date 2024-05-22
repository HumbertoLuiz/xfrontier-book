package com.nofrontier.book.dto.v1.requests;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nofrontier.book.domain.model.Book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class ProductRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank
	private String description;
	
	@NotBlank
	private String format;

	@NotBlank
	private String edition;

	@NotNull
	@Positive
	private BigDecimal price;

	@NotEmpty
	private Boolean active;

	private Book book;

}
