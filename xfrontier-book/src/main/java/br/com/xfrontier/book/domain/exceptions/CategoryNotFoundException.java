package br.com.xfrontier.book.domain.exceptions;

public class CategoryNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public CategoryNotFoundException(String message) {
		super(message);
	}

	public CategoryNotFoundException(Long categoryId) {
		this(String.format("There is no category record with code %d for the code restaurant %d", categoryId));
	}

}
