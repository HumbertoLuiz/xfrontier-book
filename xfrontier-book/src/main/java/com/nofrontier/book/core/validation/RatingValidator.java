package com.nofrontier.book.core.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import com.nofrontier.book.domain.exceptions.ValidatingException;
import com.nofrontier.book.domain.model.Rating;
import com.nofrontier.book.domain.repository.RatingRepository;

@Component
public class RatingValidator {

    @Autowired
    private RatingRepository repository;

    public void validate(Rating model) {
        validateBookStatus(model);
    }

    private void validateBookStatus(Rating model) {
        if (!model.getBook().isPaid()) {
            var message = "Book PAID";
            var fieldError = new FieldError(model.getClass().getName(), "book", null, false, null, null, message);
            throw new ValidatingException(message, fieldError);
        }

        validateRater(model);
    }

    private void validateRater(Rating model) {
        var book = model.getBook();
        var rater = model.getRater();

        if (repository.existsByBookAndRater(book, rater)) {
            var message = "User has already rated this book";
            var fieldError = new FieldError(model.getClass().getName(), "rater", null, false, null, null, message);
            throw new ValidatingException(message, fieldError);
        }
    }

}
