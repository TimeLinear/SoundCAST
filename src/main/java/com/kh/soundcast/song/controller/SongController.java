package com.kh.soundcast.song.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
	

	//키워드가 있는 ?
	@CrossOrigin(origins = {"*"})
	@GetMapping("/search/{placeNo}/{genreNo}/{moodNo}/{keyword}")
	public List<Song> selectSongList(
			@PathVariable int placeNo,
			@PathVariable int genreNo,
			@PathVariable int moodNo,
			@PathVariable String keyword			
			) {

		log.info("placeNo ? {}", placeNo);
		log.info("genreNo ? {}", genreNo);
		log.info("moodNo ? {}", moodNo);
		log.info("keyword ? {}", keyword);
		
		HashMap<String, Object> param = new HashMap<>();
		param.put("placeNo", placeNo);
		param.put("genreNo", genreNo);
		param.put("moodNo", moodNo);
		param.put("keyword", keyword.toUpperCase());
		
		List<Song> result = service.selectSongList(param);
		
		log.info("result ? {}", result);
		
		return result;
		
	}
	
	//키워드가 없는 주소를 만들어야 하나..?
	
	
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





























