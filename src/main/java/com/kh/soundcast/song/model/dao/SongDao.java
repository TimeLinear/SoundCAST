package com.kh.soundcast.song.model.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SongDao {
	
	private final SqlSessionTemplate session;
	
	
}
