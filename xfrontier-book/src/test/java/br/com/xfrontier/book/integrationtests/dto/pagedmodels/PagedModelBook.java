package br.com.xfrontier.book.integrationtests.dto.pagedmodels;

import java.util.List;

import br.com.xfrontier.book.integrationtests.dto.BookDto;
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