package br.com.xfrontier.book.infrastructure.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import br.com.xfrontier.book.domain.services.SendEmailService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Qualifier("fakeSendEmailService")
public class FakeSendEmailService implements SendEmailService {

	@Autowired
	private ProcessorEmailTemplate processorEmailTemplate;

	@Override
	public void send(Message message) {
		String body = processorEmailTemplate.processTemplate(message);

		log.info("[FAKE E-MAIL] To: {}\n{}", message.getAddressees(), body);
	}

}
