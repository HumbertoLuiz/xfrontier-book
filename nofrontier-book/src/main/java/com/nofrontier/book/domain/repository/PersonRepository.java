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
	
	
    @Query("SELECT p FROM Person p WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))")
    Page<Person> findPersonByName(@Param("firstName") String firstName, Pageable pageable);
	
    default Boolean isCpfAlreadyRegistered(Person person) {
        if (person.getCpf() == null) {
            return false;
        }

        return findByCpf(person.getCpf())
            .map(personFound -> !personFound.getId().equals(person.getId()))
            .orElse(false);
    }

    default Boolean isKeyPixAlreadyRegistered(Person person) {
        if (person.getKeyPix() == null) {
            return false;
        }

        return findByKeyPix(person.getKeyPix())
            .map(personFound -> !personFound.getId().equals(person.getId()))
            .orElse(false);
    }
	
}