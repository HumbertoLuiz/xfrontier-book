package com.nofrontier.book.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nofrontier.book.domain.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
	
	Optional<Person> findByCpf(String cpf);

	Optional<Person> findByKeyPix(String keyPix);
	
    @Query("SELECT DISTINCT p FROM Person p LEFT JOIN FETCH p.addresses")
    Page<Person> findAllWithAddresses(Pageable pageable);
	
	@Modifying
	@Query("UPDATE Person p SET p.enabled = false WHERE p.id =:id")
	void disablePerson(@Param("id") Long id);
	
	//%AND%
	// Fernanda
	// Alessandra
//	@Query("SELECT p FROM Person p WHERE p.firstName LIKE LOWER(CONCAT ('%',:firstName,'%'))")
//	Page<Person> findPersonsByName(@Param("firstName") String firstName, Pageable pageable);
	
	
	
    @Query("SELECT p FROM Person p WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))")
    Page<Person> findPersonByName(@Param("firstName") String firstName, Pageable pageable);
	
	
}