package br.com.xfrontier.book.core.events;

import java.io.Serializable;

import org.springframework.context.ApplicationEvent;

import br.com.xfrontier.book.domain.model.User;

import lombok.Getter;

@Getter
public class NewUserEvent extends ApplicationEvent implements Serializable {

	private static final long serialVersionUID = 1L;

	private User user;

	public NewUserEvent(Object source, User user) {
		super(source);
		this.user = user;
	}

}