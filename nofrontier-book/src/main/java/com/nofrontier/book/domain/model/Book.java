package com.nofrontier.book.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
@Table(name = "books")
public class Book extends IdBaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 250)
	private String title;

	@Column(nullable = false, length = 180)
	private String author;

	@Column(nullable = false, unique = true, length = 13)
	private String isbn;
	
	@Column(nullable = false)
	private BigDecimal price;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date launchDate;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createDate;

	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime lastModified;

	@CreatedBy
	@Column(nullable = false, updatable = false)
	private Integer createdBy;

	@LastModifiedBy
	@Column(insertable = false)
	private Integer lastModifiedBy;
	
	@Column(nullable = false)
	private Boolean active;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

}
