package com.demo.authappservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.demo.authappservice.jwt.JwtTokenConfigurer;
import com.demo.authappservice.jwt.JwtTokenProvider;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

	@Autowired
	private JwtTokenProvider tokenProvider;

	protected void configure(HttpSecurity http) throws Exception {
		// Entry points
		http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeHttpRequests()
				// .mvcMatchers("/api/**").permitAll()
				.requestMatchers("/api/manage/user/**").permitAll().requestMatchers("/api/user/refreshjwttoken/**")
				.hasAnyRole("USER", "ADMIN").requestMatchers("/api/admin/**").hasRole("ADMIN")
				.requestMatchers("/api/user/**").hasRole("USER").anyRequest().authenticated();

		http.apply(new JwtTokenConfigurer(tokenProvider));
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}
}
