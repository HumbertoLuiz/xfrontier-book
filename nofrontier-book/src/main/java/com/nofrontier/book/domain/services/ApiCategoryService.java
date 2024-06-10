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
import com.nofrontier.book.dto.v1.requests.CategoryRequest;
import com.nofrontier.book.dto.v1.responses.CategoryResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApiCategoryService {

	private Logger logger = Logger.getLogger(ApiCategoryService.class.getName());
	
	private static final String MSG_CATEGORY_IN_USE = "Code category %d cannot be removed because there is a constraint in use";

	private final CategoryRepository categoryRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	PagedResourcesAssembler<CategoryResponse> assembler;

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public CategoryResponse findById(Long id) {
		logger.info("Finding one category!");
		var entity = categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Maps the saved entity to CategoryResponse
		CategoryResponse categoryResponse = modelMapper.map(entity, CategoryResponse.class);
		categoryResponse.add(linkTo(methodOn(CategoryRestController.class)
				.findById(categoryResponse.getKey())).withSelfRel());

		return categoryResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public PagedModel<EntityModel<CategoryResponse>> findAll(Pageable pageable) {
		logger.info("Finding all categories!");
		var categoryPage = categoryRepository.findAll(pageable);
		var categoryResponsesPage = categoryPage
				.map(category -> modelMapper.map(category, CategoryResponse.class));
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
	public CategoryResponse create(CategoryRequest categoryRequest) {
		if (categoryRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Creating a new category!");

		// Maps the CategoryRequest to the Category entity
		var entity = modelMapper.map(categoryRequest, Category.class);

		// Saves the new entity in the database
		var savedEntity = categoryRepository.save(entity);

		// Maps the saved entity to CategoryResponse
		CategoryResponse categoryResponse = modelMapper.map(savedEntity,
				CategoryResponse.class);
		categoryResponse.add(linkTo(methodOn(CategoryRestController.class)
				.findById(categoryResponse.getKey())).withSelfRel());

		return categoryResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public CategoryResponse update(Long id, CategoryRequest categoryRequest) {
		if (categoryRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Updating one category!");

		var entity = categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Updating entity fields with request values
		entity.setTitle(categoryRequest.getTitle());
		entity.setName(categoryRequest.getName());
		entity.setDescription(categoryRequest.getDescription());

		var updatedEntity = categoryRepository.save(entity);

		// Converting the updated entity to the response
		CategoryResponse categoryResponse = modelMapper.map(updatedEntity,
				CategoryResponse.class);
		categoryResponse.add(linkTo(methodOn(CategoryRestController.class)
				.findById(categoryResponse.getKey())).withSelfRel());

		return categoryResponse;
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
