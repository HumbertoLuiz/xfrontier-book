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

import com.nofrontier.book.api.v1.controller.BookRestController;
import com.nofrontier.book.core.modelmapper.ModelMapperConfig;
import com.nofrontier.book.domain.exceptions.RequiredObjectIsNullException;
import com.nofrontier.book.domain.exceptions.ResourceNotFoundException;
import com.nofrontier.book.domain.model.Book;
import com.nofrontier.book.domain.repository.BookRepository;
import com.nofrontier.book.dto.v1.BookDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

	private Logger logger = Logger.getLogger(BookService.class.getName());

	private final BookRepository bookRepository;

	@Autowired
	PagedResourcesAssembler<BookDto> assembler;

	// -------------------------------------------------------------------------------------------------------------

	public PagedModel<EntityModel<BookDto>> findAll(Pageable pageable) {
		logger.info("Finding all books!");
		var bookPage = bookRepository.findAll(pageable);
		var bookVosPage = bookPage
				.map(p -> ModelMapperConfig.parseObject(p, BookDto.class));
		bookVosPage.map(p -> p.add(
				linkTo(methodOn(BookRestController.class).findById(p.getKey()))
						.withSelfRel()));
		Link link = linkTo(methodOn(BookRestController.class).findAll(
				pageable.getPageNumber(), pageable.getPageSize(), "asc"))
				.withSelfRel();
		return assembler.toModel(bookVosPage, link);
	}

	// -------------------------------------------------------------------------------------------------------------

	public PagedModel<EntityModel<BookDto>> findBookByAuthor(String author,
			Pageable pageable) {
		logger.info("Finding all books!");
		var bookPage = bookRepository.findBookByAuthor(author, pageable);
		var bookVosPage = bookPage
				.map(p -> ModelMapperConfig.parseObject(p, BookDto.class));
		bookVosPage.map(p -> p.add(
				linkTo(methodOn(BookRestController.class).findById(p.getKey()))
						.withSelfRel()));
		Link link = linkTo(methodOn(BookRestController.class).findAll(
				pageable.getPageNumber(), pageable.getPageSize(), "asc"))
				.withSelfRel();
		return assembler.toModel(bookVosPage, link);
	}

	// -------------------------------------------------------------------------------------------------------------

	public BookDto findById(Long id) {
		logger.info("Finding one book!");
		var entity = bookRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		var vo = ModelMapperConfig.parseObject(entity, BookDto.class);
		vo.add(linkTo(methodOn(BookRestController.class).findById(id))
				.withSelfRel());
		return vo;
	}

	// -------------------------------------------------------------------------------------------------------------

	public BookDto create(BookDto book) {
		if (book == null)
			throw new RequiredObjectIsNullException();
		logger.info("Creating one book!");
		var entity = ModelMapperConfig.parseObject(book, Book.class);
		var vo = ModelMapperConfig.parseObject(bookRepository.save(entity),
				BookDto.class);
		vo.add(linkTo(methodOn(BookRestController.class).findById(vo.getKey()))
				.withSelfRel());
		return vo;
	}

	// -------------------------------------------------------------------------------------------------------------

	public BookDto update(BookDto book) {
		if (book == null)
			throw new RequiredObjectIsNullException();
		logger.info("Updating one book!");
		var entity = bookRepository.findById(book.getKey())
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		entity.setTitle(book.getTitle());
		entity.setAuthor(book.getAuthor());
		entity.setIsbn(book.getIsbn());
		entity.setPrice(book.getPrice());
		entity.setLaunchDate(book.getLaunchDate());
		entity.setCreateDate(book.getCreateDate());
		entity.setLastModified(book.getLastModified());
		entity.setCreatedBy(book.getCreatedBy());
		entity.setLastModifiedBy(book.getLastModifiedBy());
		entity.setActive(book.getActive());
		var vo = ModelMapperConfig.parseObject(bookRepository.save(entity),
				BookDto.class);
		vo.add(linkTo(methodOn(BookRestController.class).findById(vo.getKey()))
				.withSelfRel());
		return vo;
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