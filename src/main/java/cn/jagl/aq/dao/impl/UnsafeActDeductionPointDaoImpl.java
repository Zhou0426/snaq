package cn.jagl.aq.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.UnsafeActDeductionPointDao;
import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.DepartmentType;
import cn.jagl.aq.domain.UnsafeActDeductionPoint;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 * @author 马辉
 * @since JDK1.8
 * @history 2017年5月5日下午9:36:34 马辉 新建
 */
@Repository("unsafeActDeductionPointDao")
public class UnsafeActDeductionPointDaoImpl extends BaseDaoHibernate5<UnsafeActDeductionPoint> implements UnsafeActDeductionPointDao {

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject query(Byte type, String departmentTypeSn,String departmentSn,int page,int rows) {
		String hql="";
		if(type==0){
			if(departmentTypeSn!=null&&departmentTypeSn.trim().length()>0){
				hql="select u from UnsafeActDeductionPoint u where u.departmentType.departmentTypeSn='"+departmentTypeSn+"'";				
			}else{
				hql="select u from UnsafeActDeductionPoint u where u.departmentType is not null";
			}
		}else{
			hql="select u from UnsafeActDeductionPoint u where u.department.departmentSn='"+departmentSn+"'";
		}
		
		List<UnsafeActDeductionPoint> list=getSessionFactory().getCurrentSession().createQuery(hql)
				.setFirstResult(rows*(page-1))
				.setMaxResults(rows)
				.list();
		
		JSONArray array=new JSONArray();
		
		for(UnsafeActDeductionPoint u:list){
			JSONObject jo=new JSONObject();
			jo.put("id", u.getId());
			jo.put("year", u.getYear());
			jo.put("unsafeActLevel", u.getUnsafeActLevel());
			jo.put("deductionPoints", u.getDeductionPoints());
			Department department=u.getDepartment();
			if(department!=null){
				jo.put("departmentSn", department.getDepartmentSn());
				jo.put("departmentName", department.getDepartmentName());
			}
			DepartmentType departmentType=u.getDepartmentType();
			if(departmentType!=null){
				jo.put("departmentTypeSn", departmentType.getDepartmentTypeSn());
				jo.put("departmentTypeName", departmentType.getDepartmentTypeName());
			}
			array.add(jo);
		}
		
		JSONObject data=new JSONObject();
		data.put("rows", array);
		data.put("total", getSessionFactory().getCurrentSession().createQuery(hql.replaceFirst("u", "count(u)")).uniqueResult());
		return data;
	}

	@Override
	public UnsafeActDeductionPoint getByMany(Byte type, String departmentTypeSn, String departmentSn,int year, int level) {
		String hql="";
		if(type==0){
			hql="select u from UnsafeActDeductionPoint u where u.departmentType.departmentTypeSn='"+departmentTypeSn+"' and u.year=:year and u.unsafeActLevel=:level";
		}else{
			hql="select u from UnsafeActDeductionPoint u where u.department.departmentSn='"+departmentSn+"' and u.year=:year and u.unsafeActLevel=:level";
		}
		return (UnsafeActDeductionPoint) getSessionFactory().getCurrentSession().createQuery(hql)
				.setInteger("year", year)
				.setInteger("level", level)
				.uniqueResult();
		
	}

	
}
