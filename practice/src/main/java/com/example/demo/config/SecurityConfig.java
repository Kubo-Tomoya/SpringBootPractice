package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	    	.csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/admin/signup", "/admin/signin", "/css/**").permitAll()
	            .requestMatchers("/contact/**").permitAll()
	            .requestMatchers("/admin/**").authenticated()
	        )
	        .formLogin(form -> form
	            .loginPage("/admin/signin")
	            .loginProcessingUrl("/admin/signin")
	            .defaultSuccessUrl("/admin/contacts",true) 
	            .permitAll()
	        )
	        .logout(logout -> logout
	            .logoutUrl("/admin/signout")
	            .logoutSuccessUrl("/admin/signin")
	        );

	    return http.build();
	}


	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder(); 
	    }
	    
	}