package com.nofrontier.book.domain.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nofrontier.book.domain.model.Book;

@Repository
public interface BookRepository
		extends
			CustomJpaRepository<Book, Long>,
			BookRepositoryQueries,
			JpaSpecificationExecutor<Book> {

	@Query("from Book r join fetch r.order")
	List<Book> findAll();

	List<Book> queryByShippingRateBetween(BigDecimal taxaInicial,
			BigDecimal taxaFinal);

	@Query("from Book where title like %:title% and order.id = :id")
	List<Book> findByTitle(String title, @Param("id") Long order);

	List<Book> findByTitleContainingAndOrderId(String title, Long order);

	Optional<Book> findFirstBookByTitleContaining(String title);

	List<Book> findTop2ByTitleContaining(String title);

	int countByOrderId(Long order);

	boolean existsResponsible(Long bookId, Long userId);

}