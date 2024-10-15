package com.nofrontier.book.domain.model;

import java.io.Serializable;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "permission")
public class Permission extends IdBaseEntity implements GrantedAuthority, Serializable {

	private static final long serialVersionUID = 1L;
    
	@Column(name = "name", nullable = false )
	private String name;
	
	@Column(name = "description", nullable = false )
	private String description;

	@Override
	public String getAuthority() {
		return this.description;
	}

}
