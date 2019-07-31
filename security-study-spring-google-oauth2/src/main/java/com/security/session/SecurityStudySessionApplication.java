package com.security.session;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

import com.security.session.exception.CustomException;

@SpringBootApplication
public class SecurityStudySessionApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityStudySessionApplication.class, args);
	}

	@Bean
	public ErrorPageRegistrar errorPageRegistrar() {
		return new ErrorPageRegistrar() {
			@Override
			public void registerErrorPages(ErrorPageRegistry epr) {
				epr.addErrorPages(new ErrorPage(HttpStatus.UNAUTHORIZED, "/error"));
				epr.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/error"));
			}
		};
	}
	
}
