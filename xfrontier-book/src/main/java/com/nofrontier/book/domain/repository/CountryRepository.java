package com.nofrontier.book.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nofrontier.book.domain.model.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {

}
