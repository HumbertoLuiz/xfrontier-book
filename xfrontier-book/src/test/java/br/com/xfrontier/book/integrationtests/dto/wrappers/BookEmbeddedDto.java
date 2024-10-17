package br.com.xfrontier.book.integrationtests.dto.wrappers;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import br.com.xfrontier.book.integrationtests.dto.BookDto;

public class BookEmbeddedDto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @JsonProperty("bookDtoList")
    private List<BookDto> books;

    public BookEmbeddedDto() {}

    public List<BookDto> getBooks() {
        return books;
    }

    public void setBooks(List<BookDto> books) {
        this.books = books;
    }
}