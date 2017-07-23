package cn.jagl.aq.dao;



import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.NearMiss;
public interface NearMissDao extends BaseDao<NearMiss>{
	//根据审核状态查询总数
	long findTotal(String nearMissState);
	//获取所有未遂事件编号
	List<String> getAllNearMissSn();
	//根据未遂事件编号获取未遂事件记录
	NearMiss getByNearMissSn(String nearMissSn);
	//用hql查询数目
	public long countHql(String hql);
}
