package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.domain.NearMissType;

public interface NearMissTypeDao {
	//ͨ��δ���¼����ͱ�Ż�ȡδ���¼�����
	NearMissType getByNearMissTypeSn(String nearMissTypeSn);
	//��ȡ����δ���¼����ͱ��
	List<NearMissType> getAllNearMissType();
}
