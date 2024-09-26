package com.nofrontier.book.integrationtests.controller.withyaml;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.nofrontier.book.configs.TestConfigs;
import com.nofrontier.book.core.enums.BookStatus;
import com.nofrontier.book.dto.v1.TokenDto;
import com.nofrontier.book.integrationtests.controller.withyaml.mapper.YMLMapper;
import com.nofrontier.book.integrationtests.dto.AccountCredentialsDto;
import com.nofrontier.book.integrationtests.dto.BookDto;
import com.nofrontier.book.integrationtests.dto.pagedmodels.PagedModelBook;
import com.nofrontier.book.integrationtests.testcontainers.AbstractIntegrationTest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class BookControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;

    private static YMLMapper yamlMapper;

    private static BookDto book;

    @BeforeAll
    public static void setup() {
    	
    	yamlMapper = new YMLMapper();
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
                    .config(
                        RestAssuredConfig
                            .config()
                            .encoderConfig(EncoderConfig.encoderConfig()
                                    .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                    .basePath("/auth/signin")
                    .port(TestConfigs.SERVER_PORT)
                    .contentType(TestConfigs.CONTENT_TYPE_YML)
    				.accept(TestConfigs.CONTENT_TYPE_YML)
                    .body(user, yamlMapper)
                    .when()
                        .post()
                    .then()
                        .statusCode(200)
                    .extract()
                    .body()
                        .as(TokenDto.class, yamlMapper)
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
        // Preenche os campos do livro com valores mockados
        mockBook(); 
        
        // Configura o RestAssured para lidar com YAML como Content-Type
        book = given()
                    .config(RestAssuredConfig.config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                    .spec(specification)
                    .contentType(TestConfigs.CONTENT_TYPE_YML) // Define o tipo de conteúdo como YAML
                    .accept(TestConfigs.CONTENT_TYPE_YML)      // Aceita resposta no formato YAML
                    .body(book, yamlMapper)                    // Serializa o objeto book usando o YAMLMapper
                .when()
                    .post()  // Envia uma requisição POST
                .then()
                    .statusCode(200)  // Verifica se o status da resposta é 200 (OK)
                    .extract()
                    .body()
                    .as(BookDto.class, yamlMapper);  // Desserializa a resposta para BookDto usando o YAMLMapper
        
        // Verifique se o objeto book não é nulo
        assertNotNull(book);
        
        // Verifique todos os campos retornados para garantir que o livro foi criado corretamente
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

        // Validação dos valores retornados
        assertTrue(book.getId() > 0);  // Verifica se o ID é maior que 0
        assertEquals("Docker Deep Dive", book.getTitle());  // Verifica o título do livro
        assertEquals("Nigel Poulton", book.getAuthor());    // Verifica o autor
        assertEquals("9780136657521", book.getIsbn());      // Verifica o ISBN
        assertEquals(LocalDate.of(1999, 2, 6), book.getLaunchDate()); // Verifica a data de lançamento
        
        // Verifique se as datas de registro e atualização estão dentro de um intervalo de 5 segundos
        assertTrue(book.getRegistrationDate().isAfter(OffsetDateTime.now().minusSeconds(15)));
        assertTrue(book.getRegistrationDate().isBefore(OffsetDateTime.now().plusSeconds(15)));
        assertTrue(book.getUpdateDate().isAfter(OffsetDateTime.now().minusSeconds(15)));
        assertTrue(book.getUpdateDate().isBefore(OffsetDateTime.now().plusSeconds(15)));
        
        // Valida os outros campos
        assertEquals(Integer.valueOf(1), book.getCreatedBy());
        assertEquals(Integer.valueOf(1), book.getLastModifiedBy());
        assertEquals(Boolean.TRUE, book.getActive());
        assertEquals(BookStatus.PAID, book.getBookStatus());
        assertEquals(BigDecimal.valueOf(11.99), book.getShippingRate());
        assertEquals(BigDecimal.valueOf(55.99), book.getPrice());
        assertEquals("Test", book.getObservation());
        assertEquals("Test", book.getReasonCancellation());
        assertEquals(Long.valueOf(7), book.getCategoryId());
    }

    @Test
    @Order(3)
    public void testUpdate() throws JsonMappingException, JsonProcessingException {
        
        book.setTitle("Docker Deep Dive - Updated");

        BookDto bookUpdated = given()
                    .config(
                        RestAssuredConfig
                            .config()
                            .encoderConfig(EncoderConfig.encoderConfig()
                                    .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                    .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
                    .body(book, yamlMapper)
                    .when()
                    .put()
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
                        .as(BookDto.class, yamlMapper);
        
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
        assertTrue(bookUpdated.getRegistrationDate().isAfter(OffsetDateTime.now().minusSeconds(15)));
        assertTrue(bookUpdated.getRegistrationDate().isBefore(OffsetDateTime.now().plusSeconds(15)));
        assertTrue(bookUpdated.getUpdateDate().isAfter(OffsetDateTime.now().minusSeconds(15)));
        assertTrue(bookUpdated.getUpdateDate().isBefore(OffsetDateTime.now().plusSeconds(15)));
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
        var foundBook = given()
                    .config(
                        RestAssuredConfig
                            .config()
                            .encoderConfig(EncoderConfig.encoderConfig()
                                    .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                    .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
                    .pathParam("id", book.getId())
                    .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
                        .as(BookDto.class, yamlMapper);
        
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
        assertTrue(foundBook.getRegistrationDate().isAfter(OffsetDateTime.now().minusSeconds(15)));
        assertTrue(foundBook.getRegistrationDate().isBefore(OffsetDateTime.now().plusSeconds(15)));
        assertTrue(foundBook.getUpdateDate().isAfter(OffsetDateTime.now().minusSeconds(15)));
        assertTrue(foundBook.getUpdateDate().isBefore(OffsetDateTime.now().plusSeconds(15)));
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
        given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
            .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
                    .pathParam("id", book.getId())
                    .when()
                    .delete("{id}")
                .then()
                    .statusCode(204);
    }
    
    @Test
    @Order(6)
    public void testFindAll() throws JsonMappingException, JsonProcessingException {
        var response = given()
                    .config(
                        RestAssuredConfig
                            .config()
                            .encoderConfig(EncoderConfig.encoderConfig()
                                    .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                    .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
            	.queryParams("page", 0 , "limit", 12, "direction", "asc")
                    .when()
                    .get()
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
                    	.as(PagedModelBook.class, yamlMapper); 


        List<BookDto> content = response.getContent();

        BookDto foundBookOne = content.get(0);
        
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
        assertEquals(BigDecimal.valueOf(0.00).setScale(2), foundBookOne.getShippingRate().setScale(2));
        assertEquals(BigDecimal.valueOf(0.00).setScale(2), foundBookOne.getPrice().setScale(2));
        assertEquals(Long.valueOf(7), foundBookOne.getCategoryId());
        
        BookDto foundBookFive = content.get(4);
        
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
        assertEquals(BigDecimal.valueOf(0.00).setScale(2), foundBookFive.getShippingRate().setScale(2));
        assertEquals(BigDecimal.valueOf(0.00).setScale(2), foundBookFive.getPrice().setScale(2));
        assertEquals(Long.valueOf(7), foundBookFive.getCategoryId());
    }
     
	@Test
	@Order(9)
	public void testHATEOAS() throws JsonMappingException, JsonProcessingException {
		
		var unthreatedContent = given()
                    .config(
                        RestAssuredConfig
                            .config()
                            .encoderConfig(EncoderConfig.encoderConfig()
                                    .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                    .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
            	.queryParams("page", 0 , "size", 12, "direction", "asc")
                    .when()
                    .get()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
						.asString();
		
		var content = unthreatedContent.replace("\n", "").replace("\r", "");
		
		assertTrue(content.contains("rel: \"self\"    href: \"http://localhost:8888/api/books/v1/3\""));
		assertTrue(content.contains("rel: \"self\"    href: \"http://localhost:8888/api/books/v1/5\""));
		assertTrue(content.contains("rel: \"self\"    href: \"http://localhost:8888/api/books/v1/7\""));
		
		assertTrue(content.contains("rel: \"first\"  href: \"http://localhost:8888/api/books/v1?direction=asc&page=0&size=12&sort=title,asc\""));
		assertTrue(content.contains("rel: \"self\"  href: \"http://localhost:8888/api/books/v1?page=0&size=12&direction=asc\""));
		assertTrue(content.contains("rel: \"next\"  href: \"http://localhost:8888/api/books/v1?direction=asc&page=1&size=12&sort=title,asc\""));
		assertTrue(content.contains("rel: \"last\"  href: \"http://localhost:8888/api/books/v1?direction=asc&page=1&size=12&sort=title,asc\""));
		
		assertTrue(content.contains("page:  size: 12  totalElements: 15  totalPages: 2  number: 0"));
    }
     
    private void mockBook() {
        book.setTitle("Docker Deep Dive");
        book.setAuthor("Nigel Poulton");
        book.setIsbn("9780136657521");
        book.setLaunchDate(LocalDate.of(1999, 2, 06));
        book.setRegistrationDate(OffsetDateTime.now());
        book.setUpdateDate(OffsetDateTime.now());
        book.setCreatedBy(Integer.valueOf(1));
        book.setLastModifiedBy(Integer.valueOf(1));
        book.setActive(true);
        book.setBookStatus(BookStatus.PAID);
        book.setShippingRate(BigDecimal.valueOf(11.99));
        book.setPrice(BigDecimal.valueOf(55.99));
        book.setObservation("Test");
        book.setReasonCancellation("Test");
        book.setCategoryId(Long.valueOf(7));
    } 
}