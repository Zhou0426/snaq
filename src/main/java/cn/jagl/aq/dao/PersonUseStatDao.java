package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.PersonUseStat;

public interface PersonUseStatDao extends BaseDao<PersonUseStat> {

	long countHql(String hql);

}
