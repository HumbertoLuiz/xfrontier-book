package br.com.xfrontier.book.domain.exceptions;

public class StorageServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public StorageServiceException() {}

    public StorageServiceException(String message) {
        super(message);
    }

}