package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.PostLevel;

public interface PostLevelDao extends BaseDao<PostLevel>{

	PostLevel getBySn(String levelSn);
	long countHql(String hql);
}
