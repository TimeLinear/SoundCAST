package com.kh.soundcast.member.model.dto;

public class GoogleUserInfoResponse {
	private String aud;
	private String exp;
	private String iat;
	private String iss; // 해당 토큰 발급 소셜명(https://accounts.google.com)
	private String sub; // 구글의 사용자 식별자(고유 식별자 키로 사용 가능)
	private String email;
	private String email_verified;
	private String name;
	private String nonce;
	private String picture;
}
