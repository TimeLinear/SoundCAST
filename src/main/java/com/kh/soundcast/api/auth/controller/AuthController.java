package com.kh.soundcast.api.auth.controller;

import java.util.HashMap;

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
import com.kh.soundcast.api.auth.model.service.AuthService;
import com.kh.soundcast.member.model.vo.Member;
import com.kh.soundcast.member.model.vo.MemberExt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
	
	private final AuthService authService;
	private final JwtProvider jwtProvider;
	
	//@CrossOrigin(origins = {"http://localhost:3000"})
	@PostMapping("/login/{socialType}")
	public ResponseEntity<HashMap<String,Object>> authCheck(
			@PathVariable String socialType,
			@RequestBody HashMap<String,String> param
			) throws JsonProcessingException{
		log.info("socialType= {} , accessToken = {}", socialType,param);
		
		MemberExt user = (MemberExt) authService.kakaoLogin(param.get("accessToken"),socialType);
		
		log.debug("로그인 직후 유저 정보 - {}", user);
		log.debug("로그인 직후 유저 정보(자기소개) - {}", user.getMemberIntroduce());
		
		HashMap<String,Object> map = new HashMap<>();
		HashMap<String,Object> userSubject = new HashMap<>();
		userSubject.put("socialType", socialType);
		userSubject.put("socialId", user.getMemberSocial().getMemberSocialSocialId());
		String ACCESS_TOKEN = jwtProvider.createToken(userSubject);
		map.put("jwtToken", ACCESS_TOKEN);
		map.put("member", user);
		
		log.debug("유저의 권한 값 - {}", user.getAuthorities());
		log.debug("유저 정보 - {}", user);
		
		return ResponseEntity.ok(map);
	}
	
	
	@PostMapping("/enroll/{socialType}")
	public ResponseEntity<HashMap<String, Object>> enroll(@PathVariable String socialType,
			@RequestBody HashMap<String, String> param) throws Exception {

		String clientId;
		MemberExt member;
		switch (socialType) {
		case "google":
			member = authService.googleLogin(socialType, param);
			clientId = authService.getClientId(param);
			break;
//	    case "naver":
//	        member = authService.naverLogin(socialType, credential);
//	        break;
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
		MemberExt followCheck = (MemberExt)authService.login(mNo);
		member.setFollower(followCheck.getFollower());
		member.setFollowing(followCheck.getFollowing());
		
		log.debug("토큰 로그인 시도 유저 정보2 - {}", member);
		
		HashMap<String,Object> map = new HashMap<>();
		
		
		if(member != null) {
			map.put("member", member);
			
		}
		
		return ResponseEntity.ok(map);
	}
	
	
}
