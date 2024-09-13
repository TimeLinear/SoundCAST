package com.kh.soundcast.report.model.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.soundcast.song.model.vo.ReportExt;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReportDao {

	private final SqlSessionTemplate session;
	
	public List<ReportExt> selectRecentReport() {
		return session.selectList("report.selectRecentReport");
	}

}
