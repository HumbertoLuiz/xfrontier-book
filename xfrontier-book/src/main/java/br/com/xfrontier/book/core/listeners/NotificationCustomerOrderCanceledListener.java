package br.com.xfrontier.book.core.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import br.com.xfrontier.book.core.events.OrderCancelledEvent;
import br.com.xfrontier.book.domain.model.Order;
import br.com.xfrontier.book.domain.services.SendEmailService;
import br.com.xfrontier.book.domain.services.SendEmailService.Message;


@Component
public class NotificationCustomerOrderCanceledListener {

	@Autowired
	private SendEmailService sendEmailService;
	
	@TransactionalEventListener
	public void toCancelOrder(OrderCancelledEvent event) {
		Order order = event.getOrder();
		
		var message = Message.builder()
				.subject(order.getBooks().getClass() + " - Order cancelled")
				.body("emails/order-cancelled.html")
				.variable("order", order)
				.addressee(order.getCustomer().getEmail())
				.build();

		sendEmailService.send(message);
	}
	
}
