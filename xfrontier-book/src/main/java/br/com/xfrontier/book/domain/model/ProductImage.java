package br.com.xfrontier.book.domain.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
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
public class ProductImage extends IdBaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	private Product product;

	private String fileName;
	private String description;
	private String contentType;
	private Long size;

	public Long getBookId() {
		if (getProduct() != null) {
			return getProduct().getBook().getId();
		}
		return null;
	}
}