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
import com.nofrontier.book.dto.v1.BookDto;

import jakarta.annotation.PostConstruct;

@Service
public class ApiBookService {

	private Logger logger = Logger.getLogger(ApiBookService.class.getName());
	
	private static final String MSG_BOOK_IN_USE = "Code book %d cannot be removed because there is a constraint in use";

	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	PagedResourcesAssembler<BookDto> assembler;
	
    @PostConstruct
    public void configureModelMapper() {
    	modelMapper.typeMap(Book.class, BookDto.class)
        	.addMapping(Book::getId, BookDto::setKey)
        	.addMapping(src -> src.getCategory().getId(), BookDto::setCategoryId);
    	modelMapper.typeMap(BookDto.class, Book.class)
        	.addMapping(BookDto::getKey, Book::setId);
    }

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public BookDto findById(Long id) {
		logger.info("Finding one book!");
		var entity = bookRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Maps the saved entity to BookResponse
		BookDto bookDtoResponse = modelMapper.map(entity, BookDto.class);
		bookDtoResponse.add(linkTo(methodOn(BookRestController.class)
				.findById(bookDtoResponse.getKey())).withSelfRel());

		return bookDtoResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public PagedModel<EntityModel<BookDto>> findBookByAuthor(String author,
			Pageable pageable) {
		logger.info("Finding books by author: {}");
		var bookPage = bookRepository.findByAuthor(author, pageable);
		var bookResponsesPage = bookPage
				.map(book -> modelMapper.map(book, BookDto.class));
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
	public PagedModel<EntityModel<BookDto>> findAll(Pageable pageable) {
		logger.info("Finding all books!");
		var bookPage = bookRepository.findAll(pageable);
		var bookResponsesPage = bookPage
				.map(book -> modelMapper.map(book, BookDto.class));
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
	public BookDto create(BookDto bookDtoRequest) {
	    if (bookDtoRequest == null) {
	        throw new RequiredObjectIsNullException();
	    }
	    logger.info("Creating a new book!");

	    // Mapeia o BookDto para Book sem o Category
	    var entity = modelMapper.map(bookDtoRequest, Book.class);

	    // Busca a Category pelo categoryId do DTO
	    Long categoryId = bookDtoRequest.getCategoryId();
	    logger.info("Category ID to find: " + categoryId);
	    Category category = categoryRepository.findById(categoryId)
	            .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + categoryId));
	    // Atribui a Category à entidade Book
	    entity.setCategory(category);

	    var savedEntity = bookRepository.save(entity);

	    BookDto bookDtoResponse = modelMapper.map(savedEntity, BookDto.class);
	    bookDtoResponse.add(linkTo(methodOn(BookRestController.class)
	            .findById(bookDtoResponse.getKey())).withSelfRel());

	    return bookDtoResponse;
	}


	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public BookDto update(BookDto bookDtoRequest) {
		if (bookDtoRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Updating one book!");

		var entity = bookRepository.findById(bookDtoRequest.getKey())
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Updating entity fields with request values
		entity.setTitle(bookDtoRequest.getTitle());
		entity.setAuthor(bookDtoRequest.getAuthor());
		entity.setIsbn(bookDtoRequest.getIsbn());
		entity.setLaunchDate(bookDtoRequest.getLaunchDate());
		entity.setRegistrationDate(bookDtoRequest.getRegistrationDate());
		entity.setUpdateDate(bookDtoRequest.getUpdateDate());
		entity.setCreatedBy(bookDtoRequest.getCreatedBy());
		entity.setLastModifiedBy(bookDtoRequest.getLastModifiedBy());
		// Ensure that 'active' is not null
	    if (bookDtoRequest.getActive() != null) {
	        entity.setActive(bookDtoRequest.getActive());
	    } else {
	        throw new IllegalArgumentException("Active field cannot be null");
	    }
		entity.setBookStatus(bookDtoRequest.getBookStatus());
		entity.setShippingRate(bookDtoRequest.getShippingRate());
		entity.setPrice(bookDtoRequest.getPrice());
		entity.setObservation(bookDtoRequest.getObservation());
		entity.setReasonCancellation(bookDtoRequest.getReasonCancellation());

	    // Atualiza a Category se necessário
	    if (bookDtoRequest.getCategoryId() != null) {
	        Category category = categoryRepository.findById(bookDtoRequest.getCategoryId())
	                .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + bookDtoRequest.getCategoryId()));
	        entity.setCategory(category);
	    }
		
		var updatedEntity = bookRepository.save(entity);

		// Converting the updated entity to the response
		BookDto bookDtoResponse = modelMapper.map(updatedEntity,
				BookDto.class);
		bookDtoResponse.add(linkTo(methodOn(BookRestController.class)
				.findById(bookDtoResponse.getKey())).withSelfRel());

		return bookDtoResponse;
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