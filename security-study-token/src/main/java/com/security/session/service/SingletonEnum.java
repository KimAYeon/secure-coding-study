/**
 * 
 */
package com.security.session.service;

import org.apache.commons.codec.binary.Base64;

import com.security.session.model.User;

/**
 * @author Ayeon
 *
 */
public enum SingletonEnum {
	INSTANCE {
		public String createToken(User user) {
			String auth = user.getId() + ":" + user.getPassword();
			byte[] encodedAuth = Base64.encodeBase64(auth.getBytes());
			String authHeader = "Basic " + new String(encodedAuth);
			
			return authHeader;
		}
	};
	
	public abstract String createToken(User user);
	public String parseToToken(String token) {
		String decodeAuth = "";
		if(token != null && token.startsWith("Basic")) {
			token = token.substring(6);
			decodeAuth = new String(Base64.decodeBase64(token));
		}
		
		return decodeAuth;
	}
}
