package com.nofrontier.book.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nofrontier.book.domain.model.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

}
