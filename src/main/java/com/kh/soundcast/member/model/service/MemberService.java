package com.kh.soundcast.member.model.service;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.kh.soundcast.api.auth.model.dao.AuthDao;
import com.kh.soundcast.member.model.dao.MemberDao;
import com.kh.soundcast.member.model.vo.Member;
import com.kh.soundcast.member.model.vo.MemberBanner;
import com.kh.soundcast.member.model.vo.MemberExt;
import com.kh.soundcast.member.model.vo.ProfileImage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
	
	private final MemberDao dao;

	public void updateMemberProfile(HashMap<String, Object> params) {
		dao.updateMemberProfile(params);
	}

	public int insertMemberBanner(MemberBanner banner) {
		return dao.insertMemberBanner(banner);
		
	}

	public int insertMemberProfile(ProfileImage profile) {
		return dao.insertMemberProfile(profile);
		
	}

	public MemberExt selectModifyMember(int memberNo) {
		return dao.selectModifymember(memberNo);
	}
	
}
