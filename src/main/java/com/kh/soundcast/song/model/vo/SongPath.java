package com.kh.soundcast.song.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongPath {
	private int songPathNo; // 음원 파일 경로 번호
	private String songPathName; // 음원 파일 경로명
}
