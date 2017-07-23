package cn.jagl.aq.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.jagl.aq.domain.DepartmentUseStat;
import cn.jagl.aq.domain.PersonUseStat;

public class DepartmentUseStatAction extends BaseAction <DepartmentUseStat>{
	private static final long serialVersionUID = 1L;
	//全局变量
	private Integer page;
	private Integer rows;//分页条件
	private int year;
	private int month;
	private String id;
	private String departmentSn;
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public String getDepartmentSn() {
		return departmentSn;
	}
	public void setDepartmentSn(String departmentSn) {
		this.departmentSn = departmentSn;
	}
	//输出指定字段
	public PrintWriter out()throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out= response.getWriter();
        return out;
	}
	//分页展示页面
	public String show() throws IOException{
		out();
		JSONArray array=new JSONArray();
		departmentSn=id;
		String hql="From DepartmentUseStat d where d.statYear="+year+" and d.statMonth="+month;
		String hqlcount="select count(d) From DepartmentUseStat d where d.statYear="+year+" and d.statMonth="+month;
		if(departmentSn != null && !"".equals(departmentSn)){
			hql = hql + " AND d.department.parentDepartment.departmentSn = '" + departmentSn + "'";
			hqlcount = hqlcount + " AND d.department.parentDepartment.departmentSn = '" + departmentSn + "'";
		}else{
			hql = hql + " AND d.department.parentDepartment is null";		
			hqlcount = hqlcount + " AND d.department.parentDepartment is null";
		}
//		String hql="From DepartmentUseStat d where d.department.parentDepartment.departmentSn = '"+departmentSn+"' and d.statYear="+year+" and d.statMonth="+month;
//		String hqlcount="select count(d) From DepartmentUseStat d where d.department.parentDepartment.departmentSn = '"+departmentSn+"%' and d.statYear="+year+" and d.statMonth="+month;
    	List<DepartmentUseStat> departmentUseStats=departmentUseStatService.findByPage(hql, 1, 10000);
    	//long total=departmentUseStatService.countHql(hqlcount);
    	for(DepartmentUseStat d:departmentUseStats){
			 JSONObject jo=new JSONObject();
			 if(d.getDepartment()!=null){
				 jo.put("id",d.getDepartment().getDepartmentSn());
				 jo.put("loginPersonCount", d.getLoginPersonCount());
				 jo.put("month", d.getStatMonth());
				 jo.put("year", d.getStatYear());
				 jo.put("sumOnlineTime", d.getSumOnlineTime());
				 float avgLoginCount =(float)Math.round(d.getAvgLoginCount()*10000)/10000;
				 jo.put("avgLoginCount", avgLoginCount);
				 float avgLoginRatioPerDay =(float)Math.round(d.getAvgLoginRatioPerDay()*10000)/10000;
				 jo.put("avgLoginRatioPerDay", avgLoginRatioPerDay);
				 jo.put("avgOnlineTimePerCapitalDay", d.getAvgOnlineTimePerCapitaDay());
				 jo.put("departmentName", d.getDepartment().getDepartmentName());
				 jo.put("loginRatio", d.getLoginRatio());
				 jo.put("statDateTime", d.getStatDateTime());
				 if(d.getDepartment().getChildDepartments().size()>0){
					jo.put("state","closed");
				 }
				 else{
					jo.put("state","open");
				 }
			 }
			 array.put(jo);
		}
		//String str="{\"total\":"+total+",\"rows\":"+array+"}";
		out().print(array.toString());
		out().flush(); 
		out().close();
		return SUCCESS;
	}
	//年份
	public String year() throws IOException{
		JSONArray array=new JSONArray();
		//HashMap<String, Integer> h=new HashMap<String, Integer>();
    	List<Integer> years=sessionInfoService.findYearD();
    	for(int i=0;i<years.size();i++){
			 JSONObject jo=new JSONObject();
			 jo.put("year", years.get(i));
			 array.put(jo);
//			 h.put("year", years.get(i));
		}
		String str=array.toString();
		out().print(str);
		out().flush(); 
		out().close();
		return SUCCESS;
	}
	//月

	public String month() throws IOException{
		JSONArray array=new JSONArray();
    	List<Integer> months=sessionInfoService.findMonthD(year);
    	for(int i=0;i<months.size();i++){
			 JSONObject jo=new JSONObject();
			 jo.put("month", months.get(i));
			 array.put(jo);
		}
    	String str=array.toString();
		out().print(str);
		out().flush(); 
		out().close();
		return SUCCESS;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
