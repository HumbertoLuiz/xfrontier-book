package com.nofrontier.book.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

	@Bean
	OpenAPI customOpenApi() {
		return new OpenAPI()
			.info(new Info()
				.title("RESTFULL API BOOK")
				.version("v1")
				.description("Some description")
				.termsOfService("https://nofrontier.com.br/doc")
				.license(
					new License()
						.name("Apache 2.0")
						.url("https://nofrontier.com.br/doc")
					)
				);
	}
}
