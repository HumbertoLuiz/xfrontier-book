package com.nofrontier.book.core.events;

import org.springframework.context.ApplicationEvent;

import com.nofrontier.book.domain.model.Rating;

import lombok.Getter;

@Getter
public class NewRatingEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;
    
	private Rating rating;

    public NewRatingEvent(Object source, Rating rating) {
        super(source);
        this.rating = rating;
    }

}
