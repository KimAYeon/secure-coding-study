package com.security.session.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.JsonObject;
import com.security.session.common.SecurityUtils;
import com.security.session.model.User;

@Service
public class GoogleOAuth2ServiceImpl {

	@Value("${security.jwt.token.secret-key:secret-key}")
	private String secretKey;

	@Value("${security.jwt.token.expire-length:3600000}")
	private long validityInMilliseconds = 3600000; // 1h

	@Value("${security.jwt.token.client-id:client-id}")
	private String clientId;

	@Value("${security.jwt.token.application-name:application-name}")
	private String applicationName;

	@Value("${security.jwt.token.scope:scope}")
	private String scope;

	@Value("${security.jwt.token.redirect-uri:redirect-uri}")
	private String redirectUri;

	private static final HttpTransport TRANSPORT = new NetHttpTransport();
	private static final JacksonFactory JSON_FACTORY = new JacksonFactory();

	// 구글 계정 로그인 요청
	public String getAuth(HttpServletRequest request, HttpServletResponse response) {
		String requestUrl = "https://accounts.google.com/o/oauth2/v2/auth";

		StringBuilder postData = new StringBuilder();
		try {
			Map<String, Object> requestParams = new LinkedHashMap<>();
			requestParams.put("scope", scope);
			requestParams.put("access_type", "offline");
			requestParams.put("include_granted_scopes", "true");
			requestParams.put("state", "state_parameter_passthrough_value");
			requestParams.put("redirect_uri", redirectUri);
			requestParams.put("response_type", "code");
			requestParams.put("client_id", clientId);

			for (Map.Entry<String, Object> param : requestParams.entrySet()) {
				if (postData.length() != 0)
					postData.append('&');
				postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
				postData.append('=');
				postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "redirect:" + requestUrl + "?" + postData.toString();
	}

	// 토큰 발급
	public JsonObject obtainToken(HttpServletRequest request, HttpServletResponse response) {
		String url = "https://www.googleapis.com/oauth2/v4/token";

		Map<String, Object> params = new LinkedHashMap<>();
		params.put("code", request.getParameter("code"));
		params.put("client_id", clientId);
		params.put("client_secret", secretKey);
		params.put("redirect_uri", redirectUri);
		params.put("grant_type", "authorization_code");

		return SecurityUtils.postConnection(url, params);
	}

	// 토큰 재발급
	public JsonObject refreshToken(HttpServletRequest request, HttpServletResponse response) {
		String url = "https://www.googleapis.com/oauth2/v4/token";

		Map<String, Object> params = new LinkedHashMap<>();
		params.put("client_id", clientId);
		params.put("client_secret", secretKey);
		params.put("refresh_token", request.getAttribute("refresh_token"));
		params.put("grant_type", "refresh_token");

		return SecurityUtils.postConnection(url, params);
	}

	public User setUserByIdToken(JsonObject jsonObject) {
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(TRANSPORT, JSON_FACTORY)
				// Specify the CLIENT_ID of the app that accesses the backend:
				.setAudience(Collections.singletonList(clientId))
				// Or, if multiple clients access the backend:
				// .setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
				.build();

		// (Receive idTokenString by HTTPS POST)
		User user = new User();
		GoogleIdToken idToken;
		try {
			idToken = verifier.verify(jsonObject.get("id_token").getAsString());
			if (idToken != null) {
				Payload payload = idToken.getPayload();

				// Print user identifier
				String userId = payload.getSubject();
				System.out.println("User ID: " + userId);

				// Get profile information from payload
				String email = payload.getEmail();
				boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
				String name = (String) payload.get("name");
				String pictureUrl = (String) payload.get("picture");
				String locale = (String) payload.get("locale");
				String familyName = (String) payload.get("family_name");
				String givenName = (String) payload.get("given_name");
				
				user.setId(email);
				user.setName(name);
				user.setProfile(pictureUrl);
			} else {
				System.out.println("Invalid ID token.");
			}
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return user;
	}
}
