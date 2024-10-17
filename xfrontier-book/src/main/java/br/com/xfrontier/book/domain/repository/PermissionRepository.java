package br.com.xfrontier.book.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.xfrontier.book.domain.model.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

}
