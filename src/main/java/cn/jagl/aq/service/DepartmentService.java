package cn.jagl.aq.service;

import java.util.List;
import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.Standard;
import cn.jagl.aq.domain.StandardType;

public interface DepartmentService
{	
	int addDepartment(Department department);
	List<Department> getAllDepartments();	
	void deleteDepartment(int id);	
	Department getByDepartmentSn(String departmentSn);	
	void updateDepartment(Department department);	
	List<Department> findByPage(String hql , int pageNo, int pageSize);
	long findCount(Class<Department> department);
	List<Department> findByPage(String hql , int pageNo, int pageSize,Object...params);	
	List<Department>  getDepartmentsByParentDepartmentSn(String parentDepartmentSn);
	List<Department>  getDepartmentsByParentDepartmentSnNotDeleted(String parentDepartmentSn);
	public int getChildDepartmentsCount(Department department);
	Department getById(int id);
	long findNum(String parentDepartmentSn);
	List<Department> getDepartments(String departmentTypeSn);
	/**
	 * �ֹ�˾��ɫ���ݲ��ű������Ѱ�Ҳ���
	 * @param departmentSn
	 * @return
	 */
	public Department getUpNerestFgs(String departmentSn);
	/**
	 * @author 
	 * @date 2016
	 * 
	 * @param personId
	 * @return
	 */
	public Department getUpNearestImplDepartment(String departmentSn);
	/**
	 * @author 
	 * @date 2016
	 * 
	 * @param departmentSn
	 * @param departmentTypeSn
	 * @return
	 */
	public Department getUpNearestByDepartmentType(String departmentSn,String departmentTypeSn);
	/**
	 * @author 
	 * @date 2016
	 * 
	 * @param departmentSn
	 * @param departmentTypeSn
	 * @return
	 */
	public String getDownDepartmentByDepartmentType(String departmentSn, String departmentTypeSn);

	List<Department> getDepartments(String parentDepartmentSn,String departmentTypeSn);
	List<String> getAllDepartmentSn();
	List<String> getDepartmentSnByParentDepartmentSn(String parentDepartmentSn);
	/**
	 * 
	 * @param departmentSn
	 * @param standardType
	 * @return
	 * 
	 * @author 
	 * @date 2016
	 */
	public Standard getImplStandards(String departmentSn,StandardType standardType);

	public long countHql(String hql) ;
	/**
	 * @author 
	 * @date 2016
	 * 
	 */
	public List<Department> getDepartmentsNew(String parentDepartmentSn, String departmentTypeSn,String clickEchart);
	public List<Department> getAllDepartmentsByType(String departmentTypeSn);
	/**
	 * @author mahui
	 * @method 
	 * @date 2016
	 */
	public List<Department> queryDepartment(String departmentSn);
	
	public void setDepartmentNameByDepartment();
	/**
	 * 
	 * @param departmentTypeSn
	 * @param standardType
	 * @return
	 */
	public Standard queryStandardByDepartmentType(String departmentTypeSn, StandardType standardType);
	/**
	 * ����ָ��Ͳ������Ͳ�ѯ���ţ���ָ���ڲ����������Ĵ���
	 * @param departmentTypeSn 
	 * @param standardSn
	 * @param standardIndexSn
	 * @return
	 */
	net.sf.json.JSONObject queryDepartmentStandardIndex(String standardSn,
			String standardIndexSn, int page, int rows);
}
