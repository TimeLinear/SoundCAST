package com.kh.soundcast.api.auth.model.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.soundcast.api.auth.jwt.JwtProvider;
import com.kh.soundcast.api.auth.model.dao.AuthDao;
import com.kh.soundcast.api.auth.model.dto.GoogleUserInfoResponse;
import com.kh.soundcast.api.auth.model.dto.KakaoUserInfoResponse;
import com.kh.soundcast.api.auth.model.dto.NaverUserInfoResponse;
import com.kh.soundcast.member.model.vo.Member;
import com.kh.soundcast.member.model.vo.MemberExt;
import com.kh.soundcast.member.model.vo.MemberSocial;
import com.kh.soundcast.member.model.vo.ProfileImage;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
	
	private final GoogleApi googleApi;
	private final KakaoApi kakaoApi;
	private final NaverApi naverApi;
	private final AuthDao dao;
	private final JwtProvider jwtProvider;
	
	@Value("${jwt.secret}")
	private String secretKey;
	
	//구글 크리덴셜 디코딩작업 9/13일 추가 
	public GoogleUserInfoResponse googleUserInfo(HashMap<String, String> credential) {
		// credential 디코드
		String crebody = credential.get("Credential");
		log.info("crebody={}", crebody);

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

		return userInfo;
	}

	
	public HashMap<String, Object> authCheck(String socialType, HashMap<String, String> param) throws Exception {
		
		String socialId = null;
		String crebody = null;
		
		switch (socialType) {
		case "google":
			//credential 디코드 (구글)
			crebody = param.get("Credential");
			//log.info("crebody={}",crebody);
			
			socialId = googleUserInfo(param).getSub();
		break;
			
		case "kakao":
			//카카오
			String AccToken = param.get("accessToken");
			KakaoUserInfoResponse userInfoKakao = kakaoApi.getUserInfo(AccToken);
			socialId = String.valueOf(userInfoKakao.getId());
			log.info("kakao의 socialId={}", socialId);
		break;
		
		case "naver":
			log.info("naverPARAMS={}",param);
			String naverAccToken = param.get("accessToken");
			NaverUserInfoResponse userInfoNaver = naverApi.getUserInfo(naverAccToken);
			socialId=String.valueOf(userInfoNaver.getResponse().getId());
			
		break;
		
		}
		
		//현재 app 에 사용자 정보 유무 조회
		MemberExt member = dao.loadUserByUsername(socialType, socialId);

		if(member != null) {
			int mNo = member.getMemberNo();
			// 팔로우 정보들 가져오기
			List<MemberExt> following = dao.selectFollowList(mNo);
			int follower = dao.selectFollower(mNo);
			if(following.get(0) == null) {
				following.clear();
			}
			
			member.setFollowing(following);
			member.setFollower(follower);
		}
		
		HashMap<String, Object> map = new HashMap<>();	
		HashMap<String, Object> userPk = new HashMap<>();	
		
		userPk.put("socialId", socialId);
		userPk.put("socialType", socialType);
		String ACCESS_TOKEN = jwtProvider.createToken(userPk);	

		map.put("jwtToken", ACCESS_TOKEN);
		map.put("member", member);
		map.put("Credential", crebody);
		
		return map;
	}
	
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public MemberExt googleLogin(String socialType, HashMap<String, String> credential) throws Exception {

		// 현재 app 에 사용자 정보 유무 조회
		MemberExt member = dao.loadUserByUsername(socialType, googleUserInfo(credential).getSub());
		log.info("최초로 검색한 유저 정보 - {}", member);

		int result = 1;
		if (member == null) {

			MemberExt m = MemberExt.builder().profileImage(new ProfileImage(0, googleUserInfo(credential).getPicture()))
					.memberSocial(new MemberSocial(0, googleUserInfo(credential).getSub(), socialType))
					.memberNickname(googleUserInfo(credential).getName())
					.memberEmail(googleUserInfo(credential).getEmail()).build();

			result *= dao.insertProfileImage(m);
			result *= dao.insertMember(m);
			result *= dao.insertMemberSocial(m);

			if (result < 1) {
				throw new Exception();
			}

			member = (MemberExt) dao.loadUserByUsername(socialType, googleUserInfo(credential).getSub());

		}

		log.debug("로그인 시도 유저 정보 - {}", member);

		return member;

	}

	
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public MemberExt kakaoLogin(String accessToken, String socialType) {
		KakaoUserInfoResponse userInfo = kakaoApi.getUserInfo(accessToken);
		log.info("userInfo ?? {}" , userInfo);
		
		String socialId = String.valueOf(userInfo.getId());
		
		
		log.debug("유저 식별 값 - {}, 유저 소셜명 - {}", socialId, socialType);
		MemberExt user = dao.loadUserByUsername(socialId, socialType);
		log.debug("찾은 유저 정보 - {}", user);
		
		if(user == null) {
			// 회원가입
			String nickName = userInfo.getProperties().getNickname();
			String profile  = userInfo.getProperties().getProfile_image();
			String email = userInfo.getKakao_account().getEmail();
			
			// 프로필 정보를 받아야하므로 Member 클래스가 아니라,
			// Member 클래스를 확장하여 프로필 이미자와 개인 배너 정보까지 담을 수 있는
			// MemberExt 클래스를 만들어서 회원 정보를 받아줘야 한다.
			ProfileImage memberProfile = ProfileImage.builder().profileImageNo(0).profileImagePath(profile).build();
			MemberSocial memberSocial = MemberSocial.builder().memberSocialSocialId(String.valueOf(socialId))
					.memberSocialSocialName(socialType).build();
			
			
			MemberExt m = MemberExt.builder().
					memberNickname(nickName)
					.memberEmail(email)
					.profileImage(memberProfile)
					.memberSocial(memberSocial)
					.build();
			
			
			dao.insertProfileImage(m);
			dao.insertMember(m);
			dao.insertMemberSocial(m);
			// 위 주석부터 해당 주석 사이의 코드들은 MemberExt 클래스 완성이 선행되어야함
			user = dao.loadUserByUsername(socialId, socialType);
		}
		return user;
		
	}
	
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public MemberExt naverLogin(String socialType, String accessToken) {
		NaverUserInfoResponse userInfo = naverApi.getUserInfo(accessToken);
		
		String socialId = String.valueOf(userInfo.getResponse().getId());
		MemberExt member = (MemberExt)dao.loadUserByUsername(socialType, socialId);
		
		if(member == null) {
			// 회원가입
			String nickName = userInfo.getResponse().getNickname();
			String profile  = userInfo.getResponse().getProfile_image();
			String email = userInfo.getResponse().getEmail();
	
			ProfileImage memberProfile = ProfileImage.builder().profileImageNo(0).profileImagePath(profile).build();
			MemberSocial memberSocial = MemberSocial.builder().memberSocialSocialId(String.valueOf(socialId))
					.memberSocialSocialName(socialType).build();
			
			
			MemberExt m = MemberExt.builder().
					memberNickname(nickName)
					.memberEmail(email)
					.profileImage(memberProfile)
					.memberSocial(memberSocial)
					.build();
			
			dao.insertProfileImage(m);
			dao.insertMember(m);
			dao.insertMemberSocial(m);
			
			member = (MemberExt) dao.loadUserByUsername(socialType, socialId);
			
			
		}
		
		int mNo= member.getMemberNo();
		int follower = dao.selectFollower(mNo);
		List<MemberExt> commentList = dao.selectComment(mNo);
		
		if(member.getFollowing() == null) {
			member.setFollowing(new ArrayList<MemberExt>());
		}
		
		if(member.getMemberIntroduce()==null) {
			member.setMemberIntroduce("");
		}
		
		return member;
	}

	
	
	public MemberExt login(int mNo) {
		MemberExt member = new MemberExt();
		List<MemberExt> following = dao.selectFollowList(mNo);
		int follower = dao.selectFollower(mNo);

		member.setFollowing(following);
		member.setFollower(follower);
		log.info("Login following, follow = {},{}", following, follower);

		return member;
	}
	
}
