package br.com.xfrontier.book.converter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import br.com.xfrontier.book.core.enums.UserType;

public class IntegerToUserTypeConverter implements Converter<Integer, UserType> {

    @Override
    public UserType convert(MappingContext<Integer, UserType> context) {
        Integer source = context.getSource();
        return source == null ? null : UserType.fromId(source);
    }
}
