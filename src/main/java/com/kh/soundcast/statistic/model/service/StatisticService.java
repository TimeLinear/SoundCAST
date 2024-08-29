package com.kh.soundcast.statistic.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kh.soundcast.statistic.model.dao.StatisticDao;
import com.kh.soundcast.statistic.model.vo.DownloadCount;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticService {

	private final StatisticDao statisticDao;
	
//	public List<DownloadCount> selectTop5DownloadCount() {
//
//		return statisticDao.selectTop5DownloadCount();
//	}

}
