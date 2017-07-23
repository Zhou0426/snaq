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
 * @date 2016年7月25日下午1:11:17
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
	//获取无需扣扣的分数
	@Override
	public int countNotScore(String auditSn) {
		return systemAuditDao.countNotScore(auditSn);
	}
	
	//判断是否全选
	@Override
	public boolean isChecked(String auditSn, String indexSn) {		
		return systemAuditDao.isChecked(auditSn, indexSn);
	}
	//根据指标编号和审核编号判断是否应该打分
	@Override
	public boolean isScore(String auditSn, String indexSn) {
		return systemAuditDao.isScore(auditSn, indexSn);
	}
	//获取某个指标无需扣分的总分
	@Override
	public int countNotScoreBySn(String auditSn, String indexSn) {
		return systemAuditDao.countNotScoreBySn(auditSn, indexSn);
	}
	//输出审核打分表
	@Override
	public InputStream export(String standardSn, String auditSn) {
		return systemAuditDao.export(standardSn, auditSn);
	}
	//输出各矿得分情况
	@Override
	public InputStream exportSummary(String year, String month, int type, String standardSn, String departmentTypeSn) {
		return systemAuditDao.exportSummary(year, month, type, standardSn, departmentTypeSn);
	}
	
	/**
	 * @method 体系审核隐患导出
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
