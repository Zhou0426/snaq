package cn.jagl.aq.service;

import java.util.List;
import java.util.Set;

import cn.jagl.aq.domain.Resource;
import cn.jagl.aq.domain.Role;
import cn.jagl.aq.domain.RoleResource;

public interface RoleResourceService {

	int addRoleResource(RoleResource roleResource);
	List<RoleResource> getAllRoleResources();
	
	void deleteRoleResource(int id);
	
	RoleResource getByRoleResource(Role role, Resource resource);
	void add(RoleResource roleResource);
	void deleteRoleResource(Role role, Resource resource);
	Set<RoleResource> getResourcesByRoleSn(String roleSn);
	Set<RoleResource> getResourcesByRoleId(int roleId);
	
}
