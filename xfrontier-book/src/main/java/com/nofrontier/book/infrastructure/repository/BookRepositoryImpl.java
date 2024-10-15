package com.nofrontier.book.infrastructure.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.nofrontier.book.domain.model.Book;
import com.nofrontier.book.domain.repository.BookRepository;
import com.nofrontier.book.domain.repository.BookRepositoryQueries;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Predicate;

@Repository
public class BookRepositoryImpl implements BookRepositoryQueries {

    @PersistenceContext
    private EntityManager manager;

    @Autowired @Lazy
    private BookRepository bookRepository;


    @Override
    public List<Book> findBooksByTitleAndShippingRate(String title, BigDecimal initialShippingRate, BigDecimal finalShippingRate) {
        var builder = manager.getCriteriaBuilder();
        var criteria = builder.createQuery(Book.class);
        var root = criteria.from(Book.class);
        var predicates = new ArrayList<Predicate>();

        if (StringUtils.hasText(title)) {
            predicates.add(builder.like(root.get("title"), "%" + title + "%"));
        }

        if (initialShippingRate != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("shippingRate"), initialShippingRate));
        }

        if (finalShippingRate != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("shippingRate"), finalShippingRate));
        }

        criteria.where(predicates.toArray(new Predicate[0]));
        var query = manager.createQuery(criteria);
        return query.getResultList();
    }

    @Override
    public List<Book> findBooksByShippingRate(BigDecimal initialShippingRate, BigDecimal finalShippingRate) {
        var builder = manager.getCriteriaBuilder();

        var criteria = builder.createQuery(Book.class);
        var root = criteria.from(Book.class);

        var predicates = new ArrayList<Predicate>();

        if (initialShippingRate != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("shippingRate"), initialShippingRate));
        }

        if (finalShippingRate != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("shippingRate"), finalShippingRate));
        }

        criteria.where(predicates.toArray(new Predicate[0]));

        var query = manager.createQuery(criteria);
        return query.getResultList();
    }
}

