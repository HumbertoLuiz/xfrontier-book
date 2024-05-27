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
import com.nofrontier.book.domain.exceptions.RequiredObjectIsNullException;
import com.nofrontier.book.domain.exceptions.ResourceNotFoundException;
import com.nofrontier.book.domain.model.Book;
import com.nofrontier.book.domain.repository.BookRepository;
import com.nofrontier.book.dto.v1.requests.BookRequest;
import com.nofrontier.book.dto.v1.responses.BookResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

	private Logger logger = Logger.getLogger(BookService.class.getName());

	private final BookRepository bookRepository;

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

		// Mapeia a entidade salva para o BookResponse
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

		// Mapeia o BookRequest para a entidade Book
		var entity = modelMapper.map(bookRequest, Book.class);

		// Salva a nova entidade no banco de dados
		var savedEntity = bookRepository.save(entity);

		// Mapeia a entidade salva para o BookResponse
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

		// Atualizando os campos da entidade com os valores do request
		entity.setTitle(bookRequest.getTitle());
		entity.setAuthor(bookRequest.getAuthor());
		entity.setIsbn(bookRequest.getIsbn());
		entity.setLaunchDate(bookRequest.getLaunchDate());
		entity.setActive(bookRequest.getActive());

		var updatedEntity = bookRepository.save(entity);

		// Convertendo a entidade atualizada para o response
		BookResponse bookResponse = modelMapper.map(updatedEntity,
				BookResponse.class);
		bookResponse.add(linkTo(methodOn(BookRestController.class)
				.findById(bookResponse.getKey())).withSelfRel());

		return bookResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	public void delete(Long id) {
		logger.info("Deleting one book!");
		var entity = bookRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		bookRepository.delete(entity);
	}
}