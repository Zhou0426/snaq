package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.DepartmentType;
import cn.jagl.aq.domain.Hazard;
import cn.jagl.aq.domain.ManageObject;

public interface ManageObjectDao extends BaseDao<ManageObject> {
	
	//���ݹ�������Ų�ѯ����
	public ManageObject getByManageObjectSn(String manageObjectSn);
	//���ݸ����������id��ѯ����
	public List<ManageObject> getByParentSn(String parentSn,String departmentTypeSn);
	//����������ѯ��¼��
	public long findNum(String parentManageObjectSn);
	//ģ������
	public List<ManageObject> getByMoHuFind(String manageObjectSn);
	//��ȡ��������
	public List<ManageObject> getManageObjectsByHql(String hql);
	
}
