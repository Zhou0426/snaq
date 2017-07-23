package cn.jagl.aq.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.StandardDao;
import cn.jagl.aq.domain.Standard;
import cn.jagl.aq.domain.StandardType;
@SuppressWarnings("unchecked")
@Repository("standardDao")
public class StandardDaoHibernate4 extends BaseDaoHibernate5<Standard> implements StandardDao{

	@Override
	public Standard getByStandardtSn(String standardSn) {
		Query query =getSessionFactory().getCurrentSession()
				.createQuery("select s from Standard s where standardSn=:standardSn")
				.setString("standardSn", standardSn);
				return (Standard)query.uniqueResult();
	}

	@Override
	public List<Standard> query(String hql,int page, int rows) {
		//String hql="FROM Standard s WHERE s.deleted=false";
		List<Standard> standardList=getSessionFactory().getCurrentSession().createQuery(hql)
				.setFirstResult((page - 1) * rows)
				.setMaxResults(rows) //
				.list();
		return standardList;
	}

	@Override
	public long count(String hql) {
		return (Long)getSessionFactory().getCurrentSession().createQuery(hql)
				.uniqueResult();
	}
	//隐藏多条记录
	@Override
	public void hidden(String ids) {
		String hql="update Standard s SET s.deleted=true WHERE s.id in("+ids+")";
		getSessionFactory().getCurrentSession()
		.createQuery(hql).executeUpdate();
	}
	//获取所有审核指南或指标
	@Override
	public List<Standard> queryByStandardType(StandardType standardtype,String departmentTypeSn) {
		String hql="select s from Standard s where standardType=:standardType"
				+ " AND s.departmentType.departmentTypeSn='"+departmentTypeSn+"'"
				+ " AND deleted=false";
		List<Standard> standardList=getSessionFactory().getCurrentSession().createQuery(hql)
				.setParameter("standardType", standardtype)
				.list();
		return standardList;
	}

	@Override
	public List<Standard> getStandardByDepartmentTypeSns(List<String> departmentTypeSns) {
		String hql = "select s FROM Standard s WHERE s.deleted=false"
				+ " AND s.departmentType.departmentTypeSn in (:departmentTypeSns)";
		return getSessionFactory().getCurrentSession()
				.createQuery(hql)
				.setParameterList("departmentTypeSns", departmentTypeSns)
				.list();
	}
}
