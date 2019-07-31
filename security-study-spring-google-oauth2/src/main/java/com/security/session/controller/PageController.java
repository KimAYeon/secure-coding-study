package com.security.session.controller;

import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.security.session.common.SecurityUtils;
import com.security.session.service.GoogleOAuth2ServiceImpl;
import com.security.session.service.UserServiceImpl;

@Controller
public class PageController implements ErrorController {
	@Autowired UserServiceImpl userServiceImpl;
	@Autowired GoogleOAuth2ServiceImpl googleOAuth2ServiceImpl;

	@RequestMapping(value="/login/google")
	public String loginGoogle(HttpServletRequest request, HttpServletResponse response) {
		return googleOAuth2ServiceImpl.getAuth(request, response);
	}
	
	@RequestMapping(value="/main")
	public String getMainPage(HttpServletRequest request, Model model) {
		return "main";
	}
	
	@RequestMapping(value="/member")
	public String getMemberPage(HttpServletRequest request, HttpServletResponse response) {
		return "member";
	}
	
	@RequestMapping(value="/admin")
	public String getAdminPage(HttpServletRequest request, HttpServletResponse response) {
		return "admin";
	}
	
	@RequestMapping(value="/error")
    public String handleError(HttpServletRequest request, HttpServletResponse response, Model model) {
        Object status_code = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    	Object status_msg = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        HttpStatus httpStatus = HttpStatus.valueOf(Integer.valueOf(status_code.toString()));
        
        if(httpStatus.equals(HttpStatus.UNAUTHORIZED)) {
        	request.removeAttribute("loginUser");
        	SecurityUtils.removeCookie(request, response, "Authorization");
        	return "main";
        }
        
        model.addAttribute("code", status_code.toString() + " - " + httpStatus.getReasonPhrase());
        model.addAttribute("msg", status_msg.toString());
        model.addAttribute("timestamp", new Date());
        
		System.out.println("handle error : " + status_code.toString() + " - " + httpStatus.getReasonPhrase());
        return "error/error";
    }

	@Override
	public String getErrorPath() {
		return "/error";
	}
}
