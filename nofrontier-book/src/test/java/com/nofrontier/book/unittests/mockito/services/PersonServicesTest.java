package com.nofrontier.book.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ActiveProfiles;

import com.nofrontier.book.domain.exceptions.RequiredObjectIsNullException;
import com.nofrontier.book.domain.model.Person;
import com.nofrontier.book.domain.repository.PersonRepository;
import com.nofrontier.book.domain.services.ApiPersonService;
import com.nofrontier.book.dto.v1.requests.PersonRequest;
import com.nofrontier.book.dto.v1.responses.PersonResponse;
import com.nofrontier.book.unittests.mapper.mocks.MockPerson;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class PersonServicesTest {

    MockPerson input;
    
    @InjectMocks
    private ApiPersonService service;
    
    @Mock
    PersonRepository repository;
    
    @Mock
    private ModelMapper modelMapper;
    
    @BeforeEach
    void setUpMocks() throws Exception {
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        Person entity = input.mockEntity(1); 
        entity.setId(1L);
        
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(modelMapper.map(any(Person.class), eq(PersonResponse.class))).thenReturn(input.mockResponse(1));
        
        var result = service.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        
        assertTrue(result.toString().contains("links: [</api/people/v1/1>;rel=\"self\"]"));
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
        assertEquals("04888053685", result.getCpf());
        assertEquals(LocalDate.of(1991, 02, 24), result.getBirth());
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
        
        PersonRequest vo = input.mockRequest(1);
        vo.setId(1L);
        
        PersonResponse response = input.mockResponse(1);
        response.setKey(1L);

        // Ensure modelMapper is stubbed correctly for both mappings
        when(modelMapper.map(any(PersonRequest.class), eq(Person.class))).thenReturn(entity);
        when(modelMapper.map(any(Person.class), eq(PersonResponse.class))).thenReturn(response);
        
        when(repository.save(any(Person.class))).thenReturn(persisted);
        
        var result = service.create(vo);
        
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        
        assertTrue(result.toString().contains("links: [</api/people/v1/1>;rel=\"self\"]"));
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
        assertEquals("04888053685", result.getCpf());
        assertEquals(LocalDate.of(1991, 02, 24), result.getBirth());
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
        
        PersonRequest vo = input.mockRequest(1);
        vo.setId(1L);
        
        PersonResponse response = input.mockResponse(1);
        response.setKey(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(modelMapper.map(any(PersonRequest.class), eq(Person.class))).thenReturn(entity);
        when(repository.save(any(Person.class))).thenReturn(persisted);
        when(modelMapper.map(any(Person.class), eq(PersonResponse.class))).thenReturn(response);
        
        var result = service.update(1L, vo);
        
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        
        assertTrue(result.toString().contains("links: [</api/people/v1/1>;rel=\"self\"]"));
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
        assertEquals("04888053685", result.getCpf());
        assertEquals(LocalDate.of(1991, 02, 24), result.getBirth());
        assertEquals("3436834703", result.getPhoneNumber());
        assertEquals("34988681043", result.getMobileNumber());
        assertEquals("04888053685", result.getKeyPix());
        assertEquals(true, result.getEnabled());
    }
    
    @Test
    void testUpdateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.update(null, null);
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