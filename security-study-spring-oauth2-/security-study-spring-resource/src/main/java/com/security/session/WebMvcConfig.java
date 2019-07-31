package com.security.session;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.security.session.spring.MessageUtils;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

//	@Autowired
//	CertificationInterceptor certificationInterceptor;
//
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(certificationInterceptor)
//			.addPathPatterns("/*")
//			.excludePathPatterns("/main")
//			.excludePathPatterns("/auth/*")
//			.excludePathPatterns("/error/*");
//	}

	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setBasenames("/WEB-INF/messages");
		messageSource.setCacheSeconds(5);
		return messageSource;
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST", "PUT", "DELETE")
				.allowedHeaders("*").allowCredentials(true).maxAge(3600);
	}

	@Bean
	public MessageSourceAccessor messageSourceAccessor() {
		return new MessageSourceAccessor(messageSource());
	}

	@Bean
	public MessageUtils messageUtils() {
		MessageUtils messageUtils = new MessageUtils();
		messageUtils.setMessageSource(messageSourceAccessor());
		return messageUtils;
	}

	@Bean
	public DataSource dataSource() {
	    DriverManagerDataSource dataSource = new DriverManagerDataSource();
	 
	    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
	    dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/sys?characterEncoding=UTF-8&serverTimezone=UTC");
	    dataSource.setUsername("root");
	    dataSource.setPassword("root");
	    return dataSource;
	}
}
