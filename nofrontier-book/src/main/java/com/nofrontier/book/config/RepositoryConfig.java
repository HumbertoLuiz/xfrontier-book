//package com.nofrontier.book.config;
//
//import java.lang.reflect.ParameterizedType;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//@Configuration
//public class RepositoryConfig {
//
//	@Autowired
//	private List<JpaRepository<?, Long>> repositories;
//
//	@Bean
//	Map<Class<?>, JpaRepository<?, Long>> repositoryMap() {
//		Map<Class<?>, JpaRepository<?, Long>> map = new HashMap<>();
//		for (JpaRepository<?, Long> repository : repositories) {
//			Class<?> domainClass = (Class<?>) ((ParameterizedType) repository
//					.getClass().getGenericInterfaces()[0])
//					.getActualTypeArguments()[0];
//			map.put(domainClass, repository);
//		}
//		return map;
//	}
//}
