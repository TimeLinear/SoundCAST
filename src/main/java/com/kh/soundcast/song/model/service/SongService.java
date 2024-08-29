package com.kh.soundcast.song.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kh.soundcast.song.model.dao.SongDao;
import com.kh.soundcast.song.model.vo.SongExt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SongService {
	
	private final SongDao songDao;

	public List<SongExt> selectTop5Music() {
		return songDao.selectTop5Music();
	}

	public List<SongExt> selectNewMusic() {
		return songDao.selectNewMusic();
	}
}
