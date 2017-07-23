package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.ManagementReview;

public interface ManagementReviewDao extends BaseDao<ManagementReview>{

	//hql��ѯ����
	public long countHql(String hql) ;
	//����id��ȡʵ��
	public ManagementReview getById(int id);

	public ManagementReview getBySn(String reviewSn);
}
