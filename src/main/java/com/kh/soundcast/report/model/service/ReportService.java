package com.kh.soundcast.report.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kh.soundcast.report.model.dao.ReportDao;
import com.kh.soundcast.song.model.vo.ReportExt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

	private final ReportDao reportDao;
	
	public List<ReportExt> selectRecentReport() {
		return reportDao.selectRecentReport();
	}

}
