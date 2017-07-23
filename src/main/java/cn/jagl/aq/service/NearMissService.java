package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.NearMiss;

public interface NearMissService {

	//��ҳ
	List<NearMiss> findByPage(String hql , int pageNo, int pageSize);
	//�������״̬��ѯ����
	long findTotal(String nearMissState);
	//��ȡ����δ���¼����
	List<String> getAllNearMissSn();
	//����δ���¼���Ż�ȡδ���¼���¼
	NearMiss getByNearMissSn(String nearMissSn);
	//����δ���¼�ʵ��
	void add(NearMiss nearMiss);
	//ɾ��δ���¼�ʵ��
	void delete(NearMiss nearMiss);
	//����δ���¼�ʵ��
	void update(NearMiss nearMiss);
	//ɾ��δ���¼�ʵ��
	void deleteById(int id);
	//��hql��ѯ��Ŀ
	long countHql(String hql);
}
