package com.nofrontier.book.domain.model;

import java.io.Serializable;
import java.time.OffsetDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@ToString(onlyExplicitlyIncluded= true)
public class PaymentMethod extends IdBaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(nullable = false)
	private String description;
	
	@UpdateTimestamp
	private OffsetDateTime dateUpdate;
	
}