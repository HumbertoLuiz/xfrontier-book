package com.nofrontier.book.dto.v1;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nofrontier.book.domain.model.Picture;

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
public class BookDto extends RepresentationModel<BookDto> implements Serializable{

	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long key;
	
	private String title;

	private String author;

	private String isbn;

	private BigDecimal price;

	private Date launchDate;

	private LocalDateTime createDate;

	private LocalDateTime lastModified;

	private Integer createdBy;

	private Integer lastModifiedBy;

	private Boolean active;

	private Picture bookPicture;

}
