package com.nofrontier.book.integrationtests.swagger;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.nofrontier.book.configs.TestConfigs;
import com.nofrontier.book.integrationtests.testcontainers.AbstractIntegrationTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTest extends AbstractIntegrationTest {
	
	@Test
	public void shouldDisplaySwaggerUiPage() {
		var content =
		given()
		.basePath("swagger-ui/index.html")
		.port(TestConfigs.SERVER_PORT)
		.when()
			.get()
		.then()
		.assertThat()
			.statusCode(anyOf(is(200), is(304), is(401), is(403), is(404), is(500)))
		.extract()
			.body()
				.asString();
		Assertions.assertTrue(content.contains("Swagger UI"));
	}
}
