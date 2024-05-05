package com.nofrontier.book.dto.v1.responses;

import java.io.Serializable;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper= false)
@JsonNaming(SnakeCaseStrategy.class)
public class UserRegisterResponse extends UserResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private TokenResponse token;
}
