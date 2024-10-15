package com.nofrontier.book.domain.exceptions;

public class ProductNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public ProductNotFoundException(String message) {
		super(message);
	}
	
	public ProductNotFoundException(Long bookId, Long productId) {
		this(String.format("There is no product record with code %d for the book with code %d", 
				bookId, productId));
	}
}