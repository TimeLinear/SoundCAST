package com.kh.soundcast.member.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import com.kh.soundcast.api.auth.jwt.JwtProvider;
import com.kh.soundcast.api.auth.model.service.AuthService;
import com.kh.soundcast.common.Utils;
import com.kh.soundcast.member.model.service.MemberService;
import com.kh.soundcast.member.model.vo.MemberBanner;
import com.kh.soundcast.member.model.vo.MemberExt;
import com.kh.soundcast.member.model.vo.ProfileImage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
	
	private final MemberService memberService;
	private final WebApplicationContext applicationContext;
	
	@Value("${file.upload-dir}")
	private String uploadBaseDir; // SoundCast 프로젝트 파일/src/main/resources/static/images 의 상대 경로
	// java.nio.file.Paths 클래스의 스태틱 메소드를 통해 폴더 or 파일의 절대 경로 얻어올 수 있음
	
	
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
//    			String profilePath = profileSavePath + "/" + changeProfileName;
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
}
