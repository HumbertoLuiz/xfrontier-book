package br.com.xfrontier.book.api.v1.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.xfrontier.book.domain.model.Book;
import br.com.xfrontier.book.domain.services.ApiBookPaymentMethodService;
import br.com.xfrontier.book.dto.v1.MessageResponse;
import br.com.xfrontier.book.dto.v1.PaymentMethodDto;
import jakarta.validation.Valid;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080", "https://xfrontier.com.br"})
@RestController
@RequestMapping("/api/books/{id}/payment/v1")
public class BookPaymentMethodRestController {

	@Autowired
	private ApiBookPaymentMethodService service;

	@PostMapping("/pay")
	public MessageResponse pay(@RequestBody @Valid Set<Book> books,
			PaymentMethodDto request, @PathVariable Long id) {
		return service.pay(books, request, id);
	}

}
