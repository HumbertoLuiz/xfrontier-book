package com.nofrontier.book.integrationstests.controller.withxml;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.nofrontier.book.configs.TestConfigs;
import com.nofrontier.book.integrationstests.dto.RefreshRequest;
import com.nofrontier.book.integrationstests.dto.TokenRequest;
import com.nofrontier.book.integrationstests.dto.TokenResponse;
import com.nofrontier.book.integrationstests.testcontainers.AbstractIntegrationTest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "classpath:application-test.yml")
@TestMethodOrder(OrderAnnotation.class)
@ActiveProfiles("test")
public class AuthControllerXmlTest extends AbstractIntegrationTest {

	private static TokenResponse tokenResponse;
	
    @Test
    @Order(1)
    public void testSignin() throws JsonMappingException, JsonProcessingException {
    	TokenRequest user = new TokenRequest("test@mail.com", "test@123");
        
		RequestSpecification specification = new RequestSpecBuilder()
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
			.build();
		
		var response = given().spec(specification)
				.basePath("/auth/token")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_XML)
				.body(user)
					.when()
				.post()
					.then()
	                .assertThat()
	                .statusCode(anyOf(is(200), is(304), is(401), is(403), is(404), is(500)))
	                .extract()
	                .response(); // Extract the full response
	        
	        // Log the response for debugging
	        System.out.println("Response: " + response.asString());
	        
	        tokenResponse = response.as(TokenResponse.class);
	        
	        assertNotNull(tokenResponse);
	        assertNotNull(tokenResponse.getAccess());
	        assertNotNull(tokenResponse.getRefresh());
	}
	
	@Test
	@Order(2)
	public void testRefresh() throws JsonMappingException, JsonProcessingException {
		
        assertNotNull(tokenResponse);
        assertNotNull(tokenResponse.getAccess());
        assertNotNull(tokenResponse.getRefresh());
        
        RequestSpecification specification = new RequestSpecBuilder()
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
        
        RefreshRequest refreshRequest = new RefreshRequest();
        refreshRequest.setRefresh(tokenResponse.getRefresh());
        
        TokenResponse newTokenResponse = given().spec(specification)
                .basePath("/auth/refresh")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .body(refreshRequest)  // Sending the refresh token in the request body
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(anyOf(is(200), is(401), is(403), is(404), is(500)))
                .extract()
                .body()
                .as(TokenResponse.class);

        assertNotNull(newTokenResponse);
        assertNotNull(newTokenResponse.getAccess());
        assertNotNull(newTokenResponse.getRefresh());
    }
}
