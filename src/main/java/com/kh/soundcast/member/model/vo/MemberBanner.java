package com.kh.soundcast.member.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberBanner {
	private int memberBannerNo;
	private String memberBannerPath; // 파일명을 포함한 경로
}
