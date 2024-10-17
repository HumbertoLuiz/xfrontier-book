package br.com.xfrontier.book.domain.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

import br.com.xfrontier.book.api.v1.controller.CountryRestController;
import br.com.xfrontier.book.domain.exceptions.CountryNotFoundException;
import br.com.xfrontier.book.domain.exceptions.EntityInUseException;
import br.com.xfrontier.book.domain.exceptions.RequiredObjectIsNullException;
import br.com.xfrontier.book.domain.exceptions.ResourceNotFoundException;
import br.com.xfrontier.book.domain.model.Country;
import br.com.xfrontier.book.domain.repository.CountryRepository;
import br.com.xfrontier.book.dto.v1.CountryDto;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApiCountryService {

	private Logger logger = Logger.getLogger(ApiCountryService.class.getName());
	
	private static final String MSG_COUNTRY_IN_USE = "Code country %d cannot be removed because there is a constraint in use";

	private final CountryRepository countryRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	PagedResourcesAssembler<CountryDto> assembler;
	
    @PostConstruct
    public void configureModelMapper() {
        modelMapper.typeMap(Country.class, CountryDto.class)
                   .addMapping(Country::getId, CountryDto::setKey);
    }
	
	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public CountryDto findById(Long id) {
		logger.info("Finding one country!");
		var entity = countryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Maps the saved entity to CountryDto
		CountryDto countryDtoResponse = modelMapper.map(entity, CountryDto.class);
		countryDtoResponse.add(linkTo(methodOn(CountryRestController.class)
				.findById(countryDtoResponse.getKey())).withSelfRel());

		return countryDtoResponse;
	}
	
	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public PagedModel<EntityModel<CountryDto>> findAll(Pageable pageable) {
		logger.info("Finding all countries!");
		var countryPage = countryRepository.findAll(pageable);
		var countryResponsesPage = countryPage
				.map(country -> modelMapper.map(country, CountryDto.class));
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
	public CountryDto create(CountryDto countryDtoRequest) {
		if (countryDtoRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Creating a new country!");

		// Maps the CountryRequest to the Country entity
		var entity = modelMapper.map(countryDtoRequest, Country.class);

		// Saves the new entity in the database
		var savedEntity = countryRepository.save(entity);

		// Maps the saved entity to CountryDto
		CountryDto countryDtoResponse = modelMapper.map(savedEntity,
				CountryDto.class);
		countryDtoResponse.add(linkTo(methodOn(CountryRestController.class)
				.findById(countryDtoResponse.getKey())).withSelfRel());

		return countryDtoResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public CountryDto update(Long id, CountryDto countryDtoRequest) {
		if (countryDtoRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Updating one country!");

		var entity = countryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Updating entity fields with request values
		entity.setName(countryDtoRequest.getName());
		entity.setInitials(countryDtoRequest.getInitials());
		
		var updatedEntity = countryRepository.save(entity);

		// Converting the updated entity to the response
		CountryDto countryDtoResponse = modelMapper.map(updatedEntity,
				CountryDto.class);
		countryDtoResponse.add(linkTo(methodOn(CountryRestController.class)
				.findById(countryDtoResponse.getKey())).withSelfRel());

		return countryDtoResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public void delete(Long id) {
		logger.info("Deleting one country");
		var entity = countryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		try {
			countryRepository.delete(entity);
			countryRepository.flush();

		} catch (EmptyResultDataAccessException e) {
			throw new CountryNotFoundException(id);

		} catch (DataIntegrityViolationException e) {
			throw new EntityInUseException(String.format(MSG_COUNTRY_IN_USE, id));
		}
	}
}