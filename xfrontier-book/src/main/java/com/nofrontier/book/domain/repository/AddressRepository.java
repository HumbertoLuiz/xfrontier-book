package com.nofrontier.book.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nofrontier.book.domain.model.Address;
import com.nofrontier.book.domain.model.User;

public interface AddressRepository extends JpaRepository<Address, Long> {

	void save(User loggedUser);

}
