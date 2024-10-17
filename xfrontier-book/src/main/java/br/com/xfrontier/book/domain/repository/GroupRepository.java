package br.com.xfrontier.book.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.xfrontier.book.domain.model.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {

}
