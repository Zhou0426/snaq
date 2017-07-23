package cn.jagl.aq.dao.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.RoleResourceDao;
import cn.jagl.aq.domain.Accident;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.Resource;
import cn.jagl.aq.domain.Role;
import cn.jagl.aq.domain.RoleResource;
@Repository("roleResourceDao")
public class RoleResourceDaoHibernate4 extends BaseDaoHibernate5<RoleResource> 
	implements RoleResourceDao 
{

	@Override
	public RoleResource getByRoleResource(Role role, Resource resource) {
		String hql="select r from RoleResource r where r.role.roleSn='"+role.getRoleSn()+"' and r.resource.resourceSn= '"+resource.getResourceSn()+"'";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (RoleResource)query.uniqueResult();
	}

	@Override
	public void deleteRoleResource(Role role, Resource resource) {
		String hql="select r from RoleResource r where r.role.roleSn='"+role.getRoleSn()+"' and r.resource.resourceSn= '"+resource.getResourceSn()+"'";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		RoleResource roleResource=(RoleResource)query.uniqueResult();
		getSessionFactory().getCurrentSession().delete(roleResource);
	}

	@Override
	public Set<RoleResource> getResourcesByRoleSn(String roleSn) {
		String hql="select r from RoleResource r left join r.role o where o.roleSn='"+roleSn+"'";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		Set set = new HashSet(Arrays.asList((List<RoleResource>)query.list()));
		return set;
	}

	@Override
	public Set<RoleResource> getResourcesByRoleId(int roleId) {
		String hql="select r from RoleResource r left join r.role o where o.id='"+roleId+"'";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		Set set = new HashSet(Arrays.asList((List<RoleResource>)query.list()));
		return set;
	}
}
