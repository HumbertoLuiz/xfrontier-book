package com.nofrontier.book.domain.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Optional;
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

import com.nofrontier.book.api.v1.controller.StateRestController;
import com.nofrontier.book.domain.exceptions.CountryNotFoundException;
import com.nofrontier.book.domain.exceptions.RequiredObjectIsNullException;
import com.nofrontier.book.domain.exceptions.ResourceNotFoundException;
import com.nofrontier.book.domain.model.Country;
import com.nofrontier.book.domain.model.State;
import com.nofrontier.book.domain.repository.CountryRepository;
import com.nofrontier.book.domain.repository.StateRepository;
import com.nofrontier.book.dto.v1.requests.StateRequest;
import com.nofrontier.book.dto.v1.responses.StateResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApiStateService {

	private Logger logger = Logger.getLogger(ApiStateService.class.getName());

	@Autowired
	PagedResourcesAssembler<StateResponse> assembler;

	@Autowired
	private ModelMapper modelMapper;

	private final StateRepository stateRepository;

	private final CountryRepository countryRepository;

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public StateResponse findById(Long id) {
		logger.info("Finding one state!");
		var entity = stateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		// Maps the saved entity to StateResponse
		StateResponse stateResponse = modelMapper.map(entity, StateResponse.class);
		stateResponse.add(linkTo(methodOn(StateRestController.class)
				.findById(stateResponse.getKey())).withSelfRel());

		return stateResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public PagedModel<EntityModel<StateResponse>> findAll(Pageable pageable) {
		logger.info("Finding all states!");
		var statePage = stateRepository.findAll(pageable);
		var stateResponsePage = statePage
				.map(state -> modelMapper.map(state, StateResponse.class));
		stateResponsePage.map(state -> state.add(linkTo(
				methodOn(StateRestController.class).findById(state.getKey()))
				.withSelfRel()));
		Link link = linkTo(methodOn(StateRestController.class).findAll(
				pageable.getPageNumber(), pageable.getPageSize(), "asc"))
				.withSelfRel();
		return assembler.toModel(stateResponsePage, link);
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public StateResponse create(StateRequest stateRequest) {
		if (stateRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Creating a new state!");

		var entity = modelMapper.map(stateRequest, State.class);

		// Get country by ID from the request
		Long countryId = stateRequest.getCountryId();
		Optional<Country> optionalCountry = countryRepository.findById(countryId);
		if (optionalCountry.isEmpty()) {
			// Handle case when state with provided ID does not exist
			throw new CountryNotFoundException(
					"State not found with ID: " + countryId);
		}
		Country country = optionalCountry.get();
		entity.setCountry(country);

		// Saves the new entity in the database
		var savedEntity = stateRepository.save(entity);

		// Maps the saved entity to StateResponse
		StateResponse stateResponse = modelMapper.map(savedEntity,
				StateResponse.class);

		// Adds the self link to StateResponse
		stateResponse.add(linkTo(methodOn(StateRestController.class)
				.findById(stateResponse.getKey())).withSelfRel());

		return stateResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public StateResponse update(Long id, StateRequest stateRequest) {
		if (stateRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Updating one state!");

		var entity = stateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Updating entity fields with request values
		entity.setName(stateRequest.getName());
		entity.setIbgeCode(stateRequest.getIbgeCode());

		var updatedEntity = stateRepository.save(entity);

		// Converting the updated entity to the response
		StateResponse stateResponse = modelMapper.map(updatedEntity,
				StateResponse.class);
		stateResponse.add(linkTo(methodOn(StateRestController.class)
				.findById(stateResponse.getKey())).withSelfRel());

		return stateResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	public void delete(Long id) {
		logger.info("Deleting one state!");
		var entity = stateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		stateRepository.delete(entity);
	}

	// -------------------------------------------------------------------------------------------------------------

}
