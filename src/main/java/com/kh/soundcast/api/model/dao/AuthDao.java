package com.kh.soundcast.api.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.kh.soundcast.member.model.dto.GoogleUserInfoResponse;
import com.kh.soundcast.member.model.vo.Comment;
import com.kh.soundcast.member.model.vo.Member;
import com.kh.soundcast.member.model.vo.MemberExt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AuthDao {

	private final SqlSessionTemplate session;

	public MemberExt loadUserByUsername(String socialType, String socialId) {
		Map<String,Object> param = new HashMap<>();
		param.put("socialId", socialId);
		param.put("socialType", socialType);
		
		log.info("param={}",param);
		return session.selectOne("auth.loadUserByUsername", param);
		
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

	public List<MemberExt> selectFollowList(int mNo) {
		return session.selectList("member.selectFollowList", mNo);
	}

	public int selectFollower(int mNo) {
		if(session.selectOne("member.selectFollowerCount", mNo) == null) {
			return 0;
		}
		
		return session.selectOne("member.selectFollowerCount", mNo);
	}

	public List<MemberExt> selectComment(int mNo) {
		return session.selectList("member.selectComment", mNo);
	}


//	public void countFollow(String socialType, String sub) {
//		Map<String,Object> param = new HashMap<>();
//		param.put("socialId", sub);
//		param.put("socialType", socialType);
//		
//		session.selectList("auth.countFollow",param );
//		
//	}
//
//	public void countFollower(String socialType, String sub) {
//		Map<String,Object> param = new HashMap<>();
//		param.put("socialId", sub);
//		param.put("socialType", socialType);
//		
//		session.selectList("auth.countFollower",param );
//		
//	}

	
	
	
	
	

}
