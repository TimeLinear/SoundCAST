package com.kh.soundcast.api.auth.model.dao;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.kh.soundcast.common.Utils;
import com.kh.soundcast.member.model.vo.Member;
import com.kh.soundcast.member.model.vo.MemberExt;
import com.kh.soundcast.member.model.vo.ProfileImage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AuthDao {

private final SqlSessionTemplate session;
	
	@Value("${file.upload-dir}")
	private String uploadBaseDir; 

	public MemberExt loadUserByUsername(String socialType, String socialId) {
		log.debug("전달된 loadUser param - socialType={}, socialId={}", socialType, socialId);
		Map<String,Object> param = new HashMap<>();
		param.put("socialType", socialType);
		param.put("socialId", socialId);
		
		log.info("Dao param={}",param);
		return session.selectOne("auth.loadUserByUsername", param);
		
	}

	public int insertMember(MemberExt m) {
		return session.insert("auth.insertMember",m);	
	}

	public int insertProfileImage(MemberExt m) {
		
		Path path = FileSystems.getDefault().getRootDirectories().iterator().next();
		log.info("Path={}",path);
		final String osRootPath = path.toString().replace("\\\\", "");
		log.info("osRootPath={}",osRootPath);
		
		final String profileSavePath = osRootPath + uploadBaseDir + "images/member/profile/";
		
		String fileUrl = m.getProfileImage().getProfileImagePath();
		
		String changeProfileName = "images/member/profile/"+Utils.urlSaveFile(fileUrl, profileSavePath);
		
		log.info("changeProfileName= {}", changeProfileName);
		
		m.getProfileImage().setProfileImagePath(changeProfileName);
		
		return session.insert("auth.insertProfileImage",m);
		
	}
	
	public int insertMemberSocial(MemberExt m) {
		log.debug("삽입할 소셜 정보 - {}", m);
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


	public void countFollow(String socialType, String sub) {
		Map<String,Object> param = new HashMap<>();
		param.put("socialId", sub);
		param.put("socialType", socialType);
		
		session.selectList("auth.countFollow",param );
		
	}

	public void countFollower(String socialType, String sub) {
		Map<String,Object> param = new HashMap<>();
		param.put("socialId", sub);
		param.put("socialType", socialType);
		
		session.selectList("auth.countFollower",param );
		
	}
	
}
