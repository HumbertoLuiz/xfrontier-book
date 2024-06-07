package com.nofrontier.book.domain.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nofrontier.book.core.enums.BookStatus;
import com.nofrontier.book.core.services.gatewaypayment.adpaters.GatewayPaymentService;
import com.nofrontier.book.core.validation.BookCancellationValidator;
import com.nofrontier.book.domain.exceptions.BookNotFoundException;
import com.nofrontier.book.domain.model.Book;
import com.nofrontier.book.domain.repository.BookRepository;
import com.nofrontier.book.dto.v1.requests.BookCancellationRequest;
import com.nofrontier.book.dto.v1.responses.MessageResponse;

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

