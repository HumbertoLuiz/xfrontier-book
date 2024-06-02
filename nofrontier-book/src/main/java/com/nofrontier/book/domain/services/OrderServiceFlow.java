package com.nofrontier.book.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nofrontier.book.domain.model.Order;
import com.nofrontier.book.domain.repository.OrderRepository;

@Service
public class OrderServiceFlow {

	@Autowired
	private IssueOrderService issueOrder;

	@Autowired
	private OrderRepository orderRepository;

	@Transactional
	public void confirm(String orderCode) {
		Order order = issueOrder.findOrFail(orderCode);
		order.confirm();

		orderRepository.save(order);
	}

	@Transactional
	public void cancel(String codigoOrder) {
		Order order = issueOrder.findOrFail(codigoOrder);
		order.cancel();

		orderRepository.save(order);
	}

	@Transactional
	public void deliver(String codigoOrder) {
		Order order = issueOrder.findOrFail(codigoOrder);
		order.deliver();
	}

}
