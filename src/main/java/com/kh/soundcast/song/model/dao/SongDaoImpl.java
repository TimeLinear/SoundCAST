package com.kh.soundcast.song.model.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.kh.soundcast.song.model.vo.Genre;
import com.kh.soundcast.song.model.vo.Mood;
import com.kh.soundcast.song.model.vo.Song;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SongDaoImpl implements SongDao{

	private final SqlSession session;

	@Override
	public Song selectSong(int songNo) {
		return session.selectOne("song.selectSong", songNo);
	}
	
	@Override
	public List<Song> selectSongList(HashMap<String, Object> param) {
		return session.selectList("song.selectSongList", param);
	}

	@Override
	public List<Genre> selectAllGenres() {
		return session.selectList("song.selectAllGenres");
	}

	@Override
	public List<Mood> selectAllMoods() {
		return session.selectList("song.selectAllMoods");
	}

	
}
