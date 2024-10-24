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

import br.com.xfrontier.book.domain.model.Group;
import br.com.xfrontier.book.domain.services.ApiGroupService;
import br.com.xfrontier.book.dto.v1.PermissionDto;

@CrossOrigin(origins = {"http://localhost:8080", "https://xfrontier.com.br"})
@RestController
@RequestMapping(path = "/api/groups/{groupId}/permissions/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class GroupPermissionRestController {

	@Autowired
	private ApiGroupService apiGroupService;

	@Autowired
	private PagedResourcesAssembler<PermissionDto> pagedResourcesAssembler;

	@GetMapping
	public PagedModel<EntityModel<PermissionDto>> list(
			@PathVariable Long groupId,
			@RequestParam(required = false, defaultValue = "0") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			Pageable pageable) {

		Group group = apiGroupService.findOrFail(groupId);

		// Convert the Set<Permission> to a List<PermissionResponse>
		List<PermissionDto> permissions = group.getPermissions().stream()
				.map(permission -> {
					PermissionDto response = new PermissionDto();
					response.setKey(permission.getId());
					response.setName(permission.getName());
					response.setDescription(permission.getDescription());
					// Add other fields if necessary
					return response;
				}).collect(Collectors.toList());

		// Create a Page object from the List<PermissionResponse>
		Page<PermissionDto> permissionsPage = new PageImpl<>(permissions,
				pageable, permissions.size());

		PagedModel<EntityModel<PermissionDto>> permissionsPagedModel = pagedResourcesAssembler
				.toModel(permissionsPage);

		permissionsPagedModel
				.add(WebMvcLinkBuilder
						.linkTo(WebMvcLinkBuilder
								.methodOn(GroupPermissionRestController.class)
								.list(groupId, page, size, pageable))
						.withSelfRel());

		// Add custom links for each permission
		permissionsPagedModel.getContent().forEach(permissionEntityModel -> {
			PermissionDto permissionResponse = permissionEntityModel
					.getContent();
			if (permissionResponse != null) {
				// Link to disassociate permission
				permissionEntityModel.add(WebMvcLinkBuilder
						.linkTo(WebMvcLinkBuilder
								.methodOn(GroupPermissionRestController.class)
								.disassociate(groupId,
										permissionResponse.getKey()))
						.withRel("disassociate"));
			}
		});

		return permissionsPagedModel;
	}

	@DeleteMapping("/{permissionId}")
	@Transactional
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> disassociate(@PathVariable Long grupoId,
			@PathVariable Long permissaoId) {
		apiGroupService.disassociatePermission(grupoId, permissaoId);

		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{permissionId}")
	@Transactional
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> associate(@PathVariable Long grupoId,
			@PathVariable Long permissaoId) {
		apiGroupService.associatePermission(grupoId, permissaoId);

		return ResponseEntity.noContent().build();
	}

}
