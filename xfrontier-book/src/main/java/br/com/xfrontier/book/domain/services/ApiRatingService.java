package br.com.xfrontier.book.domain.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.xfrontier.book.core.publishers.NewRatingPublisher;
import br.com.xfrontier.book.core.validation.RatingValidator;
import br.com.xfrontier.book.domain.exceptions.BookNotFoundException;
import br.com.xfrontier.book.domain.model.Book;
import br.com.xfrontier.book.domain.model.Rating;
import br.com.xfrontier.book.domain.repository.BookRepository;
import br.com.xfrontier.book.domain.repository.RatingRepository;
import br.com.xfrontier.book.dto.v1.MessageResponse;
import br.com.xfrontier.book.dto.v1.RatingDto;
import br.com.xfrontier.book.utils.SecurityUtils;


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
    
    public MessageResponse rateBook(RatingDto request, Long id) {
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
