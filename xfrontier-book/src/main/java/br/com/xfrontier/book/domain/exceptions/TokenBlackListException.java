package br.com.xfrontier.book.domain.exceptions;

public class TokenBlackListException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TokenBlackListException() {
        super("The given token is not valid");
    }

    public TokenBlackListException(String message) {
        super(message);
    }

}

