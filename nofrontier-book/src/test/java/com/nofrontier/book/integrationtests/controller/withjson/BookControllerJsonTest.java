package com.nofrontier.book.integrationtests.controller.withjson;

import static io.restassured.RestAssured.given;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import com.nofrontier.book.configs.TestConfigs;
import com.nofrontier.book.dto.v1.AccountCredentialsDto;
import com.nofrontier.book.core.enums.BookStatus;
import com.nofrontier.book.integrationtests.testcontainers.AbstractIntegrationTest;
import com.nofrontier.book.integrationtests.dto.BookDto;
import com.nofrontier.book.integrationtests.dto.TokenDto;
import com.nofrontier.book.integrationtests.dto.wrappers.WrapperBookDto;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class BookControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static BookDto book;

    @BeforeAll
    public static void setup() {
    	
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		
		objectMapper.setTimeZone(TimeZone.getTimeZone("UTC"));

        
        book = new BookDto();
    }
    
    @Test
    @Order(1)
    public void authorization() {
        AccountCredentialsDto user = new AccountCredentialsDto();
        user.setEmail("admin@mail.com");
        user.setPassword("admin123");

        var token =
                given()
                    .basePath("/auth/signin")
                    .port(TestConfigs.SERVER_PORT)
                    .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .body(user)
                    .when()
                        .post()
                    .then()
                        .statusCode(200)
                    .extract()
                    .body()
                        .as(TokenDto.class)
                    .getAccessToken();

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
    @Order(3)
    public void testUpdate() throws JsonMappingException, JsonProcessingException {
        
        book.setTitle("Docker Deep Dive - Updated");

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .body(objectMapper.writeValueAsString(book))
                    .when()
                    .put()
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
        assertNotNull(bookUpdated.getRegistrationDate());
        assertNotNull(bookUpdated.getUpdateDate());
        assertNotNull(bookUpdated.getCreatedBy());
        assertNotNull(bookUpdated.getLastModifiedBy());
        assertTrue(bookUpdated.getActive());
        assertNotNull(bookUpdated.getBookStatus());
        assertNotNull(bookUpdated.getShippingRate());
        assertNotNull(bookUpdated.getPrice());
        assertNotNull(bookUpdated.getObservation());
        assertNotNull(bookUpdated.getReasonCancellation());
        assertNotNull(bookUpdated.getCategoryId());
        
        assertEquals(bookUpdated.getId(), book.getId());
        
        assertEquals("Docker Deep Dive - Updated", bookUpdated.getTitle());
        assertEquals("Nigel Poulton", bookUpdated.getAuthor());
        assertEquals("9780136657521", bookUpdated.getIsbn());
        assertEquals(LocalDate.of(1999, 2, 06), bookUpdated.getLaunchDate());
        assertTrue(bookUpdated.getRegistrationDate().isAfter(OffsetDateTime.now().minusSeconds(5)));
        assertTrue(bookUpdated.getRegistrationDate().isBefore(OffsetDateTime.now().plusSeconds(5)));
        assertTrue(bookUpdated.getUpdateDate().isAfter(OffsetDateTime.now().minusSeconds(5)));
        assertTrue(bookUpdated.getUpdateDate().isBefore(OffsetDateTime.now().plusSeconds(5)));
        assertEquals(1, bookUpdated.getCreatedBy());
        assertEquals(1, bookUpdated.getLastModifiedBy());
        assertEquals(true, bookUpdated.getActive());
        assertEquals(BookStatus.PAID, bookUpdated.getBookStatus());
        assertEquals(BigDecimal.valueOf(11.99), bookUpdated.getShippingRate());
        assertEquals(BigDecimal.valueOf(55.99), bookUpdated.getPrice());
        assertEquals("Test", bookUpdated.getObservation());
        assertEquals("Test", bookUpdated.getReasonCancellation());
        assertEquals(7L, bookUpdated.getCategoryId());
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
        assertNotNull(foundBook.getRegistrationDate());
        assertNotNull(foundBook.getUpdateDate());
        assertNotNull(foundBook.getCreatedBy());
        assertNotNull(foundBook.getLastModifiedBy());
        assertTrue(foundBook.getActive());
        assertNotNull(foundBook.getBookStatus());
        assertNotNull(foundBook.getShippingRate());
        assertNotNull(foundBook.getPrice());
        assertNotNull(foundBook.getObservation());
        assertNotNull(foundBook.getReasonCancellation());
        assertNotNull(foundBook.getCategoryId());
        
        assertEquals(foundBook.getId(), book.getId());
        
        assertEquals("Docker Deep Dive - Updated", foundBook.getTitle());
        assertEquals("Nigel Poulton", foundBook.getAuthor());
        assertEquals("9780136657521", foundBook.getIsbn());
        assertEquals(LocalDate.of(1999, 2, 06), foundBook.getLaunchDate());
        assertTrue(foundBook.getRegistrationDate().isAfter(OffsetDateTime.now().minusSeconds(5)));
        assertTrue(foundBook.getRegistrationDate().isBefore(OffsetDateTime.now().plusSeconds(5)));
        assertTrue(foundBook.getUpdateDate().isAfter(OffsetDateTime.now().minusSeconds(5)));
        assertTrue(foundBook.getUpdateDate().isBefore(OffsetDateTime.now().plusSeconds(5)));
        assertEquals(1, foundBook.getCreatedBy());
        assertEquals(1, foundBook.getLastModifiedBy());
        assertEquals(true, foundBook.getActive());
        assertEquals(BookStatus.PAID, foundBook.getBookStatus());
        assertEquals(BigDecimal.valueOf(11.99), foundBook.getShippingRate());
        assertEquals(BigDecimal.valueOf(55.99), foundBook.getPrice());
        assertEquals("Test", foundBook.getObservation());
        assertEquals("Test", foundBook.getReasonCancellation());
        assertEquals(7L, foundBook.getCategoryId());
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
                    .statusCode(204);
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
                    .statusCode(200)
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
        assertNotNull(foundBookOne.getRegistrationDate());
        assertNotNull(foundBookOne.getUpdateDate());
        assertNotNull(foundBookOne.getCreatedBy());
        assertNotNull(foundBookOne.getLastModifiedBy());
        assertTrue(foundBookOne.getActive());
        assertNotNull(foundBookOne.getBookStatus());
        assertNotNull(foundBookOne.getShippingRate());
        assertNotNull(foundBookOne.getPrice());
        assertNotNull(foundBookOne.getCategoryId());
        
        assertTrue(foundBookOne.getId() > 0);
        
        assertEquals("Big Data: como extrair volume, variedade, velocidade e valor da avalanche de informação cotidiana", foundBookOne.getTitle());
        assertEquals("Viktor Mayer-Schonberger e Kenneth Kukier", foundBookOne.getAuthor());
        assertEquals("9781848547902", foundBookOne.getIsbn());
        assertEquals(LocalDate.of(2017, 11, 07), foundBookOne.getLaunchDate()); 
        assertEquals(1, foundBookOne.getCreatedBy());
        assertEquals(1, foundBookOne.getLastModifiedBy());
        assertEquals(true, foundBookOne.getActive());
        assertEquals(BookStatus.PAID, foundBookOne.getBookStatus());
        assertEquals(OffsetDateTime.parse("2024-05-15T10:30:00Z"), foundBookOne.getRegistrationDate());
        assertEquals(OffsetDateTime.parse("2024-05-15T10:30:00Z"), foundBookOne.getUpdateDate());

        assertEquals(BigDecimal.valueOf(0.00).setScale(2), foundBookOne.getPrice().setScale(2));

        
        BookDto foundBookFive = books.get(4);
        
        assertNotNull(foundBookFive.getId());
        assertNotNull(foundBookFive.getTitle());
        assertNotNull(foundBookFive.getAuthor());
        assertNotNull(foundBookFive.getIsbn());
        assertNotNull(foundBookFive.getLaunchDate());
        assertNotNull(foundBookFive.getRegistrationDate());
        assertNotNull(foundBookFive.getUpdateDate());
        assertNotNull(foundBookFive.getCreatedBy());
        assertNotNull(foundBookFive.getLastModifiedBy());
        assertTrue(foundBookFive.getActive());
        assertNotNull(foundBookFive.getBookStatus());
        assertNotNull(foundBookFive.getShippingRate());
        assertNotNull(foundBookFive.getPrice());
        assertNotNull(foundBookFive.getCategoryId());
        
        assertTrue(foundBookFive.getId() > 0);
        
        assertEquals("Domain Driven Design", foundBookFive.getTitle());
        assertEquals("Eric Evans", foundBookFive.getAuthor());
        assertEquals("9780321125217", foundBookFive.getIsbn());
        assertEquals(LocalDate.of(2017, 11, 07), foundBookFive.getLaunchDate());        
        assertEquals(1, foundBookFive.getCreatedBy());
        assertEquals(1, foundBookFive.getLastModifiedBy());
        assertEquals(true, foundBookFive.getActive());
        assertEquals(BookStatus.PAID, foundBookFive.getBookStatus());
        assertEquals(OffsetDateTime.parse("2024-05-15T10:30:00Z"), foundBookFive.getRegistrationDate());
        assertEquals(OffsetDateTime.parse("2024-05-15T10:30:00Z"), foundBookFive.getUpdateDate());
        
        assertEquals(BigDecimal.valueOf(0.00).setScale(2), foundBookFive.getPrice().setScale(2));

    }
	
	@Test
	@Order(7)
	public void testHATEOAS() throws JsonMappingException, JsonProcessingException {
		
		var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
            	.queryParams("page", 0, "size", 12, "direction", "asc")
                    .when()
                    .get()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                .asString();
		
	    // Checks if the _embedded field exists
	    assertTrue(content.contains("_embedded"));

	    // Check the _links.self.href link of the first personDto
	    String selfLink = JsonPath.read(content, "$._embedded.bookDtoList[0]._links.self.href");
	    assertNotNull(selfLink);
	    assertTrue(selfLink.contains("/api/books/v1/12"));

	    // Checks if there are 7 links corresponding to the people on the list
	    List<String> links = JsonPath.read(content, "$._embedded.bookDtoList[*]._links.self.href");
	    assertEquals(12, links.size()); // Check if there are 7 people on the list

	    // Check that the first link matches what you expect
	    assertTrue(links.get(0).contains("/api/books/v1/12"));
	    assertTrue(links.get(1).contains("/api/books/v1/3"));
	    assertTrue(links.get(2).contains("/api/books/v1/5"));
	    assertTrue(links.get(3).contains("/api/books/v1/2"));
	    assertTrue(links.get(4).contains("/api/books/v1/8"));
	    assertTrue(links.get(5).contains("/api/books/v1/11"));
	    assertTrue(links.get(6).contains("/api/books/v1/7"));
	    assertTrue(links.get(7).contains("/api/books/v1/15"));
	    assertTrue(links.get(8).contains("/api/books/v1/9"));
	    assertTrue(links.get(9).contains("/api/books/v1/4"));
	    assertTrue(links.get(10).contains("/api/books/v1/10"));
	    assertTrue(links.get(11).contains("/api/books/v1/13"));
	    
	    // Check the main _links of the pagination
	    String paginationSelfLink = JsonPath.read(content, "$._links.self.href");
	    assertNotNull(paginationSelfLink);
	    
	    // Making the assertion to check if the link contains 'http://localhost:8888/'
	    assertTrue(paginationSelfLink.contains("http://localhost:8888/"));
	    
	    // Check the full pagination link
	    assertTrue(paginationSelfLink.contains("/api/books/v1?page=0&size=12&direction=asc"));
	    
	    // Checks that the 'page' object contains the expected values
	    // Convertendo o valor retornado para Integer
	    Integer pageSize = Integer.parseInt(JsonPath.read(content, "$.page.size").toString());
	    Integer totalElements = Integer.parseInt(JsonPath.read(content, "$.page.totalElements").toString());
	    Integer totalPages = Integer.parseInt(JsonPath.read(content, "$.page.totalPages").toString());
	    Integer pageNumber = Integer.parseInt(JsonPath.read(content, "$.page.number").toString());

	    // Making assertions about values
	    assertEquals(12, pageSize);           // The expected page size
	    assertEquals(15, totalElements);       // The total number of elements expected
	    assertEquals(2, totalPages);          // The total number of pages expected
	    assertEquals(0, pageNumber);          // The expected page number    
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