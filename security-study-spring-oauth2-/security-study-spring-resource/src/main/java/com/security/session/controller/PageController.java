package com.security.session.controller;

import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.security.session.service.UserServiceImpl;

@Controller
public class PageController implements ErrorController {
	@Autowired UserServiceImpl userServiceImpl;
	
	@RequestMapping(value="/main")
	public String getMainPage(HttpServletRequest request, Model model) {
		return "main";
	}
	
	@RequestMapping(value="/member")
	public String getMemberPage(HttpSession session) {
		return "member";
	}
	
	@RequestMapping(value="/admin")
	public String getAdminPage(HttpSession session) {
		return "admin";
	}
	
	@RequestMapping(value="/error/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status_code = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    	Object status_msg = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        HttpStatus httpStatus = HttpStatus.valueOf(Integer.valueOf(status_code.toString()));
        model.addAttribute("code", status_code.toString() + " - " + httpStatus.getReasonPhrase());
        model.addAttribute("msg", status_msg.toString());
        model.addAttribute("timestamp", new Date());
        return "error/error";
    }

	@Override
	public String getErrorPath() {
		return "/error";
	}
}
