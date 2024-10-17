package br.com.xfrontier.book.core.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import br.com.xfrontier.book.domain.exceptions.ValidatingException;
import br.com.xfrontier.book.domain.model.Book;

@Component
public class BookCancellationValidator {

    public void validate(Book book) {
        validateStatus(book);
    }

    private void validateStatus(Book book) {
        var isNotPaidOrNotRated = !(book.isPaid() || book.isRated());
        if (isNotPaidOrNotRated) {
            var message = "Daily rate to be canceled must have the status PAID or RATED";
            var fieldError = new FieldError(book.getClass().getName(), "status", book.getPaymentMethods(), false, null, null, message);
            throw new ValidatingException(message, fieldError);
        }

    }

}
