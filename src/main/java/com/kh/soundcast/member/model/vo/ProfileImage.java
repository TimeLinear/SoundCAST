package com.kh.soundcast.member.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileImage {
	private int profileImageNo;
	private String profileImagePath; // 파일명을 포함한 경로
}
