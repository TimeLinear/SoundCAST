package com.kh.soundcast.song.model.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kh.soundcast.song.model.dao.SongDao;
import com.kh.soundcast.song.model.vo.Genre;
import com.kh.soundcast.song.model.vo.Mood;
import com.kh.soundcast.song.model.vo.Song;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {
	
private final SongDao dao;
	
	@Override
	public Song selectSong(int songNo) {
		return dao.selectSong(songNo);
	}
	
	@Override
	public List<Song> selectSongList(HashMap<String, Object> param) {
		return dao.selectSongList(param);
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
