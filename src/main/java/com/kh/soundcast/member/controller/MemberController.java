package com.kh.soundcast.member.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.soundcast.member.jwt.JwtProvider;
import com.kh.soundcast.member.model.service.AuthService;
import com.kh.soundcast.member.model.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
	
	private final MemberService memberService;

    @PostMapping("/modify")
    //@CrossOrigin(origins = "http://localhost:3000")
    public String updateProfile(
            @RequestParam(value = "backgroundImage", required = false) MultipartFile backgroundImage,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestParam("nickName") String nickName,
            @RequestParam("email") String email,
            @RequestParam("introduce") String introduce) {

        // 파일 처리
        if (backgroundImage != null) {
            System.out.println("Background Image: " + backgroundImage.getOriginalFilename());
            // 파일 저장 로직 추가
        }

        if (profileImage != null) {
            System.out.println("Profile Image: " + profileImage.getOriginalFilename());
            // 파일 저장 로직 추가
        }

        // 텍스트 데이터 처리
        System.out.println("NickName: " + nickName);
        System.out.println("Email: " + email);
        System.out.println("Introduce: " + introduce);

        // TODO: 데이터베이스에 저장하는 로직 추가

        return "Profile updated successfully";
    }
}
