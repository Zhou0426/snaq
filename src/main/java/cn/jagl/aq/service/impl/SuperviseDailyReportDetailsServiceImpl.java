package cn.jagl.aq.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.DepartmentDao;
import cn.jagl.aq.dao.SuperviseDailyReportDao;
import cn.jagl.aq.dao.SuperviseDailyReportDetailsDao;
import cn.jagl.aq.domain.SuperviseDailyReportDetails;
import cn.jagl.aq.service.SuperviseDailyReportDetailsService;
@Service("superviseDailyReportDetailsService")
public class SuperviseDailyReportDetailsServiceImpl implements SuperviseDailyReportDetailsService {
	@Resource(name="superviseDailyReportDao")
	private SuperviseDailyReportDao superviseDailyReportDao;
	@Resource(name="superviseDailyReportDetailsDao")
	private SuperviseDailyReportDetailsDao superviseDailyReportDetailsDao;
	@Resource(name="departmentDao")
	private DepartmentDao departmentDao;
	

	@Override
	public void updateSuperviseDailyReportDetails(SuperviseDailyReportDetails superviseDailyReportDetails) {
		superviseDailyReportDetailsDao.update(superviseDailyReportDetails);
	}
	@Override
	public SuperviseDailyReportDetails getByMany(int id, String departmentSn,
			String superviseItemSn) {
		return superviseDailyReportDetailsDao.getByMany(id, departmentSn, superviseItemSn);
	}

	@Override
	public void save(SuperviseDailyReportDetails superviseDailyReportDetails) {
		superviseDailyReportDetailsDao.save(superviseDailyReportDetails);
	}
	



}
