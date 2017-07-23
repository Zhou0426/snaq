package cn.jagl.aq.dao;



import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.NearMiss;
public interface NearMissDao extends BaseDao<NearMiss>{
	//�������״̬��ѯ����
	long findTotal(String nearMissState);
	//��ȡ����δ���¼����
	List<String> getAllNearMissSn();
	//����δ���¼���Ż�ȡδ���¼���¼
	NearMiss getByNearMissSn(String nearMissSn);
	//��hql��ѯ��Ŀ
	public long countHql(String hql);
}
