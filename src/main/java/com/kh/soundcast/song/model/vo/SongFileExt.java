package com.kh.soundcast.song.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongFileExt extends SongFile {
	// 음원 파일 경로 명(songFilePathNo 필드와 짝)
	private String songFileSongPathName; 
}