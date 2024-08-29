package com.kh.soundcast.song.controller;

import java.net.MalformedURLException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;										   
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import com.kh.soundcast.song.model.service.SongService;
import com.kh.soundcast.song.model.vo.Genre;
import com.kh.soundcast.song.model.vo.Mood;
import com.kh.soundcast.song.model.vo.Song;
import com.kh.soundcast.song.model.vo.SongExt;
import com.kh.soundcast.statistic.model.vo.Download;						

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
	public ResponseEntity<String> updateSong(
			@PathVariable int songNo,
			@RequestPart("songInfo") SongExt songInfo,
			@RequestPart(required = false, value="songFile") MultipartFile songFile,
			@RequestPart(required = false, value="songImage") MultipartFile songImage
			) {
		
		log.info("songNo ? {}", songNo);
		log.info("songInfo ? {}", songInfo);
		log.info("songFile ? {}", songFile);
		log.info("songImage ? {}", songImage);
	
		
		int result = service.updateSong(songNo, songInfo, songFile, songImage);
		
		if(result > 0) {
			return ResponseEntity.ok().body("수정 성공 하였습니다.");
		} else {
			return ResponseEntity.notFound().build();
		}
		
	}
	
	
	@GetMapping("/memberSongList/{memberNo}")
	public List<Song> getMemberSongList(
			@PathVariable String memberNo
			) {
		int mNo = Integer.parseInt(memberNo);
		log.info("songListMno={}", mNo);
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
	
	
	
	@Value("${file.upload-dir}")
	private String uploadBaseDir;
	
	@CrossOrigin(origins = {"http://localhost:3000"})
	@GetMapping("/download/{songNo}")
	public ResponseEntity<Resource> downloadSong(
			@PathVariable int songNo,
			@RequestParam int memberNo
			) {
		
		//다운로드 기록 확인 후 다운로드 insert (미로그인 시에는 다운로드 기록 안함. memberNo==0 => 미로그인 상태)
		if(memberNo != 0) {
			//다운로드 기록 검색
			log.info("memberNo ? {}", memberNo);
			
			HashMap<String, Object> param = new HashMap<>();
			param.put("songNo", songNo);
			param.put("memberNo", memberNo);
			
			List<Download> history = service.checkDownload(param);
			
			log.info("history ? {}", history);
			
			//위 select 결과가 null 일때 insert			
			if(history == null || history.size() == 0) {
				int result = service.insertDownload(param);
				if(result < 1) {
					log.info("다운로드 기록 실패");
				}
			}
		}
		//다운로드 파일 보내기 
		log.info("songNo ? {}", songNo);
		
		SongExt song = service.selectSong(songNo);
		log.info("song ? {}", song);
		
		Path path = FileSystems.getDefault().getRootDirectories().iterator().next();
		final String osRootPath = path.toString().replace("\\\\", "");
		
		String fileReadPath = "file:///"+osRootPath+uploadBaseDir+song.getSongFile().getSongPathName()
								+song.getSongFile().getSongFileChangeName();
		log.info("fileReadPath ? {}", fileReadPath);
		
		try {
			Resource resource = new UrlResource(fileReadPath);
			
			if(!resource.exists()) {
				return ResponseEntity.notFound().build();
			}
			
			String contentType = "application/octet-stream";
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
			
			return ResponseEntity.ok()
					.headers(headers)
					.contentType(org.springframework.http.MediaType.parseMediaType(contentType))
					.body(resource);			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
	}
	
	
	
	
}





























