package com.kh.soundcast.member.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
	private int commentNo;
	private int commentWriterMemberNo;
	private int commentMemberNo;
	private String commentText;
	private String commentCreateDate;
	private String commentModifyDate;
}
