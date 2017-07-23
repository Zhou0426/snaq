package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.DepartmentUseStat;
import cn.jagl.aq.domain.PersonUseStat;

public interface DepartmentUseStatDao extends BaseDao<DepartmentUseStat>{

	public long countHql(String hql);


}
