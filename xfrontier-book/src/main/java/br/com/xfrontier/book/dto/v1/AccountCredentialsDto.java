package br.com.xfrontier.book.dto.v1;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@JsonPropertyOrder({"email", "password"})
public class AccountCredentialsDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@NotBlank(message = "Username cannot be blank")
	@Email
	private String email;
	@NotBlank(message = "Password cannot be blank")
	private String password;
	
	public AccountCredentialsDto() {}
	
	public AccountCredentialsDto(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, password);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccountCredentialsDto other = (AccountCredentialsDto) obj;
		return Objects.equals(email, other.email)
				&& Objects.equals(password, other.password);
	}

}