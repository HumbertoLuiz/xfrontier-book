package com.nofrontier.book.unittests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ActiveProfiles;

import com.nofrontier.book.domain.model.Person;
import com.nofrontier.book.integrationstests.dto.PersonDto;
import com.nofrontier.book.unittests.mapper.mocks.MockPerson;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ActiveProfiles("test")
public class ModelMapperConverterTest {

    private static final ModelMapper modelMapper = new ModelMapper();
    
    MockPerson inputObject;
    
    @BeforeEach
    public void setUp() {
    	inputObject = new MockPerson();
    }

    @Test
    public void parseEntityToVOTest() {
        PersonDto output = modelMapper.map(inputObject.mockEntity(), PersonDto.class);
        assertEquals(Long.valueOf(0L), output.getKey());
        assertEquals("First Name Test0", output.getFirstName());
        assertEquals("Last Name Test0", output.getLastName());
        assertEquals("Male", output.getGender());
    }

    @Test
    public void parseEntityListToVOListTest() {
        List<PersonDto> outputList = inputObject.mockEntityList().stream()
                .map(entity -> modelMapper.map(entity, PersonDto.class))
                .collect(Collectors.toList());
        
        PersonDto outputZero = outputList.get(0);
        
        assertEquals(Long.valueOf(0L), outputZero.getKey());
        assertEquals("First Name Test0", outputZero.getFirstName());
        assertEquals("Last Name Test0", outputZero.getLastName());
        assertEquals("Male", outputZero.getGender());
        
        PersonDto outputSeven = outputList.get(7);
        
        assertEquals(Long.valueOf(7L), outputSeven.getKey());
        assertEquals("First Name Test7", outputSeven.getFirstName());
        assertEquals("Last Name Test7", outputSeven.getLastName());
        assertEquals("Female", outputSeven.getGender());
        
        PersonDto outputTwelve = outputList.get(12);
        
        assertEquals(Long.valueOf(12L), outputTwelve.getKey());
        assertEquals("First Name Test12", outputTwelve.getFirstName());
        assertEquals("Last Name Test12", outputTwelve.getLastName());
        assertEquals("Male", outputTwelve.getGender());
    }

    @Test
    public void parseVOToEntityTest() {
        Person output = modelMapper.map(inputObject.mockRequest(), Person.class);
        assertEquals(Long.valueOf(0L), output.getId());
        assertEquals("First Name Test0", output.getFirstName());
        assertEquals("Last Name Test0", output.getLastName());
        assertEquals("Male", output.getGender());
    }

    @Test
    public void parserVOListToEntityListTest() {
        List<Person> outputList = inputObject.mockRequestList().stream()
                .map(request -> modelMapper.map(request, Person.class))
                .collect(Collectors.toList());
        Person outputZero = outputList.get(0);
        
        assertEquals(Long.valueOf(0L), outputZero.getId());
        assertEquals("First Name Test0", outputZero.getFirstName());
        assertEquals("Last Name Test0", outputZero.getLastName());
        assertEquals("Male", outputZero.getGender());
        
        Person outputSeven = outputList.get(7);
        
        assertEquals(Long.valueOf(7L), outputSeven.getId());
        assertEquals("First Name Test7", outputSeven.getFirstName());
        assertEquals("Last Name Test7", outputSeven.getLastName());
        assertEquals("Female", outputSeven.getGender());
        
        Person outputTwelve = outputList.get(12);
        
        assertEquals(Long.valueOf(12L), outputTwelve.getId());
        assertEquals("First Name Test12", outputTwelve.getFirstName());
        assertEquals("Last Name Test12", outputTwelve.getLastName());
        assertEquals("Male", outputTwelve.getGender());
    }
}
