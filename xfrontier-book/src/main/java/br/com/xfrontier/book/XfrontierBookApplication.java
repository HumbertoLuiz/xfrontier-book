package br.com.xfrontier.book;

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

import br.com.xfrontier.book.infrastructure.repository.CustomJpaRepositoryImpl;

@ComponentScan({
		"br.com.xfrontier.book", 
		"br.com.xfrontier.book.config",
		"br.com.xfrontier.book.api.v1.controller",
		"br.com.xfrontier.book.domain.model",
		"br.com.xfrontier.book.domain.repository",
		"br.com.xfrontier.book.domain.services",
		"br.com.xfrontier.book.core.modelMapper",
		"br.com.xfrontier.book.core.services",
		"br.com.xfrontier.book.dto.v1.requests",
		"br.com.xfrontier.book.dto.v1.responses",
		"br.com.xfrontier.book.infrastructure.repository",
		"br.com.xfrontier.book.infrastructure.service"})
@EnableJpaRepositories(basePackages = "br.com.xfrontier.book.domain.repository", repositoryBaseClass = CustomJpaRepositoryImpl.class)
@SpringBootApplication(scanBasePackages = {
		"br.com.xfrontier.book",
		"br.com.xfrontier.book.config", 
		"br.com.xfrontier.book.api.v1.controller",
		"br.com.xfrontier.book.domain.model",
		"br.com.xfrontier.book.domain.repository",
		"br.com.xfrontier.book.domain.services",
		"br.com.xfrontier.book.core.modelMapper",
		"br.com.xfrontier.book.core.services",
		"br.com.xfrontier.book.dto.v1.requests",
		"br.com.xfrontier.book.dto.v1.responses",
		"br.com.xfrontier.book.infrastructure.repository",
		"br.com.xfrontier.book.infrastructure.service"})
public class XfrontierBookApplication {

	public static void main(String[] args) {
		SpringApplication.run(XfrontierBookApplication.class, args);

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
