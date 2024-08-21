package com.kh.soundcast.member.model.service;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.kh.soundcast.member.model.dto.KakaoUserInfoResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoApi {
	
	private final WebClient webClient;
	
	//요청보낼 url주소
	private static final String KAKAO_USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";
	
	public KakaoUserInfoResponse getUserInfo(String accessToken) {
		log.info("!!!!! = {}",accessToken);
		return webClient.get()
				 .uri(KAKAO_USER_INFO_URI)
				 .header("Authorization", "Bearer "+accessToken)
				 .retrieve()
				 .bodyToFlux(KakaoUserInfoResponse.class)
				 .blockFirst()
				 ;
		 
	}
	
}