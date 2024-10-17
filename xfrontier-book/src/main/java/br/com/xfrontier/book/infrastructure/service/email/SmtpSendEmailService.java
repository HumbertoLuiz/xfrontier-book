package br.com.xfrontier.book.infrastructure.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import br.com.xfrontier.book.core.email.EmailProperties;
import br.com.xfrontier.book.domain.services.SendEmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@Qualifier("smtpSendEmailService")
public class SmtpSendEmailService implements SendEmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private EmailProperties emailProperties;

	@Autowired
	private ProcessorEmailTemplate processorEmailTemplate;

	@Override
	public void send(Message message) {
		try {
			MimeMessage mimeMessage = createMimeMessage(message);

			mailSender.send(mimeMessage);
		} catch (Exception e) {
			throw new EmailException("Unable to send e-mail", e);
		}
	}

	protected MimeMessage createMimeMessage(Message message)
			throws MessagingException {
		String body = processorEmailTemplate.processTemplate(message);

		MimeMessage mimeMessage = mailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
		helper.setFrom(emailProperties.getSender());
		helper.setTo(message.getAddressees().toArray(new String[0]));
		helper.setSubject(message.getSubject());
		helper.setText(body, true);

		return mimeMessage;
	}

}