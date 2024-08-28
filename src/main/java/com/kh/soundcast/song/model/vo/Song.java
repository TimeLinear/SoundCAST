package com.kh.soundcast.song.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Song {
	private int songNo; // 음원 번호
	private int songMemberNo; // 업로더 회원 번호
	private int songMoodNo; // 음원 분위기 번호
	private int songGenreNo; // 음원 장르 번호
	private int songImageNo; // 음원 커버 이미지 번호
	private int songFileNo; // 음원 파일 번호
	private int songPlaceNo; //공식, 비공식 분류 0 - 공식, 1 - 비공식
	private String songTitle; // 음원명(유저가 입력)
	private String songLicense; // 음원 출처(유저가 입력)
	private String songDetail; // 음원 상세(유저가 입력)
	private String songCreateDate;  // 업로드일자
	private String songModifyDate; // 수정일자
	private String songStatus; // Y, N
}
