package com.nofrontier.book.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import com.nofrontier.book.domain.exceptions.RequiredObjectIsNullException;
import com.nofrontier.book.domain.model.Book;
import com.nofrontier.book.domain.repository.BookRepository;
import com.nofrontier.book.domain.services.ApiBookService;
import com.nofrontier.book.dto.v1.requests.BookRequest;
import com.nofrontier.book.dto.v1.responses.BookResponse;
import com.nofrontier.book.unittests.mapper.mocks.MockBook;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

	MockBook input;

	@InjectMocks
	private ApiBookService bookService;

	@Mock
	BookRepository bookRepository;

	@BeforeEach
	void setUpMocks() throws Exception {
		input = new MockBook();
		MockitoAnnotations.openMocks(this);
	}

	// -------------------------------------------------------------------------------------------------------------

	@Test
	void testFindById() {
		Book entity = input.mockEntity(1);
		entity.setId(1L);
		when(bookRepository.findById(1L)).thenReturn(Optional.of(entity));
		var result = bookService.findById(1L);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
		assertEquals("Some Title1", result.getTitle());
		assertEquals("Some Author1", result.getAuthor());
		assertEquals(LocalDateTime.of(2024, 5, 6, 10, 30), result.getLaunchDate());
		assertEquals(Boolean.valueOf(true), result.getActive());
	}

	// -------------------------------------------------------------------------------------------------------------

	@Test
	void testCreate() {
        Book entity = input.mockEntity(1); 
        entity.setId(1L);

        Book persisted = entity;
        persisted.setId(1L);

        BookRequest vo = input.mockRequest(1);

        when(bookRepository.save(entity)).thenReturn(persisted);

        var result = bookService.create(vo);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Some Author1", result.getAuthor());
        assertEquals("Some Title1", result.getTitle());
        assertNotNull(result.getLaunchDate());
        assertEquals(Boolean.valueOf(true), result.getActive());
	}

	// -------------------------------------------------------------------------------------------------------------

	@Test
	void testCreateWithNullBook() {
		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			bookService.create(null);
		});
		String expectedMessage = "It is not allowed to persist a null object!";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	// -------------------------------------------------------------------------------------------------------------

    @Test
    void testUpdate() {
        Book entity = input.mockEntity(1); 

        Book persisted = entity;
        persisted.setId(1L);

        BookRequest vo = input.mockRequest(1);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(bookRepository.save(entity)).thenReturn(persisted);

        var result = bookService.update(null, vo);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Some Author1", result.getAuthor());
        assertEquals("Some Title1", result.getTitle());
        assertNotNull(result.getLaunchDate());
        assertEquals(Boolean.valueOf(true), result.getActive());
    }

	// -------------------------------------------------------------------------------------------------------------

	@Test
	void testUpdateWithNullBook() {
		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			bookService.update(null, null);
		});
		String expectedMessage = "It is not allowed to persist a null object!";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	// -------------------------------------------------------------------------------------------------------------

	@Test
	void testDelete() {
		Book entity = input.mockEntity(1);
		entity.setId(1L);
		when(bookRepository.findById(1L)).thenReturn(Optional.of(entity));
		bookService.delete(1L);
	}
	
	// -------------------------------------------------------------------------------------------------------------


	@Test
    void testFindAll(Pageable pageable) {
        List<Book> bookList = input.mockEntityList();
        Page<Book> bookPage = new PageImpl<>(bookList);

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        PagedModel<EntityModel<BookResponse>> books = bookService.findAll(pageable);

        assertNotNull(books);
        assertEquals(14, books.getContent().size());

        // Convert PagedModel content to list for easier access by index
        List<EntityModel<BookResponse>> bookResponses = new ArrayList<>(books.getContent());

        var bookOne = bookResponses.get(1).getContent();
        assertNotNull(bookOne);
        assertNotNull(bookOne.getKey());
        assertNotNull(bookOne.getLinks());
        assertTrue(bookOne.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Some Author1", bookOne.getAuthor());
        assertEquals("Some Title1", bookOne.getTitle());
        assertNotNull(bookOne.getLaunchDate());
        assertEquals(Boolean.valueOf(true), bookOne.getActive());

        var bookFour = bookResponses.get(4).getContent();
        assertNotNull(bookFour);
        assertNotNull(bookFour.getKey());
        assertNotNull(bookFour.getLinks());
        assertTrue(bookFour.toString().contains("links: [</api/book/v1/4>;rel=\"self\"]"));
        assertEquals("Some Author4", bookFour.getAuthor());
        assertEquals("Some Title4", bookFour.getTitle());
        assertNotNull(bookFour.getLaunchDate());
        assertEquals(Boolean.valueOf(true), bookFour.getActive());

        var bookSeven = bookResponses.get(7).getContent();
        assertNotNull(bookSeven);
        assertNotNull(bookSeven.getKey());
        assertNotNull(bookSeven.getLinks());
        assertTrue(bookSeven.toString().contains("links: [</api/book/v1/7>;rel=\"self\"]"));
        assertEquals("Some Author7", bookSeven.getAuthor());
        assertEquals("Some Title7", bookSeven.getTitle());
        assertNotNull(bookSeven.getLaunchDate());
        assertEquals(Boolean.valueOf(true), bookSeven.getActive());
    }
}
