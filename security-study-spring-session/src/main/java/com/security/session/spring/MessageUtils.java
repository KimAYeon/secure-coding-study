package com.security.session.spring;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;

public class MessageUtils {
	private static MessageSourceAccessor resources;
	public void setMessageSource(MessageSourceAccessor messageSource) {
		this.resources = messageSource;
	}
	
	public static String getMessage(String code) {
		return resources.getMessage(code, Locale.getDefault());
	}
	
	public static String getMessage(String code, String args[]){
        return resources.getMessage(code, args, Locale.getDefault());
    }
}
