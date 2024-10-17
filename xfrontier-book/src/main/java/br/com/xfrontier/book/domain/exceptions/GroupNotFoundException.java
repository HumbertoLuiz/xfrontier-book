package br.com.xfrontier.book.domain.exceptions;

public class GroupNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public GroupNotFoundException(String message) {
		super(message);
	}

	public GroupNotFoundException(Long stateId) {
		this(String.format("There is no group register with a code %d", stateId));
	}

}
