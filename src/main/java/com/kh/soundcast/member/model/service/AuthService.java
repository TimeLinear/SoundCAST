package com.kh.soundcast.member.model.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.kh.soundcast.api.auth.jwt.JwtProvider;
import com.kh.soundcast.member.model.dao.AuthDao;
import com.kh.soundcast.member.model.dto.GoogleUserInfoResponse;
import com.kh.soundcast.member.model.vo.Member;
import com.kh.soundcast.member.model.vo.MemberExt;
import com.kh.soundcast.member.model.vo.MemberSocial;
import com.kh.soundcast.member.model.vo.ProfileImage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
	
	private final GoogleApi googleApi;
	private final AuthDao authDao;
	private final JwtProvider jwtProvider;
	
	@Value("{google.client.id}")
	private String clientId;
	
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public MemberExt googleLogin(String socialType, HashMap<String, String> credential) throws Exception {
		
		//credential 디코드 
		String crebody = credential.get("Credential");
		log.info("crebody={}",crebody);
		
		// JWT는 3개의 부분으로 구성되어 있음. 헤더, 페이로드, 서명
		String[] parts = crebody.split("\\.");
		
		// Base64Url 디코딩
		String header = new String(Base64.getUrlDecoder().decode(parts[0]));
		String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
		
		
		ObjectMapper objectMapper = new ObjectMapper();
		GoogleUserInfoResponse userInfo = new GoogleUserInfoResponse();
		try {
			userInfo = objectMapper.readValue(payloadJson, GoogleUserInfoResponse.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		//현재 app 에 사용자 정보 유무 조회
		MemberExt member = authDao.loadUserByUsername(socialType, userInfo.getSub());
		log.info("최초로 검색한 유저 정보 - {}", member);
	
		
		
		
		int result = 1;
		if(member == null) {
			
			MemberExt m = MemberExt.builder()
					.profileImage(new ProfileImage(0, userInfo.getPicture()))
					.memberSocial(new MemberSocial(0 ,userInfo.getSub(),socialType))
					.memberNickname(userInfo.getName())
					.memberEmail(userInfo.getEmail())
					.build();
			
		
			result *= authDao.insertProfileImage(m);
			result *= authDao.insertMember(m);
			result *= authDao.insertMemberSocial(m);
			
			if(result < 1) {
				throw new Exception();
			}
			
			member = (MemberExt)authDao.loadUserByUsername(socialType, userInfo.getSub());
//			authDao.countFollow(socialType, userInfo.getSub());
//			authDao.countFollower(socialType, userInfo.getSub());
		}
		
		
				
		log.debug("로그인 시도 유저 정보 - {}", member);
		
		return member;


	}
	
	public String getClientId(HashMap<String, String> credential) {
		//credential 디코드 
				String crebody =credential.get("Credential");
				log.info("crebody={}",crebody);
				
				// JWT는 3개의 부분으로 구성되어 있음. 헤더, 페이로드, 서명
				String[] parts = crebody.split("\\.");
				
				// Base64Url 디코딩
				String header = new String(Base64.getUrlDecoder().decode(parts[0]));
				String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
				
				
				ObjectMapper objectMapper = new ObjectMapper();
				GoogleUserInfoResponse userInfo = new GoogleUserInfoResponse();
				try {
					userInfo = objectMapper.readValue(payloadJson, GoogleUserInfoResponse.class);
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				
			String sub =userInfo.getSub();
				
			return sub;	
	}



	public HashMap<String, Object> authCheck(String socialType, HashMap<String, String> credential) throws Exception {
		
		//credential 디코드 
		String crebody = credential.get("Credential");
		log.info("crebody={}",crebody);
		
		// JWT는 3개의 부분으로 구성되어 있음. 헤더, 페이로드, 서명
		String[] parts = crebody.split("\\.");
		
		// Base64Url 디코딩
		String header = new String(Base64.getUrlDecoder().decode(parts[0]));
		String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
		
		
		ObjectMapper objectMapper = new ObjectMapper();
		GoogleUserInfoResponse userInfo = new GoogleUserInfoResponse();
		try {
			userInfo = objectMapper.readValue(payloadJson, GoogleUserInfoResponse.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		//현재 app 에 사용자 정보 유무 조회
		MemberExt member = authDao.loadUserByUsername(socialType, userInfo.getSub());
		log.info("service m = {}", member);
		
		HashMap<String, Object> map = new HashMap<>();	
		HashMap<String, Object> userPk = new HashMap<>();	
		
		userPk.put("socialId", userInfo.getSub());
		userPk.put("socialType", socialType);
		String ACCESS_TOKEN = jwtProvider.createToken(userPk);	
		
		
		map.put("jwtToken", ACCESS_TOKEN);
		map.put("member", member);
		map.put("Credential", crebody);
		
		return map;
	}
}
