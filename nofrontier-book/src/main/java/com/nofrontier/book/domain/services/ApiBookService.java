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

import com.nofrontier.book.api.v1.controller.BookRestController;
import com.nofrontier.book.domain.exceptions.BookNotFoundException;
import com.nofrontier.book.domain.exceptions.CategoryNotFoundException;
import com.nofrontier.book.domain.exceptions.EntityInUseException;
import com.nofrontier.book.domain.exceptions.RequiredObjectIsNullException;
import com.nofrontier.book.domain.exceptions.ResourceNotFoundException;
import com.nofrontier.book.domain.model.Book;
import com.nofrontier.book.domain.model.Category;
import com.nofrontier.book.domain.repository.BookRepository;
import com.nofrontier.book.domain.repository.CategoryRepository;
import com.nofrontier.book.dto.v1.requests.BookRequest;
import com.nofrontier.book.dto.v1.responses.BookResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApiBookService {

	private Logger logger = Logger.getLogger(ApiBookService.class.getName());
	
	private static final String MSG_BOOK_IN_USE = "Code book %d cannot be removed because there is a constraint in use";

	private final BookRepository bookRepository;
	
	private final CategoryRepository categoryRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	PagedResourcesAssembler<BookResponse> assembler;

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public BookResponse findById(Long id) {
		logger.info("Finding one book!");
		var entity = bookRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Maps the saved entity to BookResponse
		BookResponse bookResponse = modelMapper.map(entity, BookResponse.class);
		bookResponse.add(linkTo(methodOn(BookRestController.class)
				.findById(bookResponse.getKey())).withSelfRel());

		return bookResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public PagedModel<EntityModel<BookResponse>> findBookByAuthor(String author,
			Pageable pageable) {
		logger.info("Finding books by author: {}");
		var bookPage = bookRepository.findByAuthor(author, pageable);
		var bookResponsesPage = bookPage
				.map(book -> modelMapper.map(book, BookResponse.class));
		bookResponsesPage.map(book -> book.add(linkTo(
				methodOn(BookRestController.class).findById(book.getKey()))
				.withSelfRel()));
		Link link = linkTo(methodOn(BookRestController.class).findBookByAuthor(
				author, pageable.getPageNumber(), pageable.getPageSize(),
				"asc")).withSelfRel();
		return assembler.toModel(bookResponsesPage, link);
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public PagedModel<EntityModel<BookResponse>> findAll(Pageable pageable) {
		logger.info("Finding all books!");
		var bookPage = bookRepository.findAll(pageable);
		var bookResponsesPage = bookPage
				.map(book -> modelMapper.map(book, BookResponse.class));
		bookResponsesPage.map(book -> book.add(linkTo(
				methodOn(BookRestController.class).findById(book.getKey()))
				.withSelfRel()));
		Link link = linkTo(methodOn(BookRestController.class).findAll(
				pageable.getPageNumber(), pageable.getPageSize(), "asc"))
				.withSelfRel();
		return assembler.toModel(bookResponsesPage, link);
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public BookResponse create(BookRequest bookRequest) {
		if (bookRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Creating a new book!");

		// Maps the BookRequest to the Book entity
		var entity = modelMapper.map(bookRequest, Book.class);

		// Get category by ID from the request
		Long categoryId = bookRequest.getCategoryId();
		Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
		if (optionalCategory.isEmpty()) {
			// Handle case when person with provided ID does not exist
			throw new CategoryNotFoundException(
					"Book not found with ID: " + categoryId);
		}
		Category category = optionalCategory.get();
		entity.setCategory(category);
		
		// Saves the new entity in the database
		var savedEntity = bookRepository.save(entity);

		// Maps the saved entity to BookResponse
		BookResponse bookResponse = modelMapper.map(savedEntity,
				BookResponse.class);
		bookResponse.add(linkTo(methodOn(BookRestController.class)
				.findById(bookResponse.getKey())).withSelfRel());

		return bookResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public BookResponse update(Long id, BookRequest bookRequest) {
		if (bookRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Updating one book!");

		var entity = bookRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Updating entity fields with request values
		entity.setTitle(bookRequest.getTitle());
		entity.setAuthor(bookRequest.getAuthor());
		entity.setIsbn(bookRequest.getIsbn());
		entity.setLaunchDate(bookRequest.getLaunchDate());
		entity.setCreatedBy(bookRequest.getCreatedBy());
		entity.setLastModifiedBy(bookRequest.getLastModifiedBy());
		// Ensure that 'active' is not null
	    if (bookRequest.getActive() != null) {
	        entity.setActive(bookRequest.getActive());
	    } else {
	        throw new IllegalArgumentException("Active field cannot be null");
	    }
		entity.setBookStatus(bookRequest.getBookStatus());
		entity.setShippingRate(bookRequest.getShippingRate());
		entity.setPrice(bookRequest.getPrice());
		entity.setObservation(bookRequest.getObservation());
		entity.setReasonCancellation(bookRequest.getReasonCancellation());

		var updatedEntity = bookRepository.save(entity);

		// Converting the updated entity to the response
		BookResponse bookResponse = modelMapper.map(updatedEntity,
				BookResponse.class);
		bookResponse.add(linkTo(methodOn(BookRestController.class)
				.findById(bookResponse.getKey())).withSelfRel());

		return bookResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public void delete(Long id) {
		logger.info("Deleting one book!");
		var entity = bookRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		try {
			bookRepository.delete(entity);
			bookRepository.flush();

		} catch (EmptyResultDataAccessException e) {
			throw new BookNotFoundException(id);

		} catch (DataIntegrityViolationException e) {
			throw new EntityInUseException(String.format(MSG_BOOK_IN_USE, id));
		}
	}

}