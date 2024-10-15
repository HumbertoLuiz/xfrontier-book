package com.nofrontier.book.core.services.email.adapters;

import com.nofrontier.book.core.services.email.dtos.EmailParams;

public interface EmailService {

    void sendMailTemplateHtml(EmailParams params);
}
