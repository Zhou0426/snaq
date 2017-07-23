package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.SuperviseDailyReport;
import cn.jagl.aq.domain.SuperviseDailyReportDetails;

public interface SuperviseDailyReportDetailsDao extends BaseDao<SuperviseDailyReportDetails>{

	public List<SuperviseDailyReportDetails> getSuperviseDailyReportDetailsByDateandDept(SuperviseDailyReport superviseDailyReport, String departmentSn);

	//根据日报id，部门、item编号查询
	public SuperviseDailyReportDetails getByMany(int id, String departmentSn,String superviseItemSn);
	
}
