package com.security.session.service;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.Oauth2.Userinfo;
import com.google.api.services.oauth2.model.Tokeninfo;
import com.security.session.exception.CustomException;
import com.security.session.exception.InvalidTokenException;
import com.security.session.spring.UserDetailServiceImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

	/**
	 * THIS IS NOT A SECURE PRACTICE! For simplicity, we are storing a static key
	 * here. Ideally, in a microservices environment, this key would be kept on a
	 * config-server.
	 */
	@Value("${security.jwt.token.secret-key:secret-key}")
	private String secretKey;

	@Value("${security.jwt.token.expire-length:3600000}")
	private long validityInMilliseconds = 3600000; // 1h

	@Value("${security.jwt.token.client-id:client-id}")
	private String clientId;

	@Value("${security.jwt.token.application-name:application-name}")
	private String applicationName;

	private static final HttpTransport TRANSPORT = new NetHttpTransport();
	private static final JacksonFactory JSON_FACTORY = new JacksonFactory();

	@Autowired
	private UserDetailServiceImpl userDetailServiceImpl;

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	public String createToken(String username, List<SimpleGrantedAuthority> roles) {

		Claims claims = Jwts.claims().setSubject(username);
		claims.put("auth",
				roles.stream().map(s -> s.getAuthority()).filter(Objects::nonNull).collect(Collectors.toList()));

		Date now = new Date();
		Date validity = new Date(now.getTime() + validityInMilliseconds);

		return Jwts.builder()//
				.setClaims(claims)//
				.setIssuedAt(now)//
				.setExpiration(validity)//
				.signWith(SignatureAlgorithm.HS256, secretKey)//
				.compact();
	}

	public Authentication getAuthentication(String token) {
		UserDetails userDetails = userDetailServiceImpl.loadUserByUsername(getUsername(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public String getUsername(String accessToken) {
		if (accessToken != null) {
			// Check that the Access Token is valid.
			try {
				GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
				Oauth2 oauth2 = new Oauth2.Builder(TRANSPORT, JSON_FACTORY, credential)
						.setApplicationName(applicationName).build();
				Tokeninfo tokenInfo = oauth2.tokeninfo().setAccessToken(accessToken).execute();
				
				if (!tokenInfo.getIssuedTo().equals(clientId)) {
					throw new CustomException("Issued To does not match our client ID!", HttpStatus.FORBIDDEN);
				}
				return tokenInfo.getEmail();
			} catch (IOException e) {
				throw new InvalidTokenException("Expired or invalid JWT token", HttpStatus.UNAUTHORIZED);
			}
		} else {
			return null;
		}
	}

	public String resolveToken(String bearerToken) {
		System.out.println("bearerToken : " + bearerToken);
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	public boolean validateToken(String accessToken) {
		if (accessToken != null) {
			try {			
				GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
				Oauth2 oauth2 = new Oauth2.Builder(TRANSPORT, JSON_FACTORY, credential)
						.setApplicationName(applicationName).build();
				Tokeninfo tokenInfo = oauth2.tokeninfo().setAccessToken(accessToken).execute();
				
				if (!tokenInfo.getIssuedTo().equals(clientId)) {
					throw new CustomException("Issued To does not match our client ID!", HttpStatus.FORBIDDEN);
				}
			} catch (IOException e) {
				throw new InvalidTokenException("Expired or invalid JWT token", HttpStatus.UNAUTHORIZED);
			}
			return true;
		} else {
			return false;
		}
	}
}
