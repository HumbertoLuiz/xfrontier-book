package com.nofrontier.book.core.enums;


import java.util.Arrays;
import java.util.List;

public enum OrderStatus {

	CREATED("Created"), 
	CONFIRMED("Confirmed", CREATED), 
	DELIVERED("Delivered", CONFIRMED),
	CANCELLED("Cancelled", CREATED);

	private String description;
	private List<OrderStatus> previousStatus;

	OrderStatus(String description, OrderStatus... previousStatus) {
		this.description = description;
		this.previousStatus = Arrays.asList(previousStatus);
	}

	public String getDescription() {
		return this.description;
	}

	public boolean cannotChangeTo(OrderStatus newStatus) {
		return !newStatus.previousStatus.contains(this);
	}

	public boolean canChangeTo(OrderStatus newStatus) {
		return !cannotChangeTo(newStatus);
	}

}