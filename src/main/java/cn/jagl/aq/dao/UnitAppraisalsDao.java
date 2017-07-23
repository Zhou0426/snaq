package cn.jagl.aq.dao;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.UnitAppraisals;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author mahui
 * @method 
 * @date 2016年8月17日下午6:14:57
 */
public interface UnitAppraisalsDao extends BaseDao<UnitAppraisals> {
	//定时计算得分
	public void score();
	//根据部门编号和日期查询实体
	public UnitAppraisals queryByMany(String departmentSn,LocalDate localDate,Byte type);
	//根据部门编号，年份份获取该部门一年的所有月份平均分
	public JSONArray queryScore(String year, String departmentTypeSn,Byte type);
	//根据年份和月份和部门编号查询该部门该月的得分
	public List<Object> queryMonth(String year,String month,String departmentSn,Byte type);
	//查看扣分详情
	public JSONArray scoreDetail(String departmentSn,LocalDate localdate,String ksDate,String jsDate,Byte type);
	//根据部门编号和当月日期查询实体集合--处室考核
	public List<UnitAppraisals> queryByDepartmentDate(String departmentSn, String ksDate, String jsDate);
	
	//查询某一天扣分详情
	public JSONObject socoreByDay(String departmentSn,LocalDate local,Byte type);
	
	//导出部门类型下的某月考核打分说明
	public InputStream exportMonthByDepartmentTypeSn(String departmentTypeSn,String departmentTypeName,String year,String month,Byte type);
	
	//导出某部门下的某月考核打分说明
	public InputStream exportMonthByDepartmentSn(String departmentSn,String departmentName,String year,String month,Byte type);
	
	/**
	 * @method 安全考核隐患导出
	 * @author mahui
	 * @return String
	 */
	InputStream exportUnsafeCondition(String departmentSn,LocalDate localdate);
}
