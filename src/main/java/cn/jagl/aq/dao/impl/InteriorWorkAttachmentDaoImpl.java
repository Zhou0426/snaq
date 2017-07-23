package cn.jagl.aq.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.InteriorWorkAttachmentDao;
import cn.jagl.aq.domain.InteriorWorkAttachment;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Repository("interiorWorkAttachmentDao")
public class InteriorWorkAttachmentDaoImpl extends BaseDaoHibernate5<InteriorWorkAttachment>
		implements InteriorWorkAttachmentDao {

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject queryByInteriorWorkSn(String interiorWorkSn, Integer page, Integer rows) {
		String hql = "SELECT i FROM InteriorWorkAttachment i WHERE i.interiorWork.interiorWorkSn=:interiorWorkSn"
				+ " AND i.deleted = false ORDER BY i.id DESC";
		List<InteriorWorkAttachment> list = getSessionFactory().getCurrentSession()
											.createQuery(hql)
											.setString("interiorWorkSn", interiorWorkSn)
											.setFirstResult( ( page - 1 ) * rows )
											.setMaxResults( rows )
											.list();
		JSONArray ja = new JSONArray();
		for( InteriorWorkAttachment interiorWorkAttachment : list ){
			JSONObject jo = new JSONObject();
			jo.put("id", interiorWorkAttachment.getId());
			jo.put("attachmentType", interiorWorkAttachment.getAttachmentType());
			jo.put("logicalFileName", interiorWorkAttachment.getLogicalFileName());
			jo.put("physicalFileName", interiorWorkAttachment.getPhysicalFileName());
			ja.add(jo);
		}
		hql = "SELECT count(i) FROM InteriorWorkAttachment i WHERE i.interiorWork.interiorWorkSn=:interiorWorkSn"
				+ " AND i.deleted = false ORDER BY i.id DESC";
		long total = (long) getSessionFactory().getCurrentSession()
					.createQuery(hql)
					.setString("interiorWorkSn", interiorWorkSn)
					.uniqueResult();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("rows", ja);
		jsonObject.put("total", total);
		return jsonObject;
	}

	

}
