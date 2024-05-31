package com.nofrontier.book.domain.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nofrontier.book.core.publishers.NewRatingPublisher;
import com.nofrontier.book.core.validation.RatingValidator;
import com.nofrontier.book.domain.exceptions.BookNotFoundException;
import com.nofrontier.book.domain.model.Book;
import com.nofrontier.book.domain.model.Rating;
import com.nofrontier.book.domain.repository.BookRepository;
import com.nofrontier.book.domain.repository.RatingRepository;
import com.nofrontier.book.dto.v1.requests.RatingRequest;
import com.nofrontier.book.dto.v1.responses.MessageResponse;
import com.nofrontier.book.utils.SecurityUtils;


@Service
public class ApiRatingService {

    @Autowired
    private RatingRepository ratingRepository;
    
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RatingValidator validator;

    @Autowired
    private NewRatingPublisher newRatingPublisher;

    public MessageResponse rateBook(RatingRequest request, Long id) {
        var book = findBookById(id);
        var rater = securityUtils.getLoggedUser();
        var model = modelMapper.map(request, Rating.class);
        model.setRater(rater);
        model.setBook(book);
        model.setVisibility(true);

        validator.validate(model);

        model = ratingRepository.save(model);
        newRatingPublisher.publish(model);

        return new MessageResponse("Rating successfully completed!");
    }


    private Book findBookById(Long id) {
        var message = String.format("Book with id %d not found", id);
        return bookRepository.findById(id)
            .orElseThrow(() -> new BookNotFoundException(message));
    }

}
