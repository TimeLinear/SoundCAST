package com.kh.soundcast.member.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.soundcast.member.model.vo.MemberBanner;
import com.kh.soundcast.member.model.vo.MemberExt;
import com.kh.soundcast.member.model.vo.ProfileImage;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberDao {

	private final SqlSessionTemplate session;

	public void updateMemberProfile(HashMap<String, Object> params) {
		session.update("member.updateMemberProfile",params);
		
	}

	public int insertMemberBanner(MemberBanner banner) {
		return session.insert("member.insertMemberBanner",banner);
		
	}

	public int insertMemberProfile(ProfileImage profile) {
		return session.insert("member.insertMemberProfile",profile);
		
	}

	public MemberExt selectModifymember(int memberNo) {
		return session.selectOne("member.selectModifymember",memberNo);
	}
	
	// 관리자 페이지 시작
	public List<MemberExt> selectMembers() {
		return session.selectList("member.selectMembers");
	}

	public List<MemberExt> searchMembers(Map<String, Object> param) {
		return session.selectList("member.searchMembers", param);
	}

	public int deleteMembers(List<Long> deleteList) {
		return session.update("member.deleteMembers", deleteList);
	}
	// 관리자 페이지 끝
}
