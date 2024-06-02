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

import com.nofrontier.book.api.v1.controller.ProductRestController;
import com.nofrontier.book.domain.exceptions.ProductNotFoundException;
import com.nofrontier.book.domain.exceptions.RequiredObjectIsNullException;
import com.nofrontier.book.domain.exceptions.ResourceNotFoundException;
import com.nofrontier.book.domain.model.Product;
import com.nofrontier.book.domain.repository.ProductRepository;
import com.nofrontier.book.dto.v1.requests.ProductRequest;
import com.nofrontier.book.dto.v1.responses.ProductResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApiProductService {

	private Logger logger = Logger.getLogger(ApiProductService.class.getName());

	private final ProductRepository productRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	PagedResourcesAssembler<ProductResponse> assembler;

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public ProductResponse findById(Long id) {
		logger.info("Finding one product!");
		var entity = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Maps the saved entity to ProductResponse
		ProductResponse productResponse = modelMapper.map(entity, ProductResponse.class);
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
	public PagedModel<EntityModel<ProductResponse>> findAll(Pageable pageable) {
		logger.info("Finding all products!");
		var productPage = productRepository.findAll(pageable);
		var productResponsesPage = productPage
				.map(product -> modelMapper.map(product, ProductResponse.class));
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
	public ProductResponse create(ProductRequest productRequest) {
		if (productRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Creating a new product!");

		// Maps the ProductRequest to the Product entity
		var entity = modelMapper.map(productRequest, Product.class);

		// Saves the new entity in the database
		var savedEntity = productRepository.save(entity);

		// Maps the saved entity to ProductResponse
		ProductResponse productResponse = modelMapper.map(savedEntity,
				ProductResponse.class);
		productResponse.add(linkTo(methodOn(ProductRestController.class)
				.findById(productResponse.getKey())).withSelfRel());

		return productResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public ProductResponse update(Long id, ProductRequest productRequest) {
		if (productRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Updating one product!");

		var entity = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Updating entity fields with request values
		entity.setDescription(productRequest.getDescription());
		entity.setFormat(productRequest.getFormat());
		entity.setEdition(productRequest.getEdition());
		entity.setPrice(productRequest.getPrice());
		entity.setActive(productRequest.getActive());

		var updatedEntity = productRepository.save(entity);

		// Converting the updated entity to the response
		ProductResponse productResponse = modelMapper.map(updatedEntity,
				ProductResponse.class);
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