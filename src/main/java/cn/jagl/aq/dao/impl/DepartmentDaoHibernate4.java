package cn.jagl.aq.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.DepartmentDao;
import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.DepartmentType;
import cn.jagl.aq.domain.Standard;
import cn.jagl.aq.domain.StandardType;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@SuppressWarnings("unchecked")
@Repository("departmentDao")
public class DepartmentDaoHibernate4 extends BaseDaoHibernate5<Department>
	implements DepartmentDao
{
	@Resource(name="departmentTypeDao")
	private DepartmentTypeDaoHibernate4 departmentTypeDao;
	public Department getByDepartmentSn(String departmentSn)
	{
		Query query =getSessionFactory().getCurrentSession()
		.createQuery("select p from Department p where departmentSn=:departmentSn").setString("departmentSn", departmentSn);
		return (Department)query.uniqueResult();
	}
	public List<Department> getDepartmentsByParentDepartmentSn(String parentDepartmentSn){
		Query query =getSessionFactory().getCurrentSession()
				.createQuery("select p from Department p where p.parentDepartment.departmentSn=:departmentSn Order by p.showSequence asc").setString("departmentSn", parentDepartmentSn);
				return (List<Department>)query.list();
	}
	public List<Department> getDepartmentsByParentDepartmentSnNotDeleted(String parentDepartmentSn){
		Query query =getSessionFactory().getCurrentSession()
				.createQuery("select p from Department p where p.deleted=false and p.parentDepartment.departmentSn=:departmentSn Order by p.showSequence asc").setString("departmentSn", parentDepartmentSn);
				return (List<Department>)query.list();
	}
	public int getChildDepartmentsCount(Department department){
		Query query =getSessionFactory().getCurrentSession()
				.createQuery("select p from Department p where p.parentDepartment.departmentSn=:departmentSn").setString("departmentSn", department.getDepartmentSn());
				return (int)query.list().size();
	}
	@Override
	public long findNum(String parentDepartmentSn) {
		String hql="select COUNT(p) FROM Department p WHERE p.parentDepartment.departmentSn=:parentDepartmentSn";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		query=query.setParameter("parentDepartmentSn", parentDepartmentSn);
		return (Long)query.uniqueResult();
	}
	@Override
	public List<Department> getDepartments(String departmentTypeSn) {
		Query query =getSessionFactory().getCurrentSession()
				.createQuery("select p from Department p where p.departmentType.departmentTypeSn=:departmentTypeSn and p.deleted != true").setString("departmentTypeSn", departmentTypeSn);
				return (List<Department>)query.list();
	}
	@Override
	public List<Department> getDepartmentes(String departmentTypeSn) {
		Query query =getSessionFactory().getCurrentSession()
				.createQuery("select p from Department p where p.departmentType.departmentTypeSn=:departmentTypeSn").setString("departmentTypeSn", departmentTypeSn);
				return (List<Department>)query.list();
	}
	@Override
	public Department getUpNearestImplDepartment(String departmentSn) {
		String sql="select department.* from department inner join department_type on department.department_type_sn=department_type.department_type_sn where department_type.is_impl_department_type=1 and LOCATE(department.department_sn,'"+departmentSn+"')=1 order by CHAR_LENGTH(department.department_sn) desc";
		List<Department> departments=getSessionFactory().getCurrentSession()
				.createSQLQuery(sql).addEntity(Department.class).list();
		if(departments.size()==0)
			return null;
		else
			return departments.get(0);
	}
	@Override
	public Department getUpNearestByDepartmentType(String departmentSn,String departmentTypeSn) {
		String sql="select department.* from department inner join department_type on department.department_type_sn=department_type.department_type_sn where department_type.department_type_sn='"+departmentTypeSn+"' and LOCATE(department.department_sn,'"+departmentSn+"')=1 order by CHAR_LENGTH(department.department_sn) desc";
		return (Department) getSessionFactory().getCurrentSession()
						.createSQLQuery(sql).addEntity(Department.class).list().get(0);
	}

	public String getDownDepartmentByDepartmentTypeabc(String departmentSn, String departmentTypeSn) {
		String hql;
		Query query;
		
		hql="select p FROM Department p WHERE p.departmentSn like:departmentSnAll and p.departmentType.departmentTypeSn=:departmentTypeSn";
		query=getSessionFactory().getCurrentSession().createQuery(hql);
		query=query.setParameter("departmentSnAll", departmentSn+"%");
		query=query.setParameter("departmentTypeSn", departmentTypeSn);
		
		List<Department> list=(List<Department>)query.list();
		if(list.size()!=1){
				int i=0;
				int j=0;
				String parentDepartment="";
				for(Department de:list){
					if(!de.getDepartmentSn().equals(departmentSn)){
						if(i==0){
							parentDepartment=de.getParentDepartment().getDepartmentSn();
							i++;
						}else{
							if(parentDepartment==de.getParentDepartment().getDepartmentSn()){
								i++;
								continue;
							}else{
								break;
							}
						}
					}else{
						j++;
					}
				}
				if(list.size()==(i+j)){
					hql="select count(*) FROM Department p WHERE p.parentDepartment.departmentSn='"+departmentSn+"'";
					query=getSessionFactory().getCurrentSession().createQuery(hql);
					if((i+j)==(Long)query.uniqueResult()){
						hql="i.departmentSn like '"+departmentSn+"%' and i.departmentSn !='"+departmentSn+"'";
						return hql;
					}else{
						hql="";
						for(Department de:list){
							if(!de.getDepartmentSn().equals(departmentSn)){
								hql+="i.departmentSn like '"+de.getDepartmentSn()+"%' or ";
							}
						}
						if(hql.lastIndexOf("or")!=-1){
							hql=hql.substring(0, hql.lastIndexOf("or"));
						}
					}
				}else{
						hql="";
						for(Department de:list){
							if(!de.getDepartmentSn().equals(departmentSn)){
								hql+="i.departmentSn like '"+de.getDepartmentSn()+"%' or ";
							}
						}
						if(hql.lastIndexOf("or")!=-1){
							hql=hql.substring(0, hql.lastIndexOf("or"));
						}
				}
				
		}else{
			for(Department de:list){
				hql=de.getDepartmentSn();
			}
			hql="i.departmentSn like '"+hql+"%'";
		}
		return hql;
	}
	@Override
	public String getDownDepartmentByDepartmentType(String departmentSn, String departmentTypeSn) {
		String hql="";
		String str=departmentTypeDao.getDownDepartmentTypeByParent(departmentTypeSn);
		hql="select p FROM Department p WHERE p.departmentSn like '"+departmentSn+"%' AND p.departmentType.departmentTypeSn in "+str;
		List<Department> list=(List<Department>)getSessionFactory().getCurrentSession().createQuery(hql).list();
		hql="";
		for(Department de:list){
			hql+="i.departmentSn like '"+de.getDepartmentSn()+"%' or ";
		}
		if(hql.lastIndexOf("or")!=-1){
			hql=hql.substring(0, hql.lastIndexOf("or"));
		}
		return hql;
	}

	@Override
	public List<Department> getDepartments(String parentDepartmentSn,String departmentTypeSn) {
		Department department=this.getByDepartmentSn(parentDepartmentSn);
		String hql="";
		Query query;
		if(department!=null){
			if(!department.getDepartmentType().getDepartmentTypeSn().equals(departmentTypeSn)){	
				hql="select p FROM Department p WHERE p.departmentSn like:departmentSn and p.departmentType.departmentTypeSn=:departmentTypeSn";
				query=getSessionFactory().getCurrentSession().createQuery(hql);
				query=query.setParameter("departmentSn", parentDepartmentSn+"%");
				query=query.setParameter("departmentTypeSn", departmentTypeSn);
			}
			else{
				hql="select p FROM Department p WHERE p.parentDepartment.departmentSn=:departmentSn";
				query=getSessionFactory().getCurrentSession().createQuery(hql);
				query=query.setParameter("departmentSn", parentDepartmentSn);
			}
		}else{
			return null;
		}
		return (List<Department>)query.list();
	}
	@SuppressWarnings("unused")
	@Override
	public List<Department> getDepartmentsNew(String parentDepartmentSn,String departmentTypeSn,String clickEchart) {
		Department department=this.getByDepartmentSn(parentDepartmentSn);
		StringBuffer hqll=new StringBuffer();
		String hql="";
		Query query;
		if(department!=null){
			if(clickEchart!=null && !"".equals(clickEchart)){
				hql="select p from Department p where p.parentDepartment.departmentSn ='"+parentDepartmentSn+"'";
			}else{
				hql="select p from Department p where p.departmentSn ='"+parentDepartmentSn+"'";
			}
			if(departmentTypeSn!=null && !"".equals(departmentTypeSn)){
				hql="select p from Department p where p.departmentSn like '"+parentDepartmentSn+"%' and p.departmentType.departmentTypeSn ='"+departmentTypeSn+"' and p.departmentSn !='"+parentDepartmentSn+"'";
			}
			query=getSessionFactory().getCurrentSession().createQuery(hql);
		}else{
			return null;
		}
		return (List<Department>)query.list();
	}
	@Override
	public List<String> getAllDepartmentSn() {
		String hql="select p.departmentSn FROM Department p";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (List<String>)query.list();
	}
	@Override
	public List<String> getDepartmentSnByParentDepartmentSn(String parentDepartmentSn) {
		String hql="select p.departmentSn FROM Department p where p.parentDepartment.departmentSn='"+parentDepartmentSn+"'";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (List<String>)query.list();
	}
	@Override
	public Standard getImplStandards(String departmentSn, StandardType standardType) {
		Department department=getUpNearestImplDepartment(departmentSn);
		String hql="";
		if(department!=null){
			hql="from Standard s where s.departmentType.departmentTypeSn=:departmentTypeSn and standardType=:standardType";
			Query query=getSessionFactory().getCurrentSession().createQuery(hql).setString("departmentTypeSn", department.getDepartmentType().getDepartmentTypeSn()).setParameter("standardType", standardType);
			List<Standard> standards=(List<Standard>)query.list();
			if(standards.size()==0)
				return null;
			else
				return standards.get(0);
		}
		return null;		
	}
	@Override
	public Standard queryStandardByDepartmentType(String departmentTypeSn, StandardType standardType) {
		String hql="from Standard s where s.departmentType.departmentTypeSn=:departmentTypeSn and standardType=:standardType";
			Query query=getSessionFactory().getCurrentSession().createQuery(hql).setString("departmentTypeSn", departmentTypeSn).setParameter("standardType", standardType);
			List<Standard> standards=(List<Standard>)query.list();
			if(standards.size()==0)
				return null;
			else
				return standards.get(0);
	}
	@Override
	public long countHql(String hql) {
		return(Long)getSessionFactory().getCurrentSession().createQuery(hql)
				.uniqueResult();
	}
	@Override
	public List<Department> getAllDepartmentsByType(String departmentTypeSn) {
		String hql="select p from Department p where p.departmentType.departmentTypeSn='"+departmentTypeSn+"'";
		Query query = getSessionFactory().getCurrentSession().createQuery(hql);
		return (List<Department>)query.list();
	}
	/**
	 * @author mahui
	 * @method 
	 * @date 2016
	 */
	@Override
	public List<Department> queryDepartment(String departmentSn) {
		int roleType=(int) ServletActionContext.getRequest().getSession().getAttribute("roleType");
		
		Department department=null;
		String hql="";
		List<Department> list=new ArrayList<Department>();
		if(roleType==0 || roleType==2){
			List<DepartmentType> departmentTypes=departmentTypeDao.getImplDepartmentTypes(departmentSn);
			hql="select d from Department d where d.deleted=false and d.departmentType.isImplDepartmentType=true and d.departmentSn like:departmentSn";
	    	if(departmentTypes.size()>0){
	    		department=getByDepartmentSn(departmentSn);
	    	}else{
	    		department=getUpNearestImplDepartment(departmentSn);
	    	}
	    	list=(List<Department>) getSessionFactory().getCurrentSession()
					.createQuery(hql)
					.setString("departmentSn", department.getDepartmentSn()+"%")
					.list();
		}else if(roleType==1) {
			department=this.getUpNerestFgs(departmentSn);
			hql="select d from Department d where d.deleted=false and d.departmentType.isImplDepartmentType=true and d.departmentSn like:departmentSn";
			list=(List<Department>) getSessionFactory().getCurrentSession()
					.createQuery(hql)
					.setString("departmentSn", department.getDepartmentSn()+"%")
					.list();
			if(list==null || list.size()==0) {
				list.add(department);
			}
		}
		return list;
	}
	@Override
	public void getDepartmentNameByDepartment() {
		List<Department> list=getSessionFactory().getCurrentSession()
								.createQuery("select d from Department d")
								.list();
		Iterator<Department> iter = list.iterator();  
		while(iter.hasNext()){
			Department de=iter.next();
			String departmentName=de.getDepartmentName();
			Department departmentImpl=this.getUpNearestImplDepartment(de.getDepartmentSn());
			if(departmentImpl!=null){
				departmentName=departmentImpl.getDepartmentName();
			}
			Department dee=this.getByDepartmentSn(de.getDepartmentSn());
			dee.setImplDepartmentName(departmentName);
			this.update(dee);
		}
	}
	@Override
	public Department getUpNerestFgs(String departmentSn) {
		String sql="select department.* from department where department_type_sn='FGS' and LOCATE(department.department_sn,'"+departmentSn+"')=1 order by CHAR_LENGTH(department.department_sn) desc";
		List<Department> departments=getSessionFactory().getCurrentSession()
				.createSQLQuery(sql).addEntity(Department.class).list();
		if(departments.size()==0)
			return null;
		else
			return departments.get(0);
	}
	@Override
	public JSONObject queryDepartmentStandardIndex(String standardSn, String standardIndexSn, int page, int rows) {
		String hql = "SELECT u.checkedDepartment.departmentSn AS departmentSn,"
					+ " u.checkedDepartment.implDepartmentName AS implDepartmentName,"
					+ " u.checkedDepartment.departmentName AS departmentName,"
					+ " u.checkedDepartment.departmentType.departmentTypeName AS departmentTypeName,"
					+ " u.checkedDepartment.principal.personName AS personName,"
					+ " COUNT(*) AS countNum"
				+ " FROM UnsafeCondition u WHERE u.deleted=false"
//					+ " AND u.checkedDepartment.departmentType.departmentTypeSn=:departmentTypeSn"
					+ " AND u.standardIndex.standard.standardSn=:standardSn"
					+ " AND u.standardIndex.indexSn=:standardIndexSn"
				+ " GROUP BY u.checkedDepartment.departmentSn ORDER BY countNum DESC";
		
		List<Object> list = getSessionFactory().getCurrentSession().createQuery(hql)
//					.setString("departmentTypeSn", departmentTypeSn)
					.setString("standardSn", standardSn)
					.setString("standardIndexSn", standardIndexSn)
					.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
					.setFirstResult(( page - 1 ) * rows).setMaxResults(rows).list();
		
		List<Object> list2 = getSessionFactory().getCurrentSession().createQuery(hql)
//					.setString("departmentTypeSn", departmentTypeSn)
					.setString("standardSn", standardSn)
					.setString("standardIndexSn", standardIndexSn)
					.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
		
		JSONArray jsonArray = new JSONArray();
		if(list.size() > 0){
			for(int i = 0; i < list.size(); i++){
				JSONObject jo = new JSONObject();
				Map<Object, Object> map = (Map<Object, Object>)list.get(i);
				jo.put("departmentSn", map.get("departmentSn"));
				jo.put("departmentName", map.get("departmentName"));
				jo.put("implDepartmentName", map.get("implDepartmentName"));
				jo.put("departmentTypeName", map.get("departmentTypeName"));
				jo.put("principal", map.get("personName"));
				jo.put("countNum", map.get("countNum"));
				jsonArray.add(jo);
			}
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("total", list2.size());
		jsonObject.put("rows", jsonArray);
		return jsonObject;
	}
	
	
}
