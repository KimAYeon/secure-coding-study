package com.security.session;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.security.session.filter.AuthenticationFilter;
import com.security.session.filter.AuthorizationFilter;

@Configuration
@ComponentScan
public class WebMvcConfig implements WebMvcConfigurer {

//	@Autowired
//	CertificationInterceptor certificationInterceptor;

//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(certificationInterceptor).addPathPatterns("/*").excludePathPatterns("/main")
//				.excludePathPatterns("/auth/*").excludePathPatterns("/error/*");
//	}
	
	@Bean
	public AuthorizationFilter authorizationFilter() {
		AuthorizationFilter authorizationFilter = new AuthorizationFilter();
		return authorizationFilter;
	}

	@Bean
	public FilterRegistrationBean<AuthenticationFilter> authenticationFilter() {
		System.out.println("FilterRegistrationBean authenticationFilter");
		FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<AuthenticationFilter>(
				new AuthenticationFilter());
		registrationBean.setOrder(Integer.MAX_VALUE-1);
		registrationBean.addUrlPatterns("/member");
		registrationBean.addUrlPatterns("/admin");
		return registrationBean;
	}
	
	@Bean
	public FilterRegistrationBean<AuthorizationFilter> authorizationFilterBean() {
		System.out.println("FilterRegistrationBean authorizationFilter");
		FilterRegistrationBean<AuthorizationFilter> registrationBean = new FilterRegistrationBean<AuthorizationFilter>(
				authorizationFilter());
		registrationBean.setOrder(Integer.MAX_VALUE);
		registrationBean.addUrlPatterns("/member");
		registrationBean.addUrlPatterns("/admin");
		return registrationBean;
	}

	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setBasenames("/WEB-INF/messages");
		messageSource.setCacheSeconds(5);
		return messageSource;
	}

//	@Bean("menuServiceImpl")
//	public MenuServiceImpl menuServiceImpl() {
//		MenuServiceImpl menuServiceImpl = new MenuServiceImpl();
//		return menuServiceImpl;
//	}
}
