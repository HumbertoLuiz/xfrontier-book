package br.com.xfrontier.book.core.enums;

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

    public static UserType fromId(Integer id) {
        for (UserType userType : values()) {
            if (userType.getId().equals(id)) {
                return userType;
            }
        }
        throw new IllegalArgumentException("Invalid UserType id: " + id);
    }

}
