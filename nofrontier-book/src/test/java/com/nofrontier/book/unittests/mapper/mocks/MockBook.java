package com.nofrontier.book.unittests.mapper.mocks;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nofrontier.book.domain.model.Book;
import com.nofrontier.book.dto.v1.requests.BookRequest;
import com.nofrontier.book.dto.v1.responses.BookResponse;

public class MockBook {


    public Book mockEntity() {
        return mockEntity(0);
    }
    
    public BookRequest mockRequest() {
        return mockRequest(0);
    }
    
    public BookResponse mockResponse(int i) {
        return mockResponse(0);
    }
    
    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookRequest> mockRequestList() {
        List<BookRequest> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockRequest(i));
        }
        return books;
    }
    
    public List<BookResponse> mockResponseList() {
        List<BookResponse> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockResponse(i));
        }
        return books;
    }

    
    public Book mockEntity(Integer number) {
        Book book = new Book();
        book.setId(number.longValue());
        book.setTitle("Some Title" + number);
        book.setAuthor("Some Author" + number);
        book.setIsbn("Some Isbn" + number);
        book.setLaunchDate(new Date());
        book.setRegistrationDate(OffsetDateTime.of(2024, 5, 6, 10, 30, number, number, null));
        book.setUpdateDate(OffsetDateTime.of(2024, 5, 6, 10, 40, number, number, null));
        book.setCreatedBy(number);
    	book.setLastModifiedBy(number);
    	book.setActive(true); 	
        return book;
    }
    
    public BookRequest mockRequest(Integer number) {
        BookRequest book = new BookRequest();
        book.setAuthor("Some Author" + number);
        book.setLaunchDate(new Date());
        book.setTitle("Some Title" + number);
        book.setActive(true);
        // Set other fields as necessary
        return book;
    }

    public BookResponse mockVO(Integer number) {
    	BookResponse book = new BookResponse();
        book.setKey(number.longValue());
        book.setTitle("Some Title" + number);
        book.setAuthor("Some Author" + number);
        book.setIsbn("Some Isbn" + number);
        book.setLaunchDate(new Date());
    	book.setActive(true); 
        // Set other fields as necessary, for example:
        // book.setOrder(mockOrderResponse(number));
        // book.setPaymentMethods(mockPaymentMethodResponseSet());
        // book.setResponsible(mockUserResponseSet());
        // book.setProducts(mockProductResponseSet());
        return book;
    }
}