package com.nofrontier.book.domain.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Set;
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

import com.nofrontier.book.api.v1.controller.PaymentMethodRestController;
import com.nofrontier.book.core.enums.BookStatus;
import com.nofrontier.book.domain.exceptions.EntityInUseException;
import com.nofrontier.book.domain.exceptions.PaymentMethodNotFoundException;
import com.nofrontier.book.domain.exceptions.RequiredObjectIsNullException;
import com.nofrontier.book.domain.exceptions.ResourceNotFoundException;
import com.nofrontier.book.domain.model.PaymentMethod;
import com.nofrontier.book.domain.repository.PaymentMethodRepository;
import com.nofrontier.book.dto.v1.PaymentMethodDto;
import com.nofrontier.book.utils.SecurityUtils;

import jakarta.annotation.PostConstruct;

@Service
public class ApiPaymentMethodService {
	
	private Logger logger = Logger.getLogger(ApiPaymentMethodService.class.getName());

	private static final String MSG_PAYMENT_FORM_IN_USE = "Payment method code %d cannot be removed as it is in use";

	@Autowired
	private PaymentMethodRepository paymentMethodRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private SecurityUtils securityUtils;
	
	@Autowired
	PagedResourcesAssembler<PaymentMethodDto> assembler;
	
    @PostConstruct
    public void configureModelMapper() {
        modelMapper.typeMap(PaymentMethod.class, PaymentMethodDto.class)
                   .addMapping(PaymentMethod::getId, PaymentMethodDto::setKey);
    }

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public PaymentMethodDto findById(Long id) {
		logger.info("Finding one payment!");
		var entity = paymentMethodRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Maps the saved entity to PaymentMethodResponse
		PaymentMethodDto paymentMethodDtoResponse = modelMapper.map(entity, PaymentMethodDto.class);
		paymentMethodDtoResponse.add(linkTo(methodOn(PaymentMethodRestController.class)
				.findById(paymentMethodDtoResponse.getKey())).withSelfRel());

		return paymentMethodDtoResponse;
	}
	
	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public PagedModel<EntityModel<PaymentMethodDto>> findAll(Pageable pageable) {
		logger.info("Finding all payments!");
		var paymentMethodPage = paymentMethodRepository.findAll(pageable);
		var paymentMethodResponsesPage = paymentMethodPage
				.map(paymentMethod -> modelMapper.map(paymentMethod, PaymentMethodDto.class));
		paymentMethodResponsesPage.map(paymentMethod -> paymentMethod.add(linkTo(
				methodOn(PaymentMethodRestController.class).findById(paymentMethod.getKey()))
				.withSelfRel()));
		Link link = linkTo(methodOn(PaymentMethodRestController.class).findAll(
				pageable.getPageNumber(), pageable.getPageSize(), "asc"))
				.withSelfRel();
		return assembler.toModel(paymentMethodResponsesPage, link);
	}
	
	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public PaymentMethodDto create(PaymentMethodDto paymentMethodDtoRequest) {
		if (paymentMethodDtoRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Creating a new payment!");

		// Maps the PaymentMethodResponse to the Book entity
		var entity = modelMapper.map(paymentMethodDtoRequest, PaymentMethod.class);

		// Saves the new entity in the database
		var savedEntity = paymentMethodRepository.save(entity);

		// Maps the saved entity to PaymentMethodResponse
		PaymentMethodDto paymentMethodDtoResponse = modelMapper.map(savedEntity,
				PaymentMethodDto.class);
		paymentMethodDtoResponse.add(linkTo(methodOn(PaymentMethodRestController.class)
				.findById(paymentMethodDtoResponse.getKey())).withSelfRel());

		return paymentMethodDtoResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public PaymentMethodDto update(Long id, PaymentMethodDto paymentMethodDtoRequest) {
		if (paymentMethodDtoRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Updating one payment!");

		var entity = paymentMethodRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Updating entity fields with request values
		entity.setDescription(paymentMethodDtoRequest.getDescription());
		entity.setPaymentStatus(paymentMethodDtoRequest.getPaymentStatus());
		entity.setTransactionId(paymentMethodDtoRequest.getTransactionId());

		var updatedEntity = paymentMethodRepository.save(entity);

		// Converting the updated entity to the response
		PaymentMethodDto paymentMethodDtoResponse = modelMapper.map(updatedEntity,
				PaymentMethodDto.class);
		paymentMethodDtoResponse.add(linkTo(methodOn(PaymentMethodRestController.class)
				.findById(paymentMethodDtoResponse.getKey())).withSelfRel());

		return paymentMethodDtoResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public void delete(Long id) {
		logger.info("Deleting one payment!");
		var entity = paymentMethodRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));		
		try {
			paymentMethodRepository.delete(entity);
			paymentMethodRepository.flush();

		} catch (EmptyResultDataAccessException e) {
			throw new PaymentMethodNotFoundException(id);

		} catch (DataIntegrityViolationException e) {
			throw new EntityInUseException(String
					.format(MSG_PAYMENT_FORM_IN_USE, id));
		}
	}

	// -------------------------------------------------------------------------------------------------------------
	
	public List<PaymentMethodDto> listPayments() {
		var loggedUser = securityUtils.getLoggedUser();
		Set<BookStatus> bookStatus = Set.of(BookStatus.PAID, BookStatus.RATED,
				BookStatus.CANCELLED);

		return paymentMethodRepository
				.findByBooksAndBookStatusIn(loggedUser, bookStatus).stream()
				.map(paymentMethod -> modelMapper.map(paymentMethod,
						PaymentMethodDto.class))
				.toList();
	}

}
