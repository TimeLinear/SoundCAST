package com.kh.soundcast.api.auth.model.service;

import java.util.HashMap;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.soundcast.api.auth.model.dao.AuthDao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
	
	private final AuthDao dao;
	
	@Override
	public UserDetails loadUserByUsername(String userPkSubject) throws UsernameNotFoundException{
		// userPkSubject => HashMap에 socialType:소셜명, socialId:소셜 고유 아이디 담아서
		// json 객체 형태로 문자열화 한 것
		// 즉, userPkSubject를 사용하려면 json 객체 형태의 문자열을 다시 역으로 해쉬맵으로 바꿔야한다.
		
		HashMap<String, Object> userPk = null;
		try {
			userPk = new ObjectMapper().readValue(userPkSubject, HashMap.class);
			// socialType과 socialId가 들어있는 해쉬맵
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			log.debug("고유 사이트 유저 토큰 오염");
			return null;
		}
		
		return dao.loadUserByUsername((String)userPk.get("socialId"), (String)userPk.get("socialType"));
	}
}
