package br.com.xfrontier.book.core.listeners;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import br.com.xfrontier.book.core.events.NewUserEvent;
import br.com.xfrontier.book.core.services.email.adapters.EmailService;
import br.com.xfrontier.book.core.services.email.dtos.EmailParams;
import br.com.xfrontier.book.domain.model.User;

@Component
public class NewUserListener {

    @Autowired
    private EmailService emailService;

    @EventListener
    public void handleNewUserEvent(NewUserEvent event) {

        var user= event.getUser();

        if (user.isCustomer()) {
            var emailParams= createEmailParams(user);
            emailService.sendMailTemplateHtml(emailParams);
        }
    }

    private EmailParams createEmailParams(User user) {
        var props= createEmailProps(user);
        var emailParams= new EmailParams();
        emailParams.setAddressee(user.getEmail());
        emailParams.setSubject("The registration has been successful completed");
        emailParams.setTemplate("emails/welcome");
        emailParams.setProps(props);
        return emailParams;
    }

    private HashMap<String, Object> createEmailProps(User user) {
        var props= new HashMap<String, Object>();
        props.put("name", user.getCompleteName());
        props.put("userType", user.getUserType().name());
        return props;
    }
}
