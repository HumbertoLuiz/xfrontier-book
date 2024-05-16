package com.nofrontier.book.dto.v1.responses;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nofrontier.book.domain.model.Permission;
import com.nofrontier.book.domain.model.Person;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonNaming(SnakeCaseStrategy.class)
public class UserResponse extends RepresentationModel<UserResponse> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("id")
	private Long key;

	private String completeName;

	private String email;

	private String password;

	private String passwordConfirmation;

	private OffsetDateTime registerDate;

	private Integer userType;

	private MultipartFile documentPicture;

	private MultipartFile userPicture;

	private Boolean enabled;

	private List<Person> persons;

	private Set<Permission> permissions;

}
