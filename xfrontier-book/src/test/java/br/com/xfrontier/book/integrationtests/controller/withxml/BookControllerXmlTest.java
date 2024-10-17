package br.com.xfrontier.book.integrationtests.controller.withxml;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import br.com.xfrontier.book.configs.TestConfigs;
import br.com.xfrontier.book.core.enums.BookStatus;
import br.com.xfrontier.book.dto.v1.TokenDto;
import br.com.xfrontier.book.integrationtests.dto.AccountCredentialsDto;
import br.com.xfrontier.book.integrationtests.dto.BookDto;
import br.com.xfrontier.book.integrationtests.dto.pagedmodels.PagedModelBook;
import br.com.xfrontier.book.integrationtests.testcontainers.AbstractIntegrationTest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class BookControllerXmlTest extends AbstractIntegrationTest {
	
	private static RequestSpecification specification;
	private static XmlMapper objectMapper;

	private static BookDto book;
	
	@BeforeAll
	public static void setup() {
		
		objectMapper = new XmlMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		
		objectMapper.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		book = new BookDto();
	}
	
	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		
		AccountCredentialsDto user = new AccountCredentialsDto("admin@mail.com", "admin123");
		
		var accessToken = given()
				.basePath("/auth/signin")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_XML)
					.accept(TestConfigs.CONTENT_TYPE_XML)
				.body(user)
					.when()
				.post()
					.then()
						.statusCode(200)
							.extract()
							.body()
								.as(TokenDto.class)
							.getAccessToken();
		
		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
				.setBasePath("/api/books/v1")
				.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}
	
	@Test
	@Order(1)
	public void testCreate() throws JsonMappingException, JsonProcessingException {
		mockBook();
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
					.body(objectMapper.writeValueAsString(book))
					.when()
					.post()
				.then()
					.statusCode(200)
						.extract()
						.body()
							.asString();
		
        book = objectMapper.readValue(content, BookDto.class);
        
        assertNotNull(book.getId());
        assertNotNull(book.getTitle());
        assertNotNull(book.getAuthor());
        assertNotNull(book.getIsbn());
        assertNotNull(book.getLaunchDate());
        assertNotNull(book.getRegistrationDate());
        assertNotNull(book.getUpdateDate());
        assertNotNull(book.getCreatedBy());
        assertNotNull(book.getLastModifiedBy());
        assertTrue(book.getActive());
        assertNotNull(book.getBookStatus());
        assertNotNull(book.getShippingRate());
        assertNotNull(book.getPrice());
        assertNotNull(book.getObservation());
        assertNotNull(book.getReasonCancellation());
        assertNotNull(book.getCategoryId());
        
        
        assertTrue(book.getId() > 0);
        assertEquals("Docker Deep Dive", book.getTitle());
        assertEquals("Nigel Poulton", book.getAuthor());
        assertEquals("9780136657521", book.getIsbn());
        assertEquals(LocalDate.of(1999, 2, 06), book.getLaunchDate());
        assertTrue(book.getRegistrationDate().isAfter(OffsetDateTime.now().minusSeconds(5)));
        assertTrue(book.getRegistrationDate().isBefore(OffsetDateTime.now().plusSeconds(5)));
        assertTrue(book.getUpdateDate().isAfter(OffsetDateTime.now().minusSeconds(5)));
        assertTrue(book.getUpdateDate().isBefore(OffsetDateTime.now().plusSeconds(5)));
        assertEquals(1, book.getCreatedBy());
        assertEquals(1, book.getLastModifiedBy());
        assertEquals(true, book.getActive());
        assertEquals(BookStatus.PAID, book.getBookStatus());
        assertEquals(BigDecimal.valueOf(11.99), book.getShippingRate());
        assertEquals(BigDecimal.valueOf(55.99), book.getPrice());
        assertEquals("Test", book.getObservation());
        assertEquals("Test", book.getReasonCancellation());
        assertEquals(7L, book.getCategoryId());
    }

	@Test
	@Order(2)
	public void testUpdate() throws JsonMappingException, JsonProcessingException {

        book.setTitle("Docker Deep Dive - Updated");
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
					.body(objectMapper.writeValueAsString(book))
					.when()
					.post()
				.then()
					.statusCode(200)
						.extract()
						.body()
							.asString();
		
        BookDto bookUpdated = objectMapper.readValue(content, BookDto.class);
        
        assertNotNull(bookUpdated.getId());
        assertNotNull(bookUpdated.getTitle());
        assertNotNull(bookUpdated.getAuthor());
        assertNotNull(bookUpdated.getIsbn());
        assertNotNull(bookUpdated.getLaunchDate());
        assertNotNull(bookUpdated.getActive());
        
        assertTrue(bookUpdated.getId() > 0);
        assertEquals("Docker Deep Dive - Updated", bookUpdated.getTitle());
        assertEquals("Nigel Poulton", bookUpdated.getAuthor());
        assertEquals("9780136657521", bookUpdated.getIsbn());
        assertEquals(LocalDate.of(1999, 2, 06), bookUpdated.getLaunchDate());
        assertEquals(true, bookUpdated.getActive());
	}

	@Test
	@Order(3)
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		mockBook();
			
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
					.pathParam("id", book.getId())
					.when()
					.get("{id}")
				.then()
					.statusCode(200)
						.extract()
						.body()
							.asString();
		
		BookDto foundBook = objectMapper.readValue(content, BookDto.class);
        
        assertNotNull(foundBook.getId());
        assertNotNull(foundBook.getTitle());
        assertNotNull(foundBook.getAuthor());
        assertNotNull(foundBook.getIsbn());
        assertNotNull(foundBook.getLaunchDate());
        assertNotNull(foundBook.getActive());
        
        assertTrue(foundBook.getId() > 0);
        assertEquals("Docker Deep Dive - Updated", foundBook.getTitle());
        assertEquals("Nigel Poulton", foundBook.getAuthor());
        assertEquals("9780136657521", foundBook.getIsbn());
        assertEquals(LocalDate.of(1999, 2, 06), foundBook.getLaunchDate());
        assertEquals(true, foundBook.getActive());
	}
	
	@Test
	@Order(4)
	public void testDelete() throws JsonMappingException, JsonProcessingException {

		given().spec(specification)
			.contentType(TestConfigs.CONTENT_TYPE_XML)
			.accept(TestConfigs.CONTENT_TYPE_XML)
				.pathParam("id", book.getId())
				.when()
				.delete("{id}")
			.then()
				.statusCode(204);
	}
	
	@Test
	@Order(5)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
            	.queryParams("page", 0 , "limit", 12, "direction", "asc")
					.when()
					.get()
				.then()
					.statusCode(200)
						.extract()
						.body()
							.asString();
		
		PagedModelBook wrapper = objectMapper.readValue(content, PagedModelBook.class);
		List<BookDto> books = wrapper.getContent();
		
		BookDto foundBookOne = books.get(0);
        
        assertNotNull(foundBookOne.getId());
        assertNotNull(foundBookOne.getTitle());
        assertNotNull(foundBookOne.getAuthor());
        assertNotNull(foundBookOne.getIsbn());
        assertNotNull(foundBookOne.getLaunchDate());
        assertNotNull(foundBookOne.getActive());
        
        assertTrue(foundBookOne.getId() > 0);
        
        assertEquals("Big Data: como extrair volume, variedade, velocidade e valor da avalanche de informação cotidiana", foundBookOne.getTitle());
        assertEquals("Viktor Mayer-Schonberger e Kenneth Kukier", foundBookOne.getAuthor());
        assertEquals("9781848547902", foundBookOne.getIsbn());
        assertEquals(LocalDate.of(2017, 11, 07), foundBookOne.getLaunchDate());
        assertEquals(true, foundBookOne.getActive());
        
        BookDto foundBookFive = books.get(4);
        
        assertNotNull(foundBookFive.getId());
        assertNotNull(foundBookFive.getTitle());
        assertNotNull(foundBookFive.getAuthor());
        assertNotNull(foundBookFive.getIsbn());
        assertNotNull(foundBookFive.getLaunchDate());
        assertNotNull(foundBookFive.getActive());
        
        assertTrue(foundBookFive.getId() > 0);
        
        assertEquals("Domain Driven Design", foundBookFive.getTitle());
        assertEquals("Eric Evans", foundBookFive.getAuthor());
        assertEquals("9780321125217", foundBookFive.getIsbn());
        assertEquals(LocalDate.of(2017, 11, 07), foundBookFive.getLaunchDate());
        assertEquals(true, foundBookFive.getActive());
	}

	
	@Test
	@Order(6)
	public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {
		
		RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
			.setBasePath("/api/books/v1")
			.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
			.build();
		
		given().spec(specificationWithoutToken)
			.contentType(TestConfigs.CONTENT_TYPE_XML)
			.accept(TestConfigs.CONTENT_TYPE_XML)
				.when()
				.get()
			.then()
			.statusCode(anyOf(is(401), is(403)));
	}
	
	@Test
	@Order(7)
	public void testHATEOAS() throws JsonMappingException, JsonProcessingException {
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
            	.queryParams("page", 0 , "size", 12, "direction", "asc")
					.when()
					.get()
				.then()
					.statusCode(200)
						.extract()
						.body()
							.asString();
		
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/books/v1/3</href></links>"));
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/books/v1/5</href></links>"));
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/books/v1/7</href></links>"));
		
		assertTrue(content.contains("<links><rel>first</rel><href>http://localhost:8888/api/books/v1?direction=asc&amp;page=0&amp;size=12&amp;sort=title,asc</href></links>"));
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/books/v1?page=0&amp;size=12&amp;direction=asc</href></links>"));
		assertTrue(content.contains("<links><rel>next</rel><href>http://localhost:8888/api/books/v1?direction=asc&amp;page=1&amp;size=12&amp;sort=title,asc</href></links>"));
		assertTrue(content.contains("<links><rel>last</rel><href>http://localhost:8888/api/books/v1?direction=asc&amp;page=1&amp;size=12&amp;sort=title,asc</href></links>"));
		
		assertTrue(content.contains("<page><size>12</size><totalElements>15</totalElements><totalPages>2</totalPages><number>0</number></page>"));
	}
	
    private void mockBook() {
        book.setTitle("Docker Deep Dive");
        book.setAuthor("Nigel Poulton");
        book.setIsbn("9780136657521");
        book.setLaunchDate(LocalDate.of(1999, 2, 06));
        book.setRegistrationDate(OffsetDateTime.now());
        book.setUpdateDate(OffsetDateTime.now());
        book.setCreatedBy(1);
        book.setLastModifiedBy(1);
        book.setActive(true);
        book.setBookStatus(BookStatus.PAID);
        book.setShippingRate(BigDecimal.valueOf(11.99));
        book.setPrice(BigDecimal.valueOf(55.99));
        book.setObservation("Test");
        book.setReasonCancellation("Test");
        book.setCategoryId(7L);
    } 
}