package br.com.xfrontier.book.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BookStatus {

	WITHOUT_PAYMENT(1, "Awaiting payment"),
    PAID(2, "Paid"),
    CANCELLED(3, "Cancelled"),
    RATED(4, "Rated");

    private Integer id;

    private String description;

}
