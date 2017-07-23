package cn.jagl.aq.dao.impl;

import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.DepartmentUseStatDao;
import cn.jagl.aq.domain.DepartmentUseStat;
@Repository("departmentUseStatDao")
public class DepartmentUseStatDaoImpl extends BaseDaoHibernate5<DepartmentUseStat> implements DepartmentUseStatDao {


	@Override
	public long countHql(String hql) {
		return(Long)getSessionFactory().getCurrentSession().createQuery(hql)
				.uniqueResult();
	}
}
