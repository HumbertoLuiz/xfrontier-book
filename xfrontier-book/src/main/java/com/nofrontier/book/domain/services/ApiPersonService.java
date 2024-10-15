package com.nofrontier.book.domain.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
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

import com.nofrontier.book.api.v1.controller.PersonRestController;
import com.nofrontier.book.core.validation.PersonValidator;
import com.nofrontier.book.domain.exceptions.EntityInUseException;
import com.nofrontier.book.domain.exceptions.PersonNotFoundException;
import com.nofrontier.book.domain.exceptions.RequiredObjectIsNullException;
import com.nofrontier.book.domain.exceptions.ResourceNotFoundException;
import com.nofrontier.book.domain.model.Person;
import com.nofrontier.book.domain.repository.PersonRepository;
import com.nofrontier.book.dto.v1.PersonDto;

import jakarta.annotation.PostConstruct;

@Service
public class ApiPersonService {

	// private Logger logger =
	// Logger.getLogger(ApiPersonService.class.getName());

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(ApiPersonService.class);
	
	private static final String MSG_PERSON_IN_USE = "Code person %d cannot be removed because there is a constraint in use";

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PersonValidator validator;

	@Autowired
	PagedResourcesAssembler<PersonDto> assembler;
	
    @PostConstruct
    public void configureModelMapper() {
        modelMapper.typeMap(Person.class, PersonDto.class)
                   .addMapping(Person::getId, PersonDto::setKey);
        modelMapper.typeMap(PersonDto.class, Person.class)
        .addMapping(PersonDto::getKey, Person::setId);
    }

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public PersonDto findById(Long id) {
		logger.info("Finding one person!");
		var entity = personRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Maps the saved entity to PersonDto
		PersonDto personResponse = modelMapper.map(entity,
				PersonDto.class);
		personResponse.add(linkTo(methodOn(PersonRestController.class)
				.findById(personResponse.getKey())).withSelfRel());

		return personResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public PagedModel<EntityModel<PersonDto>> findPersonByName(
			String firstName, Pageable pageable) {

		logger.info("Finding all people with first name like: {}", firstName);

		var personPage = personRepository.findPersonByName(firstName,
				pageable);

		var personDtoPage = personPage
				.map(person -> modelMapper.map(person, PersonDto.class));
		personDtoPage.forEach(person -> person.add(linkTo(
				methodOn(PersonRestController.class).findById(person.getKey()))
				.withSelfRel()));

		Link link = linkTo(methodOn(PersonRestController.class)
				.findPersonByName(firstName, pageable.getPageNumber(),
						pageable.getPageSize(), "asc"))
				.withSelfRel();

		return assembler.toModel(personDtoPage, link);
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public PagedModel<EntityModel<PersonDto>> findAll(Pageable pageable) {
		logger.info("Finding all People!");
		var personPage = personRepository.findAll(pageable);
		var personResponsesPage = personPage
				.map(person -> modelMapper.map(person, PersonDto.class));
		personResponsesPage.map(person -> person.add(linkTo(
				methodOn(PersonRestController.class).findById(person.getKey()))
				.withSelfRel()));
		Link link = linkTo(methodOn(PersonRestController.class).findAll(
				pageable.getPageNumber(), pageable.getPageSize(), "asc"))
				.withSelfRel();
		return assembler.toModel(personResponsesPage, link);
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public PersonDto create(PersonDto personDtoRequest) {
        if (personDtoRequest == null) {
            throw new RequiredObjectIsNullException("It is not allowed to persist a null object!");
        }
        
		logger.info("Creating a new person!");

		// Maps the PersonRequest to the Person entity
		Person entity = modelMapper.map(personDtoRequest, Person.class);
		
		validator.validate(entity);
		
		// Saves the new entity in the database
		Person savedEntity = personRepository.save(entity);

		// Maps the saved entity to PersonDto
		PersonDto personResponse = modelMapper.map(savedEntity,
				PersonDto.class);
		personResponse.add(linkTo(methodOn(PersonRestController.class)
				.findById(personResponse.getKey())).withSelfRel());

		return personResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public PersonDto update(PersonDto personDtoRequest) {
	    if (personDtoRequest == null) {
	        throw new RequiredObjectIsNullException();
	    }
	    logger.info("Updating one person!");
	    logger.info("Enabled value: " + personDtoRequest.getEnabled());
	    logger.info("PersonRequest: " + personDtoRequest.toString());

	    var entity = personRepository.findById(personDtoRequest.getKey())
	            .orElseThrow(() -> new ResourceNotFoundException(
	                    "No records found for this ID!"));

	    entity.setFirstName(personDtoRequest.getFirstName());
	    entity.setLastName(personDtoRequest.getLastName());
	    entity.setGender(personDtoRequest.getGender());
	    entity.setCpf(personDtoRequest.getCpf());
	    entity.setBirth(personDtoRequest.getBirth());
	    entity.setPhoneNumber(personDtoRequest.getPhoneNumber());
	    entity.setMobileNumber(personDtoRequest.getMobileNumber());
	    entity.setKeyPix(personDtoRequest.getKeyPix());
	    // Ensure that 'enabled' is not null
	    if (personDtoRequest.getEnabled() != null) {
	        entity.setEnabled(personDtoRequest.getEnabled());
	    } else {
	        throw new IllegalArgumentException("Enabled field cannot be null");
	    }

	    var updatedEntity = personRepository.save(entity);

	    // Converting the updated entity to the response
	    PersonDto personResponse = modelMapper.map(updatedEntity,
	            PersonDto.class);
	    personResponse.add(linkTo(methodOn(PersonRestController.class)
	            .findById(personResponse.getKey())).withSelfRel());

	    return personResponse;
	}


	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public PersonDto disablePerson(Long id) {
		logger.info("Disabling one person!");
		personRepository.disablePerson(id);
		var entity = personRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		var dto = modelMapper.map(entity, PersonDto.class);
		dto.add(linkTo(methodOn(PersonRestController.class).findById(id))
				.withSelfRel());
		return dto;
	}

	// -------------------------------------------------------------------------------------------------------------

	
	@Transactional
	public void delete(Long id) {
		logger.info("Deleting one person!");
		var entity = personRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		try {
			personRepository.delete(entity);
			personRepository.flush();

		} catch (EmptyResultDataAccessException e) {
			throw new PersonNotFoundException(id);

		} catch (DataIntegrityViolationException e) {
			throw new EntityInUseException(String.format(MSG_PERSON_IN_USE, id));
		}
	}
}