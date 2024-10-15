package com.nofrontier.book.api.v1.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nofrontier.book.domain.services.OrderServiceFlow;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/orders/{orderCode}/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class FlowOrderRestController {

	private final OrderServiceFlow orderServiceFlow;

	@PutMapping("/confirmation")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> confirm(@PathVariable String orderCode) {
		orderServiceFlow.confirm(orderCode);

		return ResponseEntity.noContent().build();
	}

	@PutMapping("/cancellation")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> cancel(@PathVariable String orderCode) {
		orderServiceFlow.cancel(orderCode);

		return ResponseEntity.noContent().build();
	}

	@PutMapping("/deliver")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> deliver(@PathVariable String orderCode) {
		orderServiceFlow.deliver(orderCode);

		return ResponseEntity.noContent().build();
	}

}
