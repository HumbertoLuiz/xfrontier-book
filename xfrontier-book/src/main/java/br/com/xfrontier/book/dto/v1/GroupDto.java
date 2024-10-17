package br.com.xfrontier.book.dto.v1;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
@JsonNaming(SnakeCaseStrategy.class)
@JsonPropertyOrder({"id", "name", "permissions"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupDto extends RepresentationModel<GroupDto>
		implements
			Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long key;

	@NotBlank(message = "{not.blank.message}")
	private String name;
	
//	@NotEmpty(message = "{not.empty.message}")
//	private Set<Long> permissionIds;
	
	private Set<PermissionDto> permissions = new HashSet<>();


	public void removePermission(Long id) {
		// TODO Auto-generated method stub
		
	}

	public void removePermission(PermissionDto permission) {
		// TODO Auto-generated method stub
		
	}
}
