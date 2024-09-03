package com.kh.soundcast.api.auth.controller;



import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kh.soundcast.api.auth.jwt.JwtProvider;
import com.kh.soundcast.api.auth.service.AuthService;
import com.kh.soundcast.member.model.service.MemberService;
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
		private final MemberService memberService;
		
		@PostMapping("/login/{socialType}")
		public ResponseEntity<HashMap<String, Object>> authCheck(
				@PathVariable String socialType,
				@RequestBody HashMap<String, String> param
				) throws Exception{
			log.info("socialType={}, param={}",socialType,param);
			HashMap<String,Object> map = new HashMap<>();
			
			switch (socialType) {
		    case "kakao":
		    	
		    	map = authService.authCheck(socialType,param);
				
//				HashMap<String,Object> userSubject = new HashMap<>();
//				userSubject.put("socialType", socialType);
//				userSubject.put("socialId", user.getMemberSocial().getMemberSocialSocialId());
//				String ACCESS_TOKEN = jwtProvider.createToken(userSubject);
//				map.put("jwtToken", ACCESS_TOKEN);
//				map.put("member", user);
//				
//				log.debug("유저의 권한 값 - {}", user.getAuthorities());
//				log.debug("유저 정보 - {}", user);
				
				break;
				
		    case "google":
			
			//1) 회원 가입되어있는지 체크
		    log.info("구글로그인회원가입체크={}",param);
			map = authService.authCheck(socialType, param);
			break;
			
		    default:
				throw new IllegalArgumentException("Unsupported socialType: " + socialType);
			}
			

			return ResponseEntity.ok(map);
			
		}

		
		
		@PostMapping("/enroll/{socialType}")
		public ResponseEntity<HashMap<String, Object>> enroll(
				@PathVariable String socialType,
				@RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
				@RequestBody HashMap<String, String> param
				) throws Exception{
			
			String clientId;
			MemberExt member;
			switch (socialType) {
		    case "google":
		    	param.put("socialType", socialType);
		        member = authService.googleLogin(socialType, param);
		    	clientId = authService.getClientId(param);
		        
		        break;
//		    case "naver":
//		        member = authService.naverLogin(socialType, credential);
//		        break;
		    case "kakao":
		    	String accessToken = param.get("accessToken");
		    	
		        member = authService.kakaoLogin(socialType, accessToken);
		        
		        clientId = member.getMemberSocial().getMemberSocialSocialId();
		        log.info("controller clientId-kakao={}", clientId);
		        break;
		        
		    default:
		        throw new IllegalArgumentException("Unsupported socialType: " + socialType);
		}
			
			
			

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
		
		

		@PostMapping("/login")
		public ResponseEntity<HashMap<String,Object>> tokenLogin(
				@RequestBody HashMap<String,String> param
				) throws JsonProcessingException{
			
			MemberExt member = (MemberExt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			log.debug("토큰 로그인 시도 유저 정보1 - {}", member);
			
			int mNo = member.getMemberNo();
			member = (MemberExt)memberService.selectOneMember(mNo);
			
//			MemberExt followCheck = (MemberExt)authService.login(mNo);
//			member.setFollower(followCheck.getFollower());
//			member.setCommentList(followCheck.getCommentList());
//			member.setFollowing(followCheck.getFollowing());
//			log.info("어스컨트롤러 Following={}", member.getFollowing().get(0));
			
			if(member.getFollowing().get(0) == null) {
				member.setFollowing(new ArrayList<MemberExt>());
			}

			if(member.getMemberIntroduce() == null) {
				member.setMemberIntroduce("");
			}
			
			log.debug("토큰 로그인 시도 유저 정보2 - {}", member);
			
			HashMap<String,Object> map = new HashMap<>();
			
			if(member != null) {
				map.put("member", member);
			}
			
			return ResponseEntity.ok(map);
		}
		
	
}



