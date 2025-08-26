package com.Springboot.Security_client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(11);
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
	    httpSecurity.cors(cors -> cors.disable())
	                .csrf(csrf -> csrf.disable())
	                .authorizeHttpRequests(auth -> auth
	                    .requestMatchers("/register","/verifyRegistration/**", "/resendVerify/**",
	                                     "/resetPassword/**","/savePassword/**", "/changePassword/**").permitAll()
	                    .requestMatchers("/api/**").authenticated()
	                )
	                .oauth2Login(oauth2login -> oauth2login
	                    .loginPage("/oauth2/authorization/api-client-oidc")
	                )
	                .oauth2Client(Customizer.withDefaults());

	    return httpSecurity.build();
	}

}