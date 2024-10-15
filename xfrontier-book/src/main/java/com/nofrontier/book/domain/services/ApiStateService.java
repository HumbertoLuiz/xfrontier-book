package com.nofrontier.book.domain.services;

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

import com.nofrontier.book.api.v1.controller.StateRestController;
import com.nofrontier.book.domain.exceptions.CountryNotFoundException;
import com.nofrontier.book.domain.exceptions.EntityInUseException;
import com.nofrontier.book.domain.exceptions.RequiredObjectIsNullException;
import com.nofrontier.book.domain.exceptions.ResourceNotFoundException;
import com.nofrontier.book.domain.exceptions.StateNotFoundException;
import com.nofrontier.book.domain.model.Country;
import com.nofrontier.book.domain.model.State;
import com.nofrontier.book.domain.repository.CountryRepository;
import com.nofrontier.book.domain.repository.StateRepository;
import com.nofrontier.book.dto.v1.StateDto;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApiStateService {

	private Logger logger = Logger.getLogger(ApiStateService.class.getName());

	private static final String MSG_STATE_IN_USE = "Code state %d cannot be removed because there is a constraint in use";

	@Autowired
	PagedResourcesAssembler<StateDto> assembler;

	@Autowired
	private ModelMapper modelMapper;

	private final StateRepository stateRepository;

	private final CountryRepository countryRepository;

    @PostConstruct
    public void configureModelMapper() {
        modelMapper.typeMap(State.class, StateDto.class)
                   .addMapping(State::getId, StateDto::setKey);
    }
    
	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public StateDto findById(Long id) {
		logger.info("Finding one state!");
		var entity = stateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		// Maps the saved entity to StateResponse
		StateDto stateDtoResponse = modelMapper.map(entity, StateDto.class);
		stateDtoResponse.add(linkTo(methodOn(StateRestController.class)
				.findById(stateDtoResponse.getKey())).withSelfRel());

		return stateDtoResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public PagedModel<EntityModel<StateDto>> findAll(Pageable pageable) {
		logger.info("Finding all states!");
		var statePage = stateRepository.findAll(pageable);
		var stateResponsePage = statePage
				.map(state -> modelMapper.map(state, StateDto.class));
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
	public StateDto create(StateDto stateDtoRequest) {
		if (stateDtoRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Creating a new state!");

		var entity = modelMapper.map(stateDtoRequest, State.class);

		// Get country by ID from the request
		Long countryId = stateDtoRequest.getCountryId();
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
		StateDto stateDtoResponse = modelMapper.map(savedEntity,
				StateDto.class);

		// Adds the self link to StateResponse
		stateDtoResponse.add(linkTo(methodOn(StateRestController.class)
				.findById(stateDtoResponse.getKey())).withSelfRel());

		return stateDtoResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public StateDto update(Long id, StateDto stateDtoRequest) {
		if (stateDtoRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Updating one state!");

		var entity = stateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Updating entity fields with request values
		entity.setName(stateDtoRequest.getName());
		entity.setIbgeCode(stateDtoRequest.getIbgeCode());

		var updatedEntity = stateRepository.save(entity);

		// Converting the updated entity to the response
		StateDto stateDtoResponse = modelMapper.map(updatedEntity,
				StateDto.class);
		stateDtoResponse.add(linkTo(methodOn(StateRestController.class)
				.findById(stateDtoResponse.getKey())).withSelfRel());

		return stateDtoResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public void delete(Long id) {
		logger.info("Deleting one address!");
		var entity = stateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		try {
			stateRepository.delete(entity);
			stateRepository.flush();

		} catch (EmptyResultDataAccessException e) {
			throw new StateNotFoundException(id);

		} catch (DataIntegrityViolationException e) {
			throw new EntityInUseException(String.format(MSG_STATE_IN_USE, id));
		}
	}
	
	// -------------------------------------------------------------------------------------------------------------

}
