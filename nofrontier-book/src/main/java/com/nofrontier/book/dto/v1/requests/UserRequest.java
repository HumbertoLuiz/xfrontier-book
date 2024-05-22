package com.nofrontier.book.dto.v1.requests;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nofrontier.book.domain.model.City;
import com.nofrontier.book.domain.model.Group;
import com.nofrontier.book.domain.model.Permission;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
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

	@NotBlank(message = "Name is mandatory")
	@Size(min = 3, max = 255)
	private String completeName;

	@NotBlank(message = "Email is mandatory")
	@Size(max = 255)
	@Email(message = "Email should be valid")
	private String email;

	@NotBlank(message = "Password is mandatory")
	@Size(min = 6, message = "Password must be at least 6 characters long")
	private String password;

	@NotBlank
	private String passwordConfirmation;

	@Future
	private OffsetDateTime registerDate;

	@NotNull
	private Integer userType;

	private MultipartFile documentPicture;

	private MultipartFile userPicture;

	@NotNull
	private Boolean enabled;

	private Long personId;

	private Set<Permission> permissions = new HashSet<>();

	private Set<Group> groups = new HashSet<>();

	private Set<City> cities = new HashSet<>();

}
