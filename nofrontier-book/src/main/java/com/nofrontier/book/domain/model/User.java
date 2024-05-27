package com.nofrontier.book.domain.model;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.nofrontier.book.core.enums.UserType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
public class User extends IdBaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "complete_name", nullable = false)
	private String completeName;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@CreationTimestamp
	@Column(nullable = false, columnDefinition = "datetime")
	private OffsetDateTime registerDate;

	@Enumerated(EnumType.STRING)
	@Column(length = 11, nullable = false)
	private UserType userType;

	@OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "document_picture", nullable = true)
	private Picture documentPicture;

	@OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "user_picture", nullable = true)
	private Picture userPicture;

	@Column(nullable = false)
	private Boolean enabled;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "person_id")
	private Person person;

	@ManyToMany(fetch = FetchType.EAGER, cascade = (CascadeType.ALL))
	@JoinTable(name = "user_permission", joinColumns = {
			@JoinColumn(name = "user_id")}, inverseJoinColumns = {
					@JoinColumn(name = "permission_id")})
	private Set<Permission> permissions = new HashSet<>();

	@ManyToMany(fetch = FetchType.EAGER, cascade = (CascadeType.ALL))
	@JoinTable(name = "user_group", joinColumns = {
			@JoinColumn(name = "user_id")}, inverseJoinColumns = {
					@JoinColumn(name = "group_id")})
	private Set<Group> groups = new HashSet<>();

	public List<String> getRoles() {
		List<String> roles = new ArrayList<>();
		for (Permission permission : permissions) {
			roles.add(permission.getDescription());
		}
		return roles;
	}

	public void addRole(Permission role) {
		permissions.add(role);
	}

	@ManyToMany(fetch = FetchType.EAGER, cascade = (CascadeType.ALL))
	@JoinTable(name = "city_user", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "city_id"))
	private Set<City> cities = new HashSet<>();

	public boolean removeGroup(Group group) {
		return getGroups().remove(group);
	}

	public boolean addGrupo(Group group) {
		return getGroups().add(group);
	}

	public boolean isNew() {
		return getId() == null;
	}

	public Boolean isCustomer() {
		return userType.equals(UserType.CUSTOMER);
	}

	public Boolean isAdmin() {
		return userType.equals(UserType.ADMIN);
	}
}
