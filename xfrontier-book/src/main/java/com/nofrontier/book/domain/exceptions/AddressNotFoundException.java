package com.nofrontier.book.domain.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class AddressNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public AddressNotFoundException(String message) {
	        super(message);
	    }

	public AddressNotFoundException(Long addressId) {
		this(String.format("There is no address registered with a code %d", addressId));
	}
}
