package com.kh.soundcast.member.model.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MemberExt extends Member{
	
	private MemberBanner memberBanner;
	private MemberSocial memberSocial;
	private ProfileImage profileImage; 
	private Comment comment;
	private Follow follow;
	private int follower;
	private List<Follow> following;

	
}
