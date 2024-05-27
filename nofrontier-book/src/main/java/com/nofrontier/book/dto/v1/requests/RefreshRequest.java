package com.nofrontier.book.dto.v1.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshRequest {

	@NotBlank(message = "{not.blank.message}")
    private String refresh;
}
