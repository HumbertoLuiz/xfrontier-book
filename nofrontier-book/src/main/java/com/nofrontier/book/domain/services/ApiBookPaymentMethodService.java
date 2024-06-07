package com.nofrontier.book.domain.services;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.nofrontier.book.core.enums.BookStatus;
import com.nofrontier.book.core.services.gatewaypayment.adpaters.GatewayPaymentService;
import com.nofrontier.book.core.validation.PaymentValidator;
import com.nofrontier.book.domain.exceptions.BookNotFoundException;
import com.nofrontier.book.domain.model.Book;
import com.nofrontier.book.domain.repository.BookRepository;
import com.nofrontier.book.dto.v1.requests.PaymentMethodRequest;
import com.nofrontier.book.dto.v1.responses.MessageResponse;

@Service
public class ApiBookPaymentMethodService {

	private final BookRepository bookRepository;
	private final PaymentValidator validator;
	private final GatewayPaymentService gatewayPaymentService;
	
	public ApiBookPaymentMethodService(BookRepository bookRepository,
			PaymentValidator validator,
			GatewayPaymentService gatewayPaymentService) {
		this.bookRepository = bookRepository;
		this.validator = validator;
		this.gatewayPaymentService = gatewayPaymentService;
	}

	public MessageResponse pay(Set<Book> books, PaymentMethodRequest request,
			Long id) {
		var book = findBookById(id);

		validator.validate(book);

		var payment = gatewayPaymentService.pay(book, books,
				request.getCardHash());
		if (payment.isAccepted()) {
			((Book) books).setPaymentMethods(BookStatus.PAID);
			bookRepository.save(book);
			return new MessageResponse("Book rate successfully paid!");
		}
		return new MessageResponse("Payment refused");
	}

	private Book findBookById(Long id) {
		var message = String.format("Book with id %d not found", id);
		return bookRepository.findById(id)
				.orElseThrow(() -> new BookNotFoundException(message));
	}

}
