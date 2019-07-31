package com.security.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.security.session.model.Menu;
import com.security.session.model.User;
import com.security.session.service.MenuServiceImpl;

public class CertificationInterceptor implements HandlerInterceptor {
	@Autowired MenuServiceImpl menuServiceImpl;

//	@Override
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//			throws Exception {
//		HttpSession session = request.getSession();
//		User user = (User) session.getAttribute("loginUser");
//		
//		if(ObjectUtils.isEmpty(user)) {
//			response.sendRedirect("/main");
//			return false;
//		} else {
//			// 유저에게 현재 페이지에 대한 권한이 있는지 체크 
//			String requestPath = request.getRequestURI();
//			Menu searchMenu = new Menu();
//			searchMenu.setPath(requestPath);
//			searchMenu.setAuth_id(user.getAuth_id());
//			Menu menu = menuServiceImpl.getMenuByAuth(searchMenu);
//			
//			if(ObjectUtils.isEmpty(menu)) {
//				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "접근 권한이 없습니다.");
//				return false;
//			}
//			session.setMaxInactiveInterval(30*60);
//			return true;
//		}
//	}
//
//	@Override
//	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
//			ModelAndView modelAndView) throws Exception {
//		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
//	}
//
//	@Override
//	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
//			throws Exception {
//		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
//	}
	
}
