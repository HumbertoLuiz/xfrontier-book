package com.nofrontier.book.domain.repository;

import java.math.BigDecimal;
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
        extends CustomJpaRepository<Book, Long>,
                JpaSpecificationExecutor<Book>,
                BookRepositoryQueries {

    @Query("SELECT b FROM Book b JOIN FETCH b.orders")
    List<Book> findAll();

    Page<Book> findByAuthor(String author, Pageable pageable);

    @Query("SELECT b FROM Book b JOIN b.orders o WHERE b.title LIKE %:title% AND o.id = :id")
    List<Book> findByTitleAndOrderId(@Param("title") String title, @Param("id") Long orderId);
    
    @Query("SELECT b FROM Book b WHERE b.title LIKE %:title% AND b.shippingRate >= :initialShippingRate AND b.shippingRate <= :finalShippingRate")
    List<Book> findBooksByTitleAndShippingRate(
            @Param("title") String title,
            @Param("initialShippingRate") BigDecimal initialShippingRate,
            @Param("finalShippingRate") BigDecimal finalShippingRate);
    
    @Query("SELECT b FROM Book b WHERE b.shippingRate >= :initialShippingRate AND b.shippingRate <= :finalShippingRate")
    List<Book> findBooksByShippingRate(
            @Param("initialShippingRate") BigDecimal initialShippingRate,
            @Param("finalShippingRate") BigDecimal finalShippingRate);

    List<Book> findByTitleContainingAndOrders_Id(String title, Long orderId);

    Optional<Book> findFirstBookByTitleContaining(String title);

    List<Book> findTop2ByTitleContaining(String title);

    int countByOrders_Id(Long orderId);

    boolean existsResponsibleById(Long userId);
}
