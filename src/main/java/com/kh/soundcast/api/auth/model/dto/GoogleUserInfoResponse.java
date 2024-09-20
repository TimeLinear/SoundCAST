package com.kh.soundcast.api.auth.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleUserInfoResponse {

	private String aud; // ID 토큰의 대상 애플리케이션에 대한 고유 식별자
	private String exp; // ID 토큰 만료기간
	private String iat; // ID 토큰 발급 시간
	private String iss; // 해당 토큰 발급 소셜명(https://accounts.google.com)
	private String sub; // 구글의 사용자 식별자(고유 식별자 키로 사용 가능)
	private String email; // 이메일 주소
	private String email_verified; // 이메일 검증이 되었는가 ?
	private String name; // 사용자의 전체 이름
	private String nonce; // 인증 요청에서 앱이 제공하는 nonce 값. 한 번만 표시되도록 하여 재생 공격으로부터 보호해야 함
	private String picture; // 사용자의 프로필 url
}
