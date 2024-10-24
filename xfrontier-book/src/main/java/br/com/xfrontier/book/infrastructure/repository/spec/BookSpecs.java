package br.com.xfrontier.book.infrastructure.repository.spec;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import br.com.xfrontier.book.domain.model.Book;
import br.com.xfrontier.book.domain.model.Order;

public class BookSpecs {

	public static Specification<Order> byFreeShipping() {
		return (root, query, builder) -> builder.equal(root.get("shippingRate"),
				BigDecimal.ZERO);
	}

	public static Specification<Book> bySimilarName(String title) {
		return (root, query, builder) -> builder.like(root.get("title"),
				"%" + title + "%");
	}

}
