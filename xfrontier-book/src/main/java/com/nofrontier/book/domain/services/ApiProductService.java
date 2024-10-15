package com.nofrontier.book.domain.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Optional;
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

import com.nofrontier.book.api.v1.controller.ProductRestController;
import com.nofrontier.book.domain.exceptions.BookNotFoundException;
import com.nofrontier.book.domain.exceptions.ProductNotFoundException;
import com.nofrontier.book.domain.exceptions.RequiredObjectIsNullException;
import com.nofrontier.book.domain.exceptions.ResourceNotFoundException;
import com.nofrontier.book.domain.model.Book;
import com.nofrontier.book.domain.model.Product;
import com.nofrontier.book.domain.repository.BookRepository;
import com.nofrontier.book.domain.repository.ProductRepository;
import com.nofrontier.book.dto.v1.ProductDto;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApiProductService {

	private Logger logger = Logger.getLogger(ApiProductService.class.getName());

	private final ProductRepository productRepository;
	
	private final BookRepository bookRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	PagedResourcesAssembler<ProductDto> assembler;
	
    @PostConstruct
    public void configureModelMapper() {
        modelMapper.typeMap(Product.class, ProductDto.class)
                   .addMapping(Product::getId, ProductDto::setKey)
        .addMapping(src -> src.getBook().getId(), ProductDto::setBookId);
    }

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public ProductDto findById(Long id) {
		logger.info("Finding one product!");
		var entity = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Maps the saved entity to ProductDto
		ProductDto productResponse = modelMapper.map(entity, ProductDto.class);
		productResponse.add(linkTo(methodOn(ProductRestController.class)
				.findById(productResponse.getKey())).withSelfRel());

		return productResponse;
	}
	
	// -------------------------------------------------------------------------------------------------------------

	public Product findOrFail(Long bookId, Long productId) {
		return productRepository.findById(bookId, productId)
			.orElseThrow(() -> new ProductNotFoundException(bookId, productId));
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public PagedModel<EntityModel<ProductDto>> findAll(Pageable pageable) {
		logger.info("Finding all products!");
		var productPage = productRepository.findAll(pageable);
		var productResponsesPage = productPage
				.map(product -> modelMapper.map(product, ProductDto.class));
		productResponsesPage.map(product -> product.add(linkTo(
				methodOn(ProductRestController.class).findById(product.getKey()))
				.withSelfRel()));
		Link link = linkTo(methodOn(ProductRestController.class).findAll(
				pageable.getPageNumber(), pageable.getPageSize(), "asc"))
				.withSelfRel();
		return assembler.toModel(productResponsesPage, link);
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public ProductDto create(ProductDto productDtoRequest) {
		if (productDtoRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Creating a new product!");

		// Maps the ProductRequest to the Product entity
		var entity = modelMapper.map(productDtoRequest, Product.class);

		// Saves the new entity in the database
		var savedEntity = productRepository.save(entity);

		// Get book by ID from the request
		Long bookId = productDtoRequest.getBookId();
		Optional<Book> optionalBook = bookRepository.findById(bookId);
		if (optionalBook.isEmpty()) {
			// Handle case when book with provided ID does not exist
			throw new BookNotFoundException(
					"Book not found with ID: " + bookId);
		}
		Book book = optionalBook.get();
		entity.setBook(book);
		
		// Maps the saved entity to ProductDto
		ProductDto productResponse = modelMapper.map(savedEntity,
				ProductDto.class);
		productResponse.add(linkTo(methodOn(ProductRestController.class)
				.findById(productResponse.getKey())).withSelfRel());

		return productResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public ProductDto update(Long id, ProductDto productDtoRequest) {
		if (productDtoRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Updating one product!");

		var entity = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Updating entity fields with request values
		entity.setDescription(productDtoRequest.getDescription());
		entity.setFormat(productDtoRequest.getFormat());
		entity.setEdition(productDtoRequest.getEdition());
		entity.setPrice(productDtoRequest.getPrice());
		entity.setActive(productDtoRequest.getActive());

		var updatedEntity = productRepository.save(entity);

		// Converting the updated entity to the response
		ProductDto productResponse = modelMapper.map(updatedEntity,
				ProductDto.class);
		productResponse.add(linkTo(methodOn(ProductRestController.class)
				.findById(productResponse.getKey())).withSelfRel());

		return productResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	public void delete(Long id) {
		logger.info("Deleting one product!");
		var entity = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		productRepository.delete(entity);
	}
}