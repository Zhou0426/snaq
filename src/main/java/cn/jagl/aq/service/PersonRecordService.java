package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.PersonRecord;

public interface PersonRecordService {

	/**
	 * ������ԱId�Ͳ��ű�Ų������������Ƿ����-������������һ������
	 */
	PersonRecord getByPersonIdDepartmentSn(String personId,String departmentSn);
	/**
	 * ������ԱId�����������о�����������һ������
	 */
	PersonRecord getByPersonId(String personId);
	/**
	 * ������ԱId�����������б��µļ�¼
	 */
	List<PersonRecord> getMonthByPersonId(String personId);
	/**
	 * ������ԱId����������������ʵ�弯��
	 */
	List<PersonRecord> getListByPersonId(String personId);
	/**
	 * ���ʵ��
	 */
	void add(PersonRecord personRecord);
	/**
	 * �޸�ʵ��
	 */
	void update(PersonRecord personRecord);
	/**
	 * ����ɾ��
	 */
	void Manydelete(String ids);
	/**
	 * ����ɾ��
	 */
	void delete(int id);
}
