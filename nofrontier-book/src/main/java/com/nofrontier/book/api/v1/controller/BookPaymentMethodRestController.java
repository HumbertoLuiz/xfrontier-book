package com.nofrontier.book.api.v1.controller;

import java.util.Set;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nofrontier.book.domain.model.Book;
import com.nofrontier.book.domain.services.ApiBookPaymentMethodService;
import com.nofrontier.book.dto.v1.PaymentMethodDto;
import com.nofrontier.book.dto.v1.MessageResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/books/{id}/payment/v1")
public class BookPaymentMethodRestController {

	private final ApiBookPaymentMethodService service;

	@PostMapping("/pay")
	public MessageResponse pay(@RequestBody @Valid Set<Book> books,
			PaymentMethodDto request, @PathVariable Long id) {
		return service.pay(books, request, id);
	}

}
