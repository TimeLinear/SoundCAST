package com.kh.soundcast.song.model.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kh.soundcast.song.model.dao.SongDao;
import com.kh.soundcast.song.model.vo.Genre;
import com.kh.soundcast.song.model.vo.Mood;
import com.kh.soundcast.song.model.vo.Song;

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

	
	
}
