package com.nofrontier.book.dto.v1.requests;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nofrontier.book.core.enums.UserType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class UserRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "{not.blank.message}")
	@Size(min = 3, max = 255, message = "{size.message}")
	private String completeName;

	@NotBlank(message = "{not.blank.message}")
	@Size(max = 255, message = "{size.message}")
	@Email(message = "{email.invalid}")
	private String email;

	@NotBlank(message = "{not.blank.message}")
	@Size(min = 6, message = "{size.message}")
	private String password;

	@NotBlank(message = "{not.blank.message}")
	private String passwordConfirmation;

	private OffsetDateTime registerDate;

	@NotNull(message = "{not.null.message}")
	private UserType userType;

	private MultipartFile documentPicture;

	private MultipartFile userPicture;

	@NotNull(message = "{not.null.message}")
	private Boolean enabled;
	
	// @NotNull(message = "{not.null.message}") // removed to be able to register user without having to specify person
	private Long personId;

	private Set<PermissionRequest> permissions = new HashSet<>();

	private Set<GroupRequest> groups = new HashSet<>();

	private Set<CityRequest> cities = new HashSet<>();

}
