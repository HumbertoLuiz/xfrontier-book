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

import com.nofrontier.book.api.v1.controller.AddressRestController;
import com.nofrontier.book.domain.exceptions.AddressNotFoundException;
import com.nofrontier.book.domain.exceptions.CityNotFoundException;
import com.nofrontier.book.domain.exceptions.EntityInUseException;
import com.nofrontier.book.domain.exceptions.PersonNotFoundException;
import com.nofrontier.book.domain.exceptions.RequiredObjectIsNullException;
import com.nofrontier.book.domain.exceptions.ResourceNotFoundException;
import com.nofrontier.book.domain.model.Address;
import com.nofrontier.book.domain.model.City;
import com.nofrontier.book.domain.model.Person;
import com.nofrontier.book.domain.repository.AddressRepository;
import com.nofrontier.book.domain.repository.CityRepository;
import com.nofrontier.book.domain.repository.PersonRepository;
import com.nofrontier.book.dto.v1.AddressDto;
import com.nofrontier.book.utils.SecurityUtils;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApiAddressService {

	private Logger logger = Logger.getLogger(ApiAddressService.class.getName());

	private static final String MSG_ADDRESS_IN_USE = "Code address %d cannot be removed because there is a constraint in use";

	
	@Autowired
	PagedResourcesAssembler<AddressDto> assembler;

	@Autowired
	private ModelMapper modelMapper;

	private final AddressRepository addressRepository;

	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private SecurityUtils securityUtils;
	
	@PostConstruct
	public void configureModelMapper() {
	    modelMapper.typeMap(Address.class, AddressDto.class)
	               .addMapping(Address::getId, AddressDto::setKey)
	               .addMapping(src -> src.getCity().getId(), AddressDto::setCityId)
	               .addMapping(src -> src.getPerson().getId(), AddressDto::setPersonId);
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public AddressDto findById(Long id) {
		logger.info("Finding one address!");
		var entity = addressRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		// Maps the saved entity to AddressResponse
		AddressDto addressDto = modelMapper.map(entity,
				AddressDto.class);
		addressDto.add(linkTo(methodOn(AddressRestController.class)
				.findById(addressDto.getKey())).withSelfRel());

		return addressDto;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public PagedModel<EntityModel<AddressDto>> findAll(Pageable pageable) {
		logger.info("Finding all addresses!");
		var addressPage = addressRepository.findAll(pageable);
		var addressDtoPage = addressPage.map(
				address -> modelMapper.map(address, AddressDto.class));
		addressDtoPage.map(address -> address
				.add(linkTo(methodOn(AddressRestController.class)
						.findById(address.getKey())).withSelfRel()));
		Link link = linkTo(methodOn(AddressRestController.class).findAll(
				pageable.getPageNumber(), pageable.getPageSize(), "asc"))
				.withSelfRel();
		return assembler.toModel(addressDtoPage, link);
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public AddressDto create(AddressDto addressDtoRequest) {
		if (addressDtoRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Creating a new address!");

		var entity = modelMapper.map(addressDtoRequest, Address.class);

		// Get city by ID from the request
		Long cityId = addressDtoRequest.getCityId();
		Optional<City> optionalCity = cityRepository.findById(cityId);
		if (optionalCity.isEmpty()) {
			// Handle case when city with provided ID does not exist
			throw new CityNotFoundException(
					"City not found with ID: " + cityId);
		}
		City city = optionalCity.get();
		entity.setCity(city);

		// Get person by ID from the request
		Long personId = addressDtoRequest.getPersonId();
		Optional<Person> optionalPerson = personRepository.findById(personId);
		if (optionalPerson.isEmpty()) {
			// Handle case when person with provided ID does not exist
			throw new PersonNotFoundException(
					"Person not found with ID: " + personId);
		}
		Person person = optionalPerson.get();
		entity.setPerson(person);
		
		// Saves the new entity in the database
		var savedEntity = addressRepository.save(entity);

		// Maps the saved entity to AddressResponse
		AddressDto addressDtoResponse = modelMapper.map(savedEntity,
				AddressDto.class);

		// Adds the self link to AddressResponse
		addressDtoResponse.add(linkTo(methodOn(AddressRestController.class)
				.findById(addressDtoResponse.getKey())).withSelfRel());

		return addressDtoResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public AddressDto update(Long id, AddressDto addressDtoRequest) {
		if (addressDtoRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Updating one address!");

		var entity = addressRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Updating entity fields with request values
		entity.setStreet(addressDtoRequest.getStreet());
		entity.setNumber(addressDtoRequest.getNumber());
		entity.setNeighborhood(addressDtoRequest.getNeighborhood());
		entity.setComplement(addressDtoRequest.getComplement());
		entity.setZipCode(addressDtoRequest.getZipCode());
		entity.setAddressType(addressDtoRequest.getAddressType());
		
		var updatedEntity = addressRepository.save(entity);

		// Converting the updated entity to the response
		AddressDto addressDtoResponse = modelMapper.map(updatedEntity,
				AddressDto.class);
		addressDtoResponse.add(linkTo(methodOn(AddressRestController.class)
				.findById(addressDtoResponse.getKey())).withSelfRel());

		return addressDtoResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public void delete(Long id) {
		logger.info("Deleting one address!");
		var entity = addressRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		try {
			addressRepository.delete(entity);
			addressRepository.flush();

		} catch (EmptyResultDataAccessException e) {
			throw new AddressNotFoundException(id);

		} catch (DataIntegrityViolationException e) {
			throw new EntityInUseException(String.format(MSG_ADDRESS_IN_USE, id));
		}
	}
	
	
	// -------------------------------------------------------------------------------------------------------------

	public AddressDto displayAddress() {
		var loggedUser = securityUtils.getLoggedUser();
		var address = loggedUser.getPerson().getAddresses();

		if (address == null) {
			var message = String.format("User address %s not found",
					(loggedUser).getEmail());
			throw new AddressNotFoundException(message);
		}
		return modelMapper.map(address, AddressDto.class);
	}

}
