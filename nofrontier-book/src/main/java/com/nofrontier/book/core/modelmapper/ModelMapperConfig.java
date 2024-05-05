package com.nofrontier.book.core.modelmapper;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nofrontier.book.domain.model.Person;
import com.nofrontier.book.dto.v1.PersonDto;

@Configuration
public class ModelMapperConfig {

	@Bean
	ModelMapper modelMapper() {
		var modelMapper = new ModelMapper();
		return modelMapper;
	}

	private static final ModelMapper mapper = new ModelMapper();

	static {
		mapper.createTypeMap(
			Person.class, 
			PersonDto.class)
		.addMapping(Person::getId, PersonDto::setKey);
		
		mapper.createTypeMap(
				PersonDto.class, 
				Person.class)
			.addMapping(PersonDto::getKey, Person::setId);
	}
	
	public static <O, D> D parseObject(O origin, Class<D> destination) {
		return mapper.map(origin, destination);
	}

	public static <O, D> List<D> parseListObjects(List<O> origin, Class<D> destination) {
		List<D> destinationObjects = new ArrayList<D>();
		for (O o : origin) {
			destinationObjects.add(mapper.map(o, destination));
		}
		return destinationObjects;
	}
}
