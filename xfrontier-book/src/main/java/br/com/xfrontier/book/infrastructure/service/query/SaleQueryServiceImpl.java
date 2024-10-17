package br.com.xfrontier.book.infrastructure.service.query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.xfrontier.book.core.enums.OrderStatus;
import br.com.xfrontier.book.core.filters.DailySalesFilter;
import br.com.xfrontier.book.domain.model.Order;
import br.com.xfrontier.book.domain.model.dto.DailySales;
import br.com.xfrontier.book.domain.services.SalesQueryService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Predicate;

@Repository
public class SaleQueryServiceImpl implements SalesQueryService {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public List<DailySales> consultDailySales(DailySalesFilter filter,
			String timeOffset) {
		var builder = manager.getCriteriaBuilder();
		var query = builder.createQuery(DailySales.class);
		var root = query.from(Order.class);
		var predicates = new ArrayList<Predicate>();

		var functionConvertTzCreationDate = builder.function("convert_tz",
				Date.class, root.get("creationDate"), builder.literal("+00:00"),
				builder.literal(timeOffset));

		var functionDateCreationDate = builder.function("date", Date.class,
				functionConvertTzCreationDate);

		var selection = builder.construct(DailySales.class,
				functionDateCreationDate, builder.count(root.get("id")),
				builder.sum(root.get("totalValue")));

		if (filter.getBookId() != null) {
			predicates.add(builder.equal(root.get("book").get("id"),
					filter.getBookId()));
		}

		if (filter.getInitialCreationDate() != null) {
			predicates.add(builder.greaterThanOrEqualTo(
					root.get("creationDate"), filter.getInitialCreationDate()));
		}

		if (filter.getExitCreationDate() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get("creationDate"),
					filter.getExitCreationDate()));
		}

		predicates.add(root.get("status").in(OrderStatus.CONFIRMED,
				OrderStatus.DELIVERED));

		query.select(selection);
		query.where(predicates.toArray(new Predicate[0]));
		query.groupBy(functionDateCreationDate);

		return manager.createQuery(query).getResultList();
	}

}
