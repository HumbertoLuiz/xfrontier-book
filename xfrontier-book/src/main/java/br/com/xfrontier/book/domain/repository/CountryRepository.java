package br.com.xfrontier.book.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.xfrontier.book.domain.model.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {

}
