package com.nofrontier.book.dto.v1.responses;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nofrontier.book.core.enums.UserType;
import com.nofrontier.book.domain.model.City;
import com.nofrontier.book.domain.model.Group;
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
@JsonPropertyOrder({"id", "completeName", "email", "password",
		"passwordConfirmation", "registerDate", "userType", "documentPicture",
		"userPicture", "enabled", "persons", "permissions", "groups", "cities",
		"address"})
public class UserResponse extends RepresentationModel<UserResponse>
		implements
			Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long key;

	private String completeName;

	private String email;

	private OffsetDateTime registerDate;

	private Integer userType;

	private MultipartFile documentPicture;

	private MultipartFile userPicture;

	private Boolean enabled;

	private Person person;

	private Set<Permission> permissions = new HashSet<>();

	private Set<Group> groups = new HashSet<>();

	private Set<City> cities = new HashSet<>();

    @JsonIgnore
    public Boolean isCustomer() {
        return userType.equals(UserType.CUSTOMER.getId());
    }
}
