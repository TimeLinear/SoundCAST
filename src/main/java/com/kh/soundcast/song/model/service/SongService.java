package com.kh.soundcast.song.model.service;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;

import com.kh.soundcast.song.model.dao.SongDao;
import com.kh.soundcast.song.model.vo.SongExt;

import com.kh.soundcast.song.model.vo.Genre;
import com.kh.soundcast.song.model.vo.Mood;
import com.kh.soundcast.song.model.vo.Report;
import com.kh.soundcast.song.model.vo.Song;
import com.kh.soundcast.song.model.vo.SongExt;
import com.kh.soundcast.statistic.model.vo.Download;

public interface SongService {

	List<Song> selectSongList(HashMap<String, Object> param);

	List<Genre> selectAllGenres();

	List<Mood> selectAllMoods();

	List<Song> getMemberSongList(int mNo);

	int updateSong(int songNo, SongExt songInfo, MultipartFile songFile, MultipartFile songImage);

	SongExt insertUnofficialSong(MultipartFile songFile, MultipartFile songImage, Song song) throws Exception;
	
	List<Download> checkDownload(HashMap<String, Object> param);
	
	int insertDownload(HashMap<String, Object> param);

	SongExt selectSong(int songNo);
	
	int updateSongStatus(int songNo);

	int insertReport(Report report);
	
	public List<SongExt> selectTop5Music();
	
	public List<SongExt> selectNewMusic();

}
