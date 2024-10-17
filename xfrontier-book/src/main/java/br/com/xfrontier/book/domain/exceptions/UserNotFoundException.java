package br.com.xfrontier.book.domain.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public UserNotFoundException(String message) {
		super(message);
	}
	
	public UserNotFoundException(Long userId) {
		this(String.format("There is no user registered with a code %d", userId));
	}
}
