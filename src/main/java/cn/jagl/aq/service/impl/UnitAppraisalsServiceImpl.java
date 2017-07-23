package cn.jagl.aq.service.impl;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.UnitAppraisalsDao;
import cn.jagl.aq.domain.UnitAppraisals;
import cn.jagl.aq.service.UnitAppraisalsService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * @author mahui
 * @method 
 * @date 2016年8月17日下午6:14:48
 */
@Service("unitAppraisalsService")
public class UnitAppraisalsServiceImpl implements UnitAppraisalsService {
	@Resource(name="unitAppraisalsDao")
	private UnitAppraisalsDao unitAppraisalsDao;
	@Scheduled(cron="0 30 2 * * ?")   //每天零点执行
	public void score() {
		unitAppraisalsDao.score();
	}
	@Override
	public void update(UnitAppraisals unitAppraisals) {
		unitAppraisalsDao.save(unitAppraisals);
	}
	@Override
	public void save(UnitAppraisals unitAppraisals) {
		unitAppraisalsDao.update(unitAppraisals);
	}
	//根据部门编号和日期查询实体
	@Override
	public UnitAppraisals queryByMany(String departmentSn, LocalDate localDate,Byte type) {
		return unitAppraisalsDao.queryByMany(departmentSn, localDate,type);
	}
	//根据部门编号，年份份获取该部门一年的所有月份平均分
	@Override
	public JSONArray queryScore(String year, String departmentTypeSn,Byte type){
		return unitAppraisalsDao.queryScore(year, departmentTypeSn,type);
	}
	//根据年份和月份和部门编号查询该部门该月的得分
	@Override
	public List<Object> queryMonth(String year, String month, String departmentSn,Byte type) {
		return unitAppraisalsDao.queryMonth(year, month, departmentSn,type);
	}
	//查看扣分详情
	@Override
	public JSONArray scoreDetail(String departmentSn, LocalDate localdate,String ksDate,String jsDate,Byte type) {
		return unitAppraisalsDao.scoreDetail(departmentSn, localdate,ksDate,jsDate,type);
	}
	@Override
	public List<UnitAppraisals> queryByDepartmentDate(String departmentSn, String ksDate, String jsDate) {
		// TODO Auto-generated method stub
		return unitAppraisalsDao.queryByDepartmentDate(departmentSn,ksDate, jsDate);
	}
	
	//查询某一天扣分详情
	@Override
	public JSONObject socoreByDay(String departmentSn, LocalDate local,Byte type) {
		return unitAppraisalsDao.socoreByDay(departmentSn, local,type);
	}
	
	//导出部门类型下的某月考核打分说明
	@Override
	public InputStream exportMonthByDepartmentTypeSn(String departmentTypeSn, String departmentTypeName,
			String year,String month,Byte type) {
		return unitAppraisalsDao.exportMonthByDepartmentTypeSn(departmentTypeSn, departmentTypeName, year, month,type);
	}
	
	//导出某部门下的某月考核打分说明
	@Override
	public InputStream exportMonthByDepartmentSn(String departmentSn, String departmentName, String year,
			String month,Byte type) {
		return unitAppraisalsDao.exportMonthByDepartmentSn(departmentSn, departmentName, year, month,type);
	}
	
	/**
	 * @method 安全考核隐患导出
	 * @author mahui
	 * @return String
	 */
	@Override
	public InputStream exportUnsafeCondition(String departmentSn, LocalDate localdate) {
		// TODO Auto-generated method stub
		return unitAppraisalsDao.exportUnsafeCondition(departmentSn, localdate);
	}
}
