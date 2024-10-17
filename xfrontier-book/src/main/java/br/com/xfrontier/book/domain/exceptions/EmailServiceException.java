package br.com.xfrontier.book.domain.exceptions;

public class EmailServiceException extends RuntimeException {
    private static final long serialVersionUID= 1L;

    public EmailServiceException() {}

    public EmailServiceException(String message) {
        super(message);
    }
}
