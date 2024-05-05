package com.nofrontier.book.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AddressType {
	
	RESIDENTIAL (1),
	COMMERCIAL (2);

    private Integer id;
}
