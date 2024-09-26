package com.nofrontier.book.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.nofrontier.book.core.enums.BookStatus;
import com.nofrontier.book.domain.exceptions.RequiredObjectIsNullException;
import com.nofrontier.book.domain.model.Book;
import com.nofrontier.book.domain.model.Category;
import com.nofrontier.book.domain.repository.BookRepository;
import com.nofrontier.book.domain.repository.CategoryRepository;
import com.nofrontier.book.domain.services.ApiBookService;
import com.nofrontier.book.dto.v1.BookDto;
import com.nofrontier.book.unittests.mapper.mocks.MockBook;
import com.nofrontier.book.unittests.mapper.mocks.MockCategory;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class ApiBookServiceTest {

    @InjectMocks
    private ApiBookService apiBookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    private MockBook mockBook;
    private MockCategory mockCategory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockBook = new MockBook();
        mockCategory = new MockCategory();

        MockitoAnnotations.openMocks(this);

        // Stubbing para ModelMapper (Book -> BookDto)
        lenient().when(modelMapper.map(Mockito.any(Book.class), Mockito.eq(BookDto.class)))
            .thenAnswer(invocation -> {
                Book source = invocation.getArgument(0);
                BookDto vo = new BookDto();
                vo.setKey(source.getId());
                vo.setAuthor(source.getAuthor());
                vo.setTitle(source.getTitle());
                vo.setIsbn(source.getIsbn());
                vo.setLaunchDate(source.getLaunchDate());
                vo.setCreatedBy(source.getCreatedBy());
                vo.setActive(source.getActive());
                vo.setBookStatus(source.getBookStatus());
                vo.setShippingRate(source.getShippingRate());
                vo.setPrice(source.getPrice());
                vo.setObservation(source.getObservation());
                vo.setReasonCancellation(source.getReasonCancellation()); 

                if (source.getCategory() != null) {
                    vo.setCategoryId(source.getCategory().getId());
                }
                return vo;
            });

        // Stubbing para ModelMapper (BookDto -> Book)
        lenient().when(modelMapper.map(Mockito.any(BookDto.class), Mockito.eq(Book.class)))
            .thenAnswer(invocation -> {
                BookDto source = invocation.getArgument(0);
                Book entity = new Book();
                entity.setId(source.getKey());
                entity.setAuthor(source.getAuthor());
                entity.setTitle(source.getTitle());
                entity.setIsbn(source.getIsbn());
                entity.setLaunchDate(source.getLaunchDate());
                entity.setCreatedBy(source.getCreatedBy());
                entity.setActive(source.getActive());
                entity.setBookStatus(source.getBookStatus());
                entity.setShippingRate(source.getShippingRate());
                entity.setPrice(source.getPrice());
                entity.setObservation(source.getObservation());
                entity.setReasonCancellation(source.getReasonCancellation());
                return entity;
            });

        // Stubbing do CategoryRepository para retornar uma Category válida
        Category mockCategory = new Category();
        mockCategory.setId(1L);
        lenient().when(categoryRepository.findById(1L)).thenReturn(Optional.of(mockCategory));

        // Stubbing do BookRepository para salvar um livro
        lenient().when(bookRepository.save(Mockito.any(Book.class))).thenAnswer(invocation -> {
            Book savedBook = invocation.getArgument(0);
            savedBook.setId(1L); // Simula o ID gerado ao salvar
            return savedBook;
        });
    }

    @Test
    void testFindById() {
        Book entity = mockBook.mockEntity(1); 
        entity.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(entity));

        var result = apiBookService.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/books/v1/1>;rel=\"self\"]"));
        assertEquals("Some Author1", result.getAuthor());
        assertEquals("Some Title1", result.getTitle());
        assertEquals("9780136657125", result.getIsbn());
        assertEquals(LocalDate.of(2010, 1, 1), result.getLaunchDate());
        assertEquals(1, result.getCreatedBy());
        assertEquals(true, result.getActive());
        assertEquals(BookStatus.PAID, result.getBookStatus());
        assertEquals(BigDecimal.valueOf(0.0), result.getShippingRate());
        assertEquals(BigDecimal.valueOf(0.0), result.getPrice());
        assertEquals(null, result.getObservation());
        assertEquals(null, result.getReasonCancellation());
    }

	@Test
	void testCreate() {
		Book entity = mockBook.mockEntity(1); 
		entity.setId(1L);
		
		Book persisted = entity;
		persisted.setId(1L);
		
		BookDto vo = mockBook.mockDto(1);
		vo.setKey(1L);

	    // Mockando o CategoryRepository para retornar uma Categoria válida
	    Category mockCat = mockCategory.mockEntity(1);
	    when(categoryRepository.findById(1L)).thenReturn(Optional.of(mockCat));

		
        // Alteração para evitar PotentialStubbingProblem
        doReturn(persisted).when(bookRepository).save(entity);
		
		var result = apiBookService.create(vo);
		
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		
        assertTrue(result.toString().contains("links: [</api/books/v1/1>;rel=\"self\"]"));
        assertEquals("Some Author1", result.getAuthor());
        assertEquals("Some Title1", result.getTitle());
        assertEquals("9780136657125", result.getIsbn());
        assertEquals(LocalDate.of(2010, 1, 1), result.getLaunchDate());
        assertEquals(1, result.getCreatedBy());
        assertEquals(true, result.getActive());
        assertEquals(BookStatus.PAID, result.getBookStatus());
        assertEquals(BigDecimal.valueOf(0.0), result.getShippingRate());
        assertEquals(BigDecimal.valueOf(0.0), result.getPrice());
        assertEquals(null, result.getObservation());
        assertEquals(null, result.getReasonCancellation());
	}


    @Test
    void testCreateWithNullBook() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            apiBookService.create(null);
        });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testUpdate() {
        Book entity = mockBook.mockEntity(1); 
        entity.setId(1L);

        Book persisted = entity;
        persisted.setId(1L);

        BookDto vo = mockBook.mockDto(1);
        vo.setKey(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(bookRepository.save(entity)).thenReturn(persisted);

        var result = apiBookService.update(vo);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/books/v1/1>;rel=\"self\"]"));
        assertEquals("Some Author1", result.getAuthor());
        assertEquals("Some Title1", result.getTitle());
        assertEquals("9780136657125", result.getIsbn());
        assertEquals(LocalDate.of(2010, 1, 1), result.getLaunchDate());
        assertEquals(1, result.getCreatedBy());
        assertEquals(true, result.getActive());
        assertEquals(BookStatus.PAID, result.getBookStatus());
        assertEquals(BigDecimal.valueOf(0.0), result.getShippingRate());
        assertEquals(BigDecimal.valueOf(0.0), result.getPrice());
        assertEquals(null, result.getObservation());
        assertEquals(null, result.getReasonCancellation());
        assertNotNull(result.getCategoryId());
    }

    @Test
    void testUpdateWithNullBook() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            apiBookService.update(null);
        });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testDelete() {
        Book entity = mockBook.mockEntity(1); 
        entity.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(entity));

        apiBookService.delete(1L);
    }
}
