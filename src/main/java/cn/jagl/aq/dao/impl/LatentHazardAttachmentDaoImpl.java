package cn.jagl.aq.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.LatentHazardAttachmentDao;
import cn.jagl.aq.domain.LatentHazardAttachment;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Repository("latentHazardAttachmentDao")
public class LatentHazardAttachmentDaoImpl extends BaseDaoHibernate5<LatentHazardAttachment>
		implements LatentHazardAttachmentDao {

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject queryAttachmentByLatentHazardSn(String latentHazardSn) {
		String hql = "SELECT l FROM LatentHazardAttachment l WHERE l.latentHazard.latentHazardSn=:latentHazardSn"
				+ " AND l.latentHazard.deleted=false AND l.deleted=false ORDER BY l.id desc";
		List<LatentHazardAttachment> list = getSessionFactory().getCurrentSession().createQuery(hql)
				.setString("latentHazardSn", latentHazardSn).list();
		JSONObject jsonObject = new JSONObject();
		JSONArray ja = new JSONArray();
		for( LatentHazardAttachment latentHazardAttachment : list ){
			JSONObject jo = new JSONObject();
			jo.put("id", latentHazardAttachment.getId());
			jo.put("attachmentType", latentHazardAttachment.getAttachmentType());
			jo.put("logicalFileName", latentHazardAttachment.getLogicalFileName());
			jo.put("physicalFileName", latentHazardAttachment.getPhysicalFileName());
			if( latentHazardAttachment.getUploadPerson() != null ){
				jo.put("uploadPerson", latentHazardAttachment.getUploadPerson().getPersonName());
			}else{
				jo.put("uploadPerson", "нч");
			}
			if( latentHazardAttachment.getUploadDateTime() != null ){
				jo.put("uploadDateTime", latentHazardAttachment.getUploadDateTime().toString());
			}else{
				jo.put("uploadDateTime", "нч");
			}
			ja.add(jo);
		}
		jsonObject.put("rows", ja);
		return jsonObject;
	}

	@Override
	public void deleteById(int id) {
		String hql = "UPDATE latent_hazard_attachment SET deleted=true WHERE id=:id";
		getSessionFactory().getCurrentSession().createSQLQuery(hql)
				.setInteger("id", id).executeUpdate();
	}


}
