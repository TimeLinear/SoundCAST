package com.kh.soundcast.song.model.dao;

import java.util.HashMap;
import java.util.List;

import com.kh.soundcast.song.model.vo.Genre;
import com.kh.soundcast.song.model.vo.Mood;
import com.kh.soundcast.song.model.vo.Song;
import com.kh.soundcast.song.model.vo.SongExt;
import com.kh.soundcast.song.model.vo.SongFile;
import com.kh.soundcast.song.model.vo.SongImage;
import com.kh.soundcast.statistic.model.vo.Download;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.soundcast.song.model.vo.SongExt;

import lombok.RequiredArgsConstructor;



public interface SongDao {

	List<Song> selectSongList(HashMap<String, Object> param);

	List<Song> selectSongMainList(int placeNo);

	List<Genre> selectAllGenres();

	List<Mood> selectAllMoods();

	int updateSongBasicInfo(Song updateSong);

	List<Song> getMemberSongList(int mNo);

	SongExt selectSong(int songNo);

	int insertNewSong(SongFile fileParam);

	int insertNewImage(SongImage imageParam);

	String selectSongPath(int unofficialPathNo);

	int insertSongFile(SongFile songFileData);

	String selectSongImagePath(int unofficialPathNo);

	int insertSongImage(SongImage songImageData);

	int insertSong(Song song);

	List<Download> checkDownload(HashMap<String, Object> param);

	int insertDownload(HashMap<String, Object> param);


@Repository
@RequiredArgsConstructor
public class SongDao {
	
	private final SqlSessionTemplate session;

	public List<SongExt> selectTop5Music() {
		return session.selectList("song.selectTop5Music");
	}

	public List<SongExt> selectNewMusic() {
		return session.selectList("song.selectNewMusic");
	}
	
	
	


}
