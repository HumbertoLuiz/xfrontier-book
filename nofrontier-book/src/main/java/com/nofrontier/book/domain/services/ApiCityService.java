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

import com.nofrontier.book.api.v1.controller.CityRestController;
import com.nofrontier.book.domain.exceptions.CityNotFoundException;
import com.nofrontier.book.domain.exceptions.EntityInUseException;
import com.nofrontier.book.domain.exceptions.RequiredObjectIsNullException;
import com.nofrontier.book.domain.exceptions.ResourceNotFoundException;
import com.nofrontier.book.domain.exceptions.StateNotFoundException;
import com.nofrontier.book.domain.model.City;
import com.nofrontier.book.domain.model.State;
import com.nofrontier.book.domain.repository.CityRepository;
import com.nofrontier.book.domain.repository.StateRepository;
import com.nofrontier.book.dto.v1.CityDto;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApiCityService {

	private Logger logger = Logger.getLogger(ApiCityService.class.getName());
	
	private static final String MSG_CITY_IN_USE = "Code city %d cannot be removed because there is a constraint in use";

	@Autowired
	PagedResourcesAssembler<CityDto> assembler;

	@Autowired
	private ModelMapper modelMapper;

	private final CityRepository cityRepository;

	private final StateRepository stateRepository;
	
    @PostConstruct
    public void configureModelMapper() {
        modelMapper.typeMap(City.class, CityDto.class)
                   .addMapping(City::getId, CityDto::setKey);
    }

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public CityDto findById(Long id) {
		logger.info("Finding one city!");
		var entity = cityRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		// Maps the saved entity to CityResponse
		CityDto cityDtoResponse = modelMapper.map(entity,
				CityDto.class);
		cityDtoResponse.add(linkTo(methodOn(CityRestController.class)
				.findById(cityDtoResponse.getKey())).withSelfRel());

		return cityDtoResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public PagedModel<EntityModel<CityDto>> findAll(Pageable pageable) {
		logger.info("Finding all cities!");
		var cityPage = cityRepository.findAll(pageable);
		var cityResponsePage = cityPage.map(
				city -> modelMapper.map(city, CityDto.class));
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
	public CityDto create(CityDto cityDtoRequest) {
		if (cityDtoRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Creating a new city!");

		var entity = modelMapper.map(cityDtoRequest, City.class);

		// Get state by ID from the request
		Long stateId = cityDtoRequest.getStateId();
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
		CityDto cityDtoResponse = modelMapper.map(savedEntity,
				CityDto.class);

		// Add the self link to CityResponse
		cityDtoResponse.add(linkTo(methodOn(CityRestController.class)
				.findById(cityDtoResponse.getKey())).withSelfRel());

		return cityDtoResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public CityDto update(Long id, CityDto cityDtoRequest) {
		if (cityDtoRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Updating one city!");

		var entity = cityRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Updating entity fields with request values
		entity.setName(cityDtoRequest.getName());
		entity.setIbgeCode(cityDtoRequest.getIbgeCode());
		
		var updatedEntity = cityRepository.save(entity);

		// Converting the updated entity to the response
		CityDto cityDtoResponse = modelMapper.map(updatedEntity,
				CityDto.class);
		cityDtoResponse.add(linkTo(methodOn(CityRestController.class)
				.findById(cityDtoResponse.getKey())).withSelfRel());

		return cityDtoResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public void delete(Long id) {
		logger.info("Deleting one city!");
		var entity = cityRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		try {
			cityRepository.delete(entity);
			cityRepository.flush();

		} catch (EmptyResultDataAccessException e) {
			throw new CityNotFoundException(id);

		} catch (DataIntegrityViolationException e) {
			throw new EntityInUseException(String.format(MSG_CITY_IN_USE, id));
		}
	}
	
	// -------------------------------------------------------------------------------------------------------------

}
