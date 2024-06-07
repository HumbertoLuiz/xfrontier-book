package com.nofrontier.book.domain.repository;

import java.time.OffsetDateTime;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nofrontier.book.core.enums.BookStatus;
import com.nofrontier.book.domain.model.PaymentMethod;
import com.nofrontier.book.domain.model.User;

public interface PaymentMethodRepository
		extends
			JpaRepository<PaymentMethod, Long> {

	@Query("select max(p.updatedAt) from PaymentMethod p")
	OffsetDateTime getLastUpdatedAt();

	@Query("select p.updatedAt from PaymentMethod p where p.id = :paymentMethodId")
	OffsetDateTime getUpdatedAtById(
			@Param("paymentMethodId") Long paymentMethodId);

	@Query("select distinct p from PaymentMethod p join p.books b join b.responsible u where u = :user and b.bookStatus in :bookStatus")
	Set<PaymentMethod> findByBooksAndBookStatusIn(@Param("user") User user,
			@Param("bookStatus") Set<BookStatus> bookStatus);
}
