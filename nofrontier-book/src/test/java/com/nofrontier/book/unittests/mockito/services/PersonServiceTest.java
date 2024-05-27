package com.nofrontier.book.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import com.nofrontier.book.domain.exceptions.RequiredObjectIsNullException;
import com.nofrontier.book.domain.model.Person;
import com.nofrontier.book.domain.repository.PersonRepository;
import com.nofrontier.book.domain.services.PersonService;
import com.nofrontier.book.dto.v1.requests.PersonRequest;
import com.nofrontier.book.dto.v1.responses.PersonResponse;
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
        
		assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
		
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
        Person entity = input.mockEntity(1);
        entity.setId(1L);

        Person persisted = entity;
        persisted.setId(1L);

        PersonRequest vo = input.mockRequest(1);

        when(personRepository.save(entity)).thenReturn(persisted);

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

        PersonRequest vo = input.mockRequest(1);

        when(personRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(personRepository.save(entity)).thenReturn(persisted);

        var result = personService.update(null, vo);

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
					personService.update(null, null);
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
	
	// -------------------------------------------------------------------------------------------------------------

	@Test
    void testFindAll(Pageable pageable) {
        List<Person> personList = input.mockEntityList();
        Page<Person> personPage = new PageImpl<>(personList);

        when(personRepository.findAll(pageable)).thenReturn(personPage);

        PagedModel<EntityModel<PersonResponse>> people = personService.findAll(pageable);

        assertNotNull(people);
        assertEquals(14, people.getContent().size());

        // Convert PagedModel content to list for easier access by index
        List<EntityModel<PersonResponse>> personResponses = new ArrayList<>(people.getContent());

        var personOne = personResponses.get(1).getContent();
        assertNotNull(personOne);
        assertNotNull(personOne.getKey());
        assertNotNull(personOne.getLinks());
        assertTrue(personOne.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
		assertEquals("First Name Test1", personOne.getFirstName());
		assertEquals("Last Name Test1", personOne.getLastName());
		assertEquals("Female", personOne.getGender());
		assertEquals("Cpf Test", personOne.getCpf());
		assertEquals(LocalDate.of(0, 0, 0), personOne.getBirth());
		assertEquals("Phone Number Test", personOne.getPhoneNumber());
		assertEquals("Mobile Number Test", personOne.getPhoneNumber());
		assertEquals("Key Pix Test", personOne.getKeyPix());
		assertEquals(Boolean.valueOf(true), personOne.getEnabled());

        var personFour = personResponses.get(4).getContent();
        assertNotNull(personFour);
        assertNotNull(personFour.getKey());
        assertNotNull(personFour.getLinks());
        assertTrue(personFour.toString().contains("links: [</api/book/v1/4>;rel=\"self\"]"));
		assertEquals("First Name Test1", personFour.getFirstName());
		assertEquals("Last Name Test1", personFour.getLastName());
		assertEquals("Female", personFour.getGender());
		assertEquals("Cpf Test", personFour.getCpf());
		assertEquals(LocalDate.of(0, 0, 0), personFour.getBirth());
		assertEquals("Phone Number Test", personFour.getPhoneNumber());
		assertEquals("Mobile Number Test", personFour.getPhoneNumber());
		assertEquals("Key Pix Test", personFour.getKeyPix());
		assertEquals(Boolean.valueOf(true), personFour.getEnabled());

        var personSeven = personResponses.get(7).getContent();
        assertNotNull(personSeven);
        assertNotNull(personSeven.getKey());
        assertNotNull(personSeven.getLinks());
        assertTrue(personSeven.toString().contains("links: [</api/book/v1/7>;rel=\"self\"]"));
		assertEquals("First Name Test1", personSeven.getFirstName());
		assertEquals("Last Name Test1", personSeven.getLastName());
		assertEquals("Female", personSeven.getGender());
		assertEquals("Cpf Test", personSeven.getCpf());
		assertEquals(LocalDate.of(0, 0, 0), personSeven.getBirth());
		assertEquals("Phone Number Test", personSeven.getPhoneNumber());
		assertEquals("Mobile Number Test", personSeven.getPhoneNumber());
		assertEquals("Key Pix Test", personSeven.getKeyPix());
		assertEquals(Boolean.valueOf(true), personSeven.getEnabled());
    }
}
