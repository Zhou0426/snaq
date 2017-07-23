package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.Hazard;
import cn.jagl.aq.domain.ManageObject;

public interface ManageObjectService {

	//��������
	int addManageObject(ManageObject manageObject);
	//��ȡ��������
	List<ManageObject> getAllManageObjects();
	//��ȡ��������
	List<ManageObject> getManageObjectsByHql(String hql);
	//ɾ������
	void deleteManageObject(int id);
	//���ݲ��ű�Ż�ȡ����
	ManageObject getByManageObjectSn(String manageObjectSn);
	//��������
	void update(ManageObject manageObject);
	//����������ѯ���м�¼��
	long findNum(String parentManageObjectSn);
	//��ҳ��ѯ
	List<ManageObject> findByPage(String hql , int page, int rows);
	//��ȡ�ܼ�¼��
	long findCount(Class<ManageObject> ManageObject);
	//����ID��ȡʵ��
	ManageObject getById(Class<ManageObject> ManageObject,int id);
	//���ݸ����������id��ȡ����
	List<ManageObject> getByParentSn(String parentSn,String departmentTypeSn);
	//ģ������
	List<ManageObject> getByMoHuFind(String manageObjectSn);
}
