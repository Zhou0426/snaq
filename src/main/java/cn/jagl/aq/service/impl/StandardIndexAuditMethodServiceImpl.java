package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.StandardIndexAuditMethodDao;
import cn.jagl.aq.domain.StandardIndexAuditMethod;
import cn.jagl.aq.service.StandardIndexAuditMethodService;
@Service("standardIndexAuditMethodService")
public class StandardIndexAuditMethodServiceImpl implements StandardIndexAuditMethodService {

	@Resource(name="standardIndexAuditMethodDao")
	private StandardIndexAuditMethodDao standardIndexAuditMethodDao;
	@Override
	public void save(StandardIndexAuditMethod standardIndexAuditMethod) {
		standardIndexAuditMethodDao.save(standardIndexAuditMethod);

	}

	@Override
	public void delete(int id) {
		standardIndexAuditMethodDao.delete(StandardIndexAuditMethod.class, id);
	}

	@Override
	public List<StandardIndexAuditMethod> queryJoinStandardIndex(int id) {
		return standardIndexAuditMethodDao.queryJoinStandardIndex(id);
	}

	@Override
	public StandardIndexAuditMethod getById(int id) {
		return (StandardIndexAuditMethod)standardIndexAuditMethodDao.getById(id);
	}

	@Override
	public StandardIndexAuditMethod getBySn(String auditMethodSn) {
		return (StandardIndexAuditMethod)standardIndexAuditMethodDao.getBySn(auditMethodSn);
	}

	@Override
	public void delete(StandardIndexAuditMethod standardIndexAuditMethod) {
		standardIndexAuditMethodDao.delete(standardIndexAuditMethod);
	}

}
