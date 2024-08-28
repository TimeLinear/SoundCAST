package com.kh.soundcast.member.model.vo;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Member implements UserDetails {
	
	private int memberNo; // 회원 번호
	private int memberProfileImageNo; 
	private String memberSocialSocialName; // 로그인한 소셜명(google, kakao, naver)
	private String memberSoicalId; //소셜ID(각소셜의 고유식별자)
	private String memberNickname;
	private String memberEmail; // 카카오의 경우엔 null 허용
	private String memberIntroduce; // 자기 소개 내용
	private String memberCreateDate; // 가입일자
	private String memberModifyDate; // 수정일자
	private String memberStatus; // 계정 유효 여부

	private List<SimpleGrantedAuthority> authorities;
	
	
	public Collection<? extends GrantedAuthority> getAuthorities(){
		return authorities;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return null;
	}
	
	
	
	
}