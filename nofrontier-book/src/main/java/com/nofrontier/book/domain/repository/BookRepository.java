package com.nofrontier.book.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nofrontier.book.domain.model.Book;
import com.nofrontier.book.domain.model.Order;
import com.nofrontier.book.domain.model.ProductImage;

public interface BookRepository extends JpaRepository<Book, Long>, BookRepositoryQueries {
	
	@Query("SELECT b FROM Book b WHERE b.author LIKE LOWER(CONCAT ('%',:author,'%'))")
	Page<Book> findBookByAuthor(@Param("author") String author, Pageable bageable);
	
	@Query("from Book where order.id = :order and id = :book")
	Optional<Book> findById(@Param("order") Long orderId, 
			@Param("book") Long bookId);
	
	List<Book> findAllByOrder(Order order);
	
	@Query("from Book b where b.active = true and b.order = :order")
	List<Book> findActiveByOrder(Order order);
	
	@Query("select f from ProductImage f join f.book b "
			+ "where b.order.id = :orderId and f.book.id = :bookId")
	Optional<ProductImage> findImageById(Long orderId, Long bookId);
	
}