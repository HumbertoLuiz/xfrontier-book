package com.nofrontier.book.unittests.mapper.mocks;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nofrontier.book.domain.model.Book;
import com.nofrontier.book.dto.v1.BookDto;

public class MockBook {


    public Book mockEntity() {
        return mockEntity(0);
    }
    
    public BookDto mockVO() {
        return mockVO(0);
    }
    
    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<Book>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookDto> mockVOList() {
        List<BookDto> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockVO(i));
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

    public BookDto mockVO(Integer number) {
        BookDto book = new BookDto();
        book.setKey(number.longValue());
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
}