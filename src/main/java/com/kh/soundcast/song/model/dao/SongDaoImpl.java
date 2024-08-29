package com.kh.soundcast.song.model.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.kh.soundcast.song.model.vo.Genre;
import com.kh.soundcast.song.model.vo.Mood;
import com.kh.soundcast.song.model.vo.Report;
import com.kh.soundcast.song.model.vo.Song;
import com.kh.soundcast.song.model.vo.SongExt;
import com.kh.soundcast.song.model.vo.SongFile;
import com.kh.soundcast.song.model.vo.SongImage;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SongDaoImpl implements SongDao{

	private final SqlSession session;

	
	@Override
	public List<Song> selectSongList(HashMap<String, Object> param) {
		return session.selectList("song.selectSongList", param);
	}

	@Override
	public List<Genre> selectAllGenres() {
		return session.selectList("song.selectAllGenres");
	}

	@Override
	public List<Mood> selectAllMoods() {
		return session.selectList("song.selectAllMoods");
	}

	@Override
	public List<Song> selectSongMainList(int placeNo) {
		return session.selectList("song.selectSongMainList", placeNo);
	}

	@Override
	public int updateSongBasicInfo(Song updateSong) {
		return session.update("song.updateSongBasicInfo", updateSong);
	}

	@Override
	public List<Song> getMemberSongList(int mNo) {
		return session.selectList("song.getMemberSongList", mNo);
	}

	@Override
	public String selectSongPath(int songPathNo) {
		return session.selectOne("song.selectSongPath", songPathNo);
	}

	@Override
	public int insertSongFile(SongFile songFileData) {
		return session.insert("song.insertSongFile", songFileData);
	}

	@Override
	public String selectSongImagePath(int songImagePathNo) {
		return session.selectOne("song.selectSongImagePath", songImagePathNo);
	}

	@Override
	public int insertSongImage(SongImage songImageData) {
		return session.insert("song.insertSongImage", songImageData);
	}

	@Override
	public int insertSong(Song song) {
		return session.insert("song.insertSong", song);
	}

	@Override
	public SongExt selectSong(Song song) {
		return session.selectOne("song.selectSong", song);
	}

	@Override
	public int updateSongStatus(int songNo) {
		return session.update("song.updateSongStatus", songNo);
	}

	@Override
	public int insertReport(Report report) {
		return session.insert("report.insertReport", report);
	}
	
	
	public List<SongExt> selectTop5Music() {
		return session.selectList("song.selectTop5Music");
	}

	public List<SongExt> selectNewMusic() {
		return session.selectList("song.selectNewMusic");
	}
}
