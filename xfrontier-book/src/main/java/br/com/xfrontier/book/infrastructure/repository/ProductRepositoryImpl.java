package br.com.xfrontier.book.infrastructure.repository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.xfrontier.book.domain.model.ProductImage;
import br.com.xfrontier.book.domain.repository.ProductRepositoryQueries;

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
