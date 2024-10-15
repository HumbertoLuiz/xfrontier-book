package com.nofrontier.book.dto.v1;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nofrontier.book.core.enums.BookStatus;
import com.nofrontier.book.core.enums.PaymentStatus;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
@JsonPropertyOrder({"id", "createdAt", "description", "value", "paymentStatus", "bookStatus", "transactionId"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentMethodDto extends RepresentationModel<PaymentMethodDto>
		implements
			Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long key;
	
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
	@NotBlank(message = "{not.blank.message}")
	@Size(min = 3, max = 255, message = "{size.message}")
	private String description;

	@NotNull(message = "{not.null.message}")
	@DecimalMin(value = "0.0", inclusive = false, message = "{positive.message}")
	private BigDecimal value;

	@NotNull(message = "{not.null.message}")
	private PaymentStatus paymentStatus;

	@NotNull(message = "{not.null.message}")
	private BookStatus bookStatus;

	@NotBlank(message = "{not.blank.message}")
	@Size(max = 255, message = "{size.message}")
	private String transactionId;	

}