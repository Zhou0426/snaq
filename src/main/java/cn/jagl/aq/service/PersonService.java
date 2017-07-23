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
	 * ��ȡ��Ա��ĳ����Դ��Ȩ�޷�Χ�б�û�н��棩
	 * 
	 * @author ���ַ�
	 * @date 2016��7��13��
	 */
	List<Department> getResourcePermissionScope(String personId, String resourceSn);
	List<Department> getResourcePermissionScopeNotDeleted(String personId, String resourceSn);
	
	List<DepartmentType> getResourceDepartmentTypeScope(String personId, String resourceSn);
	//��ҳ��������
	List<Person> findByPage(String hql , int pageNo, int pageSize);
	//��ȡ����Ա����Ŀ
	long findCount(Class<Person> person);
	//������Ա
	public List<Person> searchByQ(String q);
	List<Person> getDepartmentsByDepartmentSn(String departmentSn);
	void updatePerson(Person person);
	public List<Resource> getMenu(String personId,String parentResourceSn);
	long findNum(String departmentSn);
	/**
	 * @author mahui
	 * @method 
	 * @date 2016��7��9������3:54:15
	 */
	List<Person> getByPersonIds(String ids);
	List<String> getPersonIds();

	long getPersonNumByDepartmentSn(List<Person> persons, String departmentSn);

	List<Person> getPersonByDepartmentSn(List<Person> persons, String departmentSn);


	long findTotal(String roleSn,String departmentSn);

	long findTotalByDept(String departmentSn);
	List<Person> getBySql(String sql, int page, int rows);

	/**
	 * ��ȡָ����Ա��������Դ
	 * @param personId
	 * @return
	 * 
	 * @author ���ַ�
	 * @date 2016��7��22��
	 */
	public HashMap<String,String> getResources(String personId);
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

	//sql��ѯ����
	public long countBySQL(String sql) ;
	//����hql����ѯ����
	public Long getCountByHql(String hql);

	//hql��ѯ����
	public long countHql(String hql) ;

	Person getById(int id);
	/**
	 * ����hql����ѯ��Ա����
	 */
	public List<Person> getPersonsByHql(String hql);

	Set<Person> getPersonsByRoleId(int roleId);

	List<Role> getRoles(Person person);
	/**
	 * ��־�ļ���ʱ�ϴ�
	 */
	void documentUpload();
}
