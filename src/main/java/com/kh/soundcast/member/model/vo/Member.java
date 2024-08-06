package com.kh.soundcast.member.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {
	
	private int memberNo; // 회원 번호
	private int memberProfileImgNo; // 프로필 이미지 번호
	private int memberBannerNo; // 마이페이지 배너 이미지 번호
	private int memberSocialCategory; // 로그인한 소셜명(0:구글, 1:네이버, 2:카카오)
	private int memberGradeNo; // 회원 등급
	private String memberNickname;
	private String memberEmail; // 카카오의 경우엔 null 허용
	private String memberIntroduce; // 자기 소개 내용
	private String memberCreateDate; // 가입일자
	private String memberModifyDate; // 수정일자
	private String memberStatus; // 계정 유효 여부
	
}
