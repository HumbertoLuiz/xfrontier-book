package br.com.xfrontier.book.core.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.xfrontier.book.domain.services.SendEmailService;
import br.com.xfrontier.book.infrastructure.service.email.FakeSendEmailService;
import br.com.xfrontier.book.infrastructure.service.email.SandboxSendEmailService;
import br.com.xfrontier.book.infrastructure.service.email.SmtpSendEmailService;

@Configuration
public class EmailConfig {

	@Autowired
	private EmailProperties emailProperties;

	@Bean
	SendEmailService sendEmailService() {
		switch (emailProperties.getImpl()) {
			case FAKE:
				return new FakeSendEmailService();
			case SMTP:
				return new SmtpSendEmailService();
			case SANDBOX:
				return new SandboxSendEmailService();
			default:
				return null;
		}
	}

}