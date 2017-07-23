package cn.jagl.aq.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.HazardReportedDao;
import cn.jagl.aq.domain.HazardReported;
import cn.jagl.aq.service.HazardReportedService;

@Service("hazardReportedService")
public class HazardReportedServiceImpl implements HazardReportedService {

	@Resource(name="hazardReportedDao")
	private HazardReportedDao hazardReportedDao;
	
	@Override
	public Map<String, Object> queryAllData(String personId,HashMap<String, String> roles,int page,int rows){
		return hazardReportedDao.queryAllData(personId, roles,page,rows);
	}
	@Override
	public void deleteData(int id) {
		hazardReportedDao.deleteData(id);
	}
	@Override
	public void addData(HazardReported hazardReported) {
		hazardReportedDao.save(hazardReported);
	}
	@Override
	public Map<String, Object> queryUnsafeCondition(String departmentSn,String hazardSn,int page,int rows) {
		return hazardReportedDao.queryUnsafeCondition(departmentSn,hazardSn,page,rows);
	}
	@Override
	public Map<String, Object> queryCount(String departmentSn, String departmentTypeSn, int page, int rows) {
		return hazardReportedDao.queryCount(departmentSn, departmentTypeSn,page,rows);
	}
	@Override
	public HazardReported getById(int id) {
		return hazardReportedDao.getById(id);
	}
	@Override
	public void updateData(HazardReported hazardReported) {
		hazardReportedDao.update(hazardReported);
	}
	@Override
	public Map<String, Object> showStandardIndex(String reportSn, int page, int rows) {
		// TODO Auto-generated method stub
		return hazardReportedDao.showStandardIndex(reportSn, page, rows);
	}
}
