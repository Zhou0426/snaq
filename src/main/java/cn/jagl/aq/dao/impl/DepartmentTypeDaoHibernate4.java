package cn.jagl.aq.dao.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.DepartmentTypeDao;
import cn.jagl.aq.domain.DepartmentType;

@SuppressWarnings("unchecked")
@Repository("departmentTypeDao")
public class DepartmentTypeDaoHibernate4 extends BaseDaoHibernate5<DepartmentType>
	implements DepartmentTypeDao
{
	public DepartmentType getByDepartmentTypeSn(String departmentTypeSn)
	{
		Query query =getSessionFactory().getCurrentSession()
		.createQuery("select p from DepartmentType p where departmentTypeSn=:departmentTypeSn")
		.setString("departmentTypeSn", departmentTypeSn);
		return (DepartmentType)query.uniqueResult();
	}

	public List<DepartmentType> getDepartmentsByParentDepartmentTypeSn(String parentDepartmentTypeSn){
		Query query =getSessionFactory().getCurrentSession()
				.createQuery("select p from DepartmentType p where p.parentDepartmentType.departmentTypeSn=:departmentTypeSn")
				.setString("departmentTypeSn", parentDepartmentTypeSn);
				return (List<DepartmentType>)query.list();
	}
	@Override
	public List<DepartmentType> query(String hql) {
		List<DepartmentType> departmentTypeList=getSessionFactory().getCurrentSession().createQuery(hql)
				.list();
		return departmentTypeList;
	}

	@Override
	public List<DepartmentType> getImplDepartmentTypes(String departmentSn) {
		String sql;
		Query query;
		if(departmentSn==null){
			sql="select distinct department_type.* from department_type"
					+ " inner join department on department_type.department_type_sn=department.department_type_sn"
					+ " where department_type.is_impl_department_type=1  order by department_type_sn";
			query =getSessionFactory().getCurrentSession()
					.createSQLQuery(sql).addEntity(DepartmentType.class);
		}else{
			sql="select distinct department_type.* from department_type"
					+ " inner join department on department_type.department_type_sn=department.department_type_sn"
					+ " where department_type.is_impl_department_type=1 and department.department_sn like :department_sn"
					+ " order by department_type_sn";
			query =getSessionFactory().getCurrentSession()
					.createSQLQuery(sql).addEntity(DepartmentType.class).setString("department_sn", departmentSn+"%");
		}
		return (List<DepartmentType>)query.list();
	}
	
	@Override
	public List<DepartmentType> getImplDepartmentTypesByLeft(String departmentSn) {
		String sql;
		Query query;
		if(departmentSn==null){
			sql="select distinct department_type.* from department_type"
					+ " left join department on department_type.department_type_sn=department.department_type_sn"
					+ " where department_type.is_impl_department_type=1  order by department_type_sn";
			query =getSessionFactory().getCurrentSession()
					.createSQLQuery(sql).addEntity(DepartmentType.class);
		}else{
			sql="select distinct department_type.* from department_type"
					+ " left join department on department_type.department_type_sn=department.department_type_sn"
					+ " where department_type.is_impl_department_type=1 and department.department_sn like :department_sn"
					+ " order by department_type_sn";
			query =getSessionFactory().getCurrentSession()
					.createSQLQuery(sql).addEntity(DepartmentType.class)
					.setString("department_sn", departmentSn+"%");
		}
		return (List<DepartmentType>)query.list();
	}
	
	@Override
	public List<DepartmentType> getImplDepartmentTypesExceptSelf(String departmentSn) {
		String sql;
		Query query;
		if(departmentSn==null){
			sql="select distinct department_type.* from department_type"
					+ " inner join department on department_type.department_type_sn=department.department_type_sn"
					+ " where department_type.is_impl_department_type=1  order by department_type_sn";
			query =getSessionFactory().getCurrentSession()
					.createSQLQuery(sql).addEntity(DepartmentType.class);
		}else{
			sql="select distinct department_type.* from department_type"
					+ " inner join department on department_type.department_type_sn=department.department_type_sn"
					+ " where department_type.is_impl_department_type=1 and department.department_sn like :department_sn"
					+ " and department.department_sn!=:self order by department_type_sn";
			query =getSessionFactory().getCurrentSession()
					.createSQLQuery(sql).addEntity(DepartmentType.class)
					.setString("department_sn", departmentSn+"%")
					.setString("self", departmentSn);
		}
		return (List<DepartmentType>)query.list();
	}
	@Override
	public List<DepartmentType> getAllImplDepartmentTypes() {
		String hql="select p from DepartmentType p where p.isImplDepartmentType=:isImplDepartmentType";
		Query query = getSessionFactory().getCurrentSession().createQuery(hql)
				.setBoolean("isImplDepartmentType", true);
		return (List<DepartmentType>)query.list();
	}
	//根据部门类类型编号获取该部门类型下的所有子部门类型（包括其本身）
	@Override
	public String getDownDepartmentTypeByParent(String departmentTypeSn){
		String str="('"+departmentTypeSn+"',";
		int i=0;
		Set<DepartmentType> departmentTypes=new HashSet<DepartmentType>();
		DepartmentType departmentType=getByDepartmentTypeSn(departmentTypeSn);
		//判断自己部门类型是否存在
		if(departmentType.getChildDepartmentTypes().size()>0){
			Set<DepartmentType> set=departmentType.getChildDepartmentTypes();
			while(i==0){
				i=1;
				departmentTypes.clear();
				departmentTypes.addAll(set);
				set.clear();
				//遍历子级部门类型
				for(DepartmentType de:departmentTypes){
					str+="'"+de.getDepartmentTypeSn()+"',";
					//如果子级还有子级则赋值给set
					if(de.getChildDepartmentTypes().size()>0){
						set.addAll(de.getChildDepartmentTypes());
						i=0;
					}
				}
			}
		}
		str=str.substring(0, str.length()-1)+")";
		return str;
		
	}

	@Override
	public DepartmentType getDepartmentSn(String departmentSn) {
		String sql = "SELECT d.* FROM department_type d LEFT JOIN department de ON d.department_type_sn=de.department_type_sn"
				+ " WHERE de.department_sn=:departmentSn";
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql).addEntity(DepartmentType.class)
				.setString("departmentSn", departmentSn);
		return (DepartmentType) query.uniqueResult();
	}

	@Override
	public List<String> getAllImplDepartmentTypesIncloudSelf(String departmentSn) {
		List<String> list = new ArrayList<String>();
		
		DepartmentType departmentType = this.getDepartmentSn(departmentSn);
		String departmentTypeSn = departmentType.getDepartmentTypeSn();
		
		if( departmentType.getIsImplDepartmentType() )
			list.add(departmentTypeSn);
		
		int i = 0;
		Set<DepartmentType> departmentTypes = new HashSet<DepartmentType>();
		departmentType = getByDepartmentTypeSn(departmentTypeSn);
		//判断自己部门类型是否存在
		if(departmentType.getChildDepartmentTypes().size() > 0){
			Set<DepartmentType> set = departmentType.getChildDepartmentTypes();
			while(i == 0){
				i = 1;
				departmentTypes.clear();
				departmentTypes.addAll(set);
				set.clear();
				//遍历子级部门类型
				for( DepartmentType de : departmentTypes ){
					if( de.getIsImplDepartmentType() )
						list.add( de.getDepartmentTypeSn() );
					//如果子级还有子级则赋值给set
					if( de.getChildDepartmentTypes().size() > 0 ){
						set.addAll( de.getChildDepartmentTypes() );
						i = 0;
					}
				}
			}
		}
		return list;
	}
	
}
