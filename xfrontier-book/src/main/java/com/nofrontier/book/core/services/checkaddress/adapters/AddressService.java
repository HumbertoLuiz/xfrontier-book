package com.nofrontier.book.core.services.checkaddress.adapters;

import com.nofrontier.book.core.services.checkaddress.dtos.AddressResponse;
import com.nofrontier.book.domain.exceptions.AddressServiceException;

public interface AddressService {

	AddressResponse findAddressByzipCode(String zipCode) throws AddressServiceException;



}
