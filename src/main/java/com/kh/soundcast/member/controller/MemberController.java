package com.kh.soundcast.member.controller;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import com.kh.soundcast.common.Utils;
import com.kh.soundcast.member.model.service.MemberService;
import com.kh.soundcast.member.model.vo.Comment;
import com.kh.soundcast.member.model.vo.MemberBanner;
import com.kh.soundcast.member.model.vo.MemberExt;
import com.kh.soundcast.member.model.vo.ProfileImage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService memberService;
	private final WebApplicationContext applicationContext;
	

	@Value("${file.upload-dir}")
	private String uploadBaseDir; // SoundCast 프로젝트 파일/src/main/resources/static/images 의 상대 경로
	// java.nio.file.Paths 클래스의 스태틱 메소드를 통해 폴더 or 파일의 절대 경로 얻어올 수 있음
	
	//마이 페이지
	  @PostMapping("/modify")
	    //@CrossOrigin(origins = "http://localhost:3000")
	    @Transactional(rollbackFor = {Exception.class})
	    public MemberExt updateProfile(
	            @RequestParam(value = "backgroundImage", required = false) MultipartFile backgroundImage,
	            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
	            @RequestParam HashMap<String, Object> params) throws Exception {
	    		
	    		log.debug("가져온 데이터들 - {}", params);
	    		
	    		Path path = FileSystems.getDefault().getRootDirectories().iterator().next();
	    		final String osRootPath = path.toString().replace("\\\\", "");
	    	
	    		// 현재 로그인한 회원의 회원 정보
	    		MemberExt loginMember = (MemberExt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

	    		final String bannerSavePath = osRootPath + uploadBaseDir + "images/member/banner/";
	    		final String profileSavePath = osRootPath + uploadBaseDir + "images/member/profile/";
	    		
	    		log.debug("배너 경로 - {}, 프로필 경로 - {}", bannerSavePath, profileSavePath);
	    		
	    		// 파일 처리
	            // MEMBER_BANNER 테이블에만 삽입
	    		if (backgroundImage != null && !backgroundImage.isEmpty()) {
	    			System.out.println("Background Image: " + backgroundImage.getOriginalFilename());
	    			
	    			// PROFILE_IMAGE 테이블에 파일 정보 저장하는 로직
	    			// 서버에는 같은 이름의 파일이 여러개 존재 불가하므로, 저장할 파일 명을 랜덤으로 바꿔 저장
	    			// 랜덤 이름이 담기는 변수가 changeName
	    			String changeBannerName = Utils.saveFile(backgroundImage, bannerSavePath);
	    			// 파일 저장 로직 추가
	    			// 백엔드에서 DB로 보내야할 데이터는 상대경로 + 바뀐 파일명 하나 뿐.
	    			if(changeBannerName == null) {
	    				throw new Exception();
	    			}
	    			
	    			MemberBanner memberBanner = new MemberBanner(0, bannerSavePath + changeBannerName);
	    			
	    			memberService.insertMemberBanner(memberBanner);
	    			
	    			params.put("memberBannerNo", memberBanner.getMemberBannerNo());
	    			
	    		}else {
	    			System.out.println("변경사항이 없습니다.");
	    		}
	    		
	    		// PROFILE_IMAGE 테이블에만 삽입
	    		if (profileImage != null) {
	    			System.out.println("Profile Image: " + profileImage.getOriginalFilename());
	    			
	    			String changeProfileName = Utils.saveFile(profileImage, profileSavePath);
	    			// 파일 저장 로직 추가
	    			
	    			if(changeProfileName == null) {
	    				throw new Exception();
	    			}
//	    			String profilePath = profileSavePath + "/" + changeProfileName;
	    			ProfileImage memberProfile = new ProfileImage(0, profileSavePath + changeProfileName);  
	    			
	    			memberService.insertMemberProfile(memberProfile);
	    			
	    			params.put("memberProfileImageNo", memberProfile.getProfileImageNo());
	    			
	    		}else {
	    			System.out.println("변경사항이 없습니다.");
	    		}
	    		
	    		// 텍스트 데이터 처리
	    		System.out.println("NickName: " + params.get("nickName"));
	    		System.out.println("Email: " + params.get("email"));
	    		System.out.println("Introduce: " + params.get("introduce"));
	    		
	    		try {
	    			//방법1) 아래 처럼 nickName, email, introduce, memberBannerNo, memberProfileImageNo 각각 넘겨서 update 호출
	                memberService.updateMemberProfile(params); // MEMBER 테이블만 수정
	                //방법2) 미리 5개의 변수를 HashMap에 넣어서 update에 매개변수로 넘기기
	                //방법3) 아예 MemberExt 객체에 담아서 update에 매개변수로 넘기기.
	                // 위 3가지 방법 중 한가지 선택
	                MemberExt modifyMember = memberService.selectModifyMember(loginMember.getMemberNo());
	                
	                log.debug("수정된 회원 정보 - {}, {}", modifyMember, modifyMember.getMemberNickname());
	                
	                // 성공 시 변경된 회원 정보 보내기
	                return modifyMember;
	            } catch (Exception e) {
	                log.error("Failed to update profile", e);
	                return null;
	            }
	    		
	    	
	    }
	
	
	//유저 인포 페이지 
	@GetMapping("/memberInfo/{memberNo}")
	public MemberExt selectOneMember(
			@PathVariable String memberNo
			){
		log.info("memberNo={}",memberNo);
		int mNo = Integer.parseInt(memberNo);
		MemberExt member = memberService.selectOneMember(mNo);
		log.info("memberselect={}",member);
		
	
//		HashMap<String, Object> map = new HashMap<>();
//		
//		if(member == null && member.getMemberNo() == 0) {
//			map.put("msg", "회원 정보를 불러올 수 없습니다.");
//		} else if(member == null && member.getFollowing() == null) {
//			map.put("msg", "회원 팔로우 목록을 불러올 수 없습니다.");
//		}
//		
//		map.put("member", member);
		
		if(member.getFollowing().get(0) == null) {
			member.setFollowing(new ArrayList<MemberExt>());
		}
		
		return member;
	}
	
	@PostMapping("/follow/{memberNo}")
	public Map<String, Object> inesrtFollow(
			@PathVariable String memberNo,
			@RequestBody HashMap<String, Object> param
			){
		log.info("followParam={}",param);
		int mNo = Integer.parseInt(memberNo);
		param.put("mNo", mNo);
		
		Map<String, Object> map  = new HashMap<>();
		
		int result = memberService.insertFollow(param);
		
		if(result>0) {
			map.put("msg", "팔로우 성공");
		}else {
			map.put("msg", "팔로우 실패");
		}
		
		return map;
	}
	
	
	@DeleteMapping("/unfollow/{memberNo}")
	public Map<String, Object> deleteFollow(
			@PathVariable String memberNo,
			@RequestParam HashMap<String, Object> param
			){
		log.info("unfollowparam={}", param);
		int mNo = Integer.parseInt(memberNo);
		param.put("mNo", mNo);
		
		int result = memberService.deleteFollow(param);
		
		Map<String, Object> map  = new HashMap<>();
		if(result<0) {
			map.put("msg", "언팔로우 실패");
		}
		
		return map;
		
		
		
	}
	
	@PostMapping("/comment/insert/{memberNo}")
	public Map<String, Object> insertComment(
			@PathVariable String memberNo,
			@RequestBody HashMap<String, Object> param
			){
		int mNo = Integer.parseInt(memberNo);
		param.put("mNo", mNo);
		
		log.info("commentParam={}", param);	
		
		int result = memberService.insertComment(param);
		
		Map<String, Object> map  = new HashMap<>();
		
		if(result>0) {
			map.put("msg", "댓글 등록 완료");
		}else {
			map.put("msg", "댓글 등록 오류");
		}
		
		return map;
		
	}
	
	@DeleteMapping("/comment/delete/{commentNo}")
	public Map<String,Object> deleteComment(
			@PathVariable int commentNo,
			@RequestParam HashMap<String, Object>param
			){
		log.info("deletecomment param = {}",param);
		
		param.put("commentNo", commentNo);
		
		int result = memberService.deleteComment(param);
		
		Map<String, Object> map  = new HashMap<>();
		
		if(result>0) {
			map.put("msg","댓글 삭제 성공");
		}else {
			map.put("msg", "댓글 삭제 실패");
		}
		
		
		
		return map;
		
	}
	

	
	
	
	
	
	
	
	
	
	
	
}