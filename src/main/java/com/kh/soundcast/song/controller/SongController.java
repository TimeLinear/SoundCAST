package com.kh.soundcast.song.controller;

import java.io.FileNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.soundcast.common.Utils;
import com.kh.soundcast.song.model.service.SongService;
import com.kh.soundcast.song.model.vo.Genre;
import com.kh.soundcast.song.model.vo.Mood;
import com.kh.soundcast.song.model.vo.Report;
import com.kh.soundcast.song.model.vo.Song;
import com.kh.soundcast.song.model.vo.SongExt;

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
	
	
	@CrossOrigin(origins = {"http://localhost:3000"})
	@PutMapping("/update/{songNo}")
	public int updateSong(
			@PathVariable int songNo,
			@RequestBody SongExt song
			) {
		
		log.info("songNo ? {}", songNo);
		log.info("song ? {}", song);
		
		int result = service.updateSong(songNo, song);
		
		
		return result;
	}
	
	@GetMapping("/memberSongList/{memberNo}")
	public List<Song> getMemberSongList(
			@PathVariable String memberNo
			) {
		int mNo = Integer.parseInt(memberNo);
		List<Song> song = service.getMemberSongList(mNo);
		
		log.info("songList?={}", song);
		return song;
		
	}
	
	@PostMapping("/unofficial/upload")
	public SongExt uploadUnOfficialSong(
		@RequestPart(value = "songFile", required = false) MultipartFile songFile,
	    @RequestPart(value = "songImage", required = false) MultipartFile songImage, 
	    @RequestPart("song") Song song
			) throws Exception {
		
		log.debug("song 정보 - {}", song);
		
		SongExt insertedSong = service.insertUnofficialSong(songFile, songImage, song);
		
		return insertedSong;
	}
	
	@DeleteMapping("/delete")
	public int takeDownSong(
		@RequestParam int songNo
			) {
		return service.updateSongStatus(songNo);
	}
	
	@PostMapping("/report")
	public int reportSong(
		@RequestBody Report report
			) {
		service.insertReport(report);
		log.debug("삽입 결과 - {}", report);
		return report.getReportNo();
	}
}
