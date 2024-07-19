package com.nofrontier.book.integrationstests.controller.withxml;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.nofrontier.book.configs.TestConfigs;
import com.nofrontier.book.core.enums.BookStatus;
import com.nofrontier.book.integrationstests.dto.TokenRequest;
import com.nofrontier.book.integrationstests.dto.BookDto;
import com.nofrontier.book.integrationstests.dto.TokenResponse;
import com.nofrontier.book.integrationstests.dto.pagedmodels.PagedModelBook;
import com.nofrontier.book.integrationstests.testcontainers.AbstractIntegrationTest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "classpath:application-test.yml")
@TestMethodOrder(OrderAnnotation.class)
@ActiveProfiles("test")
public class BookControllerXmlTest extends AbstractIntegrationTest {
	
	private static RequestSpecification specification;
	private static XmlMapper objectMapper;

	private static BookDto book;
	
	@BeforeAll
	public static void setup() {
		objectMapper = new XmlMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		book = new BookDto();
	}
	
	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		
		TokenRequest user = new TokenRequest("admin@mail.com", "senha@123");
		
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
								.as(TokenResponse.class)
							.getAccess();
		
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
					.body(book)
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
        assertNotNull(book.getActive());
        assertNotNull(book.getBookStatus());
        assertNotNull(book.getShippingRate());
        assertNotNull(book.getPrice());
        assertNotNull(book.getObservation());
        assertNotNull(book.getReasonCancellation());
        assertNotNull(book.getCategoryId());
        
        
        assertTrue(book.getId() > 0);
        assertEquals("Docker Deep Dive", book.getTitle());
        assertEquals("Nigel Poulton", book.getAuthor());
        assertEquals("9780136657125", book.getIsbn());
        assertEquals(new Date(), book.getLaunchDate());
        assertEquals(OffsetDateTime.now(), book.getRegistrationDate());
        assertEquals(OffsetDateTime.now(), book.getUpdateDate());
        assertEquals(1, book.getCreatedBy());
        assertEquals(1, book.getLastModifiedBy());
        assertEquals(true, book.getActive());
        assertEquals(BookStatus.PAID, book.getBookStatus());
        assertEquals(BigDecimal.valueOf(10.0), book.getShippingRate());
        assertEquals(BigDecimal.valueOf(100.0), book.getPrice());
        assertEquals("Test observation", book.getObservation());
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
					.body(book)
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
        assertEquals("Docker Deep Dive", bookUpdated.getTitle());
        assertEquals("Nigel Poulton", bookUpdated.getAuthor());
        assertEquals("9780136657125", bookUpdated.getIsbn());
        assertEquals(new Date(), bookUpdated.getLaunchDate());
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
        assertEquals("Docker Deep Dive", foundBook.getTitle());
        assertEquals("Nigel Poulton", foundBook.getAuthor());
        assertEquals("9780136657125", foundBook.getIsbn());
        assertEquals(new Date(), foundBook.getLaunchDate());
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
        assertEquals("9780136657125", foundBookOne.getIsbn());
        assertEquals(new Date(), foundBookOne.getLaunchDate());
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
        assertEquals("9780136657125", foundBookFive.getIsbn());
        assertEquals(new Date(), foundBookFive.getLaunchDate());
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
				.statusCode(403);
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
    	book = new BookDto();
    	book.setId(16L);
        book.setTitle("Docker Deep Dive");
        book.setAuthor("Nigel Poulton");
        book.setIsbn("9780136657125");
        book.setLaunchDate(new Date());
        book.setRegistrationDate(OffsetDateTime.now());
        book.setUpdateDate(OffsetDateTime.now());
        book.setCreatedBy(1);
        book.setLastModifiedBy(1);
        book.setActive(true);
        book.setBookStatus(BookStatus.PAID);
        book.setShippingRate(BigDecimal.valueOf(10.0));
        book.setPrice(BigDecimal.valueOf(100.0));
        book.setObservation("Test observation");
        book.setReasonCancellation("Test");
        book.setCategoryId(7L);
    }
}