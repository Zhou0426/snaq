package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.InteriorWork;

public interface InteriorWorkService 
{
	
	//添加数据
	int addInteriorWork(InteriorWork interiorWork);
	//删除数据
	void deletInteriorWork(int id);
	//根据编号获取数据
	InteriorWork getByInteriorWorkSn(String InteriorWorkSn); 
	//分页获取资料
	List<InteriorWork> findByPage(String hql,int pageNo, int pageSize);
	//根据hql语句获取所有数据
	List<InteriorWork> getCountByHql(String hql); 
	//更新数据
	void update(InteriorWork interiorWork);
	//根据hql语句获取总数
	long getByHql(String hql);
	//根据sql语句获取总数
	long getBySql(String sql);
}

