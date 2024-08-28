package com.kh.soundcast.member.model.service;

import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import com.kh.soundcast.common.Utils;
import com.kh.soundcast.member.model.dao.MemberDao;
import com.kh.soundcast.member.model.dto.GoogleUserInfoResponse;
import com.kh.soundcast.member.model.vo.Comment;
import com.kh.soundcast.member.model.vo.Follow;
import com.kh.soundcast.member.model.vo.MemberBanner;
import com.kh.soundcast.member.model.vo.MemberExt;
import com.kh.soundcast.member.model.vo.ProfileImage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
	
	private final MemberDao memberDao;
	private final WebApplicationContext applicationContext;

	@Transactional(rollbackFor = Exception.class)
	public void updateMemberProfile(HashMap<String, Object> params) {
		memberDao.updateMemberProfile(params);
	}

	@Transactional(rollbackFor = Exception.class)
	public int insertMemberBanner(MemberBanner banner) {
		return memberDao.insertMemberBanner(banner);
		
	}

	@Transactional(rollbackFor = Exception.class)
	public int insertMemberProfile(ProfileImage profile) {
		return memberDao.insertMemberProfile(profile);
		
	}


	public MemberExt selectModifyMember(int memberNo) {
		return memberDao.selectModifymember(memberNo);
	}
	
	public MemberExt selectOneMember(int mNo) {
		
		MemberExt member = memberDao.selectOneMember(mNo);
		
		List<MemberExt> following = memberDao.selectFollowList(mNo);
		
		member.setFollowing(following);
		
		int follower = memberDao.selectFollower(mNo);
		
		member.setFollower(follower);
		
		List<MemberExt> comment = memberDao.selectComment(mNo);
		
		if(!comment.isEmpty()) {
			String commentText = comment.get(0).getComment().getCommentText();
			String safeCommentText = Utils.newLineHandling(Utils.XSSHandling(commentText));  
			
			comment.get(0).getComment().setCommentText(safeCommentText);
			
			member.setCommentList(comment);
		}
			
		member.setCommentList(comment);
		
		if(!(member.getMemberIntroduce()==null)) {
			String introduce = member.getMemberIntroduce();
			String safeMemberIntroduce = Utils.newLineHandling(Utils.XSSHandling(introduce));
		member.setMemberIntroduce(safeMemberIntroduce);
		
		}
		
		
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
		
		String comment = (String) param.get("comment");
		
		String safeComment = Utils.newLineClear(comment);
		
		param.put("safeComment", safeComment);
		
		return memberDao.insertComment(param);
	}

	@Transactional(rollbackFor = Exception.class)
	public int deleteComment(HashMap<String, Object> param) {
		
		return memberDao.deleteComment(param);
	}

}
