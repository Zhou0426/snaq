package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.SessionInfo;

public interface SessionInfoService {
	// save
	void save(SessionInfo loginLogout);

	// update
	public void update(SessionInfo sessionInfo);

	// public get By jsessionId
	public SessionInfo getByJsessionId(String jsessionId);

	List<SessionInfo> findByPage(String hql, Integer page, Integer rows);

	long countHql(String hql);

	List<Integer> findYear();

	List<Integer> findMonth(int year);

	List<Integer> findYearD();

	List<Integer> findMonthD(int year);
	
}
