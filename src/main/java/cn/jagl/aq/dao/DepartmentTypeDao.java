package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.DepartmentType;

public interface DepartmentTypeDao extends BaseDao<DepartmentType>
{
	public DepartmentType getByDepartmentTypeSn(String departmentTypeSn);
	public List<DepartmentType> getDepartmentsByParentDepartmentTypeSn(String parentDepartmentTypeSn);
	public List<DepartmentType> query(String hql);
	public List<DepartmentType> getImplDepartmentTypes(String departmentSn);
	public List<DepartmentType> getAllImplDepartmentTypes();
	public String getDownDepartmentTypeByParent(String departmentTypeSn);
	/**
	 * ��ȡ��������Ĺ�굥λ���ͣ������������ţ�
	 * @param departmentSn
	 * @return
	 */
	public List<DepartmentType> getImplDepartmentTypesExceptSelf(String departmentSn);
	/**
	 * ��ȡָ�����ű�ŷ�Χ�ڵĹ�굥λ�����б� - ���ѯ
	 * @param departmentSn
	 * @return
	 */
	public List<DepartmentType> getImplDepartmentTypesByLeft(String departmentSn);
	/**
	 * ���ݲ��ű�Ų�ѯ�ò��������������͵������Ӳ�������
	 * @param departmentSn
	 * @return
	 */
	DepartmentType getDepartmentSn(String departmentSn);
	/**
	 * ���ݲ��Ż�ȡ�ò��������еĹ�굥λ���ͣ������䱾��Ĳ�������
	 * @param departmentSn
	 * @return
	 */
	List<String> getAllImplDepartmentTypesIncloudSelf(String departmentSn);
}
