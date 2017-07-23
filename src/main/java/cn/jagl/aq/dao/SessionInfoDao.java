package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.SessionInfo;

public interface SessionInfoDao extends BaseDao<SessionInfo> {

	SessionInfo getByJsessionId(String jsessionId);

	long countHql(String hql);

	List<Integer> findYear();	
	List<Integer> findMonth(int year);	
	List<Integer> findYearD();	
	List<Integer> findMonthD(int year);	
}
