package com.nofrontier.book.integrationstests.controller.withjson;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nofrontier.book.configs.TestConfigs;
import com.nofrontier.book.core.enums.BookStatus;
import com.nofrontier.book.domain.services.ApiBookService;
import com.nofrontier.book.integrationstests.dto.BookDto;
import com.nofrontier.book.integrationstests.dto.TokenRequest;
import com.nofrontier.book.integrationstests.dto.TokenResponse;
import com.nofrontier.book.integrationstests.dto.wrappers.WrapperBookDto;
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
public class BookControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static BookDto book;
    
    @MockBean
    private ApiBookService bookService;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);        
        book = new BookDto();
    }
    
    @Test
    @Order(1)
    public void authorization() {
        TokenRequest user = new TokenRequest();
        user.setEmail("test@mail.com");
        user.setPassword("test@123");

        var token =
                given()
                    .basePath("/auth/token")
                    .port(TestConfigs.SERVER_PORT)
                    .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .body(user)
                    .when()
                        .post()
    				.then()
						.assertThat()
						.statusCode((is(200)))
                    .extract()
                    .body()
                        .as(TokenResponse.class)
                    .getAccess();

            specification =
                new RequestSpecBuilder()
                    .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + token)
                    .setBasePath("/api/books/v1")
                    .setPort(TestConfigs.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                    .build();
    }
      
    @Test
    @Order(2)
    public void testCreate() throws JsonMappingException, JsonProcessingException {
        
    	mockBook();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
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
    @Order(3)
    public void testUpdate() throws JsonMappingException, JsonProcessingException {
        
        book.setTitle("Docker Deep Dive - Updated");

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .body(book)
                    .when()
                    .put()
                .then()
					.assertThat()
					.statusCode((is(200)))
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
    @Order(4)
    public void testFindById() throws JsonMappingException, JsonProcessingException {
        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .pathParam("id", book.getId())
                    .when()
                    .get("{id}")
                .then()
					.assertThat()
					.statusCode((is(200)))
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
    @Order(5)
    public void testDelete() {
        given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .pathParam("id", book.getId())
                    .when()
                    .delete("{id}")
                .then()
				.assertThat()
				.statusCode((is(204)));
    }
    
    @Test
    @Order(6)
    public void testFindAll() throws JsonMappingException, JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
            	.queryParams("page", 0 , "limit", 12, "direction", "asc")
                    .when()
                    .get()
                .then()
					.assertThat()
					.statusCode((is(200)))
						.extract()
							.body()
								.asString();
        

        WrapperBookDto wrapper = objectMapper.readValue(content, WrapperBookDto.class);
        List<BookDto> books = wrapper.getEmbedded().getBooks();
		
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
	@Order(7)
	public void testHATEOAS() throws JsonMappingException, JsonProcessingException {
		
		var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
            	.queryParams("page", 0 , "size", 12, "direction", "asc")
                    .when()
                    .get()
                .then()
					.assertThat()
					.statusCode((is(200)))
						.extract()
							.body()
								.asString();
		
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/books/v1/3\"}}}"));
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/books/v1/5\"}}}"));
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/books/v1/7\"}}}"));
		
		assertTrue(content.contains("{\"first\":{\"href\":\"http://localhost:8888/api/books/v1?direction=asc&page=0&size=12&sort=title,asc\"}"));
		assertTrue(content.contains("\"self\":{\"href\":\"http://localhost:8888/api/books/v1?page=0&size=12&direction=asc\"}"));
		assertTrue(content.contains("\"next\":{\"href\":\"http://localhost:8888/api/books/v1?direction=asc&page=1&size=12&sort=title,asc\"}"));
		assertTrue(content.contains("\"last\":{\"href\":\"http://localhost:8888/api/books/v1?direction=asc&page=1&size=12&sort=title,asc\"}}"));
		
		assertTrue(content.contains("\"page\":{\"size\":12,\"totalElements\":15,\"totalPages\":2,\"number\":0}}"));
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