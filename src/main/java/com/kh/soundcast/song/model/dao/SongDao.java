package com.kh.soundcast.song.model.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.soundcast.song.model.vo.SongExt;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SongDao {
	
	private final SqlSessionTemplate session;

	public List<SongExt> selectTop5Music() {
		return session.selectList("song.selectTop5Music");
	}
	
	
}
