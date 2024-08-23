package com.kh.soundcast.member.model.dao;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.soundcast.member.model.vo.Comment;
import com.kh.soundcast.member.model.vo.Follow;
import com.kh.soundcast.member.model.vo.MemberExt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
@RequiredArgsConstructor
public class MemberDao {
	
	private final SqlSessionTemplate session;
	
	public MemberExt selectOneMember(int mNo) {
		
		return session.selectOne("member.selectOneMember",mNo);
	}

	public int insertFollow(HashMap<String, Object> param) {
		
		return session.insert("member.insertFollow", param);
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

	public int deleteFollow(HashMap<String, Object> param) {
		return session.delete("member.deleteFollow",param);
	}

	public int insertComment(HashMap<String, Object> param) {
		
		return session.insert("member.insertComment", param);
	}

	public List<MemberExt> selectComment(int mNo) {
		
		return session.selectList("member.selectComment",mNo);
	}

	
}
