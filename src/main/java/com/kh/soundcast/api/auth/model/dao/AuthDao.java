package com.kh.soundcast.api.auth.model.dao;

import java.util.HashMap;
import java.util.List;
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
	
	public MemberExt loadUserByUsername(String socialId, String socialType) {
		Map<String,Object> param = new HashMap<>();
		param.put("socialId", socialId);
		param.put("socialType", socialType);
		System.out.println("------------------------------"+param);
		
		return session.selectOne("auth.loadUserByUsername",param);
	}
	
	
	public int insertMember(MemberExt m) {
		return session.insert("auth.insertMember",m);	
	}

	
	public int insertProfileImage(MemberExt m) {
		return session.insert("auth.insertProfileImage",m);
	}

	
	public int insertMemberSocial(MemberExt m) {
		return session.insert("auth.insertMemberSocial",m);
	}
	
	
	public int insertMemberGrade(MemberExt m) {
		return session.insert("auth.insertMemberGrade",m);
	}
	
	
	public void updateMember(Member m) {
		session.update("auth.updateMember",m);
	}
	
	
	public List<MemberExt> selectFollowList(int mNo) {
		return session.selectList("member.selectFollowList", mNo);
	}

	public int selectFollower(int mNo) {
		if(session.selectOne("member.selectFollowerCount", mNo) == null) {
			return 0;
		}
		
		return session.selectOne("member.selectFollowerCount", mNo);
	}
}
