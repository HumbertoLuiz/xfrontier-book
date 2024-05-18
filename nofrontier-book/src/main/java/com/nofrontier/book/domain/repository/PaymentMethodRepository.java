package com.nofrontier.book.domain.repository;

import java.time.OffsetDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nofrontier.book.domain.model.PaymentMethod;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

	@Query("select max(updateDate) from PaymentMethod")
	OffsetDateTime getLastUpdatedDate();
	
	@Query("select updateDate from PaymentMethod where id = :paymentMethodId")
	OffsetDateTime getDateUpdatedById(Long paymentMethodId);
	
}
