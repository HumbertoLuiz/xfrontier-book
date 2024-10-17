package br.com.xfrontier.book.infrastructure.repository.spec;

import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;

import br.com.xfrontier.book.core.filters.OrderFilter;
import br.com.xfrontier.book.domain.model.Order;

import jakarta.persistence.criteria.Predicate;

public class OrderSpecs {

	public static Specification<Order> usandoFiltro(OrderFilter filtro) {
		return (root, query, builder) -> {
			if (Order.class.equals(query.getResultType())) {
				root.fetch("book").fetch("order");
				root.fetch("customer");
			}

			var predicates = new ArrayList<Predicate>();

			if (filtro.getCustomerId() != null) {
				predicates.add(builder.equal(root.get("customer").get("id"),
						filtro.getCustomerId()));
			}

			if (filtro.getBookId() != null) {
				predicates.add(builder.equal(root.get("book").get("id"),
						filtro.getBookId()));
			}

			if (filtro.getInitialCreationDate() != null) {
				predicates.add(
						builder.greaterThanOrEqualTo(root.get("creationDate"),
								filtro.getInitialCreationDate()));
			}

			if (filtro.getExitCreationDate() != null) {
				predicates
						.add(builder.lessThanOrEqualTo(root.get("creationDate"),
								filtro.getExitCreationDate()));
			}

			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}

}
