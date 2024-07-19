package com.nofrontier.book.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Product extends IdBaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "description", columnDefinition = "TEXT NOT NULL")
	private String description;
	
	@Column(nullable = false)
	private String format;
	
	@Column(nullable = false)
	private String edition;
	
	@Column(nullable = false)
	private BigDecimal price;
	
	@Column(nullable = false)
	private Boolean active;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "book_id", nullable = false)
	private Book book;
	
}