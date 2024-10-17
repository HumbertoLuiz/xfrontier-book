package br.com.xfrontier.book.integrationtests.dto;

import java.io.Serializable;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@JsonNaming(SnakeCaseStrategy.class)
public class CategoryDto extends RepresentationModel<CategoryDto>
		implements
			Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String title;

	private String name;

	private String description;

	public CategoryDto() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(description, id, name, title);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		CategoryDto other = (CategoryDto) obj;
		return Objects.equals(description, other.description)
				&& Objects.equals(id, other.id)
				&& Objects.equals(name, other.name)
				&& Objects.equals(title, other.title);
	}
	
}