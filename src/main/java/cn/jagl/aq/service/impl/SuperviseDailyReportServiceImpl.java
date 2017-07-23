package cn.jagl.aq.service.impl;

import java.io.InputStream;
import java.sql.Date;
import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.SuperviseDailyReportDao;
import cn.jagl.aq.domain.SuperviseDailyReport;
import cn.jagl.aq.service.SuperviseDailyReportService;
import net.sf.json.JSONObject;
@Service("superviseDailyReportService")
public class SuperviseDailyReportServiceImpl implements SuperviseDailyReportService {
	@Resource(name="superviseDailyReportDao")
	private SuperviseDailyReportDao superviseDailyReportDao;

	@Override
	public SuperviseDailyReport getSuperviseDailyReportByDateandSn(Date reportDate, String departmentTypeSn){
		return superviseDailyReportDao.getSuperviseDailyReportByDateandSn(reportDate, departmentTypeSn);
	}
	@Override
	public void updateDailyReport(SuperviseDailyReport superviseDailyReport) {
		superviseDailyReportDao.updateDailyReport(superviseDailyReport);
	}
	
	
	@Override
	public void save(SuperviseDailyReport superviseDailyReport) {
		superviseDailyReportDao.save(superviseDailyReport);
	}
	
	//根据部门编号，年月获取个数
	@Override
	public long countByMany(String departmentSn,Date startDate,Date endDate) {
		return superviseDailyReportDao.countByMany(departmentSn, startDate, endDate);
	}
	//分页查询
	@Override
	public JSONObject getByPage(int page, int rows, Date startDate, Date endDate, String departmentTypeSn) {
		return superviseDailyReportDao.getByPage(page, rows, startDate, endDate, departmentTypeSn);
	}
	@Override
	@Scheduled(cron="0 30 0 * * ?") 
	public void upload() {
		try {
			superviseDailyReportDao.upload();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public InputStream online(Date reportDate, String departmentTypeSn, String type) {
		return superviseDailyReportDao.online(reportDate, departmentTypeSn, type);
	}
}
