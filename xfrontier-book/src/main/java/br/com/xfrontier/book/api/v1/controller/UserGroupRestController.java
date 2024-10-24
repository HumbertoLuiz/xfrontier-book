package br.com.xfrontier.book.api.v1.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.xfrontier.book.domain.model.User;
import br.com.xfrontier.book.domain.services.ApiUserService;
import br.com.xfrontier.book.dto.v1.GroupDto;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080", "https://xfrontier.com.br"})
@RestController
@RequestMapping(path = "/api/users/{userId}/groups/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserGroupRestController {

	@Autowired
	private ApiUserService apiUserService;

	@Autowired
	private PagedResourcesAssembler<GroupDto> pagedResourcesAssembler;

	@GetMapping
	public PagedModel<EntityModel<GroupDto>> list(
			@PathVariable Long userId,
			@RequestParam(required = false, defaultValue = "0") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			Pageable pageable) {

		User user = apiUserService.findOrFail(userId);

		// Convert the Set<Group> to a List<GroupResponse>
		List<GroupDto> groups = user.getGroups().stream().map(group -> {
			GroupDto response = new GroupDto();
			response.setKey(group.getId());
			response.setName(group.getName());
			// Add other fields if necessary
			return response;
		}).collect(Collectors.toList());

		// Create a Page object from the List<GroupResponse>
		Page<GroupDto> groupsPage = new PageImpl<>(groups, pageable,
				groups.size());

		PagedModel<EntityModel<GroupDto>> groupsPagedModel = pagedResourcesAssembler
				.toModel(groupsPage);

		groupsPagedModel
				.add(WebMvcLinkBuilder
						.linkTo(WebMvcLinkBuilder
								.methodOn(UserGroupRestController.class)
								.list(userId, page, size, pageable))
						.withSelfRel());

		// Add custom links for each group
		groupsPagedModel.getContent().forEach(groupEntityModel -> {
			GroupDto groupDtoResponse = groupEntityModel.getContent();
			if (groupDtoResponse != null) {
				// Link to dissociate group
				groupEntityModel.add(WebMvcLinkBuilder
						.linkTo(WebMvcLinkBuilder
								.methodOn(UserGroupRestController.class)
								.disassociate(userId, groupDtoResponse.getKey()))
						.withRel("disassociate"));
			}
		});

		return groupsPagedModel;
	}

	@DeleteMapping("/{groupId}")
	@Transactional
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> disassociate(@PathVariable Long userId,
			@PathVariable Long groupId) {
		apiUserService.disassociateGroup(userId, groupId);

		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{groupId}")
	@Transactional
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> associate(@PathVariable Long userId,
			@PathVariable Long groupId) {
		apiUserService.associateGroup(userId, groupId);

		return ResponseEntity.noContent().build();
	}

}
