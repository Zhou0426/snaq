package cn.jagl.aq.service.impl;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.RoleDao;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.Role;
import cn.jagl.aq.service.RoleService;
@Service("roleService")
public class RoleServiceImpl implements RoleService {
	@Resource(name="roleDao")
	private RoleDao roleDao;
	
	@Override
	public int addRole(Role role) {
		return (Integer)roleDao.save(role);
	}

	@Override
	public List<Role> getAllRoles() {
		return roleDao.findAll(Role.class);
	}

	@Override
	public void deleteRole(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Role getByRoleSn(String roleSn) {
		// TODO Auto-generated method stub
		return roleDao.getByRoleSn(roleSn);
	}

	@Override
	public List<Person> getPersonByRoleSn(String roleSn) {
		// TODO Auto-generated method stub
		return roleDao.getPersonByRoleSn(roleSn);
	}

	@Override
	public void update(Role role) {
		// TODO Auto-generated method stub

		roleDao.update(role);
	}

	@Override
	public void delete(Role role) {
		roleDao.delete(role);
		
	}

	@Override
	public List<Person> findByPage1(String string, int i, int j) {
		// TODO Auto-generated method stub
		return roleDao.findByPage1(string, i, j);
	}

	@Override
	public long getCountByRoleSn(String roleSn) {
		// TODO Auto-generated method stub
		return roleDao.getCountByRoleSn(roleSn);
	}

	@Override
	public List<Role> getByRoleTypes(String roleType) {
		// TODO Auto-generated method stub
		return roleDao.getByRoleTypes(roleType);
	}

	@Override
	public List<String> getAllRoleSns(String roleType) {
		return roleDao.getAllRoleSns(roleType);
	}

	@Override
	public Role getById(int roleId) {
		return roleDao.getById(roleId);
	}

	@Override
	public List<Role> findByPage(String hql, int page, int rows) {
		// TODO Auto-generated method stub
		return roleDao.findByPage(hql, page, rows);
	}

	@Override
	public long getCountByHql(String hql) {
		// TODO Auto-generated method stub
		return roleDao.getCountByHql(hql);
	}

	@Override
	public Role getByRoleSnIngnoreState(String roleSn) {
		// TODO Auto-generated method stub
		return roleDao.getByRoleSnIngnoreState(roleSn);
	}

	@Override
	public int getRoleType(String personId) {
		// TODO Auto-generated method stub
		return roleDao.getRoleType(personId);
	}

}
