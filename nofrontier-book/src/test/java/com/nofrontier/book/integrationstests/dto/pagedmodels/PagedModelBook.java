package com.nofrontier.book.integrationstests.dto.pagedmodels;

import java.util.List;

import com.nofrontier.book.integrationstests.dto.BookDto;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PagedModelBook { 
	
	@XmlElement(name = "content") 
	private List<BookDto> content;

	public PagedModelBook() {}

	public List<BookDto> getContent() {
		return content;
	}

	public void setContent(List<BookDto> content) {
		this.content = content;
	}
}