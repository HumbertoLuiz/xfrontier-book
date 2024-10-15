package com.nofrontier.book.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nofrontier.book.api.exceptionhandler.TokenAccessDeniedHandler;
import com.nofrontier.book.api.exceptionhandler.TokenAuthenticationEntryPoint;
import com.nofrontier.book.core.services.token.jwt.JwtTokenFilter;
import com.nofrontier.book.core.services.token.jwt.JwtTokenProvider;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
    private TokenAuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
	private TokenAccessDeniedHandler accessDeniedHandler;
    
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Bean
    PasswordEncoder passwordEncoder() {
        Pbkdf2PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder(
            "",  // Salt is managed internally by the encoder
            8,   // Hashing iterations
            185000,  // Iterations
            Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256
        );

        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("pbkdf2", pbkdf2Encoder);

        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);

        return passwordEncoder;
    }
	
	@Bean
	AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

    private static final String[] SWAGGER_WHITELIST = {
        "/v3/api-docs/**", "/swagger-resources/**", "/configuration/ui/**",
        "/configuration/security/**", "/swagger-ui.html/**", "/swagger-ui/**",
        "/webjars/**"
    };

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	
    	JwtTokenFilter customFilter = new JwtTokenFilter(tokenProvider);
    	
        http
        	.httpBasic(AbstractHttpConfigurer::disable)
        	.csrf(AbstractHttpConfigurer::disable)
        
        	.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class)
        	.exceptionHandling(exceptionHandlingCustomizer -> exceptionHandlingCustomizer
                	.authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler))
        
        	.sessionManagement(sessionManagementCustomizer -> sessionManagementCustomizer
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        
        	.authorizeHttpRequests(
                authorizeHttpRequests -> authorizeHttpRequests
                    .requestMatchers(
						"/auth/signin",
						"/auth/refresh/**",
						"/auth/logout").permitAll()
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/lib/**", "/favicon.ico").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .requestMatchers("/users").denyAll())

        	.cors(cors -> {});
        
        return http.build();
    }
}
