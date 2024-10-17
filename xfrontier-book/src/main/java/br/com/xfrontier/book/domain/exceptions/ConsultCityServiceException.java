package br.com.xfrontier.book.domain.exceptions;

public class ConsultCityServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	public ConsultCityServiceException() {}

    public ConsultCityServiceException(String message) {
        super(message);
    }

}
