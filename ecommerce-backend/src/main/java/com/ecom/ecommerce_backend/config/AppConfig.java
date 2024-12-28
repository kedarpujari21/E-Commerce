package com.ecom.ecommerce_backend.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class AppConfig {
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception 
    {
		// we are storing Jwt token into localstorage using the below line.
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		//Any request starting with /api/ is authenticated.
		.authorizeHttpRequests(Authorize -> Authorize.requestMatchers("/api/**").authenticated().anyRequest().permitAll())
		.addFilterBefore(new JwtValidator(), BasicAuthenticationFilter.class)
		.csrf().disable()
		.cors().configurationSource(new CorsConfigurationSource() {
			
			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				CorsConfiguration cfg = new CorsConfiguration();
				
				//Api points from which backend should be accessible.
				cfg.setAllowedOrigins(Arrays.asList(
						"http://localhost:3000"
						));
				
				cfg.setAllowedMethods(Collections.singletonList("*")); // allowing all the methods like get,put,post,delete etc.
				cfg.setAllowCredentials(true);
				cfg.setAllowedHeaders(Collections.singletonList("*")); // allowing all the headers.
				cfg.setExposedHeaders(Arrays.asList("Authorization"));
				cfg.setMaxAge(3600L);
				return cfg;
			}
		})
		.and().httpBasic().and().formLogin();
    	return http.build();
    }
	
	// Method for Password encrypter.. converting the pasword into hash before storing into database 
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
}
