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

import com.nofrontier.book.api.v1.controller.CountryRestController;
import com.nofrontier.book.domain.exceptions.RequiredObjectIsNullException;
import com.nofrontier.book.domain.exceptions.ResourceNotFoundException;
import com.nofrontier.book.domain.model.Country;
import com.nofrontier.book.domain.repository.CountryRepository;
import com.nofrontier.book.dto.v1.requests.CountryRequest;
import com.nofrontier.book.dto.v1.responses.CountryResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApiCountryService {

	private Logger logger = Logger.getLogger(ApiCountryService.class.getName());

	private final CountryRepository countryRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	PagedResourcesAssembler<CountryResponse> assembler;

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public CountryResponse findById(Long id) {
		logger.info("Finding one country!");
		var entity = countryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Maps the saved entity to CountryResponse
		CountryResponse countryResponse = modelMapper.map(entity, CountryResponse.class);
		countryResponse.add(linkTo(methodOn(CountryRestController.class)
				.findById(countryResponse.getKey())).withSelfRel());

		return countryResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public PagedModel<EntityModel<CountryResponse>> findAll(Pageable pageable) {
		logger.info("Finding all countries!");
		var countryPage = countryRepository.findAll(pageable);
		var countryResponsesPage = countryPage
				.map(country -> modelMapper.map(country, CountryResponse.class));
		countryResponsesPage.map(country -> country.add(linkTo(
				methodOn(CountryRestController.class).findById(country.getKey()))
				.withSelfRel()));
		Link link = linkTo(methodOn(CountryRestController.class).findAll(
				pageable.getPageNumber(), pageable.getPageSize(), "asc"))
				.withSelfRel();
		return assembler.toModel(countryResponsesPage, link);
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public CountryResponse create(CountryRequest countryRequest) {
		if (countryRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Creating a new country!");

		// Maps the CountryRequest to the Country entity
		var entity = modelMapper.map(countryRequest, Country.class);

		// Saves the new entity in the database
		var savedEntity = countryRepository.save(entity);

		// Maps the saved entity to CountryResponse
		CountryResponse countryResponse = modelMapper.map(savedEntity,
				CountryResponse.class);
		countryResponse.add(linkTo(methodOn(CountryRestController.class)
				.findById(countryResponse.getKey())).withSelfRel());

		return countryResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public CountryResponse update(Long id, CountryRequest countryRequest) {
		if (countryRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Updating one country!");

		var entity = countryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Updating entity fields with request values
		entity.setName(countryRequest.getName());
		entity.setInitials(countryRequest.getInitials());

		var updatedEntity = countryRepository.save(entity);

		// Converting the updated entity to the response
		CountryResponse countryResponse = modelMapper.map(updatedEntity,
				CountryResponse.class);
		countryResponse.add(linkTo(methodOn(CountryRestController.class)
				.findById(countryResponse.getKey())).withSelfRel());

		return countryResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	public void delete(Long id) {
		logger.info("Deleting one country!");
		var entity = countryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		countryRepository.delete(entity);
	}
}