package br.com.xfrontier.book.domain.exceptions;

public class CheckCityServiceException extends RuntimeException {

    private static final long serialVersionUID= 1L;

    public CheckCityServiceException() {}

    public CheckCityServiceException(String message) {
        super(message);
    }

}