package cn.jagl.aq.service;

import java.util.List;
import cn.jagl.aq.domain.Standard;
import cn.jagl.aq.domain.StandardType;

public interface StandardService {
	int addStandard(Standard standard);
	List<Standard> getAllStandards();
	void deleteStandard(int id);
	Standard getByStandardSn(String standardSn);
	//��ѯ����
	long count(String hql);
	//��ҳ
	List<Standard> query(String hql,int page,int rows);
	//����
	void update(Standard standard);
	//id
	Standard getById(int id);
	//���ض����¼
	void hidden(String ids);
	//��ȡָ�������ָ�ϻ�ָ��
	List<Standard> queryByStandardType(StandardType standardType,String departmentTypeSn);
	/**
	 * ���ݲ������ͱ�ż��ϲ�ѯ��׼
	 * @param departmentTypeSns
	 * @return
	 */
	List<Standard> getStandardByDepartmentTypeSns(List<String> departmentTypeSns);
}
