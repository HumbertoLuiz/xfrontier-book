package com.nofrontier.book.dto.v1.requests;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nofrontier.book.core.enums.OrderStatus;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class OrderRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "{not.blank.message}")
    private String code;

	@NotNull(message = "{not.null.message}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{positive.message}")
    private BigDecimal subtotal;

	@NotNull(message = "{not.null.message}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{positive.message}")
    private BigDecimal shippingRate;

	@NotNull(message = "{not.null.message}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{positive.message}")
    private BigDecimal totalValue;

	@NotNull(message = "{not.null.message}")
    private Long shippingAddressId;
	
	@NotNull(message = "{not.null.message}")
	private OrderStatus orderStatus = OrderStatus.CREATED;

	@NotNull(message = "{not.null.message}")
    private Long paymentMethodId;

	@NotNull(message = "{not.null.message}")
    private Long customerId;

	@PastOrPresent(message = "{pastOrPresent.message}")
    private OffsetDateTime confirmationDate;
	
	@PastOrPresent(message = "{pastOrPresent.message}")
    private OffsetDateTime cancellationDate;
	
	@Future(message = "{future.message}")
    private OffsetDateTime deliveryDate;

    @NotEmpty(message = "{not.empty.message}")
    private List<OrderItemRequest> items = new ArrayList<>();
}
