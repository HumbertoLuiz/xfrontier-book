package com.nofrontier.book.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	@Query("SELECT b FROM Book b JOIN FETCH b.orders")
	List<Book> findAll();
	
	Page<Book> findByAuthor(String author, Pageable pageable);

    @Query("select b from Book b join b.orders o where b.title like %:title% and o.id = :id")
    List<Book> findByTitleAndOrderId(@Param("title") String title, @Param("id") Long orderId);

	List<Book> findByTitleContainingAndOrders_Id(String title, Long orderId);

	Optional<Book> findFirstBookByTitleContaining(String title);

	List<Book> findTop2ByTitleContaining(String title);

	int countByOrders_Id(Long orderId);

	boolean existsResponsibleById(Long userId);
}