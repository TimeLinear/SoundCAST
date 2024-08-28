package com.kh.soundcast.statistic.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.soundcast.song.model.service.SongService;
import com.kh.soundcast.song.model.vo.SongExt;
import com.kh.soundcast.statistic.model.service.StatisticService;
import com.kh.soundcast.statistic.model.vo.DownloadCount;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/statistic")
@RequiredArgsConstructor
public class StatisticController {

//	private final StatisticService statisticService;
//	
//	@CrossOrigin(origins = {"*"})
//	@GetMapping("/top5downloadcount")
//	public List<DownloadCount> selectTop5DownloadCount(/*HttpServletResponse response*/){
//		
//		List<DownloadCount> list = statisticService.selectTop5DownloadCount();
//		log.debug("list {}", list);
//
//		return list;
//	}
}
