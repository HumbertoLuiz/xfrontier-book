package br.com.xfrontier.book.infrastructure.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import br.com.xfrontier.book.core.email.EmailProperties;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@Qualifier("sandboxSendEmailService")
public class SandboxSendEmailService extends SmtpSendEmailService {

	@Autowired
	private EmailProperties emailProperties;

	@Override
	protected MimeMessage createMimeMessage(Message message)
			throws MessagingException {
		MimeMessage mimeMessage = super.createMimeMessage(message);

		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
		helper.setTo(emailProperties.getSandbox().getAddressee());

		return mimeMessage;
	}

}