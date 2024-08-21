package com.kh.soundcast.member.controller;



import java.util.Base64;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.soundcast.api.auth.jwt.JwtProvider;
import com.kh.soundcast.member.model.service.AuthService;
import com.kh.soundcast.member.model.vo.Member;
import com.kh.soundcast.member.model.vo.MemberExt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
		private final AuthService authService;
		private final JwtProvider jwtProvider;
	
		@PostMapping("/login/{socialType}")
		public ResponseEntity<HashMap<String, Object>> authCheck(
				@PathVariable String socialType,
				@RequestBody HashMap<String, String> credential
				) throws Exception{
			log.info("socialType={}, credential={}",socialType,credential);
			
			//1) 회원 가입되어있는지 체크
			HashMap<String, Object> map = authService.authCheck(socialType,credential);
			
			//1) 회원가입진행 -닉네임, 이메일, 프로필이미지, 소셜아이디
//			MemberExt member = authService.googleLogin(socialType, credential);
//			
//			log.info("member_control={}",member.getAuthorities());
//			
//			// 2) 유저 pk 값을 통해 서버용 jwt토큰 생성 후 클라이언트에게 반환해야함
//			String clientId = authService.getClientId(credential);
//			
//			HashMap<String, Object> userPk = new HashMap<>();
//			userPk.put("SocialType", socialType);
//			userPk.put("SocialId", clientId);
//			log.info("userPk={}", userPk);
//		
//			HashMap<String, Object> map = new HashMap<>();
//			String ACCESS_TOKEN = jwtProvider.createToken(userPk);
//			
//			map.put("jwtToken", ACCESS_TOKEN);
//			map.put("member", member);
//			log.info("map={}", map);


			return ResponseEntity.ok(map);
			
		}

		
		
		@PostMapping("/enroll/{socialType}")
		public ResponseEntity<HashMap<String, Object>> enroll(
				@PathVariable String socialType,
				@RequestBody HashMap<String, String> credential
				) throws Exception{
			
			
			MemberExt member = authService.googleLogin(socialType, credential);
			
			String clientId = authService.getClientId(credential);

			HashMap<String, Object> userPk = new HashMap<>();
			userPk.put("SocialType", socialType);
			userPk.put("SocialId", clientId);
			log.info("userPk={}", userPk);
			
			HashMap<String, Object> map = new HashMap<>();
			String ACCESS_TOKEN = jwtProvider.createToken(userPk);
			
			map.put("jwtToken", ACCESS_TOKEN);
			map.put("member", member);
		

			return ResponseEntity.ok(map);
			
			
			
		}
		
		
	
}



