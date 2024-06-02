package com.nofrontier.book.dto.v1.responses;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nofrontier.book.domain.model.Group;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
@JsonPropertyOrder({"id", "name", "permissions"})
public class GroupResponse extends RepresentationModel<AddressResponse>
		implements
			Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long key;
	private String name;
	private Set<PermissionResponse> permissions = new HashSet<>();

	// Construtor que converte a entidade Group para o response
	public GroupResponse(Group group) {
		this.key = group.getId();
		this.name = group.getName();
		this.permissions = group.getPermissions().stream()
				.map(PermissionResponse::new).collect(Collectors.toSet());
	}

	public void removePermission(Long key) {
		// TODO Auto-generated method stub
		
	}

	public void removePermission(PermissionResponse permission) {
		// TODO Auto-generated method stub
		
	}
}
