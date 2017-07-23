package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.DepartmentType;

public interface DepartmentTypeService
{
	
	int addDepartmentType(DepartmentType departmentType);

	List<DepartmentType> getAllDepartmentTypes();
	
	void deleteDepartmentType(int id);
	
	DepartmentType getByDepartmentTypeSn(String departmentTypeSn);
	List<DepartmentType> getDepartmentsByParentDepartmentTypeSn(String parentDepartmentTypeSn);

	List<DepartmentType> findByPage(String hql,int pageNo, int pageSize);
	List<DepartmentType> query(String hql);

	List<DepartmentType> getByParentDepartmentTypeSn(String id);
	/**
	 * ��ȡָ�����ű�ŷ�Χ�ڵĹ�굥λ�����б�
	 * @param departmentSn
	 * @return
	 */
	public List<DepartmentType> getImplDepartmentTypes(String departmentSn);

	List<DepartmentType> getAllImplDepartmentTypes();
	String getDownDepartmentTypeByParent(String departmentTypeSn);
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
