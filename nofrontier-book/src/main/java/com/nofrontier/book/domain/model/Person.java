package com.nofrontier.book.domain.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true, callSuper = false)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Person extends IdBaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(nullable = false, length = 80)
	private String firstName;
	
	@Column(nullable = false, length = 80)
	private String lastName;
	
	@Column(nullable = false, length = 6)
	private String gender;
	
	@Column(nullable = true, unique = true, length = 11)
	private String cpf;

	@Column(nullable = true)
	private LocalDate birth;

	@Column(name = "phone_number", nullable = false, length = 11)
	protected String phoneNumber;
	
	@Column(name = "mobile_number", nullable = false, length = 11)
	protected String mobileNumber;

	@Column(name = "key_pix", nullable = true, unique = true)
	private String keyPix;
	
	@Column(nullable = false)
	private Boolean enabled;
	
	@JsonManagedReference
    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<User> users = new HashSet<>();	
	
	@JsonManagedReference
    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Address> addresses = new HashSet<>();

	public void setAddresses(Address address) {}

}
