package com.kh.soundcast.song.controller;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.soundcast.song.model.service.SongService;
import com.kh.soundcast.song.model.vo.Genre;
import com.kh.soundcast.song.model.vo.Mood;
import com.kh.soundcast.song.model.vo.Song;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/song")
@RequiredArgsConstructor
public class SongController {
	
private final SongService service;
	
	@CrossOrigin(origins = {"http://localhost:3000"})
	@GetMapping("/search")
	public List<Song> selectSongList(
			@RequestParam String keyword,
			@RequestParam int genre,
			@RequestParam int mood,
			@RequestParam int placeNo 
			){
		
		log.info("keyword ? {}", keyword);
		log.info("genre ? {}", genre);
		log.info("mood ? {}", mood);
		log.info("placeNo ? {}", placeNo);
		
		HashMap<String, Object> params = new HashMap<>();
		
		params.put("keyword", keyword.toUpperCase());
		params.put("genre", genre);
		params.put("mood", mood);
		params.put("placeNo", placeNo);
		
		List<Song> result = service.selectSongList(params);
		
		log.info("result ? {}", result);
		log.info("size ? {}", result.size());		
		
		return result;
	}
	

	
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





























