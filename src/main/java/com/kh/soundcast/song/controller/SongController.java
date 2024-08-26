package com.kh.soundcast.song.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	
	@PostMapping("/upload")
	public SongExt uploadSong(
		@RequestBody MultipartFile songFile,
		@RequestBody MultipartFile songImage,
		@RequestBody HashMap<String, Object> params
			) {
		
		
		return null;
	}
}
