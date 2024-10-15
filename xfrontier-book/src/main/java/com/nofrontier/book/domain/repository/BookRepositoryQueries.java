package com.nofrontier.book.domain.repository;

import java.math.BigDecimal;
import java.util.List;

import com.nofrontier.book.domain.model.Book;

public interface BookRepositoryQueries {
	
    List<Book> findBooksByTitleAndShippingRate(String title, BigDecimal initialShippingRate, BigDecimal finalShippingRate);

    List<Book> findBooksByShippingRate(BigDecimal initialShippingRate, BigDecimal finalShippingRate);
}
