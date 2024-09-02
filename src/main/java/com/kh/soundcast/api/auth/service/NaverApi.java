package com.kh.soundcast.api.auth.service;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.kh.soundcast.member.model.dto.KakaoUserInfoResponse;
import com.kh.soundcast.member.model.dto.NaverUserInfoResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverApi {

	private final WebClient webClient;
	
	private static final String NAVER_USER_INFO_URI = "https://openapi.naver.com/v1/nid/me";
	
	public NaverUserInfoResponse getUserInfo(String accessToken) {
		log.info("!!!!! = {}",accessToken);
		return webClient.get()
				.uri(NAVER_USER_INFO_URI)
				.header("Authorization", "Bearer "+accessToken)
				.retrieve()
				.bodyToFlux(NaverUserInfoResponse.class)
				.blockFirst()
				 ;
	}
	
}
