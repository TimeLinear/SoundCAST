package com.kh.soundcast.statistic.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.kh.soundcast.member.model.dao.MemberDao;
import com.kh.soundcast.member.model.service.MemberService;
import com.kh.soundcast.statistic.model.dao.StatisticDao;
import com.kh.soundcast.statistic.model.vo.DownloadCount;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticService {
	
	private final StatisticDao stDao;

	public List<Map<String, Object>> getDownload() {
		return stDao.getDownload();
	}
}
