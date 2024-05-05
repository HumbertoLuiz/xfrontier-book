package com.nofrontier.book.domain.model;

import java.io.Serializable;

import com.nofrontier.book.core.enums.AddressType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Address extends IdBaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 64)
	protected String address;

	@Column(nullable = false, length = 10)
	private String number;

	@Column(nullable = false, length = 30)
	private String neighborhood;

	@Column(nullable = true)
	private String complement;

	@Column(name = "zip_code", nullable = false, length = 10)
	protected String zipCode;

	@Column(length = 11, nullable = false)
	@Enumerated(EnumType.STRING)
	protected AddressType addressType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "endereco_cidade_id")
	private City city;

	public Boolean isResidential() {
		return addressType.equals(AddressType.RESIDENTIAL);
	}

	public Boolean isCommercial() {
		return addressType.equals(AddressType.COMMERCIAL);
	}
}
