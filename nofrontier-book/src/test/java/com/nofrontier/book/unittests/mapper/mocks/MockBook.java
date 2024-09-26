package com.nofrontier.book.unittests.mapper.mocks;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.nofrontier.book.core.enums.BookStatus;
import com.nofrontier.book.domain.model.Book;
import com.nofrontier.book.dto.v1.BookDto;

public class MockBook {

    public Book mockEntity() {
        return mockEntity(0);
    }
    
    public BookDto mockDto() {
        return mockDto(0);
    }
    
    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<Book>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookDto> mockDtoList() {
        List<BookDto> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockDto(i));
        }
        return books;
    }
    
    public Book mockEntity(Integer number) {
        Book book = new Book();
        book.setId(number.longValue());
        book.setTitle("Some Title" + number);
        book.setAuthor("Some Author" + number);
        book.setIsbn("9780136657125");
        book.setLaunchDate(LocalDate.of(2010, 1, 1));
        book.setCreatedBy(1);
        book.setActive(true);
        book.setBookStatus(BookStatus.PAID);
        book.setShippingRate(BigDecimal.valueOf(0.0));
        book.setPrice(BigDecimal.valueOf(0.0));
        book.setObservation(null);
        book.setReasonCancellation(null);       

        return book;
    }

    public BookDto mockDto(Integer number) {
        BookDto book = new BookDto();
        book.setKey(number.longValue());
        book.setAuthor("Some Author" + number);
        book.setTitle("Some Title" + number);
        book.setIsbn("9780136657125");
        book.setLaunchDate(LocalDate.of(2010, 1, 1));
        book.setCreatedBy(1);
        book.setActive(true);
        book.setBookStatus(BookStatus.PAID);
        book.setShippingRate(BigDecimal.valueOf(0.0));
        book.setPrice(BigDecimal.valueOf(0.0));
        book.setObservation(null);
        book.setReasonCancellation(null); 
     // Adicione um ID válido para a categoria
        book.setCategoryId(1L);  // Supondo que 1L é um ID válido para o teste

        return book;
    }
}
