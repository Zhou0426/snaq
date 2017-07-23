package cn.jagl.aq.service.impl;

import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.SystemAuditDao;
import cn.jagl.aq.domain.SystemAudit;
import cn.jagl.aq.service.SystemAuditService;

/**
 * @author mahui
 * @method 
 * @date 2016��7��25������1:11:17
 */
@Service("systemAuditService")
public class SystemAuditServiceImpl implements SystemAuditService {
	@Resource(name="systemAuditDao")
	private SystemAuditDao systemAuditDao;
	@Override
	public void save(SystemAudit systemAudit) {
		systemAuditDao.save(systemAudit);
	}
	@Override
	public void update(SystemAudit systemAudit) {
		systemAuditDao.update(systemAudit);
	}

	@Override
	public List<SystemAudit> query(String hql, int page, int rows) {
		return systemAuditDao.findByPage(hql, page, rows);
	}

	@Override
	public long count(String hql) {
		return systemAuditDao.count(hql);
	}
	@Override
	public void deleteByIds(String ids) {
		systemAuditDao.deleteByIds(ids);
	}
	@Override
	public SystemAudit getById(int id) {
		return systemAuditDao.get(SystemAudit.class, id);
	}
	@Override
	public SystemAudit queryScore(String departmentSn,int quarter,String year){
		return systemAuditDao.queryScore(departmentSn, quarter, year);
	}
	//getBySn
	@Override
	public SystemAudit getBySn(String auditSn) {
		return systemAuditDao.getBySn(auditSn);
	}
	//��ȡ����ۿ۵ķ���
	@Override
	public int countNotScore(String auditSn) {
		return systemAuditDao.countNotScore(auditSn);
	}
	
	//�ж��Ƿ�ȫѡ
	@Override
	public boolean isChecked(String auditSn, String indexSn) {		
		return systemAuditDao.isChecked(auditSn, indexSn);
	}
	//����ָ���ź���˱���ж��Ƿ�Ӧ�ô��
	@Override
	public boolean isScore(String auditSn, String indexSn) {
		return systemAuditDao.isScore(auditSn, indexSn);
	}
	//��ȡĳ��ָ������۷ֵ��ܷ�
	@Override
	public int countNotScoreBySn(String auditSn, String indexSn) {
		return systemAuditDao.countNotScoreBySn(auditSn, indexSn);
	}
	//�����˴�ֱ�
	@Override
	public InputStream export(String standardSn, String auditSn) {
		return systemAuditDao.export(standardSn, auditSn);
	}
	//�������÷����
	@Override
	public InputStream exportSummary(String year, String month, int type, String standardSn, String departmentTypeSn) {
		return systemAuditDao.exportSummary(year, month, type, standardSn, departmentTypeSn);
	}
	
	/**
	 * @method ��ϵ�����������
	 * @author mahui
	 * @return String
	 */
	@Override
	public InputStream exportUnsafeCondition(String auditSn) {
		return systemAuditDao.exportUnsafeCondition(auditSn);
	}
	@Override
	public SystemAudit queryShScore(String departmentSn, String year) {
		return systemAuditDao.queryShScore(departmentSn, year);
	}

}
