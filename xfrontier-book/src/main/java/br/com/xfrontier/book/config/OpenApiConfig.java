package br.com.xfrontier.book.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("SALES BOOK API")
                        .version("v1")
                        .description("REST API for sales book management")
                        .termsOfService("https://xfrontier.com.br/doc")
//                        .contact(new Contact()
//                                .name("XXXXXXXXX")
//                                .url("XXXXXXXX")
//                                .email("XXXXXXXX"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.com"))
                        ).externalDocs(new ExternalDocumentation()
                                .description("NoFrontier")
                                .url("https://xfrontier.com.br/doc"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }

    @Bean
    GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("v1")
                .pathsToMatch("/api/**")
                .build();
    }
}
