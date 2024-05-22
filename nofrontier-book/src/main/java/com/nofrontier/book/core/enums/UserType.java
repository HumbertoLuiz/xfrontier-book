package com.nofrontier.book.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserType {
    ADMIN(1),
    MANAGER(2),
    CUSTOMER(3),
    REGISTER(4);

    private final Integer id;

}
