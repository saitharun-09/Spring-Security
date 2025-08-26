package com.SpringSecurity.OAuth_Authorization_Server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.SpringSecurity.OAuth_Authorization_Server.service.CustomAuthenticationProvider;

@EnableWebSecurity
public class DefaultSecurityConfig {

	@Autowired
	private CustomAuthenticationProvider customAuthenticationProvider;
	
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		
		http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
		        							   .formLogin(Customizer.withDefaults());
		    return http.build();
		}

	@Autowired
	public void bindAuthenticationProvider(AuthenticationManagerBuilder authenticationManagerBuilder) {
		authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider);
	}
}