package com.nofrontier.book;

//import java.util.HashMap;
//import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
//import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm;

import com.nofrontier.book.infrastructure.repository.CustomJpaRepositoryImpl;

@ComponentScan({
		"com.nofrontier.book", 
		"com.nofrontier.book.config",
		"com.nofrontier.book.api.v1.controller",
		"com.nofrontier.book.domain.model",
		"com.nofrontier.book.domain.repository",
		"com.nofrontier.book.domain.services",
		"com.nofrontier.book.core.modelMapper",
		"com.nofrontier.book.core.services",
		"com.nofrontier.book.dto.v1.requests",
		"com.nofrontier.book.dto.v1.responses",
		"com.nofrontier.book.infrastructure.repository",
		"com.nofrontier.book.infrastructure.service"})
@EnableJpaRepositories(basePackages = "com.nofrontier.book.domain.repository", repositoryBaseClass = CustomJpaRepositoryImpl.class)
@SpringBootApplication(scanBasePackages = {
		"com.nofrontier.book",
		"com.nofrontier.book.config", 
		"com.nofrontier.book.api.v1.controller",
		"com.nofrontier.book.domain.model",
		"com.nofrontier.book.domain.repository",
		"com.nofrontier.book.domain.services",
		"com.nofrontier.book.core.modelMapper",
		"com.nofrontier.book.core.services",
		"com.nofrontier.book.dto.v1.requests",
		"com.nofrontier.book.dto.v1.responses",
		"com.nofrontier.book.infrastructure.repository",
		"com.nofrontier.book.infrastructure.service"})
public class NofrontierBookApplication {

	public static void main(String[] args) {
		SpringApplication.run(NofrontierBookApplication.class, args);

//		 Map<String, PasswordEncoder> encoders = new HashMap<>();		 
//		 Pbkdf2PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder( "", 8, 185000, SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
//		 
//		 encoders.put("pbkdf2", pbkdf2Encoder); 
//		 DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
//		 passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
//		 
//		 String result1 = passwordEncoder.encode("admin123"); 
//		 String result2 = passwordEncoder.encode("admin234");
//		 
//		 System.out.println("My hash result1 " + result1);
//		 System.out.println("My hash result2 " + result2);
		 
	}
}
