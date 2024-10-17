package br.com.xfrontier.book.domain.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Table(name = "states")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded= true, callSuper= false)
@EqualsAndHashCode(onlyExplicitlyIncluded= true, callSuper= false)
public class State extends IdBaseEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 45)
	private String name;

    @Column(name = "ibge_code", unique = true)
    private String ibgeCode;
    
	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "state")
	private Set<City> cities = new HashSet<>();
    
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "country_id")
	private Country country;

}