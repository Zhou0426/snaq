package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.SessionInfoDao;
import cn.jagl.aq.domain.SessionInfo;
import cn.jagl.aq.service.SessionInfoService;
@Service("sessionInfoService")
public class SessionInfoServiceImpl implements SessionInfoService {
	@Resource(name="sessionInfoDao")
	private SessionInfoDao sessionInfoDao;
	@Override
	public void save(SessionInfo loginLogout) {
		sessionInfoDao.save(loginLogout);
	}

	@Override
	public void update(SessionInfo loginLogout) {
		sessionInfoDao.update(loginLogout);
	}

	@Override
	public SessionInfo getByJsessionId(String jsessionId) {
		return sessionInfoDao.getByJsessionId(jsessionId);
	}

	@Override
	public List<SessionInfo> findByPage(String hql, Integer page, Integer rows) {
		return sessionInfoDao.findByPage(hql, page, rows);
	}

	@Override
	public long countHql(String hql) {
		return sessionInfoDao.countHql(hql);
	}

	@Override
	public List<Integer> findYear() {
		return sessionInfoDao.findYear();
	}

	@Override
	public List<Integer> findMonth(int year) {
		return sessionInfoDao.findMonth(year);
	}
	@Override
	public List<Integer> findYearD() {
		return sessionInfoDao.findYear();
	}

	@Override
	public List<Integer> findMonthD(int year) {
		return sessionInfoDao.findMonth(year);
	}

}
