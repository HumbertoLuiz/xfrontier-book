package br.com.xfrontier.book.dto.v1;

import java.io.Serializable;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class BookCancellationRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    
	@NotNull
    @NotEmpty
    @Size(min = 3, max = 255)
    private String reasonCancellation;

}
