package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.CertificationTypeDao;
import cn.jagl.aq.domain.CertificationType;
import cn.jagl.aq.service.CertificationTypeService;
import net.sf.json.JSONObject;

@Service("certificationTypeService")
public class CertificationTypeServiceImpl implements CertificationTypeService{
	
	@Resource(name="certificationTypeDao")
	private CertificationTypeDao certificationTypeDao;

	@Override
	public CertificationType getByCertificationTypeSn(String certificationTypeSn) {
		return certificationTypeDao.getByCertificationTypeSn(certificationTypeSn);
	}

	@Override
	public long countHql(String hql) {
		return certificationTypeDao.countHql(hql);
	}

	@Override
	public List<CertificationType> findByPage(String hql, int pageNo, int pageSize) {
		return certificationTypeDao.findByPage(hql, pageNo, pageSize);
	}

	@Override
	public void add(CertificationType certificationType) {
		certificationTypeDao.save(certificationType);		
	}

	@Override
	public void delete(CertificationType certificationType) {
		certificationTypeDao.delete(certificationType);		
	}

	@Override
	public void update(CertificationType certificationType) {
		certificationTypeDao.update(certificationType);
	}

	@Override
	public CertificationType getById(int id) {
		return certificationTypeDao.getById(id);
	}

	@Override
	public CertificationType getByCertificationTypeName(String stringValue) {
		return certificationTypeDao.getByCertificationTypeName(stringValue);
	}

	@Override
	public JSONObject queryStatistics(String departmentSn, int page, int rows) {
		return certificationTypeDao.queryStatistics(departmentSn, page, rows);
	}
}
