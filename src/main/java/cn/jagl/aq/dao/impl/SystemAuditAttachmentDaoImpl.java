package cn.jagl.aq.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.SystemAuditAttachmentDao;
import cn.jagl.aq.domain.SystemAuditAttachment;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 * @author 马辉
 * @since JDK1.8
 * @history 2017年4月18日下午6:11:00 马辉 新建
 */
@Repository("systemAuditAttachmentDao")
public class SystemAuditAttachmentDaoImpl extends BaseDaoHibernate5<SystemAuditAttachment> implements SystemAuditAttachmentDao{

	@Override
	public long count(String auditSn) {
		String hql="select count(a) from SystemAuditAttachment a where a.deleted=false and a.systemAudit.auditSn=:auditSn";
		
		return (long) getSessionFactory().getCurrentSession().createQuery(hql)
				.setString("auditSn", auditSn)
				.uniqueResult();
	}

	@Override
	public JSONObject delete(int id) {
		String hql="update SystemAuditAttachment s SET s.deleted=true where s.id=:id";
		JSONObject jo=new JSONObject();
		
		try{
			getSessionFactory().getCurrentSession().createQuery(hql).setInteger("id", id).executeUpdate();
			jo.put("status", true);
		}catch(Exception e){
			jo.put("status", false);
		}
		return jo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONArray query(String auditSn) {
		JSONArray array=new JSONArray();
		String hql="select a from SystemAuditAttachment a where a.deleted=false and a.systemAudit.auditSn=:auditSn";
		List<SystemAuditAttachment> list=getSessionFactory().getCurrentSession().createQuery(hql)
				.setString("auditSn", auditSn).list();
		
		for(SystemAuditAttachment s:list){
			JSONObject jo=new JSONObject();
			jo.put("id", s.getId());
			jo.put("attachmentType", s.getAttachmentType());
			jo.put("logicalFileName", s.getLogicalFileName());
			jo.put("physicalFileName", s.getPhysicalFileName());
			array.add(jo);
		}
		return array;
	}
}
