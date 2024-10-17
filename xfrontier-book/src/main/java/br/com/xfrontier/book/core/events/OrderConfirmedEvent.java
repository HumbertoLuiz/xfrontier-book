package br.com.xfrontier.book.core.events;

import br.com.xfrontier.book.domain.model.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderConfirmedEvent {

	private Order order;

}
