package com.nofrontier.book.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyRegisteredException extends ValidatingException {

	private static final long serialVersionUID = 1L;

	public UserAlreadyRegisteredException(String message, FieldError fieldError) {
		super(message, fieldError);
	}

}
