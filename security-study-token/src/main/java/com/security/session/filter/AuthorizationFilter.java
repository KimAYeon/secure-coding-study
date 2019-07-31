package com.security.session.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import com.security.session.model.Menu;
import com.security.session.model.User;
import com.security.session.service.MenuServiceImpl;

//@WebFilter(
//		filterName = "authorizationFilter",
//		value = {"/member", "/admin"}
//)
public class AuthorizationFilter implements Filter {
	
	@Autowired MenuServiceImpl menuServiceImpl;
	
	public AuthorizationFilter() {
		super();
		System.out.println("OncePerRequestFilter AuthorizationFilter");
	}

//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//			throws ServletException, IOException {
//		System.out.println("AuthorizationFilter");
//		HttpSession session = ((HttpServletRequest) request).getSession();
//		User user = (User) session.getAttribute("loginUser");
//		
//		if(!ObjectUtils.isEmpty(user)) {
//			String requestPath = ((HttpServletRequest) request).getRequestURI();
//			Menu searchMenu = new Menu();
//			searchMenu.setPath(requestPath);
//			searchMenu.setAuth_id(user.getAuth_id());
//			Menu menu = menuServiceImpl.getMenuByAuth(searchMenu);
//			System.out.println(menu);
//			
//			if(menu == null) {
//				((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "접근 권한이 없습니다.");
//			} else {
//				filterChain.doFilter(request, response);
//			}
//		} else {
//			filterChain.doFilter(request, response);
//		}
//	}
	
//	@Inject
//	private MenuServiceImpl menuServiceImpl;
//
//	@Override
//	public void init(FilterConfig filterConfig) throws ServletException {
//		Filter.super.init(filterConfig);
//		ApplicationContext ctx = WebApplicationContextUtils
//			      .getRequiredWebApplicationContext(filterConfig.getServletContext());
//			    this.menuServiceImpl = ctx.getBean(MenuServiceImpl.class);
////	    SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
//	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("AuthorizationFilter");
		
		User user = (User) request.getAttribute("loginUser");
		
		if(!ObjectUtils.isEmpty(user)) {
			String requestPath = ((HttpServletRequest) request).getRequestURI();
			Menu searchMenu = new Menu();
			searchMenu.setPath(requestPath);
			searchMenu.setAuth_id(user.getAuth_id());
			Menu menu = menuServiceImpl.getMenuByAuth(searchMenu);
			System.out.println(menu);
			
			if(menu == null) {
				((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "접근 권한이 없습니다.");
			} else {
				chain.doFilter(request, response);
			}
		} else {
			chain.doFilter(request, response);
		}
	}

}
