package com.kh.soundcast.song.model.service;

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
	public List selectSongList() {
		
		return dao.selectSongList();
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
