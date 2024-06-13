package com.nofrontier.book.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nofrontier.book.core.filters.AccessTokenRequestFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	private final UserDetailsService userDetailsService;
	private final AuthenticationEntryPoint authenticationEntryPoint;
	private final AccessDeniedHandler accessDeniedHandler;
	private final AccessTokenRequestFilter accessTokenRequestFilter;

	public SecurityConfig(UserDetailsService userDetailsService,
			AuthenticationEntryPoint authenticationEntryPoint,
			AccessDeniedHandler accessDeniedHandler,
			AccessTokenRequestFilter accessTokenRequestFilter) {
		this.authenticationEntryPoint = authenticationEntryPoint;
		this.userDetailsService = userDetailsService;
		this.accessDeniedHandler = accessDeniedHandler;
		this.accessTokenRequestFilter = accessTokenRequestFilter;
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder);
	}

	@Bean
	AuthenticationManager authenticationManager(
			AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	private static final String[] SWAGGER_WHITELIST = {"/v3/api-docs/**",
			"/swagger-resources/**", "/configuration/ui/**",
			"/configuration/security/**", "/swagger-ui.html/**",
			"/swagger-ui/**", "/webjars/**"};

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.httpBasic(AbstractHttpConfigurer::disable);
		http.securityMatchers(
				requestMatcherCustomizer -> requestMatcherCustomizer
						.requestMatchers("/api/**", "/auth/**"));
		http.authorizeHttpRequests(
				authorizeHttpRequestsCustomizer -> authorizeHttpRequestsCustomizer
				//.anyRequest().permitAll()
						.requestMatchers(SWAGGER_WHITELIST).permitAll()

						.requestMatchers("/auth/token", "/auth/refresh")
						.permitAll()

						.requestMatchers("/css/**", "/js/**", "/img/**",
								"/lib/**", "/favicon.ico")
						.permitAll()

						.requestMatchers("/api/**").authenticated()
						.requestMatchers("/users").denyAll());

		http.csrf(AbstractHttpConfigurer::disable);
		
		// http.oauth2ResourceServer(
		// oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder())));

		http.sessionManagement(
				sessionManagementCustomizer -> sessionManagementCustomizer
						.sessionCreationPolicy(
								SessionCreationPolicy.STATELESS));

		http.addFilterBefore(accessTokenRequestFilter,
				UsernamePasswordAuthenticationFilter.class).exceptionHandling(
						exceptionHandlingCustomizer -> exceptionHandlingCustomizer
								.authenticationEntryPoint(
										authenticationEntryPoint)
								.accessDeniedHandler(accessDeniedHandler));
		http.cors(AbstractHttpConfigurer::disable);
		return http.build();
	}

	// @Bean
	// JwtDecoder jwtDecoder() {
	// // Configure the JwtDecoder with the necessary parameters.
	// // For example, using a public key or a JWK Set URL
	// String jwkSetUri = "https://example.com/.well-known/jwks.json";
	// return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
	// }
}
