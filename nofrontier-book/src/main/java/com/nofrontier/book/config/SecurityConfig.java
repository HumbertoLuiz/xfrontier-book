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

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationEntryPoint authenticationEntryPoint;

	@Autowired
	private AccessDeniedHandler accessDeniedHandler;

	@Autowired
	private AccessTokenRequestFilter accessTokenRequestFilter;

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

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.authorizeHttpRequests(
				authorizeHttpRequestsCustomizer -> authorizeHttpRequestsCustomizer.anyRequest().permitAll());
		
//		http.authorizeHttpRequests(
//				requestMatcherCustomizer -> requestMatcherCustomizer
//						.requestMatchers("/**").permitAll())
//				.authorizeHttpRequests(
//						authorizeRequestsCustomizer -> authorizeRequestsCustomizer
//								.requestMatchers("/**").permitAll()
//								.requestMatchers(HttpMethod.POST,
//										"/auth/token")
//								.permitAll()
//								.requestMatchers("/css/**", "/js/**", "/img/**",
//										"/lib/**", "/favicon.ico")
//								.permitAll()
//								.requestMatchers("/auth/refresh/**",
//										"/swagger-ui/**", "/v3/api-docs/**")
//								.permitAll().anyRequest().authenticated());

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
		http.httpBasic(AbstractHttpConfigurer::disable);
		http.csrf(AbstractHttpConfigurer::disable);
		http.cors(AbstractHttpConfigurer::disable);
		return http.build();
	}

}
