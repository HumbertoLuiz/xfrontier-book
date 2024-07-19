package com.nofrontier.book.integrationstests.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlRootElement
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// @Mapping("id")
	@JsonProperty("id")
	private Long key;
	
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
}
