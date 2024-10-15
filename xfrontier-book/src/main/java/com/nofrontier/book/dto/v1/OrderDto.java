package com.nofrontier.book.dto.v1;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nofrontier.book.core.enums.OrderStatus;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
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
@JsonPropertyOrder({"id", "code", "subtotal", "shipping_rate", "total_value",
        "shipping_address_id", "order_status", "payment_method_id", "user_customer_id"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDto extends RepresentationModel<OrderDto>
        implements
            Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private Long key;

    private String code;

    @NotNull(message = "{not.null.message}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{positive.message}")
    private BigDecimal subtotal;

    @JsonProperty("shipping_rate")
    @NotNull(message = "{not.null.message}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{positive.message}")
    private BigDecimal shippingRate;

    @JsonProperty("total_value")
    @NotNull(message = "{not.null.message}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{positive.message}")
    private BigDecimal totalValue;

    
    @JsonProperty("shipping_address_id")
    @NotNull(message = "{not.null.message}")
    private Long shippingAddressId;

    @JsonIgnore
    private AddressDto shippingAddress;

    
    @JsonProperty("order_status")
    @NotNull(message = "{not.null.message}")
    private OrderStatus orderStatus = OrderStatus.CREATED;

    private OffsetDateTime creationDate;

    @PastOrPresent(message = "{pastOrPresent.message}")
    private OffsetDateTime confirmationDate;

    @PastOrPresent(message = "{pastOrPresent.message}")
    private OffsetDateTime cancellationDate;

    @Future(message = "{future.message}")
    private OffsetDateTime deliveryDate;

    
    @JsonProperty("payment_method_id")
    @NotNull(message = "{not.null.message}")
    private Long paymentMethodId;

    @JsonIgnore
    private PaymentMethodDto paymentMethod;

    
    @JsonProperty("user_customer_id")
    @NotNull(message = "{not.null.message}")
    private Long customerId;

    @JsonIgnore
    private UserDto customer;

    @JsonIgnore
    private List<OrderItemDto> items = new ArrayList<>();
    
    public Long getBooks() {
        return null;
    }

    public void setBooks(BookDto book) {}

    public void calculateTotalValue() {}
}