package com.nofrontier.book.integrationstests.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nofrontier.book.core.enums.BookStatus;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlRootElement
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(SnakeCaseStrategy.class)
public class BookDto implements Serializable {

	private static final long serialVersionUID = 1L;

    private Long id;
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
	private BigDecimal shippingRate;
    private BigDecimal price;
    private String observation;
    private String reasonCancellation;
	private Long categoryId;
}