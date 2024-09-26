package com.nofrontier.book.integrationtests.dto.pagedmodels;

import java.util.List;

import com.nofrontier.book.integrationtests.dto.PersonDto;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PagedModelPerson {

	@XmlElement(name = "content")
	private List<PersonDto> content;

	public PagedModelPerson() {}

	public List<PersonDto> getContent() {
		return content;
	}

	public void setContent(List<PersonDto> content) {
		this.content = content;
	}
}
