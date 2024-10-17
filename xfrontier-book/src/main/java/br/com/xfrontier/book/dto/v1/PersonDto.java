package br.com.xfrontier.book.dto.v1;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import br.com.xfrontier.book.core.validation.Age;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@JsonNaming(SnakeCaseStrategy.class)
@JsonPropertyOrder({"id", "firstName", "lastName", "gender", "cpf", "birth",
		"phoneNumber", "mobileNumber", "keyPix", "enabled", "users",
		"addresses"})
public class PersonDto extends RepresentationModel<PersonDto>
		implements
			Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long key;

	@NotBlank(message = "{not.blank.message}")
	@Size(min = 3, max = 255, message = "{size.message}")
	private String firstName;

	@NotBlank(message = "{not.blank.message}")
	@Size(min = 3, max = 255, message = "{size.message}")
	private String lastName;

	@NotBlank(message = "{not.blank.message}")
	@Size(min = 4, max = 6, message = "{size.message}")
	private String gender;

	@Pattern(regexp = "^\\d{11}$|^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$", message = "{cpf.invalid}")
	private String cpf;

	@JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
	@Past(message = "{past.message}")
	@Age(min = 18, max = 100, message = "{size.message}")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate birth;

	@NotBlank(message = "{not.blank.message}")
	@Size(min = 10, max = 11, message = "{size.message}")
	private String phoneNumber;

	@NotBlank(message = "{not.blank.message}")
	@Size(min = 11, max = 11, message = "{size.message}")
	private String mobileNumber;

	@Size(min = 11, max = 255, message = "{size.message}")
	private String keyPix;

	@NotNull(message = "{not.null.message}")
	private Boolean enabled;
	
	public PersonDto() {}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public LocalDate getBirth() {
		return birth;
	}

	public void setBirth(LocalDate birth) {
		this.birth = birth;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getKeyPix() {
		return keyPix;
	}

	public void setKeyPix(String keyPix) {
		this.keyPix = keyPix;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(birth, cpf, enabled, firstName,
				gender, key, keyPix, lastName, mobileNumber, phoneNumber);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonDto other = (PersonDto) obj;
		return Objects.equals(birth, other.birth)
				&& Objects.equals(cpf, other.cpf)
				&& Objects.equals(enabled, other.enabled)
				&& Objects.equals(firstName, other.firstName)
				&& Objects.equals(gender, other.gender)
				&& Objects.equals(key, other.key)
				&& Objects.equals(keyPix, other.keyPix)
				&& Objects.equals(lastName, other.lastName)
				&& Objects.equals(mobileNumber, other.mobileNumber)
				&& Objects.equals(phoneNumber, other.phoneNumber);
	}

	// @NotEmpty(message = "{not.empty.message}") // Removendo a anotação
	// @NotEmpty para tornar campo opcional
	//private Set<UserDto> users = new HashSet<>();

	// @NotEmpty(message = "{not.empty.message}") // Removendo a anotação
	// @NotEmpty para tornar campo opcional
	//private Set<AddressDto> addresses = new HashSet<>();

}
