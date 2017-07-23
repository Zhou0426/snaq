package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.SystemAuditAttachment;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *	附件
 * @author 马辉
 * @since JDK1.8
 * @history 2017年4月18日下午6:10:19 马辉 新建
 */
public interface SystemAuditAttachmentDao extends BaseDao<SystemAuditAttachment> {
	//根据体系审核编号查询未删除个数
	public long count(String auditSn);
	
	//删除
	public JSONObject delete(int id);
	
	//查询集合
	public JSONArray query(String auditSn);
}
