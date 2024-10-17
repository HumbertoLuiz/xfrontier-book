package br.com.xfrontier.book.dto.v1;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

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
public class UserRegisterResponse extends UserDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private TokenDto token;
	
	private List<String> roles;
}
