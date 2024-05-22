package com.nofrontier.book.core.modelmapper;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nofrontier.book.core.enums.UserType;
import com.nofrontier.book.domain.model.Person;
import com.nofrontier.book.dto.v1.PersonDto;

@Configuration
public class ModelMapperConfig {

    @Bean
    ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                   .setFieldMatchingEnabled(true)
                   .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                   .setMatchingStrategy(MatchingStrategies.STRICT);

        // Ignorar coleções não inicializadas
        modelMapper.getConfiguration()
                   .setPropertyCondition(context -> {
                       Object source = context.getSource();
                       if (source instanceof org.hibernate.collection.spi.PersistentCollection) {
                           return ((org.hibernate.collection.spi.PersistentCollection<?>) source).wasInitialized();
                       }
                       return true;
                   });
        
        // Configurar o conversor UserType para Integer
        Converter<UserType, Integer> userTypeToIntegerConverter = ctx -> ctx.getSource().getId();
        modelMapper.createTypeMap(UserType.class, Integer.class);
        modelMapper.addConverter(userTypeToIntegerConverter);
        
//        // Adicionando o conversor genérico para ID para entidade
//        modelMapper.addConverter(new IdToEntityConverter());
        
        return modelMapper;
    }


    private static final ModelMapper mapper = new ModelMapper();

    static {
        mapper.createTypeMap(Person.class, PersonDto.class)
              .addMapping(Person::getId, PersonDto::setKey);

        mapper.createTypeMap(PersonDto.class, Person.class)
              .addMapping(PersonDto::getKey, Person::setId);
    }

    public static <O, D> D parseObject(O origin, Class<D> destination) {
        return mapper.map(origin, destination);
    }

    public static <O, D> List<D> parseListObjects(List<O> origin, Class<D> destination) {
        List<D> destinationObjects = new ArrayList<>();
        for (O o : origin) {
            destinationObjects.add(mapper.map(o, destination));
        }
        return destinationObjects;
    }
}

