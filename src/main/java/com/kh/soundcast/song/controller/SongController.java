package com.kh.soundcast.song.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.soundcast.song.model.service.SongService;
import com.kh.soundcast.song.model.vo.Genre;
import com.kh.soundcast.song.model.vo.Mood;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SongController {
	
	private final SongService service;
	
//	@CrossOrigin(origins= {"*"})
//	@GetMapping("/selectAll")
//	public List selectSongList() {
//		
//		List list = service.selectSongList();
//		
//		return list;
//	}
//		
//	@CrossOrigin(origins = {"*"})
//	@GetMapping("/selectSong/{songNo}")
//	public Song selectSong(@PathVariable int songNo) {
//		
//		log.info("songNo ? {}", songNo);		
//		Song selectedSong = service.selectSong(songNo);
//		
//		return selectedSong;
//	}
//		
//	@CrossOrigin(origins = {"*"})
//	@GetMapping("/searchSong/{searchKeyword}")
//	public List selectSongList(@PathVariable String keyword) {
//		
//		log.info("keyword ? {}" , keyword);
//		
//		return null;
//	}
	
	
	@CrossOrigin(origins = {"http://localhost:3000"})
	@GetMapping("/genres")
	public List<Genre> selectAllGenres() {
		
		List<Genre> genres = service.selectAllGenres();
		log.info("genres ? {}", genres);
		
		return genres;
	}
	
	@CrossOrigin(origins = {"http://localhost:3000"})
	@GetMapping("/moods")
	public List<Mood> selectAllMoods(){
		
		List<Mood> moods = service.selectAllMoods();
		
		return moods;
	}
	
	
	
}





























