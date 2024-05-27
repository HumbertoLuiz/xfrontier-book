package com.nofrontier.book.unittests.mapper.mocks;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.nofrontier.book.domain.model.Person;
import com.nofrontier.book.dto.v1.requests.PersonRequest;
import com.nofrontier.book.dto.v1.responses.PersonResponse;

public class MockPerson {

	public Person mockEntity() {
		return mockEntity(0);
	}

    public PersonRequest mockRequest() {
        return mockRequest(0);
    }
    
    public PersonResponse mockResponse(int i) {
        return mockResponse(0);
    }

	public List<Person> mockEntityList() {
		List<Person> persons = new ArrayList<Person>();
		for (int i = 0; i < 14; i++) {
			persons.add(mockEntity(i));
		}
		return persons;
	}
	
    public List<PersonRequest> mockRequestList() {
        List<PersonRequest> persons = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockRequest(i));
        }
        return persons;
    }

    public List<PersonResponse> mockResponseList() {
        List<PersonResponse> persons = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockResponse(i));
        }
        return persons;
    }

	public Person mockEntity(Integer number) {
		Person person = new Person();
		person.setId(number.longValue());
		person.setFirstName("First Name Test" + number);
		person.setLastName("Last Name Test" + number);
		person.setGender(((number % 2) == 0) ? "Male" : "Female");
		person.setCpf("Cpf Test" + number);
		person.setBirth(LocalDate.of(0, 0, 0));
		person.setPhoneNumber("Phone Number Test" + number);
		person.setMobileNumber("Mobile Number Test" + number);
		person.setKeyPix("Key Pix Test" + number);
		person.setEnabled(true);
		return person;
	}

    public PersonRequest mockRequest(Integer number) {
        PersonRequest person = new PersonRequest();
        person.setFirstName("First Name Test" + number);
        person.setLastName("Last Name Test" + number);
        person.setGender(((number % 2) == 0) ? "Male" : "Female");
        return person;
    }
	
	public PersonResponse mockDto(Integer number) {
		PersonResponse person = new PersonResponse();
		person.setKey(number.longValue());
		person.setFirstName("First Name Test" + number);
		person.setLastName("Last Name Test" + number);
		person.setGender(((number % 2) == 0) ? "Male" : "Female");
		person.setCpf("Cpf Test" + number);
		person.setBirth(LocalDate.of(0, 0, 0));
		person.setPhoneNumber("Phone Number Test" + number);
		person.setMobileNumber("Mobile Number Test" + number);
		person.setKeyPix("Key Pix Test" + number);
		person.setEnabled(true);
		return person;
	}
 
}