package cn.jagl.aq.dao.impl;

import java.util.List;
import java.util.Set;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.RoleDao;
import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.Role;
import cn.jagl.aq.domain.RoleType;
@Repository("roleDao")
public class RoleDaoHibernate4 extends BaseDaoHibernate5<Role> 
	implements RoleDao 
{

	@Override
	public List<Person> getPersonByRoleSn(String roleSn) {
		String hql="select r.persons FROM Role r WHERE r.roleSn= '"+roleSn +"'";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (List<Person>)query.list();
	}
	@Override
	public Role getByRoleSn(String roleSn) {
		String hql="select r FROM Role r WHERE r.deleted=false and r.roleSn= '"+roleSn +"'";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (Role)query.uniqueResult();
	}

	public List<Person> findByPage1(String string, int i, int j){

		// 创建查询语句
		return getSessionFactory().getCurrentSession()
			.createQuery(string)
			.setFirstResult((i - 1) * j)//设置每页起始的记录编号
			.setMaxResults(j)//设置需要查询的最大结果集
			.list();
	}
	@Override
	public long getCountByRoleSn(String roleSn) {
		String hql="select r.persons.size() FROM Role r WHERE r.roleSn= '"+roleSn +"'";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (Long)query.uniqueResult();
	};

	@Override
	public List<Role> getByRoleTypes(String roleType) {
		// TODO Auto-generated method stub
		String hql="select r from Role r where r.deleted=false and r.roleType in "+roleType;
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (List<Role>)query.list();
	}	
	@Override
	public List<String> getAllRoleSns(String roleType) {
		// TODO Auto-generated method stub
		String hql="select r.roleSn from Role r where r.roleType in "+roleType;
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (List<String>)query.list();
	}
	@Override
	public Role getById(int roleId) {
		String hql="select r FROM Role r WHERE r.id= "+roleId;
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (Role)query.uniqueResult();
	}
	@Override
	public long getCountByHql(String hql) {
		return(Long)getSessionFactory().getCurrentSession().createQuery(hql)
				.uniqueResult();
	}
	@Override
	public Role getByRoleSnIngnoreState(String roleSn) {
		String hql="select r FROM Role r WHERE r.roleSn= '"+roleSn +"'";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (Role)query.uniqueResult();
	}
	@Override
	public int getRoleType(String personId) {
		String hql="select distinct r.roleType from Role r inner join r.persons p where p.personId=:personId and "
				+ "not exists(select r2 from Role r2 inner join r2.persons p2 where p2.personId=:personId2 and r2.roleType<r.roleType)";
		try{
			RoleType roleType=(RoleType) getSessionFactory().getCurrentSession().createQuery(hql)
					.setString("personId", personId)
					.setString("personId2", personId)
					.uniqueResult();
			if(roleType!=null){
				if(roleType.equals(RoleType.集团角色)){
					return 0;
				}else if(roleType.equals(RoleType.分公司角色)){
					return 1;
				}else if(roleType.equals(RoleType.贯标单位角色)){
					return 2;
				}
			}else{
				return -1;
			}
		}catch(Exception e){
			return -1;
		}

		return -1;
	}
}
