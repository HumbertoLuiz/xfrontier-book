package com.nofrontier.book.dto.v1.requests;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class GroupRequest implements Serializable {

	private static final long serialVersionUID = 1L;

    @NotBlank(message = "{not.blank.message}")
    private Long id;
	
    @NotBlank(message = "{not.blank.message}")
    private String name;

    @NotEmpty(message = "{not.empty.message}")
    private Set<Long> permissionIds;
}
