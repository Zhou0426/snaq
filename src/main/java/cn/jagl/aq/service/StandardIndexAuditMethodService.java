package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.StandardIndexAuditMethod;

/**
 * @author mahui
 * @method 
 * @date 2016年7月21日下午9:15:39
 */
public interface StandardIndexAuditMethodService {
	void save(StandardIndexAuditMethod standardIndexAuditMethod);
	void delete(int id);
	List<StandardIndexAuditMethod> queryJoinStandardIndex(int id);
	StandardIndexAuditMethod getById(int id);
	StandardIndexAuditMethod getBySn(String auditMethodSn);
	void delete(StandardIndexAuditMethod standardIndexAuditMethod);
}
