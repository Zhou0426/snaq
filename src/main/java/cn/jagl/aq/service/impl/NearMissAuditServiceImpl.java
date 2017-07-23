package cn.jagl.aq.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.DepartmentDao;
import cn.jagl.aq.dao.NearMissAuditDao;
import cn.jagl.aq.domain.NearMissAudit;
import cn.jagl.aq.service.NearMissAuditService;
@Service("nearMissAuditService")
public class NearMissAuditServiceImpl implements NearMissAuditService{
	@Resource(name="nearMissAuditDao")
	private NearMissAuditDao nearMissAuditDao;

	@Override
	public void add(NearMissAudit nearMissAudit) {
		nearMissAuditDao.save(nearMissAudit);
	}
}
