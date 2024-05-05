package com.nofrontier.book.dto.v1.responses;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nofrontier.book.core.enums.UserType;

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
	private Integer userType;
	private String cpf;
	private LocalDate birth;
	private String phoneNumber;
	private String keyPix;
    private String userPicture;

	@JsonIgnore
	public Boolean isCustomer() {
		return userType.equals(UserType.CUSTOMER.getId());
	}
}
