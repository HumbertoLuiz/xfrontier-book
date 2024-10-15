package com.nofrontier.book.integrationtests.controller.withxml;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

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
import com.nofrontier.book.configs.TestConfigs;
import com.nofrontier.book.dto.v1.TokenDto;
import com.nofrontier.book.integrationtests.dto.AccountCredentialsDto;
import com.nofrontier.book.integrationtests.dto.PersonDto;
import com.nofrontier.book.integrationtests.dto.pagedmodels.PagedModelPerson;
import com.nofrontier.book.integrationtests.testcontainers.AbstractIntegrationTest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerXmlTest extends AbstractIntegrationTest {
	
	private static RequestSpecification specification;
	private static XmlMapper objectMapper;

	private static PersonDto personDto;
	
	@BeforeAll
	public static void setup() {
		
		objectMapper = new XmlMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);		
	    
	    objectMapper.registerModule(new JavaTimeModule());
	    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		
		personDto = new PersonDto();
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
				.setBasePath("/api/people/v1")
				.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}
	
	@Test
	@Order(1)
	public void testCreate() throws JsonMappingException, JsonProcessingException {
		mockPerson();
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
					.body(objectMapper.writeValueAsString(personDto))
					.when()
					.post()
				.then()
					.statusCode(200)
						.extract()
						.body()
							.asString();
		
		PersonDto persistedPerson = objectMapper.readValue(content, PersonDto.class);
		personDto = persistedPerson;
		
		assertNotNull(persistedPerson);
		
		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getGender());
		assertNotNull(persistedPerson.getCpf());
		assertNotNull(persistedPerson.getBirth());
		assertNotNull(persistedPerson.getPhoneNumber());
		assertNotNull(persistedPerson.getMobileNumber());
		assertNotNull(persistedPerson.getKeyPix());
		assertTrue(persistedPerson.getEnabled());
		
		assertTrue(persistedPerson.getId() > 0);
		
		assertEquals("Nelson", persistedPerson.getFirstName());
		assertEquals("Piquet", persistedPerson.getLastName());
		assertEquals("Male", persistedPerson.getGender());
		assertEquals("04888053683", persistedPerson.getCpf());
		assertEquals(LocalDate.of(1991, 2, 24), persistedPerson.getBirth());        
		assertEquals("3436834703", persistedPerson.getPhoneNumber());
		assertEquals("34988681043", persistedPerson.getMobileNumber());
		assertEquals("04888053683", persistedPerson.getKeyPix());
		assertEquals(true, persistedPerson.getEnabled());
	}
	

	@Test
	@Order(2)
	public void testUpdate() throws JsonMappingException, JsonProcessingException {
		personDto.setLastName("Piquet Souto Maior");
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
					.body(objectMapper.writeValueAsString(personDto))
					.when()
					.post()
				.then()
					.statusCode(200)
						.extract()
						.body()
							.asString();
		
		PersonDto persistedPerson = objectMapper.readValue(content, PersonDto.class);
		personDto = persistedPerson;
		
		assertNotNull(persistedPerson);
		
		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getGender());
		assertNotNull(persistedPerson.getCpf());
		assertNotNull(persistedPerson.getBirth());
		assertNotNull(persistedPerson.getPhoneNumber());
		assertNotNull(persistedPerson.getMobileNumber());
		assertNotNull(persistedPerson.getKeyPix());
		assertTrue(persistedPerson.getEnabled());
		
		assertEquals(personDto.getId(), persistedPerson.getId());
		
		assertEquals("Nelson", persistedPerson.getFirstName());
		assertEquals("Piquet Souto Maior", persistedPerson.getLastName());
		assertEquals("Male", persistedPerson.getGender());
		assertEquals("04888053683", persistedPerson.getCpf());
		assertEquals(LocalDate.of(1991, 2, 24), persistedPerson.getBirth());        
		assertEquals("3436834703", persistedPerson.getPhoneNumber());
		assertEquals("34988681043", persistedPerson.getMobileNumber());
		assertEquals("04888053683", persistedPerson.getKeyPix());
		assertEquals(true, persistedPerson.getEnabled());
	}
	

	@Test
	@Order(3)
	public void testDisablePersonById() throws JsonMappingException, JsonProcessingException {
			
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
					.pathParam("id", personDto.getId())
					.when()
					.patch("{id}")
				.then()
					.statusCode(200)
						.extract()
						.body()
							.asString();
		
		PersonDto persistedPerson = objectMapper.readValue(content, PersonDto.class);
		personDto = persistedPerson;
		
		assertNotNull(persistedPerson);
		
		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getGender());
		assertNotNull(persistedPerson.getCpf());
		assertNotNull(persistedPerson.getBirth());
		assertNotNull(persistedPerson.getPhoneNumber());
		assertNotNull(persistedPerson.getMobileNumber());
		assertNotNull(persistedPerson.getKeyPix());
		assertFalse(persistedPerson.getEnabled());
		
		assertEquals(personDto.getId(), persistedPerson.getId());
		
		assertEquals("Nelson", persistedPerson.getFirstName());
		assertEquals("Piquet Souto Maior", persistedPerson.getLastName());
		assertEquals("Male", persistedPerson.getGender());
		assertEquals("04888053683", persistedPerson.getCpf());
		assertEquals(LocalDate.of(1991, 2, 24), persistedPerson.getBirth());        
		assertEquals("3436834703", persistedPerson.getPhoneNumber());
		assertEquals("34988681043", persistedPerson.getMobileNumber());
		assertEquals("04888053683", persistedPerson.getKeyPix());
		assertEquals(false, persistedPerson.getEnabled());
	}

	@Test
	@Order(4)
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		mockPerson();
			
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
					.pathParam("id", personDto.getId())
					.when()
					.get("{id}")
				.then()
					.statusCode(200)
						.extract()
						.body()
							.asString();
		
		PersonDto persistedPerson = objectMapper.readValue(content, PersonDto.class);
		personDto = persistedPerson;
		
		assertNotNull(persistedPerson);
		
		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getCpf());
		assertNotNull(persistedPerson.getBirth());
		assertNotNull(persistedPerson.getPhoneNumber());
		assertNotNull(persistedPerson.getMobileNumber());
		assertNotNull(persistedPerson.getKeyPix());
		assertFalse(persistedPerson.getEnabled());
		
		assertEquals(personDto.getId(), persistedPerson.getId());
		
		assertEquals("Nelson", persistedPerson.getFirstName());
		assertEquals("Piquet Souto Maior", persistedPerson.getLastName());
		assertEquals("Male", persistedPerson.getGender());
		assertEquals("04888053683", persistedPerson.getCpf());
		assertEquals(LocalDate.of(1991, 2, 24), persistedPerson.getBirth());        
		assertEquals("3436834703", persistedPerson.getPhoneNumber());
		assertEquals("34988681043", persistedPerson.getMobileNumber());
		assertEquals("04888053683", persistedPerson.getKeyPix());
		assertEquals(false, persistedPerson.getEnabled());
	}
	
	@Test
	@Order(5)
	public void testDelete() throws JsonMappingException, JsonProcessingException {

		given().spec(specification)
			.contentType(TestConfigs.CONTENT_TYPE_XML)
			.accept(TestConfigs.CONTENT_TYPE_XML)
				.pathParam("id", personDto.getId())
				.when()
				.delete("{id}")
			.then()
				.statusCode(204);
	}
	
	@Test
	@Order(6)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.queryParams("page", 0, "size", 12, "direction", "asc")
					.when()
					.get()
				.then()
					.statusCode(200)
						.extract()
						.body()
							.asString();
		
		PagedModelPerson wrapper = objectMapper.readValue(content, PagedModelPerson.class);
		var people = wrapper.getContent();
		
		PersonDto foundPersonOne = people.get(0);
		
		assertNotNull(foundPersonOne.getId());
		assertNotNull(foundPersonOne.getFirstName());
		assertNotNull(foundPersonOne.getLastName());
		assertNotNull(foundPersonOne.getGender());
		assertNotNull(foundPersonOne.getCpf());
		assertNotNull(foundPersonOne.getBirth());
		assertNotNull(foundPersonOne.getPhoneNumber());
		assertNotNull(foundPersonOne.getMobileNumber());
		assertNotNull(foundPersonOne.getKeyPix());
		assertTrue(foundPersonOne.getEnabled());
		
		assertEquals(7, foundPersonOne.getId());
		
		assertEquals("Carlos", foundPersonOne.getFirstName());
		assertEquals("Lima", foundPersonOne.getLastName());
		assertEquals("Male", foundPersonOne.getGender());
		assertEquals("75117537182", foundPersonOne.getCpf());
		assertEquals(LocalDate.of(1999, 4, 06), foundPersonOne.getBirth());        
		assertEquals("6125898456", foundPersonOne.getPhoneNumber());
		assertEquals("61981096924", foundPersonOne.getMobileNumber());
		assertEquals("75117537182", foundPersonOne.getKeyPix());
		assertEquals(true, foundPersonOne.getEnabled());
		
		PersonDto foundPersonThree = people.get(2);
		
		assertNotNull(foundPersonThree.getId());
		assertNotNull(foundPersonThree.getFirstName());
		assertNotNull(foundPersonThree.getLastName());
		assertNotNull(foundPersonThree.getGender());
		assertNotNull(foundPersonThree.getCpf());
		assertNotNull(foundPersonThree.getBirth());
		assertNotNull(foundPersonThree.getPhoneNumber());
		assertNotNull(foundPersonThree.getMobileNumber());
		assertNotNull(foundPersonThree.getKeyPix());
		assertTrue(foundPersonThree.getEnabled());
		
		assertEquals(1, foundPersonThree.getId());
		
		assertEquals("João", foundPersonThree.getFirstName());
		assertEquals("da Silva", foundPersonThree.getLastName());
		assertEquals("Male", foundPersonThree.getGender());
		assertEquals("04888053685", foundPersonThree.getCpf());
		assertEquals(LocalDate.of(1991, 2, 24), foundPersonThree.getBirth());        
		assertEquals("3436834703", foundPersonThree.getPhoneNumber());
		assertEquals("34988681043", foundPersonThree.getMobileNumber());
		assertEquals("04888053685", foundPersonThree.getKeyPix());
		assertEquals(true, foundPersonThree.getEnabled());
	}

	@Test
	@Order(7)
	public void testFindByName() throws JsonMappingException, JsonProcessingException {
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.pathParam("firstName", "Joa")
				.queryParams("page", 0, "size", 12, "direction", "asc")
					.when()
					.get("findPersonByName/{firstName}")
				.then()
					.statusCode(200)
						.extract()
						.body()
						.asString();
	
		PagedModelPerson wrapper = objectMapper.readValue(content, PagedModelPerson.class);
		var people = wrapper.getContent();
	
		PersonDto foundPersonOne = people.get(0);
		
		assertNotNull(foundPersonOne.getId());
		assertNotNull(foundPersonOne.getFirstName());
		assertNotNull(foundPersonOne.getLastName());
		assertNotNull(foundPersonOne.getGender());
		assertNotNull(foundPersonOne.getCpf());
		assertNotNull(foundPersonOne.getBirth());
		assertNotNull(foundPersonOne.getPhoneNumber());
		assertNotNull(foundPersonOne.getMobileNumber());
		assertNotNull(foundPersonOne.getKeyPix());
		assertTrue(foundPersonOne.getEnabled());
		
		assertEquals(1, foundPersonOne.getId());
		
		assertEquals("João", foundPersonOne.getFirstName());
		assertEquals("da Silva", foundPersonOne.getLastName());
		assertEquals("Male", foundPersonOne.getGender());
		assertEquals("04888053685", foundPersonOne.getCpf());
		assertEquals(LocalDate.of(1991, 2, 24), foundPersonOne.getBirth());        
		assertEquals("3436834703", foundPersonOne.getPhoneNumber());
		assertEquals("34988681043", foundPersonOne.getMobileNumber());
		assertEquals("04888053685", foundPersonOne.getKeyPix());
		assertEquals(true, foundPersonOne.getEnabled());
	}
	
	@Test
	@Order(8)
	public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {
		
		RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
			.setBasePath("/api/people/v1")
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
	@Order(9)
	public void testHATEOAS() throws JsonMappingException, JsonProcessingException {
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.queryParams("page", 0, "size", 12, "direction", "asc")
				.when()
					.get()
				.then()
					.statusCode(200)
						.extract()
						.body()
							.asString();
		
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/people/v1/7</href></links>"));
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/people/v1/6</href></links>"));
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/people/v1/1</href></links>"));
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/people/v1/3</href></links>"));
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/people/v1/5</href></links>"));
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/people/v1/2</href></links>"));
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/people/v1/4</href></links>"));
		
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/people/v1?page=0&amp;size=12&amp;direction=asc</href></links>"));

		assertTrue(content.contains("<page><size>12</size><totalElements>7</totalElements><totalPages>1</totalPages><number>0</number></page>"));
	}
	
	private void mockPerson() {
		personDto.setFirstName("Nelson");
		personDto.setLastName("Piquet");
		personDto.setGender("Male");
		personDto.setCpf("04888053683");
		personDto.setBirth(LocalDate.of(1991, 2, 24));        
		personDto.setPhoneNumber("3436834703");
		personDto.setMobileNumber("34988681043");
		personDto.setKeyPix("04888053683");
		personDto.setEnabled(true);
	}
}