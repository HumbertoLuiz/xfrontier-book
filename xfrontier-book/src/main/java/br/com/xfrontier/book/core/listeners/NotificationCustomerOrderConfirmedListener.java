package br.com.xfrontier.book.core.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import br.com.xfrontier.book.core.events.OrderConfirmedEvent;
import br.com.xfrontier.book.domain.model.Order;
import br.com.xfrontier.book.domain.services.SendEmailService;
import br.com.xfrontier.book.domain.services.SendEmailService.Message;

@Component
public class NotificationCustomerOrderConfirmedListener {

	@Autowired
	private SendEmailService sendEmailService;
	
	@TransactionalEventListener
	public void toConfirmOrder(OrderConfirmedEvent event) {
		Order order = event.getOrder();
		
		var message = Message.builder()
				.subject(order.getBooks().getClass() + " - Order confirmed")
				.body("emails/order-confirmed.html")
				.variable("order", order)
				.addressee(order.getCustomer().getEmail())
				.build();

		sendEmailService.send(message);
	}
	
}
