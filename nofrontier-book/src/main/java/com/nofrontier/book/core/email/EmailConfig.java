package com.nofrontier.book.core.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nofrontier.book.domain.services.SendEmailService;
import com.nofrontier.book.infrastructure.service.email.FakeEnvioEmailService;
import com.nofrontier.book.infrastructure.service.email.SandboxEnvioEmailService;
import com.nofrontier.book.infrastructure.service.email.SmtpEnvioEmailService;

@Configuration
public class EmailConfig {

	@Autowired
	private EmailProperties emailProperties;

	@Bean
	SendEmailService envioEmailService() {
		switch (emailProperties.getImpl()) {
			case FAKE:
				return new FakeEnvioEmailService();
			case SMTP:
				return new SmtpEnvioEmailService();
			case SANDBOX:
				return new SandboxEnvioEmailService();
			default:
				return null;
		}
	}

}