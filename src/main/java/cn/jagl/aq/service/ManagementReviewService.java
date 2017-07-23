package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.Certification;
import cn.jagl.aq.domain.ManagementReview;

public interface ManagementReviewService {
	//hql查询总数
	public long countHql(String hql) ;
	//分页查询
	List<ManagementReview> findByPage(String hql , int pageNo, int pageSize);
	//增加未遂事件实体
	void add(ManagementReview managementReview);
	//删除未遂事件实体
	void delete(ManagementReview managementReview);
	//更新未遂事件实体
	void update(ManagementReview managementReview);
	//根据id获取实体
	public ManagementReview getById(int id);
	public ManagementReview getBySn(String reviewSn);
}
