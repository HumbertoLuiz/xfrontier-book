package com.nofrontier.book;

//import java.util.Base64;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.nofrontier.book.infrastructure.repository.CustomJpaRepositoryImpl;

//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;

@EnableConfigurationProperties
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
    "com.nofrontier.book.infrastructure.service"
})
@EnableJpaRepositories(basePackages = "com.nofrontier.book.domain.repository", repositoryBaseClass = CustomJpaRepositoryImpl.class)
@SpringBootApplication(exclude = { 
    SecurityAutoConfiguration.class, 
    ManagementWebSecurityAutoConfiguration.class, 
    UserDetailsServiceAutoConfiguration.class 
}, scanBasePackages = {
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
    "com.nofrontier.book.infrastructure.service"
})
public class NofrontierBookApplication {

	public static void main(String[] args) {
		SpringApplication.run(NofrontierBookApplication.class, args);

//        var key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // or HS512, HS384
//        var base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
//        System.out.println(base64Key);
	}

}
