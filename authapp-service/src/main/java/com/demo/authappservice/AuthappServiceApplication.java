package com.demo.authappservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class AuthappServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthappServiceApplication.class, args);
	}

	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AuthappServiceApplication.class);
	}
}
