package com.nofrontier.book.core.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import com.nofrontier.book.domain.exceptions.ValidatingException;
import com.nofrontier.book.domain.model.Book;

@Component
public class PaymentValidator {

    public void validate(Book book) {
        validateStatus(book);
    }

    private void validateStatus(Book book) {
        if (!book.isWithoutPayment()) {
            var message = "the daily rate must have the status WITHOUT PAYMENT";
            var fieldError = new FieldError(book.getClass().getName(), "status", book.getPaymentMethods(), false, null, null, message);

            throw new ValidatingException(message, fieldError);
        }
    }

}
