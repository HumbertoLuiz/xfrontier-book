package br.com.xfrontier.book.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.com.xfrontier.book.converter.YamlJackson2HttpMessageConverter;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

	private static final MediaType MEDIA_TYPE_APPLICATION_YML = MediaType.valueOf("application/x-yaml");
	private static final MediaType MULTIPART_FORM_DATA = MediaType.valueOf("multipart/form-data");
	
	@Value("${cors.originPatterns:default}")
	private String corsOriginPatterns = "";
	
	
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new YamlJackson2HttpMessageConverter());
	}
	
	public void addCorsMappings(CorsRegistry registry) {
		var allowedOrigins = corsOriginPatterns.split(",");
		registry.addMapping("/**")
			//.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT");
			.allowedMethods("*")
			.allowedOrigins(allowedOrigins)
			.allowCredentials(true);
	}
	
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
	    configurer
	        .favorParameter(false)
	        .ignoreAcceptHeader(false)
	        .useRegisteredExtensionsOnly(false)
	        .defaultContentType(MediaType.APPLICATION_JSON)
	        .mediaType("json", MediaType.APPLICATION_JSON)
	        .mediaType("xml", MediaType.APPLICATION_XML)
	        .mediaType("x-yaml", MEDIA_TYPE_APPLICATION_YML)
	        .mediaType("multipart", MULTIPART_FORM_DATA);
	}
	
}
