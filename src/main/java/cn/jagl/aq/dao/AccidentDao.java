package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.Accident;
import cn.jagl.aq.domain.AccidentLevel;
import cn.jagl.aq.domain.AccidentType;

public interface AccidentDao extends BaseDao<Accident>{

	//hql��ѯ����
	public long countHql(String hql) ;
	//�����¹ʱ�Ÿ���ʵ��
	public Accident getByAccidentSn(String accidentSn);	
}
