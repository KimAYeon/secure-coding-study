package com.security.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.security.session.filter.AuthFailureHandler;
import com.security.session.filter.AuthSuccessHandler;
import com.security.session.filter.CustomAccessDeniedHandler;
import com.security.session.filter.JwtAuthenticationFilter;
import com.security.session.filter.JwtTokenFilter;
import com.security.session.filter.RestAuthenticationEntryPoint;
import com.security.session.spring.AuthProvider;
import com.security.session.spring.JwtTokenProvider;
import com.security.session.spring.UserDetailServiceImpl;

@Configuration
@EnableWebSecurity
@ComponentScan
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	 @Autowired
	 private JwtTokenProvider jwtTokenProvider;
	 
	 @Autowired
	 private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

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
	public AuthenticationFailureHandler authenticationFailureHandler() {
		return new AuthFailureHandler();
	}
	
	@Bean
	public AuthSuccessHandler customAuthenticationSuccessHandler() {
		return new AuthSuccessHandler();
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
		    .antMatchers("/auth/**").permitAll()
			.antMatchers("/member").hasAnyRole("ADMIN", "MEMBER")
			.antMatchers("/admin").hasRole("ADMIN")
			.anyRequest().authenticated()
		.and()
			.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"))
			.logoutSuccessUrl("/main")
		.and()
			.exceptionHandling()
			.authenticationEntryPoint(restAuthenticationEntryPoint)
			.accessDeniedHandler(accessDeniedHandler())
//		.and()
//			.httpBasic()
		.and()
			.cors()
		.and()
			.csrf()
			.disable()
			.addFilter(jwtTokenFilter())
			.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
		JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManagerBean());
		jwtAuthenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/auth/login", "POST"));
		return jwtAuthenticationFilter;
	}
	
	@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());

        return source;
    }
	
	@Bean
    public JwtTokenFilter jwtTokenFilter() throws Exception {
		JwtTokenFilter jwtTokenFilter = new JwtTokenFilter(authenticationManagerBean(), jwtTokenProvider);
        return jwtTokenFilter;
    }

}
