package br.com.xfrontier.book.dto.v1;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import br.com.xfrontier.book.core.enums.UserType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
// @JsonNaming(SnakeCaseStrategy.class) // não funciona com @ModelAttribute apenas com @RequestBody
@JsonPropertyOrder({"id", "completeName", "email", "registerDate", "userType",
		"documentPicture", "userPicture", "enabled", "person", "permissions",
		"groups", "cities"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto extends RepresentationModel<UserDto>
		implements
			Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long key;

	@NotBlank(message = "{not.blank.message}")
	@Size(min = 3, max = 255, message = "{size.message}")
	private String completeName;

	@NotBlank(message = "{not.blank.message}")
	@Size(max = 255, message = "{size.message}")
	@Email(message = "{email.invalid}")
	private String email;

	@JsonIgnore
	@NotBlank(message = "{not.blank.message}")
	@Size(min = 6, message = "{size.message}")
	private String password;

	@NotBlank(message = "{not.blank.message}")
	private String passwordConfirmation;

	private OffsetDateTime registerDate;

	@NotNull(message = "{not.null.message}")
	private Integer userType;

	private MultipartFile documentPicture; // precisa usar @ModelAttribute ao
											// invés de @RequestBody senão não
											// funciona

	private MultipartFile userPicture; // precisa usar @ModelAttribute ao invés
										// de @RequestBody senão não funciona

//	private Boolean accountNonExpired;
//	
//	private Boolean accountNonLocked;
//
//	private Boolean credentialsNonExpired;
	
	@NotNull(message = "{not.null.message}")
	private Boolean enabled;
	
	// @NotNull(message = "{not.null.message}") // removed to be able to
	// register user without having to specify person
	//private Long personId;
	
	@JsonIgnore
	private PersonDto person;

	private Set<PermissionDto> permissions = new HashSet<>();

	private Set<GroupDto> groups = new HashSet<>();

	private Set<CityDto> cities = new HashSet<>();

	@JsonIgnore
	public Boolean isCustomer() {
		return userType.equals(UserType.CUSTOMER.getId());
	}

}
