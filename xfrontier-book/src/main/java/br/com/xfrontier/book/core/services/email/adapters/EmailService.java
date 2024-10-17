package br.com.xfrontier.book.core.services.email.adapters;

import br.com.xfrontier.book.core.services.email.dtos.EmailParams;

public interface EmailService {

    void sendMailTemplateHtml(EmailParams params);
}
