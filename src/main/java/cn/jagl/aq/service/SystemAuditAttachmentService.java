package cn.jagl.aq.service;

import cn.jagl.aq.domain.SystemAuditAttachment;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author 马辉
 * @since JDK1.8
 * @history 2017年4月18日下午6:11:47 马辉 新建
 */
public interface SystemAuditAttachmentService {
	//根据体系审核编号查询未删除个数
	public long count(String auditSn);
	
	//删除
	public JSONObject delete(int id);
	
	//添加
	public void save(SystemAuditAttachment systemAuditAttachment);
	
	//查询集合
	public JSONArray query(String auditSn);	
}
