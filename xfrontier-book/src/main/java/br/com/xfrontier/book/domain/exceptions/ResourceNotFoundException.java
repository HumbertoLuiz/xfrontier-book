package br.com.xfrontier.book.domain.exceptions;

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException() {}
	
	public ResourceNotFoundException(String exception) {
		super(exception);
	}

}