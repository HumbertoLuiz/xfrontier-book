package com.nofrontier.book.core.events;

import com.nofrontier.book.domain.model.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderConfirmedEvent {

	private Order order;

}
