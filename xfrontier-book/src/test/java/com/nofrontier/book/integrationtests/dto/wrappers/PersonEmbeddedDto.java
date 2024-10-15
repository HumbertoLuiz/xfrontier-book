package com.nofrontier.book.integrationtests.dto.wrappers;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nofrontier.book.integrationtests.dto.PersonDto;

public class PersonEmbeddedDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("personDtoList")
	private List<PersonDto> persons;

	public PersonEmbeddedDto() {}

	public List<PersonDto> getPersons() {
		return persons;
	}

	public void setPersons(List<PersonDto> persons) {
		this.persons = persons;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((persons == null) ? 0 : persons.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonEmbeddedDto other = (PersonEmbeddedDto) obj;
		if (persons == null) {
			if (other.persons != null)
				return false;
		} else if (!persons.equals(other.persons))
			return false;
		return true;
	}
}
