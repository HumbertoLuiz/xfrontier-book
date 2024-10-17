package br.com.xfrontier.book.unittests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ActiveProfiles;

import br.com.xfrontier.book.domain.model.Person;
import br.com.xfrontier.book.dto.v1.PersonDto;
import br.com.xfrontier.book.unittests.mapper.mocks.MockPerson;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ActiveProfiles("test")
public class ModelMapperConverterTest {

    private ModelMapper modelMapper;
    
    MockPerson inputObject;
    
    @BeforeEach
    public void setUp() {
    	inputObject = new MockPerson();
    	modelMapper = new ModelMapper();

        modelMapper.typeMap(Person.class, PersonDto.class)
        	.addMapping(Person::getId, PersonDto::setKey);
        modelMapper.typeMap(PersonDto.class, Person.class)
        	.addMapping(PersonDto::getKey, Person::setId);

    }    
    
    @Test
    public void parseEntityToDtoTest() {
        PersonDto output = modelMapper.map(inputObject.mockEntity(), PersonDto.class);
        assertEquals(Long.valueOf(0L), output.getKey());
        assertEquals("First Name Test0", output.getFirstName());
        assertEquals("Last Name Test0", output.getLastName());
        assertEquals("Male", output.getGender());
        assertEquals("04888053685", output.getCpf());
        assertEquals(LocalDate.of(1991, 02, 24), output.getBirth());
        assertEquals("3436834703", output.getPhoneNumber());
        assertEquals("34988681043", output.getMobileNumber());
        assertEquals("04888053685", output.getKeyPix());
        assertEquals(true, output.getEnabled());       
    }

    @Test
    public void parseEntityListToDtoListTest() {
        List<PersonDto> outputList = inputObject.mockEntityList().stream()
                .map(entity -> modelMapper.map(entity, PersonDto.class))
                .collect(Collectors.toList());
        
        PersonDto outputZero = outputList.get(0);
        
        assertEquals(Long.valueOf(0L), outputZero.getKey());
        assertEquals("First Name Test0", outputZero.getFirstName());
        assertEquals("Last Name Test0", outputZero.getLastName());
        assertEquals("Male", outputZero.getGender());
        assertEquals("04888053685", outputZero.getCpf());
        assertEquals(LocalDate.of(1991, 02, 24), outputZero.getBirth());
        assertEquals("3436834703", outputZero.getPhoneNumber());
        assertEquals("34988681043", outputZero.getMobileNumber());
        assertEquals("04888053685", outputZero.getKeyPix());
        assertEquals(true, outputZero.getEnabled());    
        
        PersonDto outputSeven = outputList.get(7);
        
        assertEquals(Long.valueOf(7L), outputSeven.getKey());
        assertEquals("First Name Test7", outputSeven.getFirstName());
        assertEquals("Last Name Test7", outputSeven.getLastName());
        assertEquals("Female", outputSeven.getGender());
        assertEquals("04888053685", outputSeven.getCpf());
        assertEquals(LocalDate.of(1991, 02, 24), outputSeven.getBirth());
        assertEquals("3436834703", outputSeven.getPhoneNumber());
        assertEquals("34988681043", outputSeven.getMobileNumber());
        assertEquals("04888053685", outputSeven.getKeyPix());
        assertEquals(true, outputSeven.getEnabled());    
        
        PersonDto outputTwelve = outputList.get(12);
        
        assertEquals(Long.valueOf(12L), outputTwelve.getKey());
        assertEquals("First Name Test12", outputTwelve.getFirstName());
        assertEquals("Last Name Test12", outputTwelve.getLastName());
        assertEquals("Male", outputTwelve.getGender());
        assertEquals("04888053685", outputTwelve.getCpf());
        assertEquals(LocalDate.of(1991, 02, 24), outputTwelve.getBirth());
        assertEquals("3436834703", outputTwelve.getPhoneNumber());
        assertEquals("34988681043", outputTwelve.getMobileNumber());
        assertEquals("04888053685", outputTwelve.getKeyPix());
        assertEquals(true, outputTwelve.getEnabled());    
    }

    @Test
    public void parseDtoToEntityTest() {
        Person output = modelMapper.map(inputObject.mockDto(), Person.class);
        assertEquals(Long.valueOf(0L), output.getId());
        assertEquals("First Name Test0", output.getFirstName());
        assertEquals("Last Name Test0", output.getLastName());
        assertEquals("Male", output.getGender());
        assertEquals("04888053685", output.getCpf());
        assertEquals(LocalDate.of(1991, 02, 24), output.getBirth());
        assertEquals("3436834703", output.getPhoneNumber());
        assertEquals("34988681043", output.getMobileNumber());
        assertEquals("04888053685", output.getKeyPix());
        assertEquals(true, output.getEnabled()); 
    }

    @Test
    public void parserDtoListToEntityListTest() {
    	
        List<Person> outputList = inputObject.mockDtoList().stream()
                .map(request -> modelMapper.map(request, Person.class))
                .collect(Collectors.toList());
        
        Person outputZero = outputList.get(0);
        
        assertEquals(Long.valueOf(0L), outputZero.getId());
        assertEquals("First Name Test0", outputZero.getFirstName());
        assertEquals("Last Name Test0", outputZero.getLastName());
        assertEquals("Male", outputZero.getGender());
        assertEquals("04888053685", outputZero.getCpf());
        assertEquals(LocalDate.of(1991, 02, 24), outputZero.getBirth());
        assertEquals("3436834703", outputZero.getPhoneNumber());
        assertEquals("34988681043", outputZero.getMobileNumber());
        assertEquals("04888053685", outputZero.getKeyPix());
        assertEquals(true, outputZero.getEnabled()); 
        
        Person outputSeven = outputList.get(7);
        
        assertEquals(Long.valueOf(7L), outputSeven.getId());
        assertEquals("First Name Test7", outputSeven.getFirstName());
        assertEquals("Last Name Test7", outputSeven.getLastName());
        assertEquals("Female", outputSeven.getGender());
        assertEquals("04888053685", outputSeven.getCpf());
        assertEquals(LocalDate.of(1991, 02, 24), outputSeven.getBirth());
        assertEquals("3436834703", outputSeven.getPhoneNumber());
        assertEquals("34988681043", outputSeven.getMobileNumber());
        assertEquals("04888053685", outputSeven.getKeyPix());
        assertEquals(true, outputSeven.getEnabled()); 
        
        Person outputTwelve = outputList.get(12);
        
        assertEquals(Long.valueOf(12L), outputTwelve.getId());
        assertEquals("First Name Test12", outputTwelve.getFirstName());
        assertEquals("Last Name Test12", outputTwelve.getLastName());
        assertEquals("Male", outputTwelve.getGender());
        assertEquals("04888053685", outputTwelve.getCpf());
        assertEquals(LocalDate.of(1991, 02, 24), outputTwelve.getBirth());
        assertEquals("3436834703", outputTwelve.getPhoneNumber());
        assertEquals("34988681043", outputTwelve.getMobileNumber());
        assertEquals("04888053685", outputTwelve.getKeyPix());
        assertEquals(true, outputTwelve.getEnabled()); 
    }
}
