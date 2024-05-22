package com.nofrontier.book.domain.repository;

import java.math.BigDecimal;
import java.util.List;

import com.nofrontier.book.domain.model.Book;

public interface BookRepositoryQueries {

	List<Book> find(String title, BigDecimal initialShippingRate,
			BigDecimal finalShippingRate);

	List<Book> findWithFreeShipping(String title);

}