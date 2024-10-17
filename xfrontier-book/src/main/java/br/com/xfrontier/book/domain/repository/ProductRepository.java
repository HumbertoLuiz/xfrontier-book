package br.com.xfrontier.book.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.xfrontier.book.domain.model.Book;
import br.com.xfrontier.book.domain.model.Product;
import br.com.xfrontier.book.domain.model.ProductImage;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryQueries {

	@Query("from Product where book.id = :book and id = :product")
	Optional<Product> findById(@Param("book") Long bookId, 
			@Param("product") Long productId);
	
	List<Product> findAllByBook(Book book);
	
	@Query("from Product p where p.active = true and p.book = :book")
	List<Product> findActivesByBook(Book book);
	
	@Query("select i from ProductImage i join i.product p "
			+ "where p.book.id = :bookId and i.product.id = :productId")
	Optional<ProductImage> findImageById(Long bookId, Long productId);
	
}
