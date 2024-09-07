package com.kh.soundcast.song.model.service;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

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

	int updateSong(int songNo, SongExt song);

	List<Song> getMemberSongList(int mNo);

	SongExt insertUnofficialSong(MultipartFile songFile, MultipartFile songImage, Song song) throws Exception;
	
	public List<Download> checkDownload(HashMap<String, Object> param);
	
	public int insertDownload(HashMap<String, Object> param);

	public SongExt selectSong(int songNo);
	
	int updateSongStatus(int songNo);

	int insertReport(Report report);
	
	public List<SongExt> selectTop5Music();
	
	public List<SongExt> selectNewMusic();

}