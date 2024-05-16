package com.nofrontier.book.dto.v1.requests;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nofrontier.book.domain.model.Permission;
import com.nofrontier.book.domain.model.Person;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
@JsonPropertyOrder({ "id", "completeName", "email", "password", "passwordConfirmation", 
	"userType", "documentPicture", "userPicture", "enabled", "persons", "permissions" })
public class UserRequest extends RepresentationModel<UserRequest> implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long key;
	
	@NotNull
	@Size(min = 3, max = 255)
	private String completeName;

	@NotNull
	@Size(max = 255)
	@Email
	private String email;

	@NotNull
	@NotEmpty
	private String password;

	@NotNull
	@NotEmpty
	private String passwordConfirmation;

	@NotNull
	@Future
	private OffsetDateTime registerDate;
	
	@NotNull
	private Integer userType;

	@NotNull
	private MultipartFile documentPicture;
	
	@NotNull
	private MultipartFile userPicture;

	private Boolean enabled;

	private List<Person> persons;

	private Set<Permission> permissions;
}
