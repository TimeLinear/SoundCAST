package com.kh.soundcast.member.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.soundcast.member.model.service.MemberService;
import com.kh.soundcast.member.model.vo.Comment;
import com.kh.soundcast.member.model.vo.MemberExt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService memberService;
	

	@GetMapping("/memberInfo/{memberNo}")
	public MemberExt selectOneMember(
			@PathVariable String memberNo
			){
		log.info("memberNo={}",memberNo);
		int mNo = Integer.parseInt(memberNo);
		MemberExt member = memberService.selectOneMember(mNo);
		log.info("memberselect={}",member);
		
//		
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
	
	@PostMapping("/comment/{memberNo}")
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
	
	
//	@GetMapping("/commentList/{memberNo}")
//	public List<MemberExt> commentList(
//			@PathVariable String memberNo
//			){
//		
//	}
//	

	
	
	
	
	
	
	
	
	
	
	
}
