package com.nofrontier.book.domain.repository;

import java.math.BigDecimal;
import java.util.List;

import com.nofrontier.book.domain.model.Order;

public interface OrderRepositoryQueries {

	List<Order> find(String code, 
			BigDecimal initialShippingRate, BigDecimal finalShippingRate);
	
	List<Order> findWithFreeShipping(String code);

}