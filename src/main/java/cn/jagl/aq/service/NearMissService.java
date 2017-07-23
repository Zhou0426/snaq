package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.NearMiss;

public interface NearMissService {

	//分页
	List<NearMiss> findByPage(String hql , int pageNo, int pageSize);
	//根据审核状态查询总数
	long findTotal(String nearMissState);
	//获取所有未遂事件编号
	List<String> getAllNearMissSn();
	//根据未遂事件编号获取未遂事件记录
	NearMiss getByNearMissSn(String nearMissSn);
	//增加未遂事件实体
	void add(NearMiss nearMiss);
	//删除未遂事件实体
	void delete(NearMiss nearMiss);
	//更新未遂事件实体
	void update(NearMiss nearMiss);
	//删除未遂事件实体
	void deleteById(int id);
	//用hql查询数目
	long countHql(String hql);
}
