package com.kh.soundcast.api.auth.model.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.kh.soundcast.api.auth.model.dto.GoogleUserInfoResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GoogleApi {
	
	@Value("${jwt.secret}")
	private String secretKey;
	@Value("{google.client.id")
	private String clientId;
	
}
