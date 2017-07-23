package cn.jagl.aq.dao;

import java.util.Set;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.Resource;
import cn.jagl.aq.domain.Role;
import cn.jagl.aq.domain.RoleResource;

public interface RoleResourceDao extends BaseDao<RoleResource>{

	RoleResource getByRoleResource(Role role, Resource resource);

	void deleteRoleResource(Role role, Resource resource);

	Set<RoleResource> getResourcesByRoleSn(String roleSn);
	Set<RoleResource> getResourcesByRoleId(int roleId);

}
