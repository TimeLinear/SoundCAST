<<<<<<< HEAD
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
=======
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
>>>>>>> origin/myPage_sds
