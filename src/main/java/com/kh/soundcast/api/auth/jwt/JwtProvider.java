package com.kh.soundcast.api.auth.jwt;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.soundcast.member.model.service.UserDetailServiceImpl;
import com.kh.soundcast.member.model.vo.Member;
import com.kh.soundcast.member.model.vo.MemberExt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

	private final UserDetailServiceImpl service;
	
	@Value("${jwt.secret}")
	private String secretKey;
	
	@PostConstruct
	public void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}
	
	//현재 app자체 토큰생성
	public String createToken(HashMap<String, Object> userPk) throws JsonProcessingException {
		String userPkStr = new ObjectMapper().writeValueAsString(userPk);
		
		Claims claims = Jwts.claims().setSubject(userPkStr);		
		Date now =new Date();
		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime()+(30*60*1000))) //30분
			.signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, secretKey)
			.compact();
		
	}
	

	public String resolveToken(HttpServletRequest request) {
		log.info("요청 사항 - {}", request.getHeader("Authorization"));
		String accessToken = request.getHeader("Authorization");
				
		if(accessToken == null) {
			return null;
		}

		accessToken = accessToken.replace("Bearer ", "");
		
		log.info("resolvetoken={}", accessToken);
		
		return accessToken;
		
		
	
	}

	public boolean validationToken(String token) {
		if(token.equals("undefined") || token == null) {
			return false;
		}
		
		try {
			Jws<Claims> claimsJws=Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			Claims claims = claimsJws.getBody();
			
			
			String subJson = (String) claims.get("sub");
			log.info("vali-sub={}",subJson);
//			ObjectMapper objectMapper = new ObjectMapper();
//            Map<String, Object> sub = objectMapper.readValue(subJson, new TypeReference<Map<String, Object>>() {});
//
//	        String socialId = (String) sub.get("SocialId");
//	        log.info("SocialId={}", socialId);
//	        
//	        String socialType = (String)sub.get("SocialType");
	        
	        MemberExt member = (MemberExt) service.loadUserByUsername(subJson);
			
			return (!claimsJws.getBody().getExpiration().before(new Date()) && !(member == null) );
					//!claims.getBody().getExpiration().before(new Date());
			//여기서 && 조건걸어줌 멤버가 null x , 데이트가 지낫는지 확인 
			//디코드후 loadbyusername으로 호출해서 멤버객체 가져오기
			
			
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		
		
	}
	
	/*
	 * Authentication : 사용자 인증정보가 담겨있는 객체
	 * - Principal : 인증된 사용자 정보
	 * - Credentials :인증에 필요한 비밀번호를 저장하는 객체
	 *				내부적으로 인증작업시 필요하며, 보호되고 있음.
	 * - Authorities : 인증된 사용자가 가진 권한 목록
	 * 
	 * */
	public Authentication getAuthentication(String token) throws JsonMappingException, JsonProcessingException {
		
		Member member = (Member) service.loadUserByUsername(getUserPk(token));

		return new UsernamePasswordAuthenticationToken(member, "", member.getAuthorities());
	}
	
	//토큰에서 userPk값을 꺼내는 메서드
	public String getUserPk(String token) throws JsonMappingException, JsonProcessingException {
		
		String userPkString = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
//		HashMap<String, Object> userPks = new ObjectMapper().readValue(userPkString, new TypeReference<HashMap<String, Object>>() {});
//		
//		String SocialId =(String)userPks.get("SocialId");
//		
		return userPkString;
	}

}
