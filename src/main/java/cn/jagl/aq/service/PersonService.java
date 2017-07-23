package cn.jagl.aq.service;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.DepartmentType;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.Resource;
import cn.jagl.aq.domain.Role;


public interface PersonService {
	
	void addPerson(Person person);
	
	List<Person> getAllPeople();
	
	void deletePerson(int id);
	
	Person getByPersonId(String personId);
	
	List<Resource> getMenuResourceByPersonId(String personId);
	
	boolean hasResource(String personId, String resourceSn);
	/**
	 * 获取人员对某个资源的权限范围列表（没有交叉）
	 * 
	 * @author 孟现飞
	 * @date 2016年7月13日
	 */
	List<Department> getResourcePermissionScope(String personId, String resourceSn);
	List<Department> getResourcePermissionScopeNotDeleted(String personId, String resourceSn);
	
	List<DepartmentType> getResourceDepartmentTypeScope(String personId, String resourceSn);
	//分页加载数据
	List<Person> findByPage(String hql , int pageNo, int pageSize);
	//获取所有员工数目
	long findCount(Class<Person> person);
	//搜索人员
	public List<Person> searchByQ(String q);
	List<Person> getDepartmentsByDepartmentSn(String departmentSn);
	void updatePerson(Person person);
	public List<Resource> getMenu(String personId,String parentResourceSn);
	long findNum(String departmentSn);
	/**
	 * @author mahui
	 * @method 
	 * @date 2016年7月9日下午3:54:15
	 */
	List<Person> getByPersonIds(String ids);
	List<String> getPersonIds();

	long getPersonNumByDepartmentSn(List<Person> persons, String departmentSn);

	List<Person> getPersonByDepartmentSn(List<Person> persons, String departmentSn);


	long findTotal(String roleSn,String departmentSn);

	long findTotalByDept(String departmentSn);
	List<Person> getBySql(String sql, int page, int rows);

	/**
	 * 获取指定人员的所有资源
	 * @param personId
	 * @return
	 * 
	 * @author 孟现飞
	 * @date 2016年7月22日
	 */
	public HashMap<String,String> getResources(String personId);
	/**
	 * 获取指定人员拥有的角色
	 * @param personId
	 * @return
	 * 
	 * @author 孟现飞
	 * @date 2016年7月24日
	 */
	public HashMap<String,String> getRoles(String personId);

	List<String> getPersonSnByDepartmentSn(String departmentSn);

	//sql查询总数
	public long countBySQL(String sql) ;
	//根据hql语句查询总数
	public Long getCountByHql(String hql);

	//hql查询总数
	public long countHql(String hql) ;

	Person getById(int id);
	/**
	 * 根据hql语句查询人员集合
	 */
	public List<Person> getPersonsByHql(String hql);

	Set<Person> getPersonsByRoleId(int roleId);

	List<Role> getRoles(Person person);
	/**
	 * 日志文件定时上传
	 */
	void documentUpload();
}
