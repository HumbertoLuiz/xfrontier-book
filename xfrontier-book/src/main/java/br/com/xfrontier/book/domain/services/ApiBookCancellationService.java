package br.com.xfrontier.book.domain.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.xfrontier.book.core.enums.BookStatus;
import br.com.xfrontier.book.core.services.gatewaypayment.adpaters.GatewayPaymentService;
import br.com.xfrontier.book.core.validation.BookCancellationValidator;
import br.com.xfrontier.book.domain.exceptions.BookNotFoundException;
import br.com.xfrontier.book.domain.model.Book;
import br.com.xfrontier.book.domain.repository.BookRepository;
import br.com.xfrontier.book.dto.v1.BookCancellationRequest;
import br.com.xfrontier.book.dto.v1.MessageResponse;

@Service
public class ApiBookCancellationService {

    private final BookRepository bookRepository;
    private final BookCancellationValidator validator;
    private final GatewayPaymentService gatewayPaymentService;

    public ApiBookCancellationService(BookRepository bookRepository,
                                      BookCancellationValidator validator,
                                      GatewayPaymentService gatewayPaymentService) {
        this.bookRepository = bookRepository;
        this.validator = validator;
        this.gatewayPaymentService = gatewayPaymentService;
    }

    @Transactional(readOnly = false)
    public MessageResponse cancel(Long bookId, BookCancellationRequest request) {
        var book = findBookById(bookId);
        validator.validate(book);
        gatewayPaymentService.makeTotalRefund(book, null);
        book.setBookStatus(BookStatus.CANCELLED);
        book.setReasonCancellation(request.getReasonCancellation());
        bookRepository.save(book);
        return new MessageResponse("The per diem was successfully canceled!");
    }

    private Book findBookById(Long bookId) {
        var message = String.format("Book with id %d not found", bookId);
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(message));
    }
}

