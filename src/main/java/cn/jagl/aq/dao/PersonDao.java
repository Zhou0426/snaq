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
	//������Ա
	public List<Person> serachByQ(String q);
	public List<Person> getDepartmentsByDepartmentSn(String departmentSn);
	/**
	 * ��ȡ�û���Ȩ�޲˵�
	 * @param personId
	 * @param parentResourceSn
	 * @return
	 */
	List<Resource> getMenu(String personId, String parentResourceSn);
	public long findNum(String departmentSn);
	
	/**
	 * @author mahui
	 * @method ͨ��personids��ȡ���person
	 * @date 2016��7��9������3:46:47
	 */
	public List<Person> getByPersonIds(String ids);
	/**
	 * @author ���ַ�
	 * @date 2016��7��13��
	 * 
	 * ��ȡ�û���ĳ����Դ���˵�����ť����Ȩ�޷�Χ
	 * @param personId
	 * @param resourceSn
	 * @return
	 */
	public List<Department> getResourcePermissionScope(String personId,String resourceSn);
	List<String> getPersonIds();
	/**
	 * ��ȡָ����Ա��������Դ
	 * @param personId
	 * @return
	 * 
	 * @author ���ַ�
	 * @date 2016��7��22��
	 */
	public HashMap<String,String> getResources(String personId);
	List<Role> getRoles(Person person);
	long getPersonNumByDepartmentSn(List<Person> persons, String departmentSn);
	List<Person> getPersonByDepartmentSn(List<Person> persons, String departmentSn);
	long findTotal(String roleSn,String departmentSn);
	long findTotalByDept(String departmentSn);
	List<Person> getBySql(String sql,int page,int rows);
	/**
	 * ��ȡָ����Աӵ�еĽ�ɫ
	 * @param personId
	 * @return
	 * 
	 * @author ���ַ�
	 * @date 2016��7��24��
	 */
	public HashMap<String,String> getRoles(String personId); 
	List<String> getPersonSnByDepartmentSn(String departmentSn); 		
	//hql��ѯ����
	public long countBySQL(String sql) ;
	//����hql����ѯ����
	public Long getCountByHql(String hql);
	//hql��ѯ����
	public long countHql(String hql) ;

	Person getById(int id);
	/**
	 * ����hql����ѯ��Ա����
	 * @param hql
	 * @return
	 */
	public List<Person> getPersonsByHql(String hql);

	 Set<Person> getPersonsByRoleId(int roleId);
	 /**
	  * ��־�ļ���ʱ�ϴ�
	  */
	void documentUpload();
	public List<Department> getResourcePermissionScopeNotDeleted(String personId, String resourceSn);
}
