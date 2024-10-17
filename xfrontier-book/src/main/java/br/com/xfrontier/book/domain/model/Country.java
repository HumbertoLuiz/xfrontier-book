package br.com.xfrontier.book.domain.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
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

	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false, length = 5, unique = true)
	private String initials;

	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "country")
	private Set<State> states = new HashSet<>();

}
