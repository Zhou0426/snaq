package cn.jagl.aq.dao;

import java.io.InputStream;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.SystemAudit;

/**
 * @author mahui
 * @method 
 * @date 2016年7月25日下午12:46:50
 */
public interface SystemAuditDao extends BaseDao<SystemAudit> {
	//获取记录数
	long count(String hql);
	//多条删除
	void deleteByIds(String ids);
	//根据部门编号获取季度审核得分
	SystemAudit queryScore(String departmentSn,int quarter,String year);
	//getBySn;
	SystemAudit getBySn(String auditSn);
	//获取无需扣扣的分数
	int countNotScore(String auditSn);
	//判断是否全选
	boolean isChecked(String auditSn,String indexSn);
	//根据指标编号和审核编号判断是否应该打分
	boolean isScore(String auditSn,String indexSn);
	//获取某个指标无需扣分的总分
	int countNotScoreBySn(String auditSn,String indexSn);
	//根据部门编号获取季度审核得分
	SystemAudit queryShScore(String departmentSn,String year);
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
}
