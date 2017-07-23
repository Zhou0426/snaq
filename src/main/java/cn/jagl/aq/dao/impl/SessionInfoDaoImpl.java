package cn.jagl.aq.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.SessionInfoDao;
import cn.jagl.aq.domain.SessionInfo;
import cn.jagl.aq.domain.Sms;
@Repository("sessionInfoDao")
public class SessionInfoDaoImpl extends BaseDaoHibernate5<SessionInfo> implements SessionInfoDao {
	@Override
	public SessionInfo getByJsessionId(String jsessionId) {
		String hql="select s from SessionInfo s where s.jsessionId=:jsessionId";
		return (SessionInfo) getSessionFactory().getCurrentSession()
					.createQuery(hql)
					.setString("jsessionId",jsessionId)
					.uniqueResult();
	}

	@Override
	public long countHql(String hql) {
		return(Long)getSessionFactory().getCurrentSession().createQuery(hql)
				.uniqueResult();
	}

	@Override
	public List<Integer> findYear() {
		String hql="select distinct s.statYear from PersonUseStat s";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (List<Integer>)query.list();
	}
	@Override
	public List<Integer> findMonth(int year) {
		String hql="select distinct s.statMonth from PersonUseStat s where s.statYear="+year+" order by s.statMonth DESC";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (List<Integer>)query.list();
	}

	@Override
	public List<Integer> findYearD() {
		String hql="select distinct s.statYear from DepartmentUseStat s";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (List<Integer>)query.list();
	}
	@Override
	public List<Integer> findMonthD(int year) {
		String hql="select distinct s.statMonth from DepartmentUseStat s where s.statYear="+year+" order by s.statMonth DESC";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (List<Integer>)query.list();
	}
}
