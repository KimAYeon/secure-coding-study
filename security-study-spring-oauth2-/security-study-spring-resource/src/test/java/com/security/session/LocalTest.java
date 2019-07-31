package com.security.session;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.response.ResponseBodyExtractionOptions;

public class LocalTest {
	
	@Test
	public void testEncrypt() throws IOException {
		String accessToken = obtainAccessToken("fooClientIdPassword", "john", "123");
	     
	    assertNotNull(accessToken);
	}
	
	private String obtainAccessToken(String clientId, String username, String password) {
	    Map<String, String> params = new HashMap<String, String>();
	    params.put("grant_type", "password");
	    params.put("client_id", clientId);
	    params.put("username", username);
	    params.put("password", password);
	    io.restassured.response.Response response = RestAssured.given().auth().preemptive()
	      .basic(clientId, "secret").and().with().params(params).when()
	      .post("http://localhost:8081/spring-security-oauth-server/oauth/token");
	    
	    if(response != null) {
	    	return response.jsonPath().getString("access_token");
	    }
	    return null;
	}
}
