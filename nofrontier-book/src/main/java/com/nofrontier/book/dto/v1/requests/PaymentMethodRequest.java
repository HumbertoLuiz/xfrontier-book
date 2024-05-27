package com.nofrontier.book.dto.v1.requests;

import java.io.Serializable;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nofrontier.book.core.enums.PaymentStatus;

import jakarta.validation.constraints.NotBlank;
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
public class PaymentMethodRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "{not.blank.message}")
    @Size(min = 3, max = 255, message = "{size.message}")
    private String description;


    @NotNull(message = "{not.null.message}")
    private PaymentStatus status;

    @NotBlank(message = "{not.blank.message}")
    @Size(max = 255, message = "{size.message}")
    private String cardHash;;
}