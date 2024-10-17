package br.com.xfrontier.book.domain.services;

import java.util.Set;

import org.springframework.stereotype.Service;

import br.com.xfrontier.book.core.enums.BookStatus;
import br.com.xfrontier.book.core.services.gatewaypayment.adpaters.GatewayPaymentService;
import br.com.xfrontier.book.core.validation.PaymentValidator;
import br.com.xfrontier.book.domain.exceptions.BookNotFoundException;
import br.com.xfrontier.book.domain.model.Book;
import br.com.xfrontier.book.domain.repository.BookRepository;
import br.com.xfrontier.book.dto.v1.PaymentMethodDto;
import br.com.xfrontier.book.dto.v1.MessageResponse;

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

	public MessageResponse pay(Set<Book> books, PaymentMethodDto request,
			Long id) {
		var book = findBookById(id);

		validator.validate(book);

		var payment = gatewayPaymentService.pay(book, books,
				request.getTransactionId());
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
