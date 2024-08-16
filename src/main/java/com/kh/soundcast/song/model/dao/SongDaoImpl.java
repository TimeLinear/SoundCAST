package com.kh.soundcast.song.model.dao;

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
	public List selectSongList() {
		return session.selectList("song.selectSongList");
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
