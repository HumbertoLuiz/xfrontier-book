package com.nofrontier.book.unittests.mapper.mocks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nofrontier.book.dto.v1.requests.BookRequest;
import com.nofrontier.book.domain.model.Book;

public class MockBook {

    public Book mockEntity(Integer number) {
        Book book = new Book();
        book.setId(number.longValue());
        book.setAuthor("Some Author" + number);
        book.setLaunchDate(new Date());
        book.setTitle("Some Title" + number);
        // Define outros atributos conforme necessário para os testes
        return book;
    }

    public BookRequest mockRequest(Integer number) {
        BookRequest request = new BookRequest();
        request.setAuthor("Some Author" + number);
        request.setLaunchDate(new Date());
        request.setTitle("Some Title" + number);
        // Define outros atributos conforme necessário para os testes
        return request;
    }

    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookRequest> mockRequestList() {
        List<BookRequest> requests = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            requests.add(mockRequest(i));
        }
        return requests;
    }
}
