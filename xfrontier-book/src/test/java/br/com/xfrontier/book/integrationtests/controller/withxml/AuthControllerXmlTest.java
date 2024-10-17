package br.com.xfrontier.book.integrationtests.controller.withxml;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import br.com.xfrontier.book.configs.TestConfigs;
import br.com.xfrontier.book.dto.v1.TokenDto;
import br.com.xfrontier.book.integrationtests.dto.AccountCredentialsDto;
import br.com.xfrontier.book.integrationtests.testcontainers.AbstractIntegrationTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class AuthControllerXmlTest extends AbstractIntegrationTest {

	private static TokenDto tokenDto;
	
	@Test
	@Order(1)
	public void testSignin() throws JsonMappingException, JsonProcessingException {
		
		AccountCredentialsDto user = 
				new AccountCredentialsDto("admin@mail.com", "admin123");
		
		tokenDto = given()
				.basePath("/auth/signin")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_XML)
				.body(user)
					.when()
				.post()
					.then()
						.statusCode(200)
							.extract()
							.body()
								.as(TokenDto.class);
		
		assertNotNull(tokenDto.getAccessToken());
		assertNotNull(tokenDto.getRefreshToken());
	}
	
	@Test
	@Order(2)
	public void testRefresh() throws JsonMappingException, JsonProcessingException {
		
		var newTokenVO = given()
				.basePath("/auth/refresh")
				.port(TestConfigs.SERVER_PORT)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
					.pathParam("email", tokenDto.getEmail())
					.header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDto.getRefreshToken())
				.when()
					.put("{email}")
				.then()
					.statusCode(200)
				.extract()
					.body()
						.as(TokenDto.class);
		
		assertNotNull(newTokenVO.getAccessToken());
		assertNotNull(newTokenVO.getRefreshToken());
	}
}
