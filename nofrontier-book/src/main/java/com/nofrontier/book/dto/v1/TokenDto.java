package com.nofrontier.book.dto.v1;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers.DateDeserializer;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@JsonPropertyOrder({"email", "authenticated", "created", "expiration", "accessToken", "refreshToken"})
public class TokenDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotBlank(message = "Username cannot be blank")
	@Email
	private String email;
	
	private Boolean authenticated;

	@JsonSerialize(using = (DateSerializer.class))
	@JsonDeserialize(using = (DateDeserializer.class))
	private Date created;
    
	@JsonSerialize(using = (DateSerializer.class))
	@JsonDeserialize(using = (DateDeserializer.class))
	private Date expiration;
    
	private String accessToken;
	
	private String refreshToken;
	
	public TokenDto() {}
	
	public TokenDto(
			String email,
			Boolean authenticated,
			Date created,
			Date expiration,
			String accessToken,
			String refreshToken) {
		this.email = email;
		this.authenticated = authenticated;
		this.created = created;
		this.expiration = expiration;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public TokenDto(TokenDto accessToken, String refreshToken) {}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(Boolean authenticated) {
		this.authenticated = authenticated;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accessToken, authenticated, created, email,
				expiration, refreshToken);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TokenDto other = (TokenDto) obj;
		return Objects.equals(accessToken, other.accessToken)
				&& Objects.equals(authenticated, other.authenticated)
				&& Objects.equals(created, other.created)
				&& Objects.equals(email, other.email)
				&& Objects.equals(expiration, other.expiration)
				&& Objects.equals(refreshToken, other.refreshToken);
	}

}
