package cn.jagl.aq.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.SystemAuditAttachmentDao;
import cn.jagl.aq.domain.SystemAuditAttachment;
import cn.jagl.aq.service.SystemAuditAttachmentService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Service("systemAuditAttachmentService")
public class SystemAuditAttachmentServiceImpl implements SystemAuditAttachmentService {

	@Resource(name="systemAuditAttachmentDao")
	private SystemAuditAttachmentDao systemAuditAttachmentDao;
	@Override
	public long count(String auditSn) {
		// TODO Auto-generated method stub
		return systemAuditAttachmentDao.count(auditSn);
	}

	@Override
	public JSONObject delete(int id) {
		// TODO Auto-generated method stub
		return systemAuditAttachmentDao.delete(id);
	}

	@Override
	public void save(SystemAuditAttachment systemAuditAttachment) {
		// TODO Auto-generated method stub
		systemAuditAttachmentDao.save(systemAuditAttachment);
	}

	@Override
	public JSONArray query(String auditSn) {
		// TODO Auto-generated method stub
		return systemAuditAttachmentDao.query(auditSn);
	}

}
