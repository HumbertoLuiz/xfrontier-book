package br.com.xfrontier.book.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true, callSuper = false)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "product")
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
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id", nullable = false)
	private Book book;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "product",  fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<OrderItem> items = new ArrayList<>();
}