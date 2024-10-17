package br.com.xfrontier.book.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.xfrontier.book.domain.model.Address;
import br.com.xfrontier.book.domain.model.User;

public interface AddressRepository extends JpaRepository<Address, Long> {

	void save(User loggedUser);

}
