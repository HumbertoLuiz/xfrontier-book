package com.nofrontier.book;

//import java.util.Base64;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.nofrontier.book.infrastructure.repository.CustomJpaRepositoryImpl;

//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;

@EnableJpaRepositories(basePackages = "com.nofrontier.book.domain.repository", repositoryBaseClass = CustomJpaRepositoryImpl.class)
@SpringBootApplication(scanBasePackages = {"com.nofrontier.book",
		"com.nofrontier.book.api.v1.controller", "com.nofrontier.book.config",
		"com.nofrontier.book.domain.model",
		"com.nofrontier.book.domain.repository",
		"com.nofrontier.book.domain.services",
		"com.nofrontier.book.core.services",
		"com.nofrontier.book.infrastructure.repository",
		"com.nofrontier.book.infrastructure.service"})
public class NofrontierBookApplication {

	public static void main(String[] args) {
		SpringApplication.run(NofrontierBookApplication.class, args);

//        var key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // or HS512, HS384
//        var base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
//        System.out.println(base64Key);
	}

}
