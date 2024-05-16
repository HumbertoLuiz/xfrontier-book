package com.nofrontier.book.domain.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import com.nofrontier.book.api.v1.controller.PersonRestController;
import com.nofrontier.book.core.modelmapper.ModelMapperConfig;
import com.nofrontier.book.domain.exceptions.RequiredObjectIsNullException;
import com.nofrontier.book.domain.exceptions.ResourceNotFoundException;
import com.nofrontier.book.domain.model.Person;
import com.nofrontier.book.domain.repository.PersonRepository;
import com.nofrontier.book.dto.v1.PersonDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PersonService {

	private Logger logger = Logger.getLogger(PersonService.class.getName());

	private final PersonRepository personRepository;

	@Autowired
	PagedResourcesAssembler<PersonDto> assembler;

	// -------------------------------------------------------------------------------------------------------------

	public PagedModel<EntityModel<PersonDto>> findAll(Pageable pageable) {
		logger.info("Finding all people!");
		var personPage = personRepository.findAll(pageable);
		var personVosPage = personPage.map(p -> ModelMapperConfig.parseObject(p, PersonDto.class));
		personVosPage.map(p -> p.add(linkTo(methodOn(PersonRestController.class).findById(p.getKey())).withSelfRel()));
		Link link = linkTo(
				methodOn(PersonRestController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc"))
				.withSelfRel();
		return assembler.toModel(personVosPage, link);
	}

	// -------------------------------------------------------------------------------------------------------------

	public PagedModel<EntityModel<PersonDto>> findPersonByName(String firstname, Pageable pageable) {
		logger.info("Finding all people!");
		var personPage = personRepository.findPersonsByName(firstname, pageable);
		var personVosPage = personPage.map(p -> ModelMapperConfig.parseObject(p, PersonDto.class));
		personVosPage.map(p -> p.add(linkTo(methodOn(PersonRestController.class).findById(p.getKey())).withSelfRel()));
		Link link = linkTo(
				methodOn(PersonRestController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc"))
				.withSelfRel();
		return assembler.toModel(personVosPage, link);
	}

	// -------------------------------------------------------------------------------------------------------------

	public PersonDto findById(Long id) {
		logger.info("Finding one person!");
		var entity = personRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		var vo = ModelMapperConfig.parseObject(entity, PersonDto.class);
		vo.add(linkTo(methodOn(PersonRestController.class).findById(id)).withSelfRel());
		return vo;
	}

	// -------------------------------------------------------------------------------------------------------------

	public PersonDto create(PersonDto person) {
		if (person == null)
			throw new RequiredObjectIsNullException();
		logger.info("Creating one person!");
		var entity = ModelMapperConfig.parseObject(person, Person.class);
		var vo = ModelMapperConfig.parseObject(personRepository.save(entity), PersonDto.class);
		vo.add(linkTo(methodOn(PersonRestController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}

	// -------------------------------------------------------------------------------------------------------------

	public PersonDto update(PersonDto person) {
		if (person == null)
			throw new RequiredObjectIsNullException();
		logger.info("Updating one person!");
		var entity = personRepository.findById(person.getKey())
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setGender(person.getGender());
		entity.setCpf(person.getCpf());
		entity.setBirth(person.getBirth());
		entity.setPhoneNumber(person.getPhoneNumber());
		entity.setMobileNumber(person.getMobileNumber());
		entity.setKeyPix(person.getKeyPix());
		entity.setEnabled(person.getEnabled());
		var vo = ModelMapperConfig.parseObject(personRepository.save(entity), PersonDto.class);
		vo.add(linkTo(methodOn(PersonRestController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public PersonDto disablePerson(Long id) {
		logger.info("Disabling one person!");
		personRepository.disablePerson(id);
		var entity = personRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		var vo = ModelMapperConfig.parseObject(entity, PersonDto.class);
		vo.add(linkTo(methodOn(PersonRestController.class).findById(id)).withSelfRel());
		return vo;
	}

	// -------------------------------------------------------------------------------------------------------------

	public void delete(Long id) {
		logger.info("Deleting one person!");
		var entity = personRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		personRepository.delete(entity);
	}
}