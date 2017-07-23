package cn.jagl.aq.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.ResourceDao;
import cn.jagl.aq.dao.RoleDao;
import cn.jagl.aq.dao.RoleResourceDao;
import cn.jagl.aq.domain.Resource;
import cn.jagl.aq.domain.Role;
import cn.jagl.aq.domain.RoleResource;
import cn.jagl.aq.service.RoleResourceService;
@Service("roleResourceService")
public class RoleResourceServiceImpl implements RoleResourceService {
	@javax.annotation.Resource(name="roleDao")
	private RoleDao roleDao;
	@javax.annotation.Resource(name="resourceDao")
	private ResourceDao resourceDao;
	@javax.annotation.Resource(name="roleResourceDao")
	private RoleResourceDao roleResourceDao;

	@Override
	public int addRoleResource(RoleResource roleResource) {
		return (Integer)roleResourceDao.save(roleResource);
	}

	@Override
	public List<RoleResource> getAllRoleResources() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteRoleResource(Role role,Resource resource) {
		roleResourceDao.deleteRoleResource(role,resource);
	}

	@Override
	public RoleResource getByRoleResource(Role role, Resource resource) {
		return roleResourceDao.getByRoleResource(role,resource);
	}

	@Override
	public void add(RoleResource roleResource) {
		roleResourceDao.save(roleResource);		
	}

	@Override
	public void deleteRoleResource(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<RoleResource> getResourcesByRoleSn(String roleSn) {
		return roleResourceDao.getResourcesByRoleSn(roleSn);
	}

	@Override
	public Set<RoleResource> getResourcesByRoleId(int roleId) {
		return roleResourceDao.getResourcesByRoleId(roleId);
	}

}
