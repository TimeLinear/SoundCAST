package com.kh.soundcast.song.model.service;

import java.util.List;

import com.kh.soundcast.song.model.vo.Genre;
import com.kh.soundcast.song.model.vo.Mood;
import com.kh.soundcast.song.model.vo.Song;

public interface SongService {

	Song selectSong(int songNo);

	List selectSongList();

	List<Genre> selectAllGenres();

	List<Mood> selectAllMoods();

}
