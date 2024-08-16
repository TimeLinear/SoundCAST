package com.kh.soundcast.member.model.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.kh.soundcast.member.model.dto.GoogleUserInfoResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GoogleApi {
	
	@Value("${jwt.secret}")
	private String secretKey;
	@Value("{google.client.id")
	private String clientId;
	
	
	
	
	
	
//
//	private final WebClient webClient;
//	private static final String GOOGLE_USER_INFO_URI ="https://www.googleapis.com/userinfo/v2/me";
//	
//	public String token(String credential) {
//		Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(credential);
//		System.out.println(claims.getBody().getSubject());
//		return claims.getBody().getSubject();
//		
//	}
//	
//	
//	
//	
//	
//	public GoogleUserInfoResponse getuserInfo(String credential) {
//		
//		
//		
//		
//		
////		
////		webClient.get()
////				.uri(GOOGLE_USER_INFO_URI)
////				.header("Authorization", "Bearer "+)
////		
//		
//		
//		
//		return null;
//	}

}
