package com.security.session.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

//	@Value("${security.jwt.resource-ids}")
//	private String resourceIds;

	/*
	 * @Value("${resource.id:spring-boot-application}") private String resourceId;
	 * 
	 * @Value("${spring.security.oauth2.resource.jwt.key-value}") private String
	 * publicKey;
	 */

//	@Override
//	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//		resources.resourceId(resourceIds);
//	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/member").access("#oauth2.hasScope('read')")
				.anyRequest().authenticated();
//		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and().authorizeRequests()
//				.anyRequest().permitAll();
	}

}
