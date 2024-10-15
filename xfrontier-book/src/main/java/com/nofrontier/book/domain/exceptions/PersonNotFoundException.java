package com.nofrontier.book.domain.exceptions;

public class PersonNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public PersonNotFoundException(String message) {
		super(message);
	}

	public PersonNotFoundException(Long personId) {
		this(String.format("There is no person record with code %d for the code restaurant %d", personId));
	}

}
