package com.nofrontier.book.domain.services;

import java.util.Map;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

public interface SendEmailService {

	void send(Message message);

	@Getter
	@Builder
	class Message {

		@Singular
		private Set<String> addressees;

		@NonNull
		private String subject;

		@NonNull
		private String body;

		@Singular("variable")
		private Map<String, Object> variables;

	}

}
