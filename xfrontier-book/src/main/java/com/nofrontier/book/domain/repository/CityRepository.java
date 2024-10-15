package com.nofrontier.book.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nofrontier.book.domain.model.City;

public interface CityRepository extends JpaRepository<City, Long> {

}
