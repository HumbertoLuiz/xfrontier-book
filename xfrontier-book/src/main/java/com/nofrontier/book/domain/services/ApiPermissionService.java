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

import com.nofrontier.book.api.v1.controller.PermissionRestController;
import com.nofrontier.book.domain.exceptions.EntityInUseException;
import com.nofrontier.book.domain.exceptions.PaymentMethodNotFoundException;
import com.nofrontier.book.domain.exceptions.PermissionNotFoundException;
import com.nofrontier.book.domain.exceptions.RequiredObjectIsNullException;
import com.nofrontier.book.domain.exceptions.ResourceNotFoundException;
import com.nofrontier.book.domain.model.Permission;
import com.nofrontier.book.domain.repository.PermissionRepository;
import com.nofrontier.book.dto.v1.PermissionDto;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApiPermissionService {

	private Logger logger = Logger.getLogger(ApiGroupService.class.getName());
	
	private static final String MSG_GROUP_IN_USE = "Code permission %d cannot be removed as it is in use";

	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	PagedResourcesAssembler<PermissionDto> assembler;
	
    @PostConstruct
    public void configureModelMapper() {
        modelMapper.typeMap(Permission.class, PermissionDto.class)
                   .addMapping(Permission::getId, PermissionDto::setKey);
    }

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public PermissionDto findById(Long id) {
		logger.info("Finding one permission!");
		var entity = permissionRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Maps the saved entity to PermissionDto
		PermissionDto permissionResponse = modelMapper.map(entity,
				PermissionDto.class);
		permissionResponse.add(linkTo(methodOn(PermissionRestController.class)
				.findById(permissionResponse.getKey())).withSelfRel());

		return permissionResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public PagedModel<EntityModel<PermissionDto>> findAll(
			Pageable pageable) {
		logger.info("Finding all permissions!");
		var permissionPage = permissionRepository.findAll(pageable);
		var permissionResponsesPage = permissionPage
				.map(permission -> modelMapper.map(permission,
						PermissionDto.class));
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
	public PermissionDto create(PermissionDto permissionDtoRequest) {
		if (permissionDtoRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Creating a new permission!");

		// Maps the PermissionRequest to the Permission entity
		var entity = modelMapper.map(permissionDtoRequest, Permission.class);

		// Saves the new entity in the database
		var savedEntity = permissionRepository.save(entity);

		// Maps the saved entity to PermissionDto
		PermissionDto permissionResponse = modelMapper.map(savedEntity,
				PermissionDto.class);
		permissionResponse.add(linkTo(methodOn(PermissionRestController.class)
				.findById(permissionResponse.getKey())).withSelfRel());

		return permissionResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public PermissionDto update(Long id,
			PermissionDto permissionDtoRequest) {
		if (permissionDtoRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Updating one permission!");

		var entity = permissionRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Updating entity fields with request values
		entity.setName(permissionDtoRequest.getName());
		entity.setDescription(permissionDtoRequest.getDescription());

		var updatedEntity = permissionRepository.save(entity);

		// Converting the updated entity to the response
		PermissionDto permissionResponse = modelMapper.map(updatedEntity,
				PermissionDto.class);
		permissionResponse.add(linkTo(methodOn(PermissionRestController.class)
				.findById(permissionResponse.getKey())).withSelfRel());

		return permissionResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public void delete(Long id) {
		logger.info("Deleting one permission!");
		var entity = permissionRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		try {
			permissionRepository.delete(entity);
			permissionRepository.flush();

		} catch (EmptyResultDataAccessException e) {
			throw new PaymentMethodNotFoundException(id);

		} catch (DataIntegrityViolationException e) {
			throw new EntityInUseException(String.format(MSG_GROUP_IN_USE, id));
		}
	}
	
	// -------------------------------------------------------------------------------------------------------------

	public Permission findOrFail(Long permissionId) {
		return permissionRepository.findById(permissionId).orElseThrow(
				() -> new PermissionNotFoundException(permissionId));
	}

}
