package com.nofrontier.book.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

import com.nofrontier.book.domain.exceptions.RequiredObjectIsNullException;
import com.nofrontier.book.domain.model.Book;
import com.nofrontier.book.domain.repository.BookRepository;
import com.nofrontier.book.domain.services.BookService;
import com.nofrontier.book.dto.v1.BookDto;
import com.nofrontier.book.unittests.mapper.mocks.MockBook;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

	MockBook input;

	@InjectMocks
	private BookService bookService;

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
		assertEquals(new BigDecimal("25"), result.getPrice());
		assertEquals(LocalDateTime.of(2024, 5, 6, 10, 30), result.getLaunchDate());
		assertEquals(LocalDateTime.of(2024, 5, 6, 10, 40), result.getCreateDate());
		assertEquals(Integer.valueOf(0), result.getCreatedBy());
		assertEquals(Integer.valueOf(0), result.getLastModifiedBy());
		assertEquals(Boolean.valueOf(true), result.getActive());
	}

	// -------------------------------------------------------------------------------------------------------------

	@Test
	void testCreate() {
		Book persisted = input.mockEntity(1);
		persisted.setId(1L);		
		BookDto vo = input.mockVO(1);
		vo.setKey(1L);		
		when(bookRepository.save(any(Book.class))).thenReturn(persisted);		
		var result = bookService.create(vo);		
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
		assertEquals("Some Title1", result.getTitle());
		assertEquals("Some Author1", result.getAuthor());
		assertEquals(new BigDecimal("25"), result.getPrice());
		assertEquals(LocalDateTime.of(2024, 5, 6, 10, 30), result.getLaunchDate());
		assertEquals(LocalDateTime.of(2024, 5, 6, 10, 40), result.getCreateDate());
		assertEquals(Integer.valueOf(0), result.getCreatedBy());
		assertEquals(Integer.valueOf(0), result.getLastModifiedBy());
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
		BookDto vo = input.mockVO(1);
		vo.setKey(1L);
		when(bookRepository.findById(1L)).thenReturn(Optional.of(entity));
		when(bookRepository.save(entity)).thenReturn(persisted);
		var result = bookService.update(vo);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
		assertEquals("Some Title1", result.getTitle());
		assertEquals("Some Author1", result.getAuthor());
		assertEquals(new BigDecimal("25"), result.getPrice());
		assertEquals(LocalDateTime.of(2024, 5, 6, 10, 30), result.getLaunchDate());
		assertEquals(LocalDateTime.of(2024, 5, 6, 10, 40), result.getCreateDate());
		assertEquals(Integer.valueOf(0), result.getCreatedBy());
		assertEquals(Integer.valueOf(0), result.getLastModifiedBy());
		assertEquals(Boolean.valueOf(true), result.getActive()); 
	}

	// -------------------------------------------------------------------------------------------------------------

	@Test
	void testUpdateWithNullBook() {
		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			bookService.update(null);
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

//	@Test
//	void testFindAll(Pageable pageable) {
//		List<Book> bookList = input.mockEntityList();
//        var books = bookService.findAll(pageable);
//        when(bookRepository.findAll()).thenReturn(bookList);
//		assertNotNull(books);
//		assertEquals(14, (((List<?>) books).size()));		
//		var bookOne = ((List<?>) books).get(1);				
//
//		assertNotNull(bookOne);
//		assertNotNull(((BookDto) bookOne).getKey());
//		assertNotNull(((RepresentationModel<?>) bookOne).getLinks());
//		assertTrue(bookOne.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
//		assertEquals("Some Author1", ((Book) bookOne).getAuthor());
//		assertEquals("Some Title1", ((Book) bookOne).getTitle());
//		assertEquals(25D, ((Book) bookOne).getPrice());
//		assertNotNull(((Book) bookOne).getLaunchDate());
//		
//		var bookFour = ((List<?>) books).get(4);
//		assertNotNull(bookFour);
//		assertNotNull(((PersonDto) bookFour).getKey());
//		assertNotNull(((RepresentationModel<?>) bookFour).getLinks());
//		assertTrue(bookFour.toString().contains("links: [</api/book/v1/4>;rel=\"self\"]"));
//		assertEquals("Some Author4", ((Book) bookFour).getAuthor());
//		assertEquals("Some Title4", ((Book) bookFour).getTitle());
//		assertEquals(25D, ((Book) bookFour).getPrice());
//		assertNotNull(((Book) bookFour).getLaunchDate());
//		
//		var bookSeven = ((List<?>) books).get(7);
//		assertNotNull(bookSeven);
//		assertNotNull(((PersonDto) bookSeven).getKey());
//		assertNotNull(((RepresentationModel<?>) bookSeven).getLinks());
//		assertTrue(bookSeven.toString().contains("links: [</api/book/v1/7>;rel=\"self\"]"));
//		assertEquals("Some Author7", ((Book) bookSeven).getAuthor());
//		assertEquals("Some Title7", ((Book) bookSeven).getTitle());
//		assertEquals(25D, ((Book) bookSeven).getPrice());
//		assertNotNull(((Book) bookSeven).getLaunchDate());
//	}
}
