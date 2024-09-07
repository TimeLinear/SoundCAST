package com.kh.soundcast.song.model.dao;

import java.util.HashMap;
import java.util.List;

import com.kh.soundcast.song.model.vo.Genre;
import com.kh.soundcast.song.model.vo.Mood;
import com.kh.soundcast.song.model.vo.Report;
import com.kh.soundcast.song.model.vo.Song;
import com.kh.soundcast.song.model.vo.SongExt;
import com.kh.soundcast.song.model.vo.SongFile;
import com.kh.soundcast.song.model.vo.SongImage;
import com.kh.soundcast.statistic.model.vo.Download;

public interface SongDao {

	List<Song> selectSongList(HashMap<String, Object> param);

	List<Song> selectSongMainList(int placeNo);
	
	public SongExt selectSong(int songNo);

	List<Genre> selectAllGenres();

	List<Mood> selectAllMoods();

	int updateSongBasicInfo(Song updateSong);

	List<Song> getMemberSongList(int mNo);

	String selectSongPath(int songPathNo);

	int insertSongFile(SongFile songFileData);

	String selectSongImagePath(int songImagePathNo);

	int insertSongImage(SongImage songImageData);

	int insertSong(Song song);
	
	public List<Download> checkDownload(HashMap<String, Object> param);
	
	public int insertDownload(HashMap<String, Object> param);

	SongExt selectSong(Song song);

	int updateSongStatus(int songNo);

	int insertReport(Report report);
	
	public List<SongExt> selectTop5Music();
	
	public List<SongExt> selectNewMusic();

}
