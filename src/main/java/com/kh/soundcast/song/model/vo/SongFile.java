package com.kh.soundcast.song.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongFile {
	private int songFileNo; // 음원 파일 번호
	private int songFileSongPathNo; // 음원 파일 경로 번호 
	private String songFileChangeName; // 서버에 저장된 음원 파일명
	private String songFileOriginName; // 사용자가 업로드한 당시의 음원 파일명
}
