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

import com.nofrontier.book.api.v1.controller.CityRestController;
import com.nofrontier.book.domain.exceptions.RequiredObjectIsNullException;
import com.nofrontier.book.domain.exceptions.ResourceNotFoundException;
import com.nofrontier.book.domain.exceptions.StateNotFoundException;
import com.nofrontier.book.domain.model.City;
import com.nofrontier.book.domain.model.State;
import com.nofrontier.book.domain.repository.CityRepository;
import com.nofrontier.book.domain.repository.StateRepository;
import com.nofrontier.book.dto.v1.requests.CityRequest;
import com.nofrontier.book.dto.v1.responses.CityResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApiCityService {

	private Logger logger = Logger.getLogger(ApiCityService.class.getName());

	@Autowired
	PagedResourcesAssembler<CityResponse> assembler;

	@Autowired
	private ModelMapper modelMapper;

	private final CityRepository cityRepository;

	private final StateRepository stateRepository;

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public CityResponse findById(Long id) {
		logger.info("Finding one city!");
		var entity = cityRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		// Maps the saved entity to CityResponse
		CityResponse cityResponse = modelMapper.map(entity,
				CityResponse.class);
		cityResponse.add(linkTo(methodOn(CityRestController.class)
				.findById(cityResponse.getKey())).withSelfRel());

		return cityResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public PagedModel<EntityModel<CityResponse>> findAll(Pageable pageable) {
		logger.info("Finding all cities!");
		var cityPage = cityRepository.findAll(pageable);
		var cityResponsePage = cityPage.map(
				city -> modelMapper.map(city, CityResponse.class));
		cityResponsePage.map(city -> city
				.add(linkTo(methodOn(CityRestController.class)
						.findById(city.getKey())).withSelfRel()));
		Link link = linkTo(methodOn(CityRestController.class).findAll(
				pageable.getPageNumber(), pageable.getPageSize(), "asc"))
				.withSelfRel();
		return assembler.toModel(cityResponsePage, link);
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public CityResponse create(CityRequest cityRequest) {
		if (cityRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Creating a new city!");

		var entity = modelMapper.map(cityRequest, City.class);

		// Get state by ID from the request
		Long stateId = cityRequest.getStateId();
		Optional<State> optionalState = stateRepository.findById(stateId);
		if (optionalState.isEmpty()) {
			// Handle case when state with provided ID does not exist
			throw new StateNotFoundException(
					"State not found with ID: " + stateId);
		}
		State state = optionalState.get();
		entity.setState(state);

		// Saves the new entity in the database
		var savedEntity = cityRepository.save(entity);

		// Maps the saved entity to CityResponse
		CityResponse cityResponse = modelMapper.map(savedEntity,
				CityResponse.class);

		// Add the self link to CityResponse
		cityResponse.add(linkTo(methodOn(CityRestController.class)
				.findById(cityResponse.getKey())).withSelfRel());

		return cityResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public CityResponse update(Long id, CityRequest cityRequest) {
		if (cityRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Updating one city!");

		var entity = cityRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Updating entity fields with request values
		entity.setName(cityRequest.getName());
		entity.setIbgeCode(cityRequest.getIbgeCode());
		
		var updatedEntity = cityRepository.save(entity);

		// Converting the updated entity to the response
		CityResponse cityResponse = modelMapper.map(updatedEntity,
				CityResponse.class);
		cityResponse.add(linkTo(methodOn(CityRestController.class)
				.findById(cityResponse.getKey())).withSelfRel());

		return cityResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	public void delete(Long id) {
		logger.info("Deleting one city!");
		var entity = cityRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		cityRepository.delete(entity);
	}
	
	// -------------------------------------------------------------------------------------------------------------

}
