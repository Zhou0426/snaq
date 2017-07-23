package cn.jagl.aq.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.LatentHazardDao;
import cn.jagl.aq.domain.LatentHazard;
import cn.jagl.aq.domain.LatentHazardAttachment;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Repository("latentHazardDao")
public class LatentHazardDaoImpl extends BaseDaoHibernate5<LatentHazard> implements LatentHazardDao {

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject showData(String personId, Integer page, Integer rows) {
		String hql = "SELECT l FROM LatentHazard l WHERE l.editor.personId=:personId AND l.deleted=false ORDER BY l.id desc";
		List<LatentHazard> list = getSessionFactory().getCurrentSession().createQuery(hql)
				.setString("personId", personId).setFirstResult((page - 1) * rows)
				.setMaxResults(rows).list();
		hql = "SELECT count(*) FROM LatentHazard l WHERE l.editor.personId=:personId AND l.deleted=false";
		long total = (long) getSessionFactory().getCurrentSession().createQuery(hql)
				.setString("personId", personId).uniqueResult();
		JSONObject jsonObject = this.queryData(list, total);
		return jsonObject;
	}

	@Override
	public void deleteById(int id) {
		String hql = "UPDATE latent_hazard SET deleted=true WHERE id=:id";
		getSessionFactory().getCurrentSession().createSQLQuery(hql)
				.setInteger("id", id).executeUpdate();
		hql = "UPDATE latent_hazard_attachment SET deleted=true WHERE id=:id";
		getSessionFactory().getCurrentSession().createSQLQuery(hql)
				.setInteger("id", id).executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject showAuditData(String departmentSn, Integer page, Integer rows) {
		String hql = "SELECT l FROM LatentHazard l WHERE l.departmentReportTo.departmentSn=:departmentSn"
				+ " AND l.deleted=false AND l.status != '未上报' ORDER BY l.id desc, l.status desc";
		List<LatentHazard> list = getSessionFactory().getCurrentSession().createQuery(hql)
				.setString("departmentSn", departmentSn).setFirstResult((page - 1) * rows)
				.setMaxResults(rows).list();
		hql = "SELECT count(*) FROM LatentHazard l WHERE l.departmentReportTo.departmentSn=:departmentSn"
				+ " AND l.deleted=false AND l.status != '未上报'";
		long total = (long) getSessionFactory().getCurrentSession().createQuery(hql)
				.setString("departmentSn", departmentSn).uniqueResult();
		JSONObject jsonObject = this.queryData(list, total);
		return jsonObject;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject showCancelData(Integer page, Integer rows) {
		String hql = "SELECT l FROM LatentHazard l WHERE l.deleted=false"
				+ " AND l.status != '未上报' ORDER BY l.id desc, l.status desc";
		List<LatentHazard> list = getSessionFactory().getCurrentSession().createQuery(hql)
				.setFirstResult((page - 1) * rows)
				.setMaxResults(rows).list();
		hql = "SELECT count(*) FROM LatentHazard l WHERE l.deleted=false"
				+ " AND l.status != '未上报'";
		long total = (long) getSessionFactory().getCurrentSession().createQuery(hql)
				.uniqueResult();
		JSONObject jsonObject = this.queryData(list, total);
		return jsonObject;
	}
	
	private JSONObject queryData(List<LatentHazard> list, long total){
		JSONObject jsonObject = new JSONObject();
		JSONArray ja = new JSONArray();
		for( LatentHazard latentHazard : list ){
			JSONObject jo = new JSONObject();
			jo.put("id", latentHazard.getId());
			jo.put("latentHazardSn", latentHazard.getLatentHazardSn());
			jo.put("checkClass", latentHazard.getCheckClass());
			if( latentHazard.getCheckUnitName() != null && !"".equals(latentHazard.getCheckUnitName()) ){
				jo.put("checkUnit", latentHazard.getCheckUnitName());
				jo.put("implDepartmentName", "无");
				jo.put("departmentSn", "无");
			}else if( latentHazard.getCheckUnit() != null ){
				if( latentHazard.getCheckUnit().getImplDepartmentName() != null ){
					jo.put("implDepartmentName", latentHazard.getCheckUnit().getImplDepartmentName());
				}else{
					jo.put("implDepartmentName", "无");
				}
				jo.put("departmentSn", latentHazard.getCheckUnit().getDepartmentSn());
				jo.put("checkUnit", latentHazard.getCheckUnit().getDepartmentName());
			}else{
				jo.put("checkUnit", "无");
				jo.put("implDepartmentName", "无");
				jo.put("departmentSn", "无");
			}
			jo.put("latentHazardDescription", latentHazard.getLatentHazardDescription());
			if( latentHazard.getDepartment() != null ){
				jo.put("department", latentHazard.getDepartment().getDepartmentName());
				//jo.put("departmentImpl", latentHazard.getDepartment().getImplDepartmentName());
			}else{
				jo.put("department", "无");
			}
			if( latentHazard.getDepartmentReportTo() != null ){
				jo.put("departmentReportTo", latentHazard.getDepartmentReportTo().getDepartmentName());
				jo.put("departmentReportToSn", latentHazard.getDepartmentReportTo().getDepartmentSn());
				//jo.put("departmentImpl", latentHazard.getDepartment().getImplDepartmentName());
			}else{
				jo.put("departmentReportTo", "无");
			}
			if( latentHazard.getHappenDateTime() != null ){
				jo.put("happenDateTime", latentHazard.getHappenDateTime().toString());
			}else{
				jo.put("happenDateTime", "无");
			}
			jo.put("happenLocation", latentHazard.getHappenLocation());
			if( latentHazard.getEditor() != null ){
				jo.put("editor", latentHazard.getEditor().getPersonName());
			}else{
				jo.put("editor", "无");
			}
			jo.put("status", latentHazard.getStatus());
			if( latentHazard.getReportDateTime() != null ){
				jo.put("reportDateTime", latentHazard.getReportDateTime().toString());
			}else{
				jo.put("reportDateTime", "未上报");
			}
			if( latentHazard.getAuditDateTime() != null ){
				jo.put("auditDateTime", latentHazard.getAuditDateTime().toString());
			}else{
				jo.put("auditDateTime", "未审核");
			}
			if( latentHazard.getAuditor() != null ){
				jo.put("auditor", latentHazard.getAuditor().getPersonName());
			}else{
				jo.put("auditor", "无");
			}
			if( latentHazard.getAuditSuggestion() != null ){
				jo.put("auditSuggestion", latentHazard.getAuditSuggestion());
			}else{
				jo.put("auditSuggestion", "无");
			}
			if( latentHazard.getCancelDateTime() != null ){
				jo.put("cancelDateTime", latentHazard.getCancelDateTime().toString());
			}else{
				jo.put("cancelDateTime", "未销号");
			}
			if( latentHazard.getCancelPerson() != null ){
				jo.put("cancelPerson", latentHazard.getCancelPerson().getPersonName());
			}else{
				jo.put("cancelPerson", "无");
			}
			if( latentHazard.getLatentHazardAttachments() != null ){
				int sum = 0;
				for( LatentHazardAttachment latentHazardAttachment : latentHazard.getLatentHazardAttachments() ){
					if( latentHazardAttachment.getDeleted() != true ){
						sum = sum + 1;
					}
				}
				jo.put("latentHazardAttachment", sum);
			}else{
				jo.put("latentHazardAttachment", "0");
			}
			ja.add(jo);
		}
		jsonObject.put("total", total);
		jsonObject.put("rows", ja);
		return jsonObject;
	}

	
}
