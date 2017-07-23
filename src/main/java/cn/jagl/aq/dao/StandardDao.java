package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.Standard;
import cn.jagl.aq.domain.StandardType;

public interface StandardDao extends BaseDao<Standard> {
	//������ѯ
	Standard getByStandardtSn(String standardSn);
	//�����ѯ
	List<Standard> query(String hql,int page,int rows);
	//��ѯ����
	long count(String hql);
	//���ض����¼
	void hidden(String ids);
	//��ȡָ�����������ָ�ϻ�ָ��
	List<Standard> queryByStandardType(StandardType standardtype,String departmentTypeSn);
	/**
	 * ���ݲ������ͱ�ż��ϲ�ѯ��׼
	 * @param departmentTypeSns
	 * @return
	 */
	List<Standard> getStandardByDepartmentTypeSns(List<String> departmentTypeSns);
}
