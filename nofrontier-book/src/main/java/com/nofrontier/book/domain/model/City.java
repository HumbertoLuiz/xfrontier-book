package com.nofrontier.book.domain.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nofrontier.book.core.validation.Groups;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.ConvertGroup;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "cities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
public class City extends IdBaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private String name;
	
    @Column(name = "ibge_code", unique = true)
    private String ibgeCode;
	
	@Valid
	@ConvertGroup(from = Default.class, to = Groups.StateId.class)
	@NotNull
	@ManyToOne
	@JoinColumn(nullable = false)
	private State state;
	
	@JsonManagedReference
    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    private Set<Address> addresses = new HashSet<>();	
	
    @ManyToMany(mappedBy = "cities")
    private Set<User> users = new HashSet<>();

}