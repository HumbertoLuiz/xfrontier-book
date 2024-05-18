package com.nofrontier.book.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nofrontier.book.domain.model.State;


public interface StateRepository extends JpaRepository<State, Long> {

}
