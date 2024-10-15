package com.nofrontier.book.core.modelmapper;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nofrontier.book.converter.IntegerToUserTypeConverter;
import com.nofrontier.book.core.enums.UserType;
import com.nofrontier.book.domain.model.OrderItem;
import com.nofrontier.book.dto.v1.OrderItemDto;

@Configuration
public class ModelMapperConfig {

	@Bean
	ModelMapper modelMapper() {
		
		ModelMapper modelMapper = new ModelMapper();
		
		modelMapper.addConverter(new IntegerToUserTypeConverter());
		
		modelMapper.getConfiguration().setFieldMatchingEnabled(true)
				.setFieldAccessLevel(
						org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
				.setMatchingStrategy(MatchingStrategies.STRICT);

		// Ignorar coleções não inicializadas
		modelMapper.getConfiguration().setPropertyCondition(context -> {
			Object source = context.getSource();
			if (source instanceof org.hibernate.collection.spi.PersistentCollection) {
				return ((org.hibernate.collection.spi.PersistentCollection<?>) source)
						.wasInitialized();
			}
			return true;
		});

		// Configurar o conversor UserType para Integer
		Converter<UserType, Integer> userTypeToIntegerConverter = ctx -> ctx
				.getSource().getId();
		modelMapper.createTypeMap(UserType.class, Integer.class);
		modelMapper.addConverter(userTypeToIntegerConverter);
		
		modelMapper.createTypeMap(OrderItemDto.class, OrderItem.class)
		.addMappings(mapper -> mapper.skip(OrderItem::setId));

		return modelMapper;
	}

}
