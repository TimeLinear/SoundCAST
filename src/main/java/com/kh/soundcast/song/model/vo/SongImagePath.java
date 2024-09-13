package com.kh.soundcast.song.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongImagePath {
	private int songImagePathNo; // 음원 커버 이미지 경로 번호
	private String songImagePathName; // 음원 커버 이미지 경로명
}
