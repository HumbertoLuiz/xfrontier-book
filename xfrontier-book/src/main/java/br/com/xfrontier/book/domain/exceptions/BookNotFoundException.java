package br.com.xfrontier.book.domain.exceptions;

public class BookNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public BookNotFoundException(String message) {
		super(message);
	}

	public BookNotFoundException(Long bookId) {
		this(String.format("There is no book record with code %d for the code restaurant %d", bookId));
	}

}
