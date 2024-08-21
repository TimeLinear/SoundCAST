package com.kh.soundcast.member.model.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.soundcast.member.model.dao.AuthDao;
import com.kh.soundcast.member.model.dto.KakaoUserInfoResponse;
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
	
	private final KakaoApi kakaoApi;
	private final AuthDao dao;
	
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public Member login(String accessToken, String socialType) {
		KakaoUserInfoResponse userInfo = kakaoApi.getUserInfo(accessToken);
		log.info("userInfo ?? {}" , userInfo);
		
		String socialId = String.valueOf(userInfo.getId());
		
		
		log.debug("유저 식별 값 - {}, 유저 소셜명 - {}", socialId, socialType);
		Member user = dao.loadUserByUsername(socialId, socialType);
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
			
			
			dao.insertProfileImage(m.getProfileImage());
			dao.insertMember(m);
			dao.insertMemberSocial(m);
			// 위 주석부터 해당 주석 사이의 코드들은 MemberExt 클래스 완성이 선행되어야함
			user = dao.loadUserByUsername(socialId, socialType);
		}
		return user;
		
	}
	
	
	
	
	
	
	
	

	
	
}
