package com.nofrontier.book.domain.repository;

import java.time.OffsetDateTime;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nofrontier.book.core.enums.BookStatus;
import com.nofrontier.book.domain.model.PaymentMethod;
import com.nofrontier.book.domain.model.User;

public interface PaymentMethodRepository
		extends
			JpaRepository<PaymentMethod, Long> {

	@Query("select max(updatedAt) from PaymentMethod")
	OffsetDateTime getLastUpdatedAt();

	@Query("select updatedAt from PaymentMethod where id = :paymentMethodId")
	OffsetDateTime getUpdatedAtById(Long paymentMethodId);

	Set<PaymentMethod> findByBooksAndBookStatusIn(User user,
			Set<BookStatus> bookStatus);

}
