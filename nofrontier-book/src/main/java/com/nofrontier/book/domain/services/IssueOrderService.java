package com.nofrontier.book.domain.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.logging.Logger;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nofrontier.book.api.v1.controller.OrderRestController;
import com.nofrontier.book.domain.exceptions.BusinessException;
import com.nofrontier.book.domain.exceptions.OrderNotFoundException;
import com.nofrontier.book.domain.exceptions.RequiredObjectIsNullException;
import com.nofrontier.book.domain.exceptions.ResourceNotFoundException;
import com.nofrontier.book.domain.model.Order;
import com.nofrontier.book.domain.model.Product;
import com.nofrontier.book.domain.repository.OrderRepository;
import com.nofrontier.book.dto.v1.requests.OrderRequest;
import com.nofrontier.book.dto.v1.responses.BookResponse;
import com.nofrontier.book.dto.v1.responses.CityResponse;
import com.nofrontier.book.dto.v1.responses.OrderResponse;
import com.nofrontier.book.dto.v1.responses.PaymentMethodResponse;
import com.nofrontier.book.dto.v1.responses.UserResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IssueOrderService {

	private Logger logger = Logger.getLogger(ApiGroupService.class.getName());

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ApiBookService apiBookService;

	@Autowired
	private ApiCityService apiCityService;

	@Autowired
	private ApiUserService apiUserService;

	@Autowired
	private ApiProductService apiProductService;

	@Autowired
	private ApiPaymentMethodService apiPaymentMethodService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	PagedResourcesAssembler<OrderResponse> assembler;

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public OrderResponse findById(Long id) {
		logger.info("Finding one order!");
		var entity = orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Maps the saved entity to OrderResponse
		OrderResponse orderResponse = modelMapper.map(entity,
				OrderResponse.class);
		orderResponse.add(linkTo(methodOn(OrderRestController.class)
				.findById(orderResponse.getKey())).withSelfRel());

		return orderResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public PagedModel<EntityModel<OrderResponse>> findAll(Pageable pageable) {
		logger.info("Finding all orders!");
		var orderPage = orderRepository.findAll(pageable);
		var orderResponsesPage = orderPage
				.map(order -> modelMapper.map(order, OrderResponse.class));
		orderResponsesPage.map(order -> order.add(linkTo(
				methodOn(OrderRestController.class).findById(order.getKey()))
				.withSelfRel()));
		Link link = linkTo(methodOn(OrderRestController.class).findAll(
				pageable.getPageNumber(), pageable.getPageSize(), "asc"))
				.withSelfRel();
		return assembler.toModel(orderResponsesPage, link);
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public OrderResponse create(OrderRequest orderRequest) {
		if (orderRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Creating a new order!");

		// Maps the OrderRequest to the Order entity
		var entity = modelMapper.map(orderRequest, Order.class);

		// Saves the new entity in the database
		var savedEntity = orderRepository.save(entity);

		// Maps the saved entity to OrderResponse
		OrderResponse orderResponse = modelMapper.map(savedEntity,
				OrderResponse.class);
		orderResponse.add(linkTo(methodOn(OrderRestController.class)
				.findById(orderResponse.getKey())).withSelfRel());

		return orderResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public OrderResponse update(Long id, OrderRequest orderRequest) {
		if (orderRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Updating one order!");

		var entity = orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Updating entity fields with request values
		entity.setCode(orderRequest.getCode());
		entity.setSubtotal(orderRequest.getSubtotal());
		entity.setShippingRate(orderRequest.getShippingRate());
		entity.setTotalValue(orderRequest.getTotalValue());
		entity.setOrderStatus(orderRequest.getOrderStatus());
		entity.setConfirmationDate(orderRequest.getConfirmationDate());
		entity.setCancellationDate(orderRequest.getCancellationDate());
		entity.setDeliveryDate(orderRequest.getDeliveryDate());

		var updatedEntity = orderRepository.save(entity);

		// Converting the updated entity to the response
		OrderResponse orderResponse = modelMapper.map(updatedEntity,
				OrderResponse.class);
		orderResponse.add(linkTo(methodOn(OrderRestController.class)
				.findById(orderResponse.getKey())).withSelfRel());

		return orderResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	public void delete(Long id) {
		logger.info("Deleting one order!");
		var entity = orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		orderRepository.delete(entity);
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public Order issue(OrderResponse order) {
		validateOrder(order);
		validateItems(order);

		order.setShippingRate(order.getBooks());
		order.calculateTotalValue();

		return orderRepository.save(order);
	}

	private void validateOrder(OrderResponse order) {
		CityResponse city = apiCityService
				.findById(order.getShippingAddress().getCity().getKey());
		EntityModel<UserResponse> customer = apiUserService
				.findById(order.getCustomer().getKey());
		BookResponse book = apiBookService.findById(order.getBooks());
		PaymentMethodResponse paymentMethod = (PaymentMethodResponse) apiPaymentMethodService
				.findById(order.getPaymentMethod().getKey());

		order.getShippingAddress().setCity(city);
		order.setCustomer(customer);
		order.setBooks(book);
		order.setPaymentMethod(paymentMethod);

		if (book.doesntAcceptPaymentForm(paymentMethod)) {
			throw new BusinessException(String.format(
					"Payment method '%s' is not accepted by this book.",
					paymentMethod.getDescription()));
		}
	}

	private void validateItems(OrderResponse order) {
		order.getItems().forEach(item -> {
			Product product = apiProductService.findOrFail(order.getBooks(),
					item.getProduct().getId());

			item.setOrder(order);
			item.setProduct(product);
			item.setUnitPrice(product.getPrice());
		});
	}

	public Order findOrFail(String orderCode) {
		return orderRepository.findByCode(orderCode)
				.orElseThrow(() -> new OrderNotFoundException(orderCode));
	}

}
