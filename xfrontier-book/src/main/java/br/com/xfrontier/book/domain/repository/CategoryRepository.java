package br.com.xfrontier.book.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.xfrontier.book.domain.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}