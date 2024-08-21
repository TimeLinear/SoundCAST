package com.kh.soundcast.member.jwt;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.soundcast.member.model.service.UserDetailServiceImpl;
import com.kh.soundcast.member.model.vo.Member;
import com.kh.soundcast.member.model.vo.MemberExt;
import com.kh.soundcast.member.model.vo.MemberSocial;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
	
	public String createToken(HashMap<String, Object> userSubject) throws JsonProcessingException {

		log.debug("최소 토큰 생성 시 해쉬맵 내용 - {}", userSubject);
		
		String userPkJson = new ObjectMapper().writeValueAsString(userSubject);

		Claims claims = Jwts.claims().setSubject(userPkJson);
		Date now = new Date();
		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime()+(30*60*1000)))
				.signWith(SignatureAlgorithm.HS256, secretKey)
				.compact();
	}
	
	public String resolveToken(HttpServletRequest request) {
		String accessToken = request.getHeader("Authorization");
		accessToken = accessToken.replace("Bearer ", "");
		return accessToken; // "Bearer 암호화된 인증키"
	}
	
	public boolean validationToken(String token) {
		if(token.equals("undefined")) {
			return false;
		}
		try {
			// socialId, socialType가 들어있는 HashMap을 암호화한 것을 다시 암호화 해제하여 토큰 정보 포함한 객체 생성
			Jws<Claims> claims= Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			
			//토큰 유효성 검사
			//1) 토큰의 유효기간이 지났는지 검사
			//2) 토큰에 들어있는 socialId와 socialType을 기반으로 사용자를 DB에서 검색해서
			//	 그 결과가 null이 아닌지 검사
			
			Claims subClaims = claims.getBody();

			MemberExt member = (MemberExt)service.loadUserByUsername(subClaims.getSubject());
			log.debug("토큰 정보로 찾은 유저 - {}", member);
			// subClaims.getSubject() 가 HashMap을 막 string으로 변환했을 때의 값
					
			return !claims.getBody().getExpiration().before(new Date()) && !(member == null);
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Authentication getAuthentication(String token) {
		Member user = (Member)service.loadUserByUsername(getUserPk(token));
		// getUserPk(token)의 값은 위의 createToken 메소드에서
		// String userPkJson = new ObjectMapper().writeValueAsString(userSubject);
		// 위 코드로 만들어낸 소셜명 + 소셜아이디 해쉬맵의 json 문자열

		return new UsernamePasswordAuthenticationToken(user,"",user.getAuthorities());
	}
	
	public String getUserPk(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}
	
}
