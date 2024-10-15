package com.nofrontier.book.core.publishers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.nofrontier.book.core.events.NewRatingEvent;
import com.nofrontier.book.domain.model.Rating;

@Component
public class NewRatingPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publish(Rating rating) {
        var event = new NewRatingEvent(this, rating);
        applicationEventPublisher.publishEvent(event);
    }

}
