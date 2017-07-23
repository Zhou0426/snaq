package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.Accident;
import cn.jagl.aq.domain.AccidentType;
import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.NearMiss;

public interface AccidentService {

	//hql��ѯ����
	public long countHql(String hql) ;
	//��ҳ��ѯ
	List<Accident> findByPage(String hql , int pageNo, int pageSize);
	//�����¹ʱ�Ÿ���ʵ��
	public Accident getByAccidentSn(String accidentSn);	
	//����δ���¼�ʵ��
	void add(Accident accident);
	//ɾ��δ���¼�ʵ��
	void delete(Accident accident);
	//����δ���¼�ʵ��
	void update(Accident accident);
}
