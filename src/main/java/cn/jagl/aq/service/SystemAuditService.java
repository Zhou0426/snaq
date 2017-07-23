package cn.jagl.aq.service;

import java.io.InputStream;
import java.util.List;

import cn.jagl.aq.domain.SystemAudit;

/**
 * @author mahui
 * @method 
 * @date 2016年7月25日下午12:53:10
 */
public interface SystemAuditService {
	//添加
	public void save(SystemAudit systemAudit);
	//修改
	public void update(SystemAudit systemAudit);
	//getBySn
	public SystemAudit getBySn(String auditSn);
	//查询
	public List<SystemAudit> query(String hql,int page,int rows);
	//查询总数
	public long count(String hql);
	//多条删除
	public void deleteByIds(String ids);
	//getById
	public SystemAudit getById(int id);
	//根据部门编号获取季度审核得分
	public SystemAudit queryScore(String departmentSn,int quarter,String year);
	//获取无需扣扣的分数
	public int countNotScore(String auditSn);
	//判断是否全选
	public boolean isChecked(String auditSn,String indexSn);
	//根据指标编号和审核编号判断是否应该打分
	boolean isScore(String auditSn,String indexSn);
	//获取某个指标无需扣分的总分
	public int countNotScoreBySn(String auditSn,String indexSn);
	//输出审核打分表
	InputStream export(String standardSn,String auditSn);
	//输出各矿得分情况
	InputStream exportSummary(String year,String month,int type,String standardSn,String departmentTypeSn);
	
	/**
	 * @method 体系审核隐患导出
	 * @author mahui
	 * @return String
	 */
	InputStream exportUnsafeCondition(String auditSn);
	
	//根据部门编号获取季度审核得分
	SystemAudit queryShScore(String departmentSn,String year);
}
