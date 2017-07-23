package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.ManagementReview;

public interface ManagementReviewDao extends BaseDao<ManagementReview>{

	//hql查询总数
	public long countHql(String hql) ;
	//根据id获取实体
	public ManagementReview getById(int id);

	public ManagementReview getBySn(String reviewSn);
}
