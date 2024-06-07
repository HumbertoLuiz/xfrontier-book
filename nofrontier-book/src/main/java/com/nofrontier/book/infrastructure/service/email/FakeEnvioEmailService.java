package com.nofrontier.book.infrastructure.service.email;

import org.springframework.beans.factory.annotation.Autowired;

import com.nofrontier.book.domain.services.SendEmailService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FakeEnvioEmailService implements SendEmailService {

	@Autowired
	private ProcessorEmailTemplate processadorEmailTemplate;

	@Override
	public void send(Message message) {
		String body = processadorEmailTemplate.processTemplate(message);

		log.info("[FAKE E-MAIL] Para: {}\n{}", message.getAddressees(), body);
	}

}
