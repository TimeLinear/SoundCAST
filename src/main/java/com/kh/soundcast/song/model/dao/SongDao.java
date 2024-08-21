package com.kh.soundcast.song.model.dao;

import java.util.HashMap;
import java.util.List;

import com.kh.soundcast.song.model.vo.Genre;
import com.kh.soundcast.song.model.vo.Mood;
import com.kh.soundcast.song.model.vo.Song;

public interface SongDao {

	Song selectSong(int songNo);

	List<Song> selectSongList(HashMap<String, Object> param);

	List<Genre> selectAllGenres();

	List<Mood> selectAllMoods();


}
