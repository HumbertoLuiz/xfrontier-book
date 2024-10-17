//package br.com.xfrontier.book.converter;
//
//import java.util.Map;
//
//import org.modelmapper.Converter;
//import org.modelmapper.spi.MappingContext;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Component;
//
//import br.com.xfrontier.book.domain.model.IdBaseEntity;
//
//@Component
//public class IdToEntityConverter implements Converter<Long, IdBaseEntity> {
//
//	@Autowired
//	private Map<Class<?>, JpaRepository<?, Long>> repositories;
//
//	public IdBaseEntity convert(MappingContext<Long, IdBaseEntity> context) {
//		Long id = context.getSource();
//		Class<?> destinationType = context.getDestinationType();
//		JpaRepository<?, Long> repository = repositories.get(destinationType);
//		if (repository != null) {
//			return (IdBaseEntity) repository.findById(id).orElse(null);
//		}
//		return null;
//	}
//}
