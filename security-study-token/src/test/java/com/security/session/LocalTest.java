package com.security.session;

import org.junit.Test;

import com.security.session.service.SingletonEnum;

public class LocalTest {
	
	@Test
	public void testEncrypt() {
//		PasswordEncoder encoder = new BCryptPasswordEncoder();
//		System.out.println(encoder.encode("test"));
//		System.out.println(encoder.encode("test"));
//		System.out.println(encoder.encode("test"));
//		System.out.println(encoder.encode("test"));
//		System.out.println(encoder.encode("test"));
//		System.out.println(encoder.encode("test"));
//		System.out.println(encoder.encode("test"));
//		
//		PasswordEncoder encoder1 = new BCryptPasswordEncoder(11);
//		System.out.println(encoder1.encode("admin"));
		
		System.out.println(SingletonEnum.INSTANCE.parseToToken(""));
	}
}
