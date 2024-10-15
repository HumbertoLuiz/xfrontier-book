package com.nofrontier.book.infrastructure.repository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nofrontier.book.domain.model.ProductImage;
import com.nofrontier.book.domain.repository.ProductRepositoryQueries;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class ProductRepositoryImpl implements ProductRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;

	@Transactional
	public ProductImage save(ProductImage image) {
		return manager.merge(image);
	}

	@Transactional
	public void removeProductImage(Long id) {
		manager.remove(id);
	}

}
