package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.CertificationDao;
import cn.jagl.aq.domain.Certification;
import cn.jagl.aq.service.CertificationService;
@Service("certificationService")
public class CertificationServiceImpl implements CertificationService {

	@Resource(name="certificationDao")
	private CertificationDao certificationDao;

	@Override
	public long countHql(String hql) {
		return certificationDao.countHql(hql);
	}

	@Override
	public List<Certification> findByPage(String hql, int pageNo, int pageSize) {
		return certificationDao.findByPage(hql, pageNo, pageSize);
	}

	@Override
	public Certification getByCertificationSn(String certificationSn) {
		return certificationDao.getByCertificationSn(certificationSn);
	}

	@Override
	public void add(Certification certification) {
		certificationDao.save(certification);
	}

	@Override
	public void delete(Certification certification) {
		certificationDao.delete(certification);
		
	}

	@Override
	public void update(Certification certification) {
		certificationDao.update(certification);
		
	}

	@Override
	public Certification getById(int id) {
		return certificationDao.getById(id);
	}

	@Override
	public Certification getByCertificationSnAndTypeSn(String certificationSn, String certificationTypeSn) {
		return certificationDao.getByCertificationSnAndTypeSn(certificationSn ,certificationTypeSn);
	}

	@Override
	public void deleteByIds(String ids) {
		certificationDao.deleteByIds(ids);
	}
}