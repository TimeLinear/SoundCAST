package com.kh.soundcast.song.model.dao;

import java.util.HashMap;
import java.util.List;

import com.kh.soundcast.song.model.vo.Genre;
import com.kh.soundcast.song.model.vo.Mood;
import com.kh.soundcast.song.model.vo.Song;
import com.kh.soundcast.song.model.vo.SongExt;
import com.kh.soundcast.song.model.vo.SongFile;
import com.kh.soundcast.song.model.vo.SongImage;

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



}
