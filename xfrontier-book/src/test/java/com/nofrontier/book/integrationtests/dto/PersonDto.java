package com.nofrontier.book.integrationtests.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@JsonNaming(SnakeCaseStrategy.class)
public class PersonDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String firstName;
	private String lastName;
	private String gender;
	private String cpf;
	private LocalDate birth;
	private String phoneNumber;
	private String mobileNumber;
	private String keyPix;
	private Boolean enabled;
	
	public PersonDto() {}
	
	public PersonDto(Long id, String firstName, String lastName, String gender,
			String cpf, LocalDate birth, String phoneNumber,
			String mobileNumber, String keyPix, Boolean enabled) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.cpf = cpf;
		this.birth = birth;
		this.phoneNumber = phoneNumber;
		this.mobileNumber = mobileNumber;
		this.keyPix = keyPix;
		this.enabled = enabled;
	}


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
		return Objects.hash(birth, cpf, enabled, firstName, gender, id, keyPix,
				lastName, mobileNumber, phoneNumber);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonDto other = (PersonDto) obj;
		return Objects.equals(birth, other.birth)
				&& Objects.equals(cpf, other.cpf)
				&& Objects.equals(enabled, other.enabled)
				&& Objects.equals(firstName, other.firstName)
				&& Objects.equals(gender, other.gender)
				&& Objects.equals(id, other.id)
				&& Objects.equals(keyPix, other.keyPix)
				&& Objects.equals(lastName, other.lastName)
				&& Objects.equals(mobileNumber, other.mobileNumber)
				&& Objects.equals(phoneNumber, other.phoneNumber);
	}
	
}
