package cn.jagl.aq.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.PersonUseStat;
import cn.jagl.aq.domain.SessionInfo;

public class PersonUseStatAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	//全局变量
	private Integer page;
	private Integer rows;//分页条件
	private String personId;
	private int year;
	private int month;
	private String order;
	private String sort;
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
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
		String hql="from PersonUseStat p where p.statYear="+year+" and p.statMonth="+month+" and p.person.department.departmentSn like '"+departmentSn+"%'";
		String hqlcount="select count(p) from PersonUseStat p where p.statYear="+year+" and p.statMonth="+month+" and p.person.department.departmentSn like '"+departmentSn+"%'";
    	if(sort!=null && order!=null){
    		String h="";
			h=" order by p."+sort+" "+ order;
    		hql=hql+h;
    		hqlcount=hqlcount+h;
    	}
		List<PersonUseStat> personUseStats=personUseStatService.findByPage(hql, page, rows);
    	long total=personUseStatService.countHql(hqlcount);
    	for(PersonUseStat d:personUseStats){
			 JSONObject jo=new JSONObject();
			 jo.put("id",d.getId());
			 jo.put("loginCount", d.getLoginCount());
			 jo.put("month", d.getStatMonth());
			 jo.put("year", d.getStatYear());
			 jo.put("sumOnlineTime", d.getSumOnlineTime());
			 jo.put("avgLoginCountPerDay", d.getAvgLoginCountPerDay());
			 jo.put("avgOnlineTimePerDay", d.getAvgOnlineTimePerDay());
			 jo.put("avgOnlineTimePerLogin", d.getAvgOnlineTimePerLogin());
			 jo.put("statDateTime", d.getStatDateTime());
			 if(d.getPerson()!=null){
				 jo.put("person", d.getPerson().getPersonName());
				 jo.put("personId", d.getPerson().getPersonId());
				 jo.put("impDepartmentName", d.getPerson().getDepartment().getImplDepartmentName());
			 }
			 array.put(jo);
		}
		String str="{\"total\":"+total+",\"rows\":"+array+"}";
		out().print(str);
		out().flush(); 
		out().close();
		return SUCCESS;
	}
	//登陆详情
	public String detail() throws IOException{
		//当年当月此人的所有session记录();
		JSONArray array=new JSONArray();
		Date date=new Date();	
		SimpleDateFormat end = new SimpleDateFormat("yyyy-MM-31 23:59:59");	
		String endDateTime=end.format(date);
		String hql="from SessionInfo s where s.operator.personId= '"+personId+"' and s.lastOperationDateTime<'"+endDateTime+"'";
    	List<SessionInfo> sessionInfos=sessionInfoService.findByPage(hql, page, rows);
    	String hqlcount="select count(s) from SessionInfo s where s.operator.personId= '"+personId+"' and s.lastOperationDateTime<'"+endDateTime+"'";
    	long total=sessionInfoService.countHql(hqlcount);
    	for(SessionInfo d:sessionInfos){
			 JSONObject jo=new JSONObject();
			 jo.put("id", d.getId());
			 jo.put("internalIp", d.getInternalIp());
			 jo.put("internetIp", d.getInternetIp());
			 jo.put("jsessionId", d.getJsessionId());
			 jo.put("lastOperationDateTime", d.getLastOperationDateTime());
			 jo.put("loginDateTime", d.getLoginDateTime());
			 if(d.getOperator()!=null){
				 jo.put("operator", d.getOperator().getPersonName());
			 }
			 array.put(jo);
		}
		String str="{\"total\":"+total+",\"rows\":"+array+"}";
		out().print(str);
		out().flush(); 
		out().close();
		return SUCCESS;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	//年份
	public String year() throws IOException{
		JSONArray array=new JSONArray();
    	List<Integer> years=sessionInfoService.findYear();
		for(int i=0;i<years.size();i++){
			 JSONObject jo=new JSONObject();
			 jo.put("year", years.get(i));
			 array.put(jo);
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
    	List<Integer> months=sessionInfoService.findMonth(year);
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
	public String getDepartmentSn() {
		return departmentSn;
	}
	public void setDepartmentSn(String departmentSn) {
		this.departmentSn = departmentSn;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
}
