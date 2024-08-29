package com.kh.soundcast.song.model.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.impl.FileUploadIOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.soundcast.common.Utils;
import com.kh.soundcast.song.model.dao.SongDao;
import com.kh.soundcast.song.model.vo.Genre;
import com.kh.soundcast.song.model.vo.Mood;
import com.kh.soundcast.song.model.vo.Song;
import com.kh.soundcast.song.model.vo.SongExt;
import com.kh.soundcast.song.model.vo.SongFile;
import com.kh.soundcast.song.model.vo.SongImage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {
	
	private final SongDao dao;
	
	@Value("${file.upload-dir}")
	private String uploadBaseDir;
	
	// 원래는 Enum을 사용하는 것이 좋다
	private final static int OFFICIAL_PATH_NO = 1;
	private final static int UNOFFICIAL_PATH_NO = 2;
	private final static int OFFICIAL_PLACE_NO = 0;
	private final static int UNOFFICIAL_PLACE_NO = 1;
	
	
	@Override
	public List<Song> selectSongList(HashMap<String, Object> param) {
		
		//param의 keyword=&mood=0&genre=0 일 경우 메인화면으로 인식 
		if((param.get("keyword").equals("") || param.get("keyword") == null)
			&& (Integer.parseInt(String.valueOf(param.get("genre"))) < 1)
			&& (Integer.parseInt(String.valueOf(param.get("mood"))) < 1)
				) {
			
			log.info("메인");
			int placeNo = Integer.parseInt(String.valueOf(param.get("placeNo")));
			return dao.selectSongMainList(placeNo);
			
		} else {
			log.info("검색");
			return dao.selectSongList(param);
		}
		
	}
	
	@Override
	public List<Genre> selectAllGenres() {
		return dao.selectAllGenres();
	}

	@Override
	public List<Mood> selectAllMoods() {
		return dao.selectAllMoods();
	}

	@Override
	public int updateSong(int songNo, SongExt song) {
		//타이틀, 곡설명, 라이센스, 장르, 분위기만 변경하는 경우, song 테이블에 들어간 내용만 변경된 경우 
		
		Song updateSong = new Song();
		updateSong.setSongNo(songNo);
		updateSong.setSongMoodNo(song.getSongMoodNo());
		updateSong.setSongGenreNo(song.getSongGenreNo());
		updateSong.setSongTitle(song.getSongTitle());
		updateSong.setSongLicense(song.getSongLicense());
		updateSong.setSongDetail(song.getSongDetail());
		
		log.info("updateSong ? {}", updateSong);
		
		int result = dao.updateSongBasicInfo(updateSong);		
		
		//이미지 변경하는 경우 (x->)
		
		//song.getSongFile()
		
		//음악 파일 변경하는 경우
		
		
		return 0;
		
	}

	@Override
	public List<Song> getMemberSongList(int mNo) {
		
		return dao.getMemberSongList(mNo);
	}

	@Override
	@Transactional(rollbackFor = {Exception.class})
	public SongExt insertUnofficialSong(MultipartFile songFile, MultipartFile songImage, Song song) throws Exception {
		
		int result = 1;
		
		Path path = FileSystems.getDefault().getRootDirectories().iterator().next();
		final String osRootPath = path.toString().substring(0, path.toString().length() - 1);
		
		SongFile songFileData = new SongFile();
		SongImage songImageData = new SongImage();
		
		
		if (songFile != null) {
			
			songFileData.setSongFileOriginName(songFile.getOriginalFilename());
			
			String songPath = dao.selectSongPath(SongServiceImpl.UNOFFICIAL_PATH_NO);
			
			if(songPath == null || songPath.equals("")) {
				throw new FileNotFoundException("해당 저장소는 존재하지 않습니다.");
			}
			
			String songFileChangeName = Utils.saveFile(songFile, osRootPath + uploadBaseDir + songPath);
			if(songFileChangeName == null || songFileChangeName.equals("")) {
				throw new Exception("음원 파일 업로드 실패");
			}
			songFileData.setSongFileChangeName(songFileChangeName);
			
			songFileData.setSongFileSongPathNo(SongServiceImpl.UNOFFICIAL_PATH_NO);
			
			result *= dao.insertSongFile(songFileData);
		} else {
			return null;
		}
		
		if (songImage != null) {
			
			String songImagePath = dao.selectSongImagePath(SongServiceImpl.UNOFFICIAL_PATH_NO);
			
			if(songImagePath == null || songImagePath.equals("")) {
				throw new FileNotFoundException("해당 저장소는 존재하지 않습니다.");
			}
			
			String songImageName = Utils.saveFile(songImage, osRootPath + uploadBaseDir + songImagePath);
			if(songImageName == null || songImageName.equals("")) {
				throw new Exception("음원 파일 업로드 실패");
			}
			songImageData.setSongImageName(songImageName);

			songImageData.setSongImagePathNo(UNOFFICIAL_PATH_NO);
			
			result *= dao.insertSongImage(songImageData);
		}
		
		if(result == 0) {
			throw new Exception("음원 이미지 혹은 파일 정보 DB 삽입 실패");
		}
		
		song.setSongFileNo(songFileData.getSongFileNo());
		song.setSongImageNo(songImageData.getSongImageNo());
		song.setSongPlaceNo(SongServiceImpl.UNOFFICIAL_PLACE_NO);
		
		result *= dao.insertSong(song);
		
		if(result == 0) {
			throw new Exception("음원 정보 DB 삽입 실패");
		}
		
		return dao.selectSong(song);
	}

	@Override
	public int updateSongStatus(int songNo) {
		return dao.updateSongStatus(songNo);
	}

	
	
}
