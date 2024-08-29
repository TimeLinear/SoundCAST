package com.kh.soundcast.report.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.soundcast.report.model.service.ReportService;
import com.kh.soundcast.song.model.vo.ReportExt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

private final ReportService reportService;
	
	@CrossOrigin(origins = {"*"})
	@GetMapping("/recentreport")
	public List<ReportExt> selectRecentReport(){
		
		List<ReportExt> list = reportService.selectRecentReport();
		log.debug("list {}", list);

		return list;
	}
	
}
