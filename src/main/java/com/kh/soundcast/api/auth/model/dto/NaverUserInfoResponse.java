package com.kh.soundcast.api.auth.model.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NaverUserInfoResponse {

	private Response response;
	@Getter
	public static class Response{
		private String id;
		private String nickname;
		private String email;
		private String profile_image; //프로필 이미지 URL
	}
	
}
