package com.kh.soundcast.song.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.soundcast.song.model.service.SongService;
import com.kh.soundcast.song.model.vo.SongExt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/song")
@RequiredArgsConstructor
public class SongController {

	private final SongService songService;
	
	@CrossOrigin(origins = {"*"})
	@GetMapping("/top5Music")
	public List<SongExt> selectTop5Music(/*HttpServletResponse response*/){
		
		List<SongExt> list = songService.selectTop5Music();
		log.debug("list {}", list);

		return list;
	}
	
	@CrossOrigin(origins = {"*"})
	@GetMapping("/newMusic")
	public List<SongExt> selectNewMusic(/*HttpServletResponse response*/){
		
		List<SongExt> list = songService.selectNewMusic();
		log.debug("list {}", list);

		return list;
	}
	
}
