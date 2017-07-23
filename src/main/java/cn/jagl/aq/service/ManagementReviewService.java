package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.Certification;
import cn.jagl.aq.domain.ManagementReview;

public interface ManagementReviewService {
	//hql��ѯ����
	public long countHql(String hql) ;
	//��ҳ��ѯ
	List<ManagementReview> findByPage(String hql , int pageNo, int pageSize);
	//����δ���¼�ʵ��
	void add(ManagementReview managementReview);
	//ɾ��δ���¼�ʵ��
	void delete(ManagementReview managementReview);
	//����δ���¼�ʵ��
	void update(ManagementReview managementReview);
	//����id��ȡʵ��
	public ManagementReview getById(int id);
	public ManagementReview getBySn(String reviewSn);
}
