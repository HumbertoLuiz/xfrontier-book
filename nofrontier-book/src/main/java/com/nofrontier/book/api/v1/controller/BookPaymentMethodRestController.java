package com.nofrontier.book.api.v1.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nofrontier.book.domain.model.Book;
import com.nofrontier.book.domain.services.ApiBookPaymentMethodService;
import com.nofrontier.book.dto.v1.requests.PaymentMethodRequest;
import com.nofrontier.book.dto.v1.responses.MessageResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/books/{id}/v1")
public class BookPaymentMethodRestController {

	@Autowired
	private ApiBookPaymentMethodService service;

	@PostMapping("/pay")
	public MessageResponse pay(@RequestBody @Valid Set<Book> books,
			PaymentMethodRequest request, @PathVariable Long id) {
		return service.pay(books, request, id);
	}

}
