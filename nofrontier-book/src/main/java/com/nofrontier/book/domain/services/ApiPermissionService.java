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

import com.nofrontier.book.api.v1.controller.PermissionRestController;
import com.nofrontier.book.domain.exceptions.PermissionNotFoundException;
import com.nofrontier.book.domain.exceptions.RequiredObjectIsNullException;
import com.nofrontier.book.domain.exceptions.ResourceNotFoundException;
import com.nofrontier.book.domain.model.Permission;
import com.nofrontier.book.domain.repository.PermissionRepository;
import com.nofrontier.book.dto.v1.requests.PermissionRequest;
import com.nofrontier.book.dto.v1.responses.PermissionResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApiPermissionService {

	private Logger logger = Logger.getLogger(ApiGroupService.class.getName());

	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	PagedResourcesAssembler<PermissionResponse> assembler;

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public PermissionResponse findById(Long id) {
		logger.info("Finding one permission!");
		var entity = permissionRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Maps the saved entity to PermissionResponse
		PermissionResponse permissionResponse = modelMapper.map(entity,
				PermissionResponse.class);
		permissionResponse.add(linkTo(methodOn(PermissionRestController.class)
				.findById(permissionResponse.getKey())).withSelfRel());

		return permissionResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public PagedModel<EntityModel<PermissionResponse>> findAll(
			Pageable pageable) {
		logger.info("Finding all permissions!");
		var permissionPage = permissionRepository.findAll(pageable);
		var permissionResponsesPage = permissionPage
				.map(permission -> modelMapper.map(permission,
						PermissionResponse.class));
		permissionResponsesPage.map(permission -> permission
				.add(linkTo(methodOn(PermissionRestController.class)
						.findById(permission.getKey())).withSelfRel()));
		Link link = linkTo(methodOn(PermissionRestController.class).findAll(
				pageable.getPageNumber(), pageable.getPageSize(), "asc"))
				.withSelfRel();
		return assembler.toModel(permissionResponsesPage, link);
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public PermissionResponse create(PermissionRequest permissionRequest) {
		if (permissionRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Creating a new permission!");

		// Maps the PermissionRequest to the Permission entity
		var entity = modelMapper.map(permissionRequest, Permission.class);

		// Saves the new entity in the database
		var savedEntity = permissionRepository.save(entity);

		// Maps the saved entity to PermissionResponse
		PermissionResponse permissionResponse = modelMapper.map(savedEntity,
				PermissionResponse.class);
		permissionResponse.add(linkTo(methodOn(PermissionRestController.class)
				.findById(permissionResponse.getKey())).withSelfRel());

		return permissionResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public PermissionResponse update(Long id,
			PermissionRequest permissionRequest) {
		if (permissionRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Updating one permission!");

		var entity = permissionRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Updating entity fields with request values
		entity.setName(permissionRequest.getName());
		entity.setDescription(permissionRequest.getDescription());

		var updatedEntity = permissionRepository.save(entity);

		// Converting the updated entity to the response
		PermissionResponse permissionResponse = modelMapper.map(updatedEntity,
				PermissionResponse.class);
		permissionResponse.add(linkTo(methodOn(PermissionRestController.class)
				.findById(permissionResponse.getKey())).withSelfRel());

		return permissionResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	public void delete(Long id) {
		logger.info("Deleting one permission!");
		var entity = permissionRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		permissionRepository.delete(entity);
	}
	
	// -------------------------------------------------------------------------------------------------------------

	public Permission findOrFail(Long permissionId) {
		return permissionRepository.findById(permissionId).orElseThrow(
				() -> new PermissionNotFoundException(permissionId));
	}

}
