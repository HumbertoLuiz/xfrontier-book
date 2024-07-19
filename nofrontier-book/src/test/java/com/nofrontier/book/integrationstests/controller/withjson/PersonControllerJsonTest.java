package com.nofrontier.book.integrationstests.controller.withjson;

import static io.restassured.RestAssured.given;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nofrontier.book.configs.TestConfigs;

import com.nofrontier.book.integrationstests.dto.PersonDto;
import com.nofrontier.book.integrationstests.dto.TokenRequest;
import com.nofrontier.book.integrationstests.dto.TokenResponse;
import com.nofrontier.book.integrationstests.dto.wrappers.WrapperPersonDto;
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
public class PersonControllerJsonTest extends AbstractIntegrationTest {
	
	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;

	private static PersonDto person;
	
	@BeforeAll
	public static void setup() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		person = new PersonDto();
	}
	
	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		
		TokenRequest user = new TokenRequest("admin@mail.com", "senha@123");
		
		var accessToken = given()
				.basePath("/auth/token")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_JSON)
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
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.body(person)
					.when()
					.post()
				.then()
					.statusCode(200)
						.extract()
						.body()
							.asString();
		
		PersonDto persistedPerson = objectMapper.readValue(content, PersonDto.class);
		person = persistedPerson;
		
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
		
		assertEquals("João", persistedPerson.getFirstName());
		assertEquals("da Silv", persistedPerson.getLastName());
		assertEquals("Male", persistedPerson.getGender());
		assertEquals("04888053685", persistedPerson.getCpf());
		assertEquals("1991-02-24", persistedPerson.getBirth());
		assertEquals("3436834703", persistedPerson.getPhoneNumber());
		assertEquals("34988681043", persistedPerson.getMobileNumber());
		assertEquals("04888053685", persistedPerson.getKeyPix());
		assertEquals("true", persistedPerson.getEnabled());
	}

	@Test
	@Order(2)
	public void testUpdate() throws JsonMappingException, JsonProcessingException {
		person.setLastName("da Silva");
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.body(person)
					.when()
					.post()
				.then()
					.statusCode(200)
						.extract()
						.body()
							.asString();
		
		PersonDto persistedPerson = objectMapper.readValue(content, PersonDto.class);
		person = persistedPerson;
		
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
		
		assertEquals("João", persistedPerson.getFirstName());
		assertEquals("da Silva", persistedPerson.getLastName());
		assertEquals("Male", persistedPerson.getGender());
		assertEquals("04888053685", persistedPerson.getCpf());
		assertEquals("1991-02-24", persistedPerson.getBirth());
		assertEquals("3436834703", persistedPerson.getPhoneNumber());
		assertEquals("34988681043", persistedPerson.getMobileNumber());
		assertEquals("04888053685", persistedPerson.getKeyPix());
		assertEquals("true", persistedPerson.getEnabled());
	}

	@Test
	@Order(3)
	public void testDisablePersonById() throws JsonMappingException, JsonProcessingException {
			
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.pathParam("id", person.getId())
					.when()
					.patch("{id}")
				.then()
					.statusCode(200)
						.extract()
						.body()
							.asString();
		
		PersonDto persistedPerson = objectMapper.readValue(content, PersonDto.class);
		person = persistedPerson;
		
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
		
		assertEquals("João", persistedPerson.getFirstName());
		assertEquals("da Silva", persistedPerson.getLastName());
		assertEquals("Male", persistedPerson.getGender());
		assertEquals("04888053685", persistedPerson.getCpf());
		assertEquals("1991-02-24", persistedPerson.getBirth());
		assertEquals("3436834703", persistedPerson.getPhoneNumber());
		assertEquals("34988681043", persistedPerson.getMobileNumber());
		assertEquals("04888053685", persistedPerson.getKeyPix());
		assertEquals("true", persistedPerson.getEnabled());
	}
	
	@Test
	@Order(4)
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		mockPerson();
			
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.pathParam("id", person.getId())
					.when()
					.get("{id}")
				.then()
					.statusCode(200)
						.extract()
						.body()
							.asString();
		
		PersonDto persistedPerson = objectMapper.readValue(content, PersonDto.class);
		person = persistedPerson;
		
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
		
		assertEquals("João", persistedPerson.getFirstName());
		assertEquals("da Silva", persistedPerson.getLastName());
		assertEquals("Male", persistedPerson.getGender());
		assertEquals("04888053685", persistedPerson.getCpf());
		assertEquals("1991-02-24", persistedPerson.getBirth());
		assertEquals("3436834703", persistedPerson.getPhoneNumber());
		assertEquals("34988681043", persistedPerson.getMobileNumber());
		assertEquals("04888053685", persistedPerson.getKeyPix());
		assertEquals("true", persistedPerson.getEnabled());
	}
	
	@Test
	@Order(5)
	public void testDelete() throws JsonMappingException, JsonProcessingException {

		given().spec(specification)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.pathParam("id", person.getId())
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
				.accept(TestConfigs.CONTENT_TYPE_JSON)
				.queryParams("page", 3, "size", 10, "direction", "asc")
					.when()
					.get()
				.then()
					.statusCode(200)
						.extract()
						.body()
							.asString();
		
		WrapperPersonDto wrapper = objectMapper.readValue(content, WrapperPersonDto.class);
		var people = wrapper.getEmbedded().getPersons();
		
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
		
		assertTrue(foundPersonOne.getId() > 0);
		
		assertEquals("João", foundPersonOne.getFirstName());
		assertEquals("da Silva", foundPersonOne.getLastName());
		assertEquals("Male", foundPersonOne.getGender());
		assertEquals("04888053685", foundPersonOne.getCpf());
		assertEquals("1991-02-24", foundPersonOne.getBirth());
		assertEquals("3436834703", foundPersonOne.getPhoneNumber());
		assertEquals("34988681043", foundPersonOne.getMobileNumber());
		assertEquals("04888053685", foundPersonOne.getKeyPix());
		assertEquals("true", foundPersonOne.getEnabled());
		
		PersonDto foundPersonSix = people.get(5);
		
		assertNotNull(foundPersonSix.getId());
		assertNotNull(foundPersonSix.getFirstName());
		assertNotNull(foundPersonSix.getLastName());
		assertNotNull(foundPersonSix.getGender());
		assertNotNull(foundPersonSix.getCpf());
		assertNotNull(foundPersonSix.getBirth());
		assertNotNull(foundPersonSix.getPhoneNumber());
		assertNotNull(foundPersonSix.getMobileNumber());
		assertNotNull(foundPersonSix.getKeyPix());
		assertTrue(foundPersonSix.getEnabled());
		
		assertTrue(foundPersonSix.getId() > 0);
		
		assertEquals("João", foundPersonSix.getFirstName());
		assertEquals("da Silva", foundPersonSix.getLastName());
		assertEquals("Male", foundPersonSix.getGender());
		assertEquals("04888053685", foundPersonSix.getCpf());
		assertEquals("1991-02-24", foundPersonSix.getBirth());
		assertEquals("3436834703", foundPersonSix.getPhoneNumber());
		assertEquals("34988681043", foundPersonSix.getMobileNumber());
		assertEquals("04888053685", foundPersonSix.getKeyPix());
		assertEquals("true", foundPersonSix.getEnabled());
	}

	@Test
	@Order(7)
	public void testFindByName() throws JsonMappingException, JsonProcessingException {
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.accept(TestConfigs.CONTENT_TYPE_JSON)
				.pathParam("firstName", "ayr")
				.queryParams("page", 0, "size", 6, "direction", "asc")
					.when()
					.get("findPersonByName/{firstName}")
				.then()
					.statusCode(200)
						.extract()
						.body()
							.asString();
		
		WrapperPersonDto wrapper = objectMapper.readValue(content, WrapperPersonDto.class);
		var people = wrapper.getEmbedded().getPersons();
		
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
		
		assertTrue(foundPersonOne.getId() > 0);
		
		assertEquals("João", foundPersonOne.getFirstName());
		assertEquals("da Silva", foundPersonOne.getLastName());
		assertEquals("Male", foundPersonOne.getGender());
		assertEquals("04888053685", foundPersonOne.getCpf());
		assertEquals("1991-02-24", foundPersonOne.getBirth());
		assertEquals("3436834703", foundPersonOne.getPhoneNumber());
		assertEquals("34988681043", foundPersonOne.getMobileNumber());
		assertEquals("04888053685", foundPersonOne.getKeyPix());
		assertEquals("true", foundPersonOne.getEnabled());
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
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.when()
				.get()
			.then()
				.statusCode(403);
	}
	
	@Test
	@Order(9)
	public void testHATEOAS() throws JsonMappingException, JsonProcessingException {
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.accept(TestConfigs.CONTENT_TYPE_JSON)
				.queryParams("page", 3, "size", 10, "direction", "asc")
					.when()
					.get()
				.then()
					.statusCode(200)
						.extract()
						.body()
							.asString();
		
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/people/v1/677\"}}}"));
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/people/v1/846\"}}}"));
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/people/v1/714\"}}}"));
		
		assertTrue(content.contains("{\"first\":{\"href\":\"http://localhost:8888/api/people/v1?direction=asc&page=0&size=10&sort=firstName,asc\"}"));
		assertTrue(content.contains("\"prev\":{\"href\":\"http://localhost:8888/api/people/v1?direction=asc&page=2&size=10&sort=firstName,asc\"}"));
		assertTrue(content.contains("\"self\":{\"href\":\"http://localhost:8888/api/people/v1?page=3&size=10&direction=asc\"}"));
		assertTrue(content.contains("\"next\":{\"href\":\"http://localhost:8888/api/people/v1?direction=asc&page=4&size=10&sort=firstName,asc\"}"));
		assertTrue(content.contains("\"last\":{\"href\":\"http://localhost:8888/api/people/v1?direction=asc&page=100&size=10&sort=firstName,asc\"}}"));
		
		assertTrue(content.contains("\"page\":{\"size\":10,\"totalElements\":1007,\"totalPages\":101,\"number\":3}}"));
	}
	
	private void mockPerson() {
		person.setFirstName("João");
		person.setLastName("da Silva");
		person.setGender("Male");
		person.setCpf("04888053685");
		person.setBirth(LocalDate.of(1991, 02, 24));
		person.setPhoneNumber("3436834703");
		person.setMobileNumber("34988681043");
		person.setKeyPix("04888053685");
		person.setEnabled(true);
	}
}