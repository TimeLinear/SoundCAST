package com.kh.soundcast.song.model.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kh.soundcast.song.model.dao.SongDao;
import com.kh.soundcast.song.model.vo.Genre;
import com.kh.soundcast.song.model.vo.Mood;
import com.kh.soundcast.song.model.vo.Song;
import com.kh.soundcast.song.model.vo.SongExt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {
	
private final SongDao dao;
	
	@Override
	public List<Song> selectSongList(HashMap<String, Object> param) {
		
		//param의 keyword=&mood=0&genre=0 일 경우 메인화면으로 인식 
		if((param.get("keyword").equals("") || param.get("keyword") == null)
			&& (Integer.parseInt(String.valueOf(param.get("genre"))) < 1)
			&& (Integer.parseInt(String.valueOf(param.get("mood"))) < 1)
				) {
			
			log.info("메인");
			int placeNo = Integer.parseInt(String.valueOf(param.get("placeNo")));
			return dao.selectSongMainList(placeNo);
			
		} else {
			log.info("검색");
			return dao.selectSongList(param);
		}
		
	}
	
	@Override
	public List<Genre> selectAllGenres() {
		return dao.selectAllGenres();
	}

	@Override
	public List<Mood> selectAllMoods() {
		return dao.selectAllMoods();
	}

	@Override
	public int updateSong(int songNo, SongExt song) {
		//타이틀, 곡설명, 라이센스, 장르, 분위기만 변경하는 경우, song 테이블에 들어간 내용만 변경된 경우 
		
		Song updateSong = new Song();
		updateSong.setSongNo(songNo);
		updateSong.setSongMoodNo(song.getSongMoodNo());
		updateSong.setSongGenreNo(song.getSongGenreNo());
		updateSong.setSongTitle(song.getSongTitle());
		updateSong.setSongLicense(song.getSongLicense());
		updateSong.setSongDetail(song.getSongDetail());
		
		log.info("updateSong ? {}", updateSong);
		
		int result = dao.updateSongBasicInfo(updateSong);		
		
		//multipartfile로 songfile, songimage 파일을 받음. 
		//utils의 save 메소드 이용하여 이름 변경 후 정해진 폴더위치에 각각 저장
		// 
		
		
		//이미지 변경하는 경우 (x->)
		
		//song.getSongFile()
		
		//음악 파일 변경하는 경우
		
		
		
		
		
		
		return 0;
		
	}

	
	
}
