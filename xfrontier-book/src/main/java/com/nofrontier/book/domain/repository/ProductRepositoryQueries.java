package com.nofrontier.book.domain.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nofrontier.book.domain.model.ProductImage;

import jakarta.transaction.Transactional;

public interface ProductRepositoryQueries {

	ProductImage save(ProductImage image);
	
	@Transactional
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("delete from ProductImage pi where pi.id = :id")
	void removeProductImage(Long id);
}
