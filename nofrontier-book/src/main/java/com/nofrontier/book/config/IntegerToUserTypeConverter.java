package com.nofrontier.book.config;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import com.nofrontier.book.core.enums.UserType;

public class IntegerToUserTypeConverter implements Converter<Integer, UserType> {

    @Override
    public UserType convert(MappingContext<Integer, UserType> context) {
        Integer source = context.getSource();
        return source == null ? null : UserType.fromId(source);
    }
}
