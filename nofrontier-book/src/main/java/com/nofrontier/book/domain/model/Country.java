package com.nofrontier.book.domain.model;

import java.io.Serializable;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "countries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true, callSuper = false)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Country extends IdBaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 45)
	private String name;

	@Column(nullable = false, length = 5)
	private String code;

	// @JsonBackReference
	@OneToMany(mappedBy = "country")
	private Set<State> states;

}
