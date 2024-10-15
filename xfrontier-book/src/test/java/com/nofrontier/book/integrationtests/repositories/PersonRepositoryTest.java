package com.nofrontier.book.integrationtests.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.nofrontier.book.domain.model.Person;
import com.nofrontier.book.domain.repository.PersonRepository;
import com.nofrontier.book.integrationtests.testcontainers.AbstractIntegrationTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(OrderAnnotation.class)
public class PersonRepositoryTest extends AbstractIntegrationTest {
	
	@Autowired
	public PersonRepository repository;
	
	private static Person person;
	
	@BeforeAll
	public static void setup() {
		person = new Person();
	}
	
	@Test
	@Order(1)
	public void testFindByName() throws JsonMappingException, JsonProcessingException {
		
		Pageable pageable = PageRequest.of(0, 6, Sort.by(Direction.ASC, "firstName"));
		person = repository.findPersonByName("jo", pageable).getContent().get(0);
		
		assertNotNull(person.getId());
		assertNotNull(person.getFirstName());
		assertNotNull(person.getLastName());
		assertNotNull(person.getGender());
		assertNotNull(person.getCpf());
		assertNotNull(person.getBirth());
		assertNotNull(person.getPhoneNumber());
		assertNotNull(person.getMobileNumber());
		assertNotNull(person.getKeyPix());
		assertTrue(person.getEnabled());
		
		assertEquals(1, person.getId());
		
		assertEquals("João", person.getFirstName());
		assertEquals("da Silva", person.getLastName());
		assertEquals("Male", person.getGender());
		assertEquals("04888053685", person.getCpf());
		assertEquals(LocalDate.of(1991, 2, 24), person.getBirth());
		assertEquals("3436834703", person.getPhoneNumber());
		assertEquals("34988681043", person.getMobileNumber());
		assertEquals("04888053685", person.getKeyPix());
		assertEquals(true, person.getEnabled());
	}
	
	@Test
	@Order(2)
	public void testDisablePerson() throws JsonMappingException, JsonProcessingException {
		
		repository.disablePerson(person.getId());
		
		Pageable pageable = PageRequest.of(0, 6, Sort.by(Direction.ASC, "firstName"));
		person = repository.findPersonByName("jo", pageable).getContent().get(0);
		
		assertNotNull(person.getId());
		assertNotNull(person.getFirstName());
		assertNotNull(person.getLastName());
		assertNotNull(person.getGender());
		assertNotNull(person.getCpf());
		assertNotNull(person.getBirth());
		assertNotNull(person.getPhoneNumber());
		assertNotNull(person.getMobileNumber());
		assertNotNull(person.getKeyPix());
		assertFalse(person.getEnabled());
		
		assertEquals(1, person.getId());
		
		assertEquals("João", person.getFirstName());
		assertEquals("da Silva", person.getLastName());
		assertEquals("Male", person.getGender());
		assertEquals("04888053685", person.getCpf());
		assertEquals(LocalDate.of(1991, 2, 24), person.getBirth());
		assertEquals("3436834703", person.getPhoneNumber());
		assertEquals("34988681043", person.getMobileNumber());
		assertEquals("04888053685", person.getKeyPix());
		assertEquals(false, person.getEnabled());
	}
}