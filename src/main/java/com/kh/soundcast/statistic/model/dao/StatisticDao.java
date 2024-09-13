package com.kh.soundcast.statistic.model.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.soundcast.member.model.dao.MemberDao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
@RequiredArgsConstructor
public class StatisticDao {
	private final SqlSessionTemplate session;

	public List<Map<String, Object>> getDownload() {
		return session.selectList("statistic.getDownload");
	}
}
