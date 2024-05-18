package com.nofrontier.book.infrastructure.repository.spec;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;

import com.nofrontier.book.core.filters.OrderFilter;
import com.nofrontier.book.domain.model.Order;

import jakarta.persistence.criteria.Predicate;

public class OrderSpecs {

	public static Specification<Order> withFreeShipping() {
		return (root, query, builder) -> builder.equal(root.get("shippingRate"),
				BigDecimal.ZERO);
	}

	public static Specification<Order> withSimilarName(String name) {
		return (root, query, builder) -> builder.like(root.get("code"),
				"%" + name + "%");
	}

	public static Specification<Order> usingFilter(OrderFilter filter) {
		return (root, query, builder) -> {
			if (Order.class.equals(query.getResultType())) {
				root.fetch("order").fetch("book");
				root.fetch("customer");
			}

			var predicates = new ArrayList<Predicate>();

			if (filter.getCustomerId() != null) {
				predicates.add(builder.equal(root.get("customer").get("id"),
						filter.getCustomerId()));
			}

			if (filter.getOrderId() != null) {
				predicates.add(builder.equal(root.get("order").get("id"),
						filter.getOrderId()));
			}

			if (filter.getInitialCreationDate() != null) {
				predicates.add(
						builder.greaterThanOrEqualTo(root.get("creationDate"),
								filter.getInitialCreationDate()));
			}

			if (filter.getExitCreationDate() != null) {
				predicates
						.add(builder.lessThanOrEqualTo(root.get("creationDate"),
								filter.getExitCreationDate()));
			}

			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}

}
