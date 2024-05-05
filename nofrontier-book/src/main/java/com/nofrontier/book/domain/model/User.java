package com.nofrontier.book.domain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nofrontier.book.core.enums.UserType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "users")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String completeName;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(length = 11, nullable = false)
	@Enumerated(EnumType.STRING)
	private UserType userType;

	@OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "document_picture", nullable = true)
	private Picture documentPicture;

	@OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "user_picture", nullable = true)
	private Picture userPicture;

	@Column(nullable = false)
	private Boolean enabled;

	@JsonManagedReference
	@OneToMany(mappedBy = "users")
	private List<Person> persons = new ArrayList<>();

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_permission", joinColumns = {
			@JoinColumn(name = "id_user")}, inverseJoinColumns = {
					@JoinColumn(name = "id_permission")})
	private Set<Permission> permissions = new HashSet<>();

	public List<String> getRoles() {
		List<String> roles = new ArrayList<>();
		for (Permission permission : permissions) {
			roles.add(permission.getDescription());
		}
		return roles;
	}

	public Boolean isCustomer() {
		return userType.equals(UserType.CUSTOMER);
	}
}
