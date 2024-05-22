package com.nofrontier.book.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nofrontier.book.domain.model.Order;

@Repository
public interface OrderRepository extends CustomJpaRepository<Order, Long>,
		JpaSpecificationExecutor<Order> {

	Optional<Order> findByCode(String code);

	@Query("from Order o join fetch o.customer join fetch o.book b join fetch b.order")
	List<Order> findAll();
	
	boolean isOrderManagedBy(String orderCode, Long userId);
	
}
