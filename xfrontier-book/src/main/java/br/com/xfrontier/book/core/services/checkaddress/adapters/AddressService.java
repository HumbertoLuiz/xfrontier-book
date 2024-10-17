package br.com.xfrontier.book.core.services.checkaddress.adapters;

import br.com.xfrontier.book.core.services.checkaddress.dtos.AddressResponse;
import br.com.xfrontier.book.domain.exceptions.AddressServiceException;

public interface AddressService {

	AddressResponse findAddressByzipCode(String zipCode) throws AddressServiceException;



}
