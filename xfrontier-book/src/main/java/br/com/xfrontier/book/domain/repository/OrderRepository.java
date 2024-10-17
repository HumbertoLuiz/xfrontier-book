package br.com.xfrontier.book.domain.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.xfrontier.book.domain.model.Order;
import br.com.xfrontier.book.dto.v1.OrderDto;

@Repository
public interface OrderRepository
		extends
			CustomJpaRepository<Order, Long>,
			JpaSpecificationExecutor<Order> {

	Optional<Order> findByCode(String code);

	@Query("SELECT o FROM Order o JOIN FETCH o.customer JOIN FETCH o.books")
	List<Order> findAll();

	List<Order> findByShippingRateBetween(BigDecimal taxaInicial,
			BigDecimal taxaFinal);

	boolean existsOrderByCode(String code);

	Order save(OrderDto order);
}
