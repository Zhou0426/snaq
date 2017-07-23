package cn.jagl.aq.service.impl;

import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.InconformityItemDao;
import cn.jagl.aq.service.InconformityItemService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Service("inconformityItemService")
public class InconformityItemServiceImpl implements InconformityItemService {
	@Resource(name="inconformityItemDao")
	private InconformityItemDao inconformityItemDao;
	//区队考核查询贯标单位下的所有类型
	@Override
	public List<Object> queryAllDepartmentType(String departmentSn) {
		return inconformityItemDao.queryAllDepartmentType(departmentSn);
	}
	//区队考核查询个数
	@Override
	public JSONArray query(String departmentSn, String departmentTypeSn, String year, String month) {
		return inconformityItemDao.query(departmentSn, departmentTypeSn, year, month);
	}
	@Override
	public JSONObject queryUnsafeCondition(String departmentSn, String year, String month, String type, String value,
			int page, int rows) {
		return inconformityItemDao.queryUnsafeCondition(departmentSn, year, month, type, value, page, rows);
	}
	@Override
	public JSONObject queryUnsafeAct(String departmentSn, String year, String month, String value, int page, int rows) {
		return inconformityItemDao.queryUnsafeAct(departmentSn, year, month, value, page, rows);
	}
	
	@Override
	public InputStream exportUnsafeCondition(String departmentSn, String year, String month, String type,
			String value) {
		// TODO Auto-generated method stub
		return inconformityItemDao.exportUnsafeCondition(departmentSn, year, month, type, value);
	}
	@Override
	public InputStream exportUnsafeAct(String departmentSn, String year, String month, String value) {
		// TODO Auto-generated method stub
		return inconformityItemDao.exportUnsafeAct(departmentSn, year, month, value);
	}

}
