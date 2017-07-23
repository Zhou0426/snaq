package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.Standard;
import cn.jagl.aq.domain.StandardType;
import net.sf.json.JSONObject;

public interface DepartmentDao extends BaseDao<Department>
{
	public Department getByDepartmentSn(String departmentSn);
	public List<Department> getDepartmentsByParentDepartmentSn(String parentDepartmentSn);
	public List<Department> getDepartmentsByParentDepartmentSnNotDeleted(String parentDepartmentSn);
	
	public int getChildDepartmentsCount(Department department);
	public long findNum(String parentDepartmentSn);
	public List<Department> getDepartments(String departmentTypeSn);
	public List<Department> getDepartmentes(String departmentTypeSn);
	
	/**
	 * 分公司角色根据部门编号向下寻找部门
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
	public String getDownDepartmentByDepartmentType(String departmentSn,String departmentTypeSn);
List<Department> getDepartments(String parentDepartmentSn,String departmentTypeSn);
	/**
	 * @author 
	 * @date 2016
	 * 
	 */
	public List<String> getAllDepartmentSn();
	
	public List<String> getDepartmentSnByParentDepartmentSn(String parentDepartmentSn);
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
	/**
	 * 
	 */
	public void getDepartmentNameByDepartment();
	/**
	 * 
	 * @param departmentTypeSn
	 * @param standardType
	 * @return 
	 */
	public Standard queryStandardByDepartmentType(String departmentTypeSn, StandardType standardType);
	/**
	 * 根据指标和部门类型查询部门，和指标在部门所发生的次数
	 * @param departmentTypeSn
	 * @param standardSn
	 * @param standardIndexSn
	 * @return
	 */
	JSONObject queryDepartmentStandardIndex(String standardSn, String standardIndexSn, int page, int rows);
}
