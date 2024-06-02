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

import com.nofrontier.book.api.v1.controller.GroupRestController;
import com.nofrontier.book.domain.exceptions.EntityInUseException;
import com.nofrontier.book.domain.exceptions.GroupNotFoundException;
import com.nofrontier.book.domain.exceptions.PaymentMethodNotFoundException;
import com.nofrontier.book.domain.exceptions.RequiredObjectIsNullException;
import com.nofrontier.book.domain.exceptions.ResourceNotFoundException;
import com.nofrontier.book.domain.model.Group;
import com.nofrontier.book.domain.model.Permission;
import com.nofrontier.book.domain.repository.GroupRepository;
import com.nofrontier.book.dto.v1.requests.GroupRequest;
import com.nofrontier.book.dto.v1.responses.GroupResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApiGroupService {

	private Logger logger = Logger.getLogger(ApiGroupService.class.getName());

	private static final String MSG_GROUP_IN_USE = "Code group %d cannot be removed as it is in use";

	private final GroupRepository groupRepository;

	private final ApiPermissionService apiPermissionService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	PagedResourcesAssembler<GroupResponse> assembler;

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public GroupResponse findById(Long id) {
		logger.info("Finding one group!");
		var entity = groupRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Maps the saved entity to GroupResponse
		GroupResponse groupResponse = modelMapper.map(entity,
				GroupResponse.class);
		groupResponse.add(linkTo(methodOn(GroupRestController.class)
				.findById(groupResponse.getKey())).withSelfRel());

		return groupResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public PagedModel<EntityModel<GroupResponse>> findAll(Pageable pageable) {
		logger.info("Finding all groups!");
		var groupPage = groupRepository.findAll(pageable);
		var groupResponsesPage = groupPage
				.map(group -> modelMapper.map(group, GroupResponse.class));
		groupResponsesPage.map(group -> group.add(linkTo(
				methodOn(GroupRestController.class).findById(group.getKey()))
				.withSelfRel()));
		Link link = linkTo(methodOn(GroupRestController.class).findAll(
				pageable.getPageNumber(), pageable.getPageSize(), "asc"))
				.withSelfRel();
		return assembler.toModel(groupResponsesPage, link);
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public GroupResponse create(GroupRequest groupRequest) {
		if (groupRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Creating a new group!");

		// Maps the GroupRequest to the Group entity
		var entity = modelMapper.map(groupRequest, Group.class);

		// Saves the new entity in the database
		var savedEntity = groupRepository.save(entity);

		// Maps the saved entity to GroupResponse
		GroupResponse groupResponse = modelMapper.map(savedEntity,
				GroupResponse.class);
		groupResponse.add(linkTo(methodOn(GroupRestController.class)
				.findById(groupResponse.getKey())).withSelfRel());

		return groupResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public GroupResponse update(Long id, GroupRequest groupRequest) {
		if (groupRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Updating one group!");

		var entity = groupRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));

		// Updating entity fields with request values
		entity.setName(groupRequest.getName());

		var updatedEntity = groupRepository.save(entity);

		// Converting the updated entity to the response
		GroupResponse groupResponse = modelMapper.map(updatedEntity,
				GroupResponse.class);
		groupResponse.add(linkTo(methodOn(GroupRestController.class)
				.findById(groupResponse.getKey())).withSelfRel());

		return groupResponse;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public void delete(Long id) {
		logger.info("Deleting one group!");
		var entity = groupRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		try {
			groupRepository.delete(entity);
			groupRepository.flush();

		} catch (EmptyResultDataAccessException e) {
			throw new PaymentMethodNotFoundException(id);

		} catch (DataIntegrityViolationException e) {
			throw new EntityInUseException(String.format(MSG_GROUP_IN_USE, id));
		}
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public void disassociatePermission(Long groupId, Long permissionId) {
		Group group = findOrFail(groupId);
		Permission permission = apiPermissionService.findOrFail(permissionId);

		group.removePermission(permission);
	}

	
	
	
	@Transactional
	public void associatePermission(Long groupId, Long permissionId) {
		Group group = findOrFail(groupId);
		Permission permission = apiPermissionService.findOrFail(permissionId);

		group.addPermission(permission);
	}

	public Group findOrFail(Long groupId) {
		return groupRepository.findById(groupId)
				.orElseThrow(() -> new GroupNotFoundException(groupId));
	}

}
