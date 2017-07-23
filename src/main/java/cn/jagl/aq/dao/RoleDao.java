package cn.jagl.aq.dao;

import java.util.List;
import java.util.Set;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.Role;

public interface RoleDao extends BaseDao<Role>{

	List<Person> getPersonByRoleSn(String roleSn);
	Role getByRoleSn(String roleSn);

	List<Person> findByPage1(String string, int i, int j);
	long getCountByRoleSn(String roleSn);
	List<Role> getByRoleTypes(String roleType);
	List<String> getAllRoleSns(String roleType);
	Role getById(int roleId);
	long getCountByHql(String hql);
	Role getByRoleSnIngnoreState(String roleSn);
	
	int getRoleType(String personId);
}
