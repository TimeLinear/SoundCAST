package com.kh.soundcast.song.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongExt extends Song {

	// 아티스트 명(songMemberNo 필드와 짝)
	private String memberNickname;
	
	// 음원 분위기 명(songMoodNo 필드와 짝)
	private String songMoodName;
	
	// 음원 장르 명(songGenreNo 필드와 짝)
	private String songGenreName;
	
	// 음원 커버 이미지 객체(songImageNo 필드와 짝)
	private SongImageExt songImage;
	
	// 음원 파일 객체(songFileNo 필드와 짝)
	// 폴더까지의 경로 번호만 있는 SoneFile 클래스에 경로명까지 추가한 클래스
	private SongFileExt songFile;
}