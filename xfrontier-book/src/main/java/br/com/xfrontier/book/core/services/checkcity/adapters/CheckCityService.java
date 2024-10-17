package br.com.xfrontier.book.core.services.checkcity.adapters;

import br.com.xfrontier.book.core.services.checkcity.dtos.CityResponse;
import br.com.xfrontier.book.domain.exceptions.CheckCityServiceException;

public interface CheckCityService {

    CityResponse findCityByIbgeCode(String codigoIbge) throws CheckCityServiceException;

}