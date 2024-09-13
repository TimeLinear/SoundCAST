package com.kh.soundcast.song.model.vo;

import com.kh.soundcast.member.model.vo.MemberExt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportExt extends Report {
	private SongExt song;
	private MemberExt member;

}
