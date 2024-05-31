package com.nofrontier.book.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nofrontier.book.domain.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}