package com.kh.soundcast.member.model.service;

import java.util.HashMap;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.soundcast.member.model.dao.AuthDao;
import com.kh.soundcast.member.model.vo.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

	private final AuthDao authDao;
	
	String socialId = null;
	String socialType = null;

	@Override
	public UserDetails loadUserByUsername(String userPks) throws UsernameNotFoundException {
		HashMap<String, Object> userPk;
		
		try {
			userPk = new ObjectMapper().readValue(userPks, new TypeReference<HashMap<String, Object>>() {});
			log.info("UserDatails userPk(HashMap) = {}", userPk);
			socialId =(String)userPk.get("socialId");
			socialType = (String)userPk.get("socialType");	
			
		
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	
		return (UserDetails) authDao.loadUserByUsername(socialType, socialId);
		
	}



}