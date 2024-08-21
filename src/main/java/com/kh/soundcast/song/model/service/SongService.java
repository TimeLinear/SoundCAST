package com.kh.soundcast.song.model.service;

import org.springframework.stereotype.Service;

import com.kh.soundcast.song.model.dao.SongDao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SongService {
	
	private final SongDao songDao;
}
