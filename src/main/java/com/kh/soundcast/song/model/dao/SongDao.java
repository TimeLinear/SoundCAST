<<<<<<< HEAD
package com.kh.soundcast.song.model.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SongDao {
	
	private final SqlSessionTemplate session;
	
	
}
=======
package com.kh.soundcast.song.model.dao;

import java.util.HashMap;
import java.util.List;

import com.kh.soundcast.song.model.vo.Genre;
import com.kh.soundcast.song.model.vo.Mood;
import com.kh.soundcast.song.model.vo.Song;
import com.kh.soundcast.song.model.vo.SongExt;

public interface SongDao {

	List<Song> selectSongList(HashMap<String, Object> param);

	List<Song> selectSongMainList(int placeNo);

	List<Genre> selectAllGenres();

	List<Mood> selectAllMoods();

	int updateSongBasicInfo(Song updateSong);

	List<Song> getMemberSongList(int mNo);



}
>>>>>>> origin/myPage_sds
