package br.com.xfrontier.book.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.xfrontier.book.domain.model.PasswordReset;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {

    Optional<PasswordReset> findByEmail(String email);

    Optional<PasswordReset> findByToken(String token);

}
