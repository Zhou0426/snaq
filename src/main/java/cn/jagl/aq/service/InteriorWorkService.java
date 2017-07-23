package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.InteriorWork;

public interface InteriorWorkService 
{
	
	//�������
	int addInteriorWork(InteriorWork interiorWork);
	//ɾ������
	void deletInteriorWork(int id);
	//���ݱ�Ż�ȡ����
	InteriorWork getByInteriorWorkSn(String InteriorWorkSn); 
	//��ҳ��ȡ����
	List<InteriorWork> findByPage(String hql,int pageNo, int pageSize);
	//����hql����ȡ��������
	List<InteriorWork> getCountByHql(String hql); 
	//��������
	void update(InteriorWork interiorWork);
	//����hql����ȡ����
	long getByHql(String hql);
	//����sql����ȡ����
	long getBySql(String sql);
}

