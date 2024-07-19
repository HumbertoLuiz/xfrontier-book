package com.nofrontier.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

//import java.util.Base64;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nofrontier.book.domain.model.User;
import com.nofrontier.book.core.enums.UserType;
import com.nofrontier.book.domain.repository.UserRepository;
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
    "com.nofrontier.book.infrastructure.service"
})
public class NofrontierBookApplication {

	public static void main(String[] args) {
		SpringApplication.run(NofrontierBookApplication.class, args);

//        var key = Keys.secretKeyFor(SignatureAlgorithm.HS512); // or HS512, HS384
//        var base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
//        System.out.println(base64Key);
	}
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
    @Bean
    CommandLineRunner init(UserRepository userRepository) {
        return args -> {
            User user = new User();
            user.setEmail("test@mail.com");
            user.setPassword(passwordEncoder.encode("test@123"));
            user.setCompleteName("Test");
            user.setUserType(UserType.ADMIN);
            user.setEnabled(true);
            // Preencha os outros campos necessários
            userRepository.save(user);
        };
    }

}
