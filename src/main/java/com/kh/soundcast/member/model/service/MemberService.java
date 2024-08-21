package com.kh.soundcast.member.model.service;

import org.springframework.stereotype.Service;

import com.kh.soundcast.member.model.dao.AuthDao;
import com.kh.soundcast.member.model.dao.MemberDao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
	
	private final MemberDao dao;
	
}
