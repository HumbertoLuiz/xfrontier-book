package com.nofrontier.book.infrastructure.repository;


import static com.nofrontier.book.infrastructure.repository.spec.OrderSpecs.withFreeShipping;
import static com.nofrontier.book.infrastructure.repository.spec.OrderSpecs.withSimilarName;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.nofrontier.book.domain.model.Order;
import com.nofrontier.book.domain.repository.OrderRepository;
import com.nofrontier.book.domain.repository.OrderRepositoryQueries;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Predicate;

@Repository
public class OrderRepositoryImpl implements OrderRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;

	@Autowired @Lazy
	private OrderRepository restaurantRepository;

	public List<Order> find(String name, BigDecimal initialShippingRate, BigDecimal finalShippingRate) {
		var builder = manager.getCriteriaBuilder();

		var criteria = builder.createQuery(Order.class);
		var root = criteria.from(Order.class);

		var predicates = new ArrayList<Predicate>();

		if (StringUtils.hasText(name)) {
			predicates.add(builder.like(root.get("code"), "%" + name + "%"));
		}

		if (initialShippingRate != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get("shippingRate"), initialShippingRate));
		}

		if (finalShippingRate != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get("shippingRate"), finalShippingRate));
		}

		criteria.where(predicates.toArray(new Predicate[0]));

		var query = manager.createQuery(criteria);
		return query.getResultList();
	}

	@Override
	public List<Order> findWithFreeShipping(String name) {
		return restaurantRepository.findAll(withFreeShipping().and(withSimilarName(name)));
	}

	public List<Order> find(BigDecimal initialShippingRate, BigDecimal finalShippingRate) {
		// TODO Auto-generated method stub
		return null;
	}


}
