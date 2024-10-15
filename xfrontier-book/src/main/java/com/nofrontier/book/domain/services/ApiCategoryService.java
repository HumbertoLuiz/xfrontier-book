package com.nofrontier.book.domain.services;

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

import com.nofrontier.book.api.v1.controller.CategoryRestController;
import com.nofrontier.book.domain.exceptions.CategoryNotFoundException;
import com.nofrontier.book.domain.exceptions.EntityInUseException;
import com.nofrontier.book.domain.exceptions.RequiredObjectIsNullException;
import com.nofrontier.book.domain.exceptions.ResourceNotFoundException;
import com.nofrontier.book.domain.model.Category;
import com.nofrontier.book.domain.repository.CategoryRepository;
import com.nofrontier.book.dto.v1.CategoryDto;

import jakarta.annotation.PostConstruct;

@Service
public class ApiCategoryService {

	private Logger logger = Logger.getLogger(ApiCategoryService.class.getName());
	
	private static final String MSG_CATEGORY_IN_USE = "Code category %d cannot be removed because there is a constraint in use";

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	PagedResourcesAssembler<CategoryDto> assembler;
	
    @PostConstruct
    public void configureModelMapper() {
        modelMapper.typeMap(Category.class, CategoryDto.class)
                   .addMapping(Category::getId, CategoryDto::setKey);
        modelMapper.typeMap(CategoryDto.class, Category.class)
        .addMapping(CategoryDto::getKey, Category::setId);
    }

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public CategoryDto findById(Long id) {
		logger.info("Finding one category!");
		var entity = categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Maps the saved entity to CategoryResponse
		CategoryDto categoryDtoResponse = modelMapper.map(entity, CategoryDto.class);
		categoryDtoResponse.add(linkTo(methodOn(CategoryRestController.class)
				.findById(categoryDtoResponse.getKey())).withSelfRel());

		return categoryDtoResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public PagedModel<EntityModel<CategoryDto>> findAll(Pageable pageable) {
		logger.info("Finding all categories!");
		var categoryPage = categoryRepository.findAll(pageable);
		var categoryResponsesPage = categoryPage
				.map(category -> modelMapper.map(category, CategoryDto.class));
		categoryResponsesPage.map(category -> category.add(linkTo(
				methodOn(CategoryRestController.class).findById(category.getKey()))
				.withSelfRel()));
		Link link = linkTo(methodOn(CategoryRestController.class).findAll(
				pageable.getPageNumber(), pageable.getPageSize(), "asc"))
				.withSelfRel();
		return assembler.toModel(categoryResponsesPage, link);
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public CategoryDto create(CategoryDto categoryDtoRequest) {
		if (categoryDtoRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Creating a new category!");

		// Maps the CategoryRequest to the Category entity
		var entity = modelMapper.map(categoryDtoRequest, Category.class);

		// Saves the new entity in the database
		var savedEntity = categoryRepository.save(entity);

		// Maps the saved entity to CategoryResponse
		CategoryDto categoryDtoResponse = modelMapper.map(savedEntity,
				CategoryDto.class);
		categoryDtoResponse.add(linkTo(methodOn(CategoryRestController.class)
				.findById(categoryDtoResponse.getKey())).withSelfRel());

		return categoryDtoResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public CategoryDto update(Long id, CategoryDto categoryDtoRequest) {
		if (categoryDtoRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Updating one category!");

		var entity = categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Updating entity fields with request values
		entity.setTitle(categoryDtoRequest.getTitle());
		entity.setName(categoryDtoRequest.getName());
		entity.setDescription(categoryDtoRequest.getDescription());

		var updatedEntity = categoryRepository.save(entity);

		// Converting the updated entity to the response
		CategoryDto categoryDtoResponse = modelMapper.map(updatedEntity,
				CategoryDto.class);
		categoryDtoResponse.add(linkTo(methodOn(CategoryRestController.class)
				.findById(categoryDtoResponse.getKey())).withSelfRel());

		return categoryDtoResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public void delete(Long id) {
		logger.info("Deleting one category");
		var entity = categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		try {
			categoryRepository.delete(entity);
			categoryRepository.flush();

		} catch (EmptyResultDataAccessException e) {
			throw new CategoryNotFoundException(id);

		} catch (DataIntegrityViolationException e) {
			throw new EntityInUseException(String.format(MSG_CATEGORY_IN_USE, id));
		}
	}
}
