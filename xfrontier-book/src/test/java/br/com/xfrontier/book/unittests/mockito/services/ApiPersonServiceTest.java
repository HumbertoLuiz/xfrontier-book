package br.com.xfrontier.book.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import br.com.xfrontier.book.core.validation.PersonValidator;
import br.com.xfrontier.book.domain.exceptions.RequiredObjectIsNullException;
import br.com.xfrontier.book.domain.model.Person;
import br.com.xfrontier.book.domain.repository.PersonRepository;
import br.com.xfrontier.book.domain.services.ApiPersonService;
import br.com.xfrontier.book.dto.v1.PersonDto;
import br.com.xfrontier.book.unittests.mapper.mocks.MockPerson;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class ApiPersonServiceTest {

	MockPerson input;
	
	@InjectMocks
	private ApiPersonService service;
	
	@Mock
	PersonRepository repository;
	
	@Mock
	private ModelMapper modelMapper;
	
	@Mock
	private PersonValidator validator;
	
    @BeforeEach
    void setUpMocks() throws Exception {
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);

    // Configuração do ModelMapper para os testes
    lenient().when(modelMapper.map(Mockito.any(Person.class), Mockito.eq(PersonDto.class)))
            .thenAnswer(invocation -> {
                Person source = invocation.getArgument(0);
                PersonDto vo = new PersonDto();
                vo.setKey(source.getId());
                vo.setFirstName(source.getFirstName());
                vo.setLastName(source.getLastName());
                vo.setGender(source.getGender());
                vo.setCpf(source.getCpf());
                vo.setBirth(source.getBirth());
                vo.setPhoneNumber(source.getPhoneNumber());
                vo.setMobileNumber(source.getMobileNumber());
                vo.setKeyPix(source.getKeyPix());
                vo.setEnabled(source.getEnabled());
                return vo;
            });

    lenient().when(modelMapper.map(Mockito.any(PersonDto.class), Mockito.eq(Person.class)))
            .thenAnswer(invocation -> {
                PersonDto source = invocation.getArgument(0);
                Person entity = new Person();
                entity.setId(source.getKey());
                entity.setFirstName(source.getFirstName());
                entity.setLastName(source.getLastName());
                entity.setGender(source.getGender());
                entity.setCpf(source.getCpf());
                entity.setBirth(source.getBirth());
                entity.setPhoneNumber(source.getPhoneNumber());
                entity.setMobileNumber(source.getMobileNumber());
                entity.setKeyPix(source.getKeyPix());
                entity.setEnabled(source.getEnabled());
                return entity;
            });
    }

	@Test
	void testFindById() {
		Person entity = input.mockEntity(1); 
		entity.setId(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		
		var result = service.findById(1L);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		
		assertTrue(result.toString().contains("links: [</api/people/v1/1>;rel=\"self\"]"));
		assertEquals("First Name Test1", result.getFirstName());
		assertEquals("Last Name Test1", result.getLastName());
		assertEquals("Female", result.getGender());
        assertEquals("04888053685", result.getCpf());
        assertEquals(LocalDate.of(1991, 2, 24), result.getBirth());
        assertEquals("3436834703", result.getPhoneNumber());
        assertEquals("34988681043", result.getMobileNumber());
        assertEquals("04888053685", result.getKeyPix());
        assertEquals(true, result.getEnabled());
	}
	
	@Test
	void testCreate() {
		Person entity = input.mockEntity(1); 
		entity.setId(1L);
		
		Person persisted = entity;
		persisted.setId(1L);
		
		PersonDto vo = input.mockDto(1);
		vo.setKey(1L);

		validator.validate(entity);
		
        // Alteração para evitar PotentialStubbingProblem
        doReturn(persisted).when(repository).save(entity);
		
		var result = service.create(vo);
		
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		
		assertTrue(result.toString().contains("links: [</api/people/v1/1>;rel=\"self\"]"));
		assertEquals("First Name Test1", result.getFirstName());
		assertEquals("Last Name Test1", result.getLastName());
		assertEquals("Female", result.getGender());
        assertEquals("04888053685", result.getCpf());
        assertEquals(LocalDate.of(1991, 2, 24), result.getBirth());
        assertEquals("3436834703", result.getPhoneNumber());
        assertEquals("34988681043", result.getMobileNumber());
        assertEquals("04888053685", result.getKeyPix());
        assertEquals(true, result.getEnabled());
	}
	
	@Test
	void testCreateWithNullPerson() {
		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			service.create(null);
		});
		
		String expectedMessage = "It is not allowed to persist a null object!";
		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));
	}


	@Test
	void testUpdate() {
		Person entity = input.mockEntity(1); 
		
		Person persisted = entity;
		persisted.setId(1L);
		
		PersonDto vo = input.mockDto(1);
		vo.setKey(1L);
		

		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		when(repository.save(entity)).thenReturn(persisted);
		
		var result = service.update(vo);
		
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		
		assertTrue(result.toString().contains("links: [</api/people/v1/1>;rel=\"self\"]"));
		assertEquals("First Name Test1", result.getFirstName());
		assertEquals("Last Name Test1", result.getLastName());
		assertEquals("Female", result.getGender());
        assertEquals("04888053685", result.getCpf());
        assertEquals(LocalDate.of(1991, 2, 24), result.getBirth());
        assertEquals("3436834703", result.getPhoneNumber());
        assertEquals("34988681043", result.getMobileNumber());
        assertEquals("04888053685", result.getKeyPix());
        assertEquals(true, result.getEnabled());
	}
	
	@Test
	void testUpdateWithNullPerson() {
		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			service.update(null);
		});
		
		String expectedMessage = "It is not allowed to persist a null object!";
		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void testDelete() {
		Person entity = input.mockEntity(1); 
		entity.setId(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		
		service.delete(1L);
	}
}
