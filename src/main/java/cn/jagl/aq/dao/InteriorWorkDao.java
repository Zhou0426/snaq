package cn.jagl.aq.dao;


import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;

import cn.jagl.aq.domain.InteriorWork;;

public interface InteriorWorkDao extends BaseDao<InteriorWork>{
	
	public InteriorWork getByInteriorWorkSn(String InteriorWorkSn);

	public List<InteriorWork> getCountByHql(String hql);

	public long getByHql(String hql);

	public long getBySql(String sql); 
}

