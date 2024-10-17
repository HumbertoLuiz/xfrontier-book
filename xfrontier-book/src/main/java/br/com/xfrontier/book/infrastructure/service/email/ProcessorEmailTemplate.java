package br.com.xfrontier.book.infrastructure.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import br.com.xfrontier.book.domain.services.SendEmailService.Message;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Component
public class ProcessorEmailTemplate {

	@Autowired
	private Configuration freemarkerConfig;

	protected String processTemplate(Message message) {
		try {
			Template template = freemarkerConfig.getTemplate(message.getBody());

			return FreeMarkerTemplateUtils.processTemplateIntoString(
					template, message.getVariables());
		} catch (Exception e) {
			throw new EmailException("Email template could not be set up", e);
		}
	}

}