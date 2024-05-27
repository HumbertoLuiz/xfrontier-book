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

import com.nofrontier.book.api.v1.controller.BookRestController;
import com.nofrontier.book.api.v1.controller.PersonRestController;
import com.nofrontier.book.domain.exceptions.RequiredObjectIsNullException;
import com.nofrontier.book.domain.exceptions.ResourceNotFoundException;
import com.nofrontier.book.domain.model.Person;
import com.nofrontier.book.domain.repository.PersonRepository;
import com.nofrontier.book.dto.v1.requests.PersonRequest;
import com.nofrontier.book.dto.v1.responses.PersonResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PersonService {

	private Logger logger = Logger.getLogger(PersonService.class.getName());

	private final PersonRepository personRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	PagedResourcesAssembler<PersonResponse> assembler;

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public PersonResponse findById(Long id) {
		logger.info("Finding one person!");
		var entity = personRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Mapeia a entidade salva para o PersonResponse
		PersonResponse personResponse = modelMapper.map(entity,
				PersonResponse.class);
		personResponse.add(linkTo(methodOn(PersonRestController.class)
				.findById(personResponse.getKey())).withSelfRel());

		return personResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public PagedModel<EntityModel<PersonResponse>> findPersonsByName(
			String firstName, Pageable pageable) {
		logger.info("Finding books by author: {}");
		var personPage = personRepository.findPersonsByName(firstName,
				pageable);
		var personResponsesPage = personPage
				.map(person -> modelMapper.map(person, PersonResponse.class));
		personResponsesPage.map(person -> person.add(linkTo(
				methodOn(BookRestController.class).findById(person.getKey()))
				.withSelfRel()));
		Link link = linkTo(methodOn(PersonRestController.class)
				.findPersonsByName(firstName, pageable.getPageNumber(),
						pageable.getPageSize(), "asc"))
				.withSelfRel();
		return assembler.toModel(personResponsesPage, link);
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public PagedModel<EntityModel<PersonResponse>> findAll(Pageable pageable) {
		logger.info("Finding all People!");
		var personPage = personRepository.findAll(pageable);
		var personResponsesPage = personPage
				.map(person -> modelMapper.map(person, PersonResponse.class));
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
	public PersonResponse create(PersonRequest personRequest) {
		if (personRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Creating a new book!");

		// Mapeia o PersonRequest para a entidade Person
		var entity = modelMapper.map(personRequest, Person.class);

		// Salva a nova entidade no banco de dados
		var savedEntity = personRepository.save(entity);

		// Mapeia a entidade salva para o PersonResponse
		PersonResponse personResponse = modelMapper.map(savedEntity,
				PersonResponse.class);
		personResponse.add(linkTo(methodOn(PersonRestController.class)
				.findById(personResponse.getKey())).withSelfRel());

		return personResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public PersonResponse update(Long id, PersonRequest personRequest) {
		if (personRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Updating one book!");

		var entity = personRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		entity.setFirstName(personRequest.getFirstName());
		entity.setLastName(personRequest.getLastName());
		entity.setGender(personRequest.getGender());
		entity.setCpf(personRequest.getCpf());
		entity.setBirth(personRequest.getBirth());
		entity.setPhoneNumber(personRequest.getPhoneNumber());
		entity.setMobileNumber(personRequest.getMobileNumber());
		entity.setKeyPix(personRequest.getKeyPix());
		entity.setEnabled(personRequest.getEnabled());

		var updatedEntity = personRepository.save(entity);

		// Convertendo a entidade atualizada para o response
		PersonResponse personResponse = modelMapper.map(updatedEntity,
				PersonResponse.class);
		personResponse.add(linkTo(methodOn(BookRestController.class)
				.findById(personResponse.getKey())).withSelfRel());

		return personResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public PersonResponse disablePerson(Long id) {
		logger.info("Disabling one person!");
		personRepository.disablePerson(id);
		var entity = personRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		var dto = modelMapper.map(entity, PersonResponse.class);
		dto.add(linkTo(methodOn(PersonRestController.class).findById(id))
				.withSelfRel());
		return dto;
	}

	// -------------------------------------------------------------------------------------------------------------

	public void delete(Long id) {
		logger.info("Deleting one person!");
		var entity = personRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		personRepository.delete(entity);
	}
}