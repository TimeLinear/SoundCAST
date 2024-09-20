package com.kh.soundcast.statistic.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DownloadCount {
	private int downloadCountSongNo; // 집계되는 음원 번호
	private int downloadCountNumber; // 집계된 해당 음원의 다운로드 수
}
