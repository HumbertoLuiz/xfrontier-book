package com.nofrontier.book.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nofrontier.book.domain.model.Person;
import com.nofrontier.book.domain.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	public static final PersonRepository personRepository = null;

	Optional<User> findByEmail(String email);

	Page<User> findByCitiesIbgeCode(String ibgeCode, Pageable pageable);

	Boolean existsByCitiesIbgeCode(String ibgeCode);	

    @Query("SELECT u FROM User u JOIN FETCH u.person WHERE u.id = :id")
    Optional<User> findByIdWithPerson(@Param("id") Long id);

	default Boolean isEmailAlreadyRegistered(User user) {
		if (user.getEmail() == null) {
			return false;
		}
		return findByEmail(user.getEmail())
				.map(userFound -> !userFound.getId().equals(user.getId()))
				.orElse(false);
	}

//	 @Query("SELECT COUNT(p) > 0 FROM User u JOIN u.persons p WHERE ELEMENT(p).cpf = :cpf")
//	 boolean isCpfAlreadyRegistered(@Param("cpf") String cpf);
	 
	@Query("SELECT COUNT(u) > 0 FROM User u JOIN u.person p WHERE p.cpf = :cpf")
	boolean isCpfAlreadyRegistered(@Param("cpf") String cpf);

	default Boolean isCpfAlreadyRegistered(User user) {
		Person person = user.getPerson(); // Obter a pessoa associada ao usuário
		if (person == null) {
			return false; // Se a pessoa não estiver definida, não há necessidade de validação
		}
		if (person.getCpf() != null
				&& personRepository.findByCpf(person.getCpf()).isPresent()) {
			return true; // CPF encontrado, retorna true
		}
		return false; // CPF não encontrado na pessoa associada
	}

	// @Query("SELECT COUNT(p) > 0 FROM User u JOIN u.persons p WHERE
	// ELEMENT(p).keyPix = :keyPix")
	// boolean isKeyPixAlreadyRegistered(@Param("keyPix") String keyPix);
	
	@Query("SELECT COUNT(u) > 0 FROM User u JOIN u.person p WHERE p.keyPix = :keyPix")
	boolean isKeyPixAlreadyRegistered(@Param("keyPix") String keyPix);

	default Boolean isKeyPixAlreadyRegistered(User user) {
		Person person = user.getPerson(); // Obter a pessoa associada ao usuário
		if (person == null) {
			return false; // Se a pessoa não estiver definida, não há necessidade de validação
		}
		if (person.getKeyPix() != null && personRepository
				.findByKeyPix(person.getKeyPix()).isPresent()) {
			return true; // KeyPix encontrado, retorna true
		}
		return false; // KeyPix não encontrado na pessoa associada
	}

	boolean existsByEmail(String email);

}
