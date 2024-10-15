package com.nofrontier.book.core.publishers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.nofrontier.book.core.events.NewUserEvent;
import com.nofrontier.book.domain.model.User;

@Component
public class NewUserPublisher {

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	public void publish(User user) {
		var event = new NewUserEvent(this, user);
		eventPublisher.publishEvent(event);
	}

}