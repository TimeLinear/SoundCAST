package com.kh.soundcast.statistic.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Download {
	private int downloadSongNo; // 다운로드한 음원 번호
	private int downloadMemberNo; // 다운로드한 멤버 번호
	private Date downloadDate; // 다운로드한 일자
}
