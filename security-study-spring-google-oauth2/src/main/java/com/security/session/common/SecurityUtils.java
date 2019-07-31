package com.security.session.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SecurityUtils {

	public static JsonObject postConnection(String requestUrl, Map<String, Object> requestParams) {
		JsonObject jsonObject = null;

		try {
			URL url = new URL(requestUrl); // 호출할 url

			StringBuilder postData = new StringBuilder();
			for (Map.Entry<String, Object> param : requestParams.entrySet()) {
				if (postData.length() != 0)
					postData.append('&');
				postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
				postData.append('=');
				postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
			}
			System.out.println(postData.toString());
			byte[] postDataBytes = postData.toString().getBytes("UTF-8");

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
			conn.setDoOutput(true);
			conn.getOutputStream().write(postDataBytes); // POST 호출

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

			StringBuffer sb = new StringBuffer();
			String inputLine;

			while ((inputLine = in.readLine()) != null) { // response 출력
				System.out.println(inputLine);
				sb.append(inputLine);
			}

			jsonObject = new JsonParser().parse(sb.toString()).getAsJsonObject();

			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return jsonObject;
	}

	public static String getConnection(String requestUrl) {
		StringBuffer sb = null;
		
		try {
			URL url = new URL(requestUrl);
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        //전송방식
	        con.setRequestMethod("GET");
	        //Request Header 정의
	        con.setConnectTimeout(10000);       //컨텍션타임아웃 10초
	        con.setReadTimeout(5000);           //컨텐츠조회 타임아웃 5총

	        int responseCode = con.getResponseCode();
	        System.out.println("\nSending 'GET' request to URL : " + requestUrl);
	        System.out.println("Response Code : " + responseCode);

	        Charset charset = Charset.forName("UTF-8");
	        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));
	        
	        sb = new StringBuffer();
			String inputLine;

			while ((inputLine = in.readLine()) != null) { // response 출력
				System.out.println(inputLine);
				sb.append(inputLine);
			}
			
	        in.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return sb.toString();
    }
	
	public static HttpServletResponse removeCookie(HttpServletRequest request, HttpServletResponse response, String key) {
		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(key)) {
					cookie.setMaxAge(0);
					cookie.setValue("");
					cookie.setPath("/");

					response.addCookie(cookie);
				}
			}
		}
		
		return response;
	}
	
	public static Cookie searchCookie(HttpServletRequest request, HttpServletResponse response, String key) {
		Cookie[] cookies = request.getCookies();
		Cookie targetCookie = null;

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(key)) {
					targetCookie = cookie;
				}
			}
		}
		
		return targetCookie;
	}
}
