package br.com.xfrontier.book.domain.exceptions;

public class CityNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public CityNotFoundException(String message) {
		super(message);
	}
	
	public CityNotFoundException(Long cityId) {
		this(String.format("There is no city register with a code %d", cityId));
	}
	
}
