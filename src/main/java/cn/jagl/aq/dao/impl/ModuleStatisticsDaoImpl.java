package cn.jagl.aq.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.ModuleStatisticsDao;
import cn.jagl.aq.domain.ResourceUseStat;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Repository("moduleStatisticsDao")
public class ModuleStatisticsDaoImpl extends BaseDaoHibernate5<ResourceUseStat> implements ModuleStatisticsDao {

	@SuppressWarnings("unchecked")
	@Override
	public JSONArray findAll(int year, int month, String resourceSn) {
		String hql = "select r from ResourceUseStat r where r.statYear = " + year + " AND r.statMonth = " + month + " AND r.resource.resourceType = 'menu'";
		if(resourceSn != null && !"".equals(resourceSn)){
			hql = hql + " AND r.resource.parent.resourceSn = '" + resourceSn + "'";
		}else{
			hql = hql + " AND r.resource.parent is null";
		}
		hql = hql + " order by r.resource.resourceSn asc";
		List<ResourceUseStat> list=(List<ResourceUseStat>)getSessionFactory().getCurrentSession()
				.createQuery(hql).list();
		JSONArray array=new JSONArray();
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
		for(ResourceUseStat resourceUseStat:list){
			JSONObject jo=new JSONObject();
			if(resourceUseStat.getResource() != null){
				jo.put("id", resourceUseStat.getResource().getResourceSn());
				jo.put("resourceName", resourceUseStat.getResource().getResourceName());
				jo.put("sumClickCount", resourceUseStat.getSumClickCount());
				jo.put("avgClickCountPerDay", df.format(resourceUseStat.getAvgClickCountPerDay()));
				jo.put("sumUserCount", resourceUseStat.getSumUserCount());
				jo.put("avgUserCountPerDay", df.format(resourceUseStat.getAvgUserCountPerDay()));
//				String hql2 = "select count(*) from Resource r where r.resourceType = 'menu' AND r.parent.resourceSn = '" + resourceUseStat.getResource().getResourceSn() + "'";
//				Long total = (Long) getSessionFactory().getCurrentSession()
//						.createQuery(hql2).uniqueResult();
//				if(total > 0){
				if(resourceUseStat.getResource().getHasMenuChildren() == true){
					jo.put("state","closed");
				}else{
					jo.put("state", "open");
				}
			}
			array.add(jo);
		}
		return array;
	}

}
