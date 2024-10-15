package com.nofrontier.book.domain.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class PasswordResetNotFound extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public PasswordResetNotFound() {}

    public PasswordResetNotFound(String message) {
        super(message);
    }

}
