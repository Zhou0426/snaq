package cn.jagl.aq.service;

import java.io.InputStream;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface InconformityItemService {
	//区队考核查询贯标单位下的所有类型
	public List<Object> queryAllDepartmentType(String departmentSn);
	//区队考核查询个数
	public JSONArray query(String departmentSn, String departmentTypeSn, String year, String month);
	//区队考核查询隐患详情
	public JSONObject queryUnsafeCondition(String departmentSn,String year,String month,String type,String value,int page,int rows);
	//区队考核查询不安全行为详情
	public JSONObject queryUnsafeAct(String departmentSn,String year,String month,String value,int page,int rows);

	//区队考核查询隐患导出
	public InputStream exportUnsafeCondition(String departmentSn,String year,String month,String type,String value);

	//区队考核查询不安全行为导出
	public InputStream exportUnsafeAct(String departmentSn,String year,String month,String value);
}
