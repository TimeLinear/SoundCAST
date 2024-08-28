package com.kh.soundcast.song.model.service;

import java.util.HashMap;
import java.util.List;

import com.kh.soundcast.song.model.vo.Genre;
import com.kh.soundcast.song.model.vo.Mood;
import com.kh.soundcast.song.model.vo.Song;
import com.kh.soundcast.song.model.vo.SongExt;

public interface SongService {

	List<Song> selectSongList(HashMap<String, Object> param);

	List<Genre> selectAllGenres();

	List<Mood> selectAllMoods();

	int updateSong(int songNo, SongExt song);

	List<Song> getMemberSongList(int mNo);



}
