package com.nofrontier.book.core.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.nofrontier.book.core.events.OrderConfirmedEvent;
import com.nofrontier.book.domain.model.Order;
import com.nofrontier.book.domain.services.SendEmailService;
import com.nofrontier.book.domain.services.SendEmailService.Message;

@Component
public class NotificationCustomerOrderConfirmedListener {

	@Autowired
	private SendEmailService sendEmail;
	
	@TransactionalEventListener
	public void toConfirmOrder(OrderConfirmedEvent event) {
		Order order = event.getOrder();
		
		var message = Message.builder()
				.subject(order.getBooks().getClass() + " - Order confirmed")
				.body("emails/order-confirmed.html")
				.variable("order", order)
				.addressee(order.getCustomer().getEmail())
				.build();

		sendEmail.send(message);
	}
	
}
