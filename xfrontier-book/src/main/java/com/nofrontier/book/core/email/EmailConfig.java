package com.nofrontier.book.core.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nofrontier.book.domain.services.SendEmailService;
import com.nofrontier.book.infrastructure.service.email.FakeSendEmailService;
import com.nofrontier.book.infrastructure.service.email.SandboxSendEmailService;
import com.nofrontier.book.infrastructure.service.email.SmtpSendEmailService;

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