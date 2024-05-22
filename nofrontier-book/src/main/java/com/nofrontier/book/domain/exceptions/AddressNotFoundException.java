package com.nofrontier.book.domain.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class AddressNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public AddressNotFoundException(String message) {
	        super(message);
	    }

}
