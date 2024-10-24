package br.com.xfrontier.book.api.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.xfrontier.book.domain.services.OrderServiceFlow;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080", "https://xfrontier.com.br"})
@RestController
@RequestMapping(path = "/api/orders/{orderCode}/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class FlowOrderRestController {

	@Autowired
	private OrderServiceFlow orderServiceFlow;

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
