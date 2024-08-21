package com.kh.soundcast.member.model.dao;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.soundcast.member.model.vo.Member;
import com.kh.soundcast.member.model.vo.MemberExt;
import com.kh.soundcast.member.model.vo.ProfileImage;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AuthDao {
	private final SqlSessionTemplate session;
	
	public Member loadUserByUsername(String socialId, String socialType) {
		Map<String,Object> param = new HashMap<>();
		param.put("socialId", socialId);
		param.put("socialType", socialType);
		System.out.println("------------------------------"+param);
		
		return session.selectOne("auth.loadUserByUsername",param);
	}
	
	
	
	public void insertMember(Member m) {
		
		session.insert("auth.insertMember", m);
	}



	public void insertProfileImage(ProfileImage profileImage) {
		session.insert("auth.insertProfileImage",profileImage );
		
	}



	public void insertMemberSocial(MemberExt m) {
		session.insert("auth.insertMemberSocial",m);
		
	}
	
	public void updateMember(Member m) {
		session.update("auth.updateMember",m);
	}
	
}
