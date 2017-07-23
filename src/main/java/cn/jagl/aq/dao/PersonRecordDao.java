package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.PersonRecord;

public interface PersonRecordDao extends BaseDao<PersonRecord> {

	/**
	 * ������ԱId�Ͳ��ű�Ų������������Ƿ����
	 */
	public PersonRecord getByPersonIdDepartmentSn(String personId,String departmentSn);
	/**
	 * ������ԱId����������������ʵ�弯��
	 */
	public List<PersonRecord> getListByPersonId(String personId);
	/**
	 * ������ԱId�����������о�����������һ������
	 */
	public PersonRecord getByPersonId(String personId);
	/**
	 * ����ɾ��
	 */
	public void Manydelete(String ids);
	/**
	 * ������ԱId�����������б��µļ�¼
	 */
	public List<PersonRecord> getMonthByPersonId(String personId);
}
