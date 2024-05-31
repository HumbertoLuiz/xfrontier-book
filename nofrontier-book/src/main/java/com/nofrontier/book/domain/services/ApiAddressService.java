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

import com.nofrontier.book.api.v1.controller.AddressRestController;
import com.nofrontier.book.domain.exceptions.AddressNotFoundException;
import com.nofrontier.book.domain.exceptions.CityNotFoundException;
import com.nofrontier.book.domain.exceptions.RequiredObjectIsNullException;
import com.nofrontier.book.domain.exceptions.ResourceNotFoundException;
import com.nofrontier.book.domain.model.Address;
import com.nofrontier.book.domain.model.City;
import com.nofrontier.book.domain.repository.AddressRepository;
import com.nofrontier.book.domain.repository.CityRepository;
import com.nofrontier.book.dto.v1.requests.AddressRequest;
import com.nofrontier.book.dto.v1.responses.AddressResponse;
import com.nofrontier.book.utils.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApiAddressService {

	private Logger logger = Logger.getLogger(ApiUserService.class.getName());

	@Autowired
	PagedResourcesAssembler<AddressResponse> assembler;

	@Autowired
	private ModelMapper modelMapper;

	private final AddressRepository addressRepository;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private SecurityUtils securityUtils;

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public AddressResponse findById(Long id) {
		logger.info("Finding one address!");
		var entity = addressRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		// Maps the saved entity to AddressResponse
		AddressResponse addressResponse = modelMapper.map(entity,
				AddressResponse.class);
		addressResponse.add(linkTo(methodOn(AddressRestController.class)
				.findById(addressResponse.getKey())).withSelfRel());

		return addressResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public PagedModel<EntityModel<AddressResponse>> findAll(Pageable pageable) {
		logger.info("Finding all addresses!");
		var addressPage = addressRepository.findAll(pageable);
		var addressResponsePage = addressPage.map(
				address -> modelMapper.map(address, AddressResponse.class));
		addressResponsePage.map(address -> address
				.add(linkTo(methodOn(AddressRestController.class)
						.findById(address.getKey())).withSelfRel()));
		Link link = linkTo(methodOn(AddressRestController.class).findAll(
				pageable.getPageNumber(), pageable.getPageSize(), "asc"))
				.withSelfRel();
		return assembler.toModel(addressResponsePage, link);
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public AddressResponse create(AddressRequest addressRequest) {
		if (addressRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Creating a new address!");

		var entity = modelMapper.map(addressRequest, Address.class);

		// Get city by ID from the request
		Long cityId = addressRequest.getCityId();
		Optional<City> optionalCity = cityRepository.findById(cityId);
		if (optionalCity.isEmpty()) {
			// Handle case when city with provided ID does not exist
			throw new CityNotFoundException(
					"City not found with ID: " + cityId);
		}
		City city = optionalCity.get();
		entity.setCity(city);

		// Saves the new entity in the database
		var savedEntity = addressRepository.save(entity);

		// Maps the saved entity to AddressResponse
		AddressResponse addressResponse = modelMapper.map(savedEntity,
				AddressResponse.class);

		// Adds the self link to AddressResponse
		addressResponse.add(linkTo(methodOn(AddressRestController.class)
				.findById(addressResponse.getKey())).withSelfRel());

		return addressResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public AddressResponse update(Long id, AddressRequest addressRequest) {
		if (addressRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Updating one address!");

		var entity = addressRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Updating entity fields with request values
		entity.setStreet(addressRequest.getStreet());
		entity.setNumber(addressRequest.getNumber());
		entity.setNeighborhood(addressRequest.getNeighborhood());
		entity.setComplement(addressRequest.getComplement());
		entity.setZipCode(addressRequest.getZipCode());
		entity.setAddressType(addressRequest.getAddressType());
		
		var updatedEntity = addressRepository.save(entity);

		// Converting the updated entity to the response
		AddressResponse addressResponse = modelMapper.map(updatedEntity,
				AddressResponse.class);
		addressResponse.add(linkTo(methodOn(AddressRestController.class)
				.findById(addressResponse.getKey())).withSelfRel());

		return addressResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	public void delete(Long id) {
		logger.info("Deleting one address!");
		var entity = addressRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		addressRepository.delete(entity);
	}
	
	// -------------------------------------------------------------------------------------------------------------

	public AddressResponse displayAddress() {
		var loggedUser = securityUtils.getLoggedUser();
		var address = loggedUser.getPerson().getAddresses();

		if (address == null) {
			var message = String.format("User address %s not found",
					(loggedUser).getEmail());
			throw new AddressNotFoundException(message);
		}
		return modelMapper.map(address, AddressResponse.class);
	}

}
