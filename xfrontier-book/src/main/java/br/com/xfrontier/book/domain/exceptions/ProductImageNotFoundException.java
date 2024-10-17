package br.com.xfrontier.book.domain.exceptions;

public class ProductImageNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public ProductImageNotFoundException(String message) {
		super(message);
	}

	public ProductImageNotFoundException(Long restaurantId, Long productId) {
		this(String.format("There is no product photo record with code %d for the code restaurant %d",
				productId, restaurantId));
	}

}