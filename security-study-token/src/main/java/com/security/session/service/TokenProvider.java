package com.security.session.service;

import org.apache.commons.codec.binary.Base64;

import com.security.session.model.User;

public class TokenProvider {
	private TokenProvider() {}
	
	private static class TokenProviderHolder {
		public static final TokenProvider INSTANCE = new TokenProvider();
	}
	
	public static TokenProvider getInstance() {
		return TokenProviderHolder.INSTANCE;
	}
	
	public String createToken(User user) {
		String auth = user.getId() + ":" + user.getPassword();
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes());
		String authHeader = "Basic " + new String(encodedAuth);
		
		return authHeader;
	}
	
	public String parseToToken(String token) {
		String decodeAuth = "";
		if(token != null && token.startsWith("Basic")) {
			token = token.substring(6);
			decodeAuth = new String(Base64.decodeBase64(token));
		}
		
		return decodeAuth;
	}
}
