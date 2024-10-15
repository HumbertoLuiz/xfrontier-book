package com.nofrontier.book.core.services.checkcity.adapters;

import com.nofrontier.book.core.services.checkcity.dtos.CityResponse;
import com.nofrontier.book.domain.exceptions.CheckCityServiceException;

public interface CheckCityService {

    CityResponse findCityByIbgeCode(String codigoIbge) throws CheckCityServiceException;

}