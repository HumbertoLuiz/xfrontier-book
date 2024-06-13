package com.nofrontier.book.dto.v1.requests;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

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
//@JsonNaming(SnakeCaseStrategy.class) // não funciona com @ModelAttribute apenas com @RequestBody
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
	private Integer userType;

	private MultipartFile documentPicture; // precisa usar @ModelAttribute ao invés de @RequestBody senão não funciona

	private MultipartFile userPicture; // precisa usar @ModelAttribute ao invés de @RequestBody senão não funciona

	@NotNull(message = "{not.null.message}")
	private Boolean enabled;
	
	
    public void setComplete_name(String completeName) {
        setCompleteName(completeName);
    }

    public void setPassword_confirmation(String passwordConfirmation) {
        setPasswordConfirmation(passwordConfirmation);
    }

    public void setUser_type(Integer userType) {
        setUserType(userType);
    }

    public void setDocument_picture(MultipartFile documentPicture) {
        setDocumentPicture(documentPicture);
    }

    public void setUser_picture(MultipartFile userPicture) {
        setUserPicture(userPicture);
    }
	
	// @NotNull(message = "{not.null.message}") // removed to be able to register user without having to specify person
	private Long personId;

	private Set<PermissionRequest> permissions = new HashSet<>();

	private Set<GroupRequest> groups = new HashSet<>();

	private Set<CityRequest> cities = new HashSet<>();

}
