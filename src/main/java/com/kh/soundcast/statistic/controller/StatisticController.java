package com.kh.soundcast.statistic.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.kh.soundcast.member.controller.MemberController;
import com.kh.soundcast.member.model.service.MemberService;
import com.kh.soundcast.song.model.service.SongService;
import com.kh.soundcast.statistic.model.service.StatisticService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RestController
@RequestMapping("/statistic")
@RequiredArgsConstructor
public class StatisticController {
	
	private final StatisticService stService;
	
	@CrossOrigin(origins = {"http://localhost:3000"})
	@GetMapping("/download")
	public List<Map<String, Object>> getDownload(){
		
		List<Map<String, Object>> result = stService.getDownload();
		return result;
	}
	
}
