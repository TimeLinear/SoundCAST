package com.kh.soundcast.statistic.model.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.soundcast.statistic.model.vo.DownloadCount;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StatisticDao {

	private final SqlSessionTemplate session;
	
//	public List<DownloadCount> selectTop5DownloadCount() {
//		return session.selectList("statistic.selectTop5DownloadCount");
//	}

}
