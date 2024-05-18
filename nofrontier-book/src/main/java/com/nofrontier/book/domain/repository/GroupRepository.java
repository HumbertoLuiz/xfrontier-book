package com.nofrontier.book.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nofrontier.book.domain.model.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {

}
