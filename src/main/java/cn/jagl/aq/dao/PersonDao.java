package cn.jagl.aq.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.Resource;
import cn.jagl.aq.domain.Role;

public interface PersonDao extends BaseDao<Person> {
	public Person getByPersonId(String personId);
	//搜索人员
	public List<Person> serachByQ(String q);
	public List<Person> getDepartmentsByDepartmentSn(String departmentSn);
	/**
	 * 获取用户的权限菜单
	 * @param personId
	 * @param parentResourceSn
	 * @return
	 */
	List<Resource> getMenu(String personId, String parentResourceSn);
	public long findNum(String departmentSn);
	
	/**
	 * @author mahui
	 * @method 通过personids获取多个person
	 * @date 2016年7月9日下午3:46:47
	 */
	public List<Person> getByPersonIds(String ids);
	/**
	 * @author 孟现飞
	 * @date 2016年7月13日
	 * 
	 * 获取用户对某个资源（菜单、按钮）的权限范围
	 * @param personId
	 * @param resourceSn
	 * @return
	 */
	public List<Department> getResourcePermissionScope(String personId,String resourceSn);
	List<String> getPersonIds();
	/**
	 * 获取指定人员的所有资源
	 * @param personId
	 * @return
	 * 
	 * @author 孟现飞
	 * @date 2016年7月22日
	 */
	public HashMap<String,String> getResources(String personId);
	List<Role> getRoles(Person person);
	long getPersonNumByDepartmentSn(List<Person> persons, String departmentSn);
	List<Person> getPersonByDepartmentSn(List<Person> persons, String departmentSn);
	long findTotal(String roleSn,String departmentSn);
	long findTotalByDept(String departmentSn);
	List<Person> getBySql(String sql,int page,int rows);
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
	//hql查询总数
	public long countBySQL(String sql) ;
	//根据hql语句查询总数
	public Long getCountByHql(String hql);
	//hql查询总数
	public long countHql(String hql) ;

	Person getById(int id);
	/**
	 * 根据hql语句查询人员集合
	 * @param hql
	 * @return
	 */
	public List<Person> getPersonsByHql(String hql);

	 Set<Person> getPersonsByRoleId(int roleId);
	 /**
	  * 日志文件定时上传
	  */
	void documentUpload();
	public List<Department> getResourcePermissionScopeNotDeleted(String personId, String resourceSn);
}
