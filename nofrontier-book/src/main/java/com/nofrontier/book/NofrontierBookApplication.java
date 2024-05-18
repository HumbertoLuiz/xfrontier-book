package com.nofrontier.book;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.nofrontier.book.infrastructure.repository.CustomJpaRepositoryImpl;

@EnableJpaRepositories(
	    basePackages = "com.nofrontier.book.domain.repository",
	    repositoryBaseClass = CustomJpaRepositoryImpl.class
	)
@SpringBootApplication(scanBasePackages = {
        "com.br.nofrontier.book.api.v1.controller",
        "com.br.nofrontier.book.config",
        "com.br.nofrontier.book.domain.model",
        "com.br.nofrontier.book.domain.repository",
        "com.br.nofrontier.book.domain.services",
        "com.br.nofrontier.book.infrastructure.repository",
        "com.br.nofrontier.book.infrastructure.service"})
public class NofrontierBookApplication {

	public static void main(String[] args) {
		SpringApplication.run(NofrontierBookApplication.class, args);
	}

}
