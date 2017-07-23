package cn.jagl.aq.service;

import cn.jagl.aq.domain.SuperviseDailyReportDetails;

public interface SuperviseDailyReportDetailsService {

	public void updateSuperviseDailyReportDetails(SuperviseDailyReportDetails superviseDailyReportDetails);
	
	//根据日报id，部门、item编号查询
	public SuperviseDailyReportDetails getByMany(int id, String departmentSn,String superviseItemSn);
	
	//添加
	public void save(SuperviseDailyReportDetails superviseDailyReportDetails);
	
}
