package com.nofrontier.book.dto.v1.requests;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nofrontier.book.domain.model.OrderItem;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotEmpty(message = "Code is mandatory")
    private String code;

    @NotNull(message = "Subtotal is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "Subtotal must be greater than zero")
    private BigDecimal subtotal;

    @NotNull(message = "Shipping rate is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "Shipping rate must be greater than zero")
    private BigDecimal shippingRate;

    @NotNull(message = "Total value is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total value must be greater than zero")
    private BigDecimal totalValue;

    @NotNull(message = "Shipping address ID is mandatory")
    private Long shippingAddressId;

    @NotNull(message = "Payment method ID is mandatory")
    private Long paymentMethodId;

    @NotNull(message = "Customer ID is mandatory")
    private Long customerId;

    private OffsetDateTime confirmationDate;
    private OffsetDateTime cancellationDate;
    private OffsetDateTime deliveryDate;

    private List<OrderItem> items;
}
