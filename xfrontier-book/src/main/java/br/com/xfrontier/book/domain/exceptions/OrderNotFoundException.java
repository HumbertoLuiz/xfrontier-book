package br.com.xfrontier.book.domain.exceptions;

public class OrderNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public OrderNotFoundException(String orderCode) {
		super(String.format("There is no order with a code %s", orderCode));
	}

}
