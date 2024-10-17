package br.com.xfrontier.book.domain.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Optional;
import java.util.logging.Logger;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.xfrontier.book.api.v1.controller.OrderRestController;
import br.com.xfrontier.book.domain.exceptions.AddressNotFoundException;
import br.com.xfrontier.book.domain.exceptions.BusinessException;
import br.com.xfrontier.book.domain.exceptions.EntityInUseException;
import br.com.xfrontier.book.domain.exceptions.OrderNotFoundException;
import br.com.xfrontier.book.domain.exceptions.PaymentMethodNotFoundException;
import br.com.xfrontier.book.domain.exceptions.RequiredObjectIsNullException;
import br.com.xfrontier.book.domain.exceptions.ResourceNotFoundException;
import br.com.xfrontier.book.domain.exceptions.UserNotFoundException;
import br.com.xfrontier.book.domain.model.Address;
import br.com.xfrontier.book.domain.model.Order;
import br.com.xfrontier.book.domain.model.PaymentMethod;
import br.com.xfrontier.book.domain.model.Product;
import br.com.xfrontier.book.domain.model.User;
import br.com.xfrontier.book.domain.repository.AddressRepository;
import br.com.xfrontier.book.domain.repository.OrderRepository;
import br.com.xfrontier.book.domain.repository.PaymentMethodRepository;
import br.com.xfrontier.book.domain.repository.UserRepository;
import br.com.xfrontier.book.dto.v1.BookDto;
import br.com.xfrontier.book.dto.v1.CityDto;
import br.com.xfrontier.book.dto.v1.OrderDto;
import br.com.xfrontier.book.dto.v1.PaymentMethodDto;
import br.com.xfrontier.book.dto.v1.UserDto;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IssueOrderService {

	private Logger logger = Logger.getLogger(ApiGroupService.class.getName());

	private static final String MSG_GROUP_IN_USE = "Code order %d cannot be removed as it is in use";

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private PaymentMethodRepository paymentMethodRepository;
	
	@Autowired
	private UserRepository userRepository;

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
	PagedResourcesAssembler<OrderDto> assembler;
	
    @PostConstruct
    public void configureModelMapper() {
        modelMapper.typeMap(Order.class, OrderDto.class)
                   .addMapping(Order::getId, OrderDto::setKey)
        .addMapping(src -> src.getShippingAddress().getId(), OrderDto::setShippingAddressId)
        .addMapping(src -> src.getPaymentMethod().getId(), OrderDto::setPaymentMethodId)
        .addMapping(src -> src.getCustomer().getId(), OrderDto::setCustomerId);
    }
 

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public OrderDto findById(Long id) {
		logger.info("Finding one order!");
		var entity = orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Maps the saved entity to OrderResponse
		OrderDto orderDtoResponse = modelMapper.map(entity,
				OrderDto.class);
		orderDtoResponse.add(linkTo(methodOn(OrderRestController.class)
				.findById(orderDtoResponse.getKey())).withSelfRel());

		return orderDtoResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public PagedModel<EntityModel<OrderDto>> findAll(Pageable pageable) {
		logger.info("Finding all orders!");
		var orderPage = orderRepository.findAll(pageable);
		var orderResponsesPage = orderPage
				.map(order -> modelMapper.map(order, OrderDto.class));
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
	public OrderDto create(OrderDto orderDtoRequest) {
		if (orderDtoRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Creating a new order!");

		// Maps the OrderRequest to the Order entity
		var entity = modelMapper.map(orderDtoRequest, Order.class);

		// Get address by ID from the request
		Long addressId = orderDtoRequest.getShippingAddressId();
		Optional<Address> optionalAddress = addressRepository.findById(addressId);
		if (optionalAddress.isEmpty()) {
			// Handle case when address with provided ID does not exist
			throw new AddressNotFoundException(
					"Address not found with ID: " + addressId);
		}
		Address address = optionalAddress.get();
		entity.setShippingAddress(address);

		// Get payment by ID from the request
		Long paymentId = orderDtoRequest.getPaymentMethodId();
		Optional<PaymentMethod> optionalPayment = paymentMethodRepository.findById(paymentId);
		if (optionalPayment.isEmpty()) {
			// Handle case when payment with provided ID does not exist
			throw new PaymentMethodNotFoundException(
					"Payment not found with ID: " + paymentId);
		}
		PaymentMethod payment = optionalPayment.get();
		entity.setPaymentMethod(payment);
		
		// Get customer by ID from the request
		Long customerId = orderDtoRequest.getCustomerId();
		Optional<User> optionalCustomer = userRepository.findById(customerId);
		if (optionalCustomer.isEmpty()) {
			// Handle case when customer with provided ID does not exist
			throw new UserNotFoundException(
					"Customer not found with ID: " + customerId);
		}
		User customer = optionalCustomer.get();
		entity.setCustomer(customer);
		
		
		// Saves the new entity in the database
		var savedEntity = orderRepository.save(entity);

		// Maps the saved entity to OrderResponse
		OrderDto orderDtoResponse = modelMapper.map(savedEntity,
				OrderDto.class);
		orderDtoResponse.add(linkTo(methodOn(OrderRestController.class)
				.findById(orderDtoResponse.getKey())).withSelfRel());

		return orderDtoResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public OrderDto update(Long id, OrderDto orderDtoRequest) {
		if (orderDtoRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Updating one order!");

		var entity = orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Updating entity fields with request values
		entity.setSubtotal(orderDtoRequest.getSubtotal());
		entity.setShippingRate(orderDtoRequest.getShippingRate());
		entity.setTotalValue(orderDtoRequest.getTotalValue());
		entity.setOrderStatus(orderDtoRequest.getOrderStatus());

		var updatedEntity = orderRepository.save(entity);

		// Converting the updated entity to the response
		OrderDto orderDtoResponse = modelMapper.map(updatedEntity,
				OrderDto.class);
		orderDtoResponse.add(linkTo(methodOn(OrderRestController.class)
				.findById(orderDtoResponse.getKey())).withSelfRel());

		return orderDtoResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public void delete(Long id) {
		logger.info("Deleting one order!");
		var entity = orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		try {
			orderRepository.delete(entity);
			orderRepository.flush();

		} catch (EmptyResultDataAccessException e) {
			throw new PaymentMethodNotFoundException(id);

		} catch (DataIntegrityViolationException e) {
			throw new EntityInUseException(String.format(MSG_GROUP_IN_USE, id));
		}
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public Order issue(OrderDto order) {
		validateOrder(order);
		validateItems(order);

		order.getBooks();
		order.calculateTotalValue();

		return orderRepository.save(order);
	}

	private void validateOrder(OrderDto order) {
		CityDto city = apiCityService
				.findById(order.getShippingAddress().getCity().getKey());
		UserDto customer = apiUserService
				.findById(order.getCustomer().getKey());
		BookDto book = apiBookService.findById(order.getBooks());
		PaymentMethodDto paymentMethod = (PaymentMethodDto) apiPaymentMethodService
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

	private void validateItems(OrderDto order) {
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
