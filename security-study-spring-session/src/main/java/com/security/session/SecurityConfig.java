package com.security.session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.security.session.spring.AuthFailureHandler;
import com.security.session.spring.AuthProvider;
import com.security.session.spring.AutheSuccessHandler;
import com.security.session.spring.CustomAccessDeniedHandler;
import com.security.session.spring.UserDetailServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	/*
	 * Spring Security가 사용자를 인증하는 방법이 담긴 객체
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailServiceImpl();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new AuthProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(encoder());
		return authProvider;
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder(11);
	}

	/*
	 * Spring Security Rule을 무시하는 경로
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/error/**");
	}

	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new AutheSuccessHandler();
	}

	@Bean
	public AuthenticationFailureHandler authenticationFailureHandler() {
		return new AuthFailureHandler();
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}

	/*
	 * Spring Security Rule
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/main").permitAll()
			.antMatchers("/member").hasAnyRole("ADMIN", "MEMBER")
			.antMatchers("/admin").hasRole("ADMIN")
			.antMatchers("/oauth/**", "/oauth2/callback").permitAll()
			.anyRequest().authenticated()
		.and()
			.formLogin()
			.loginPage("/main")
			.loginProcessingUrl("/login")
			.defaultSuccessUrl("/member")
			.successForwardUrl("/member")
//			.successHandler(authenticationSuccessHandler())
			.failureHandler(authenticationFailureHandler())
			.usernameParameter("id")
			.passwordParameter("password")
			.permitAll()
		.and()
			.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"))
			.logoutSuccessUrl("/main")
		.and()
			.exceptionHandling()
			.accessDeniedHandler(accessDeniedHandler())
		.and()
			.csrf()
			.disable()
			.headers().frameOptions().disable();
	}


}
