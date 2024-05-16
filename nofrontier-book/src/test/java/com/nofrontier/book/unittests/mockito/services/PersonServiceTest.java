package com.nofrontier.book.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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
import com.nofrontier.book.domain.model.Person;
import com.nofrontier.book.domain.repository.PersonRepository;
import com.nofrontier.book.domain.services.PersonService;
import com.nofrontier.book.dto.v1.PersonDto;
import com.nofrontier.book.unittests.mapper.mocks.MockPerson;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

	MockPerson input;

	@InjectMocks
	private PersonService personService;

	@Mock
	private PersonRepository personRepository;

	@BeforeEach
	void setUpMocks() throws Exception {
		input = new MockPerson();
		MockitoAnnotations.openMocks(this);
	}

	// -------------------------------------------------------------------------------------------------------------

	@Test
	void testFindById() {
		Person entity = input.mockEntity(1);
		entity.setId(1L);
		when(personRepository.findById(1L)).thenReturn(Optional.of(entity));
		var result = personService.findById(1L);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString()
				.contains("links: [</api/person/v1/1>;rel=\"self\"]"));
		assertEquals("First Name Test1", result.getFirstName());
		assertEquals("Last Name Test1", result.getLastName());
		assertEquals("Female", result.getGender());
		assertEquals("Cpf Test", result.getCpf());
		assertEquals(LocalDate.of(0, 0, 0), result.getBirth());
		assertEquals("Phone Number Test", result.getPhoneNumber());
		assertEquals("Mobile Number Test", result.getPhoneNumber());
		assertEquals("Key Pix Test", result.getKeyPix());
		assertEquals(Boolean.valueOf(true), result.getEnabled());
	}

	// -------------------------------------------------------------------------------------------------------------

	@Test
	void testCreate() {
		Person persisted = input.mockEntity(1);
		persisted.setId(1L);
		PersonDto vo = input.mockDto(1);
		vo.setKey(1L);
		when(personRepository.save(any(Person.class))).thenReturn(persisted);
		var result = personService.create(vo);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString()
				.contains("links: [</api/person/v1/1>;rel=\"self\"]"));
		assertEquals("First Name Test1", result.getFirstName());
		assertEquals("Last Name Test1", result.getLastName());
		assertEquals("Female", result.getGender());
		assertEquals("Cpf Test", result.getCpf());
		assertEquals(LocalDate.of(0, 0, 0), result.getBirth());
		assertEquals("Phone Number Test", result.getPhoneNumber());
		assertEquals("Mobile Number Test", result.getPhoneNumber());
		assertEquals("Key Pix Test", result.getKeyPix());
		assertEquals(Boolean.valueOf(true), result.getEnabled());
	}

	// -------------------------------------------------------------------------------------------------------------

	@Test
	void testCreateWithNullPerson() {
		Exception exception = assertThrows(RequiredObjectIsNullException.class,
				() -> {
					personService.create(null);
				});
		String expectedMessage = "It is not allowed to persist a null object!";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	// -------------------------------------------------------------------------------------------------------------

	@Test
	void testUpdate() {
		Person entity = input.mockEntity(1);
		Person persisted = entity;
		persisted.setId(1L);
		PersonDto vo = input.mockDto(1);
		vo.setKey(1L);
		when(personRepository.findById(1L)).thenReturn(Optional.of(entity));
		when(personRepository.save(entity)).thenReturn(persisted);
		var result = personService.update(vo);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString()
				.contains("links: [</api/person/v1/1>;rel=\"self\"]"));
		assertEquals("First Name Test1", result.getFirstName());
		assertEquals("Last Name Test1", result.getLastName());
		assertEquals("Female", result.getGender());
		assertEquals("Cpf Test", result.getCpf());
		assertEquals(LocalDate.of(0, 0, 0), result.getBirth());
		assertEquals("Phone Number Test", result.getPhoneNumber());
		assertEquals("Mobile Number Test", result.getPhoneNumber());
		assertEquals("Key Pix Test", result.getKeyPix());
		assertEquals(Boolean.valueOf(true), result.getEnabled());
	}

	// -------------------------------------------------------------------------------------------------------------

	@Test
	void testUpdateWithNullPerson() {
		Exception exception = assertThrows(RequiredObjectIsNullException.class,
				() -> {
					personService.update(null);
				});
		String expectedMessage = "It is not allowed to persist a null object!";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	// -------------------------------------------------------------------------------------------------------------

	@Test
	void testDelete() {
		Person entity = input.mockEntity(1);
		entity.setId(1L);
		when(personRepository.findById(1L)).thenReturn(Optional.of(entity));
		personService.delete(1L);
	}
}
