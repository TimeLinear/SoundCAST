package com.kh.soundcast.member.model.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.soundcast.member.model.dao.MemberDao;
import com.kh.soundcast.member.model.vo.Comment;
import com.kh.soundcast.member.model.vo.Follow;
import com.kh.soundcast.member.model.vo.MemberExt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
	
	private final MemberDao memberDao;
	
	public MemberExt selectOneMember(int mNo) {
		
		MemberExt member = memberDao.selectOneMember(mNo);
		
		List<MemberExt> following = memberDao.selectFollowList(mNo);
		
		member.setFollowing(following);
		
		int follower = memberDao.selectFollower(mNo);
		
		member.setFollower(follower);
		
		List<MemberExt> comment = memberDao.selectComment(mNo);
		
		member.setCommentList(comment);
		
		log.info("commentList={}", member.getCommentList());
		
		return member;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public int insertFollow(HashMap<String, Object> param) {
		return memberDao.insertFollow(param);
	}

	@Transactional(rollbackFor = Exception.class)
	public int deleteFollow(HashMap<String, Object> param) {
		return memberDao.deleteFollow(param);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public int insertComment(HashMap<String, Object> param) {
		return memberDao.insertComment(param);
	}

	

}
