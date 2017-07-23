package cn.jagl.aq.service;

import java.util.List;
import java.util.Set;

import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.Role;

public interface RoleService {
	
	int addRole(Role role);
	
	List<Role> getAllRoles();

	void deleteRole(int id);
	
	Role getByRoleSn(String roleSn);
	List<Person> getPersonByRoleSn(String roleSn);

	// 更新实体
	void update(Role role);

	void delete(Role role);

	List<Person> findByPage1(String string, int i, int j);

	long getCountByRoleSn(String roleSn);
	List<Role> getByRoleTypes(String roleType);

	List<String> getAllRoleSns(String roleType);

	Role getById(int roleId);

	List<Role> findByPage(String hql, int page, int rows);

	long getCountByHql(String hql);

	Role getByRoleSnIngnoreState(String roleSn);
	
	public int getRoleType(String personId);

}
