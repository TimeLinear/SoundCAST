package com.kh.soundcast.song.model.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

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
import com.kh.soundcast.statistic.model.vo.Download;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {
	
	private final SongDao dao;
	@Value("${file.upload-dir}")
	private String uploadBaseDir;

//원래는 Enum을 사용하는 것이 좋다
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


	@Transactional(rollbackFor = Exception.class)
	@Override
	public int updateSong(int songNo, SongExt songInfo, MultipartFile songFile, MultipartFile songImage) {
		
		//songfile이 변경된 경우 //utils의 save 메소드 이용하여 이름 변경 후 정해진 폴더위치에 각각 저장
		//null이 아니거나 음원 변경되지 않은경우 originname이 동일 
		//songNo로 하나 조회 > song-origin-name이랑 songFile의 비교
		
		SongExt song = dao.selectSong(songNo);
		
		int songFileNo = 0;
		int songImageNo = 0;
		
		int result = 1;
		
		Path path = FileSystems.getDefault().getRootDirectories().iterator().next();
		final String osRootPath = path.toString().replace("\\\\", "");
		
		if(songFile != null) {
			
			String fileReadPath = osRootPath+uploadBaseDir+song.getSongFile().getSongPathName();
			log.info("fileReadPath ? {}", fileReadPath);
			File readSong = new File(fileReadPath+"/"+song.getSongFile().getSongFileChangeName());
			log.info("readSong ? {}", readSong);
				
			//originName이 동일, 크기가 동일할 때 같은 파일로 간주 ==> 아닐 때 파일 저장
			boolean fileCon1 = songFile.getOriginalFilename().equals(song.getSongFile().getSongFileOriginName());
			boolean fileCon2 = readSong.length() == songFile.getSize();
			
				if(!(fileCon1 && fileCon2)) {
					
					String fileSavePath = osRootPath+uploadBaseDir+song.getSongFile().getSongPathName();
					String songFileOriginName = songFile.getOriginalFilename();
					String songFileChangeName = Utils.saveFile(songFile, fileSavePath);
					int songFileSongPathNo = song.getSongFile().getSongFileSongPathNo();
					
					SongFile fileParam = new SongFile();
					
					fileParam.setSongFileSongPathNo(songFileSongPathNo);
					fileParam.setSongFileOriginName(songFileOriginName);
					fileParam.setSongFileChangeName(songFileChangeName);
					
					result *= dao.insertNewSong(fileParam);
					log.info("result ? {}", result);
					
					songFileNo = fileParam.getSongFileNo();
					log.info("songFileNo ? {}", songFileNo);
				
				}
		}
		//songimage가 변경된 경우 //utils의 save 메소드 이용하여 이름 변경 후 정해진 폴더위치에 각각 저장
		if(songImage != null) {
			String ImageSavePath = osRootPath+uploadBaseDir+song.getSongImage().getSongImagePathName();
			String songImageName = Utils.saveFile(songImage, ImageSavePath);
			int songImagePathNo = song.getSongImage().getSongImagePathNo();
			
			SongImage imageParam = new SongImage();
			imageParam.setSongImagePathNo(songImagePathNo);
			imageParam.setSongImageName(songImageName);
			
			result *= dao.insertNewImage(imageParam);
			log.info("result ? {}", result);
			
			songImageNo = imageParam.getSongImageNo();
			log.info("songImageNo ? {}", songImageNo);
		}
		
		//타이틀, 곡설명, 라이센스, 장르, 분위기만 변경하는 경우, song 테이블에 들어간 내용만 변경된 경우 
		Song updateSong = new Song();
		
		updateSong.setSongNo(songNo);
		updateSong.setSongMoodNo(songInfo.getSongMoodNo());
		updateSong.setSongGenreNo(songInfo.getSongGenreNo());
		updateSong.setSongTitle(songInfo.getSongTitle());
		updateSong.setSongLicense(songInfo.getSongLicense());
		updateSong.setSongDetail(songInfo.getSongDetail());
		
		if(songFileNo != 0) {
			updateSong.setSongFileNo(songFileNo);
		}
		if(songImageNo != 0) {
			updateSong.setSongImageNo(songImageNo);
		}
		
		result *= dao.updateSongBasicInfo(updateSong);
		log.info("result ? {}", result);
		
		
		return result;
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
		
		return dao.selectSong(song.getSongNo());
	}
	
	
	@Override
	public SongExt selectSong(int songNo) {
		return dao.selectSong(songNo);
	}

	@Override
	public List<Download> checkDownload(HashMap<String, Object> param) {
		return dao.checkDownload(param);
	}

	@Override
	public int insertDownload(HashMap<String, Object> param) {
		return dao.insertDownload(param);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
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
import com.kh.soundcast.song.model.vo.Report;
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

	@Override
	@Transactional(rollbackFor = {Exception.class})
	public int insertReport(Report report) {
		return dao.insertReport(report);
	}

	
	public List<SongExt> selectTop5Music() {
		return dao.selectTop5Music();
	}

	public List<SongExt> selectNewMusic() {
		return dao.selectNewMusic();
	}
	
}
