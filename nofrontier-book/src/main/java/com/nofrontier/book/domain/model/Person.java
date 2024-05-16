package com.nofrontier.book.domain.model;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
	
	@JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User users;	
	
	@OneToOne(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id", nullable = true)
	private Address address;
	
}
