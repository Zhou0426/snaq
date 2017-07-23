package cn.jagl.aq.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.jagl.aq.domain.CheckTaskAppraisals;
import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.PersonRecord;
import cn.jagl.aq.domain.UnsafeCondition;

public class CheckTaskAppraisalsAction extends BaseAction<CheckTaskAppraisals> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String departmentSn;//部门编号
	private String yearTime;//年份
	private String monthTime;//月份
	private String checkerId;//检查人编号
	private Integer checkTaskCount;//隐患检查任务个数
	private Integer unsafeActCheckTaskCount;//不安全行为检查任务个数
	private String times;//处室考核页面传来的时间
	private boolean checked;//是否选择下级部门
	private String pag;//返回参数
	private String searchContext;//按照人员搜索检查任务
	private long total;//总数
	private Map<String, Float> jsonMap=new HashMap<String, Float>();
	
	

	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public Map<String, Float> getJsonMap() {
		return jsonMap;
	}
	public void setJsonMap(Map<String, Float> jsonMap) {
		this.jsonMap = jsonMap;
	}
	public String getYearTime() {
		return yearTime;
	}
	public void setYearTime(String yearTime) {
		this.yearTime = yearTime;
	}
	public String getMonthTime() {
		return monthTime;
	}
	public void setMonthTime(String monthTime) {
		this.monthTime = monthTime;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public String getTimes() {
		return times;
	}
	public void setTimes(String times) {
		this.times = times;
	}
	public String getPag() {
		return pag;
	}
	public void setPag(String pag) {
		this.pag = pag;
	}
	public String getDepartmentSn() {
		return departmentSn;
	}
	public void setDepartmentSn(String departmentSn) {
		this.departmentSn = departmentSn;
	}
	public String getCheckerId() {
		return checkerId;
	}
	public void setCheckerId(String checkerId) {
		this.checkerId = checkerId;
	}
	public Integer getCheckTaskCount() {
		return checkTaskCount;
	}
	public void setCheckTaskCount(Integer checkTaskCount) {
		this.checkTaskCount = checkTaskCount;
	}
	//待办事项中检查任务页面显示检查人的检查任务
	public String checkTaskGetByCheckerId(){
		Person person=personService.getByPersonId(checkerId);
		List<PersonRecord> personRecords=personRecordService.getMonthByPersonId(checkerId);
		CheckTaskAppraisals checkTaskAppraisals=checkTaskAppraisalsService.getBycheckerSn(person.getPersonId(), person.getDepartment().getDepartmentSn(), LocalDate.now().withDayOfMonth(1).toString());
		String unsafeConditionCount = "";
		String unsafeActCount = "";
		//检查任务
		if(checkTaskAppraisals!=null){
			if(checkTaskAppraisals.getCheckTaskCount()!=null && !"".equals(checkTaskAppraisals.getCheckTaskCount().toString())){
				unsafeConditionCount = String.valueOf(checkTaskAppraisals.getCheckTaskCount());
				unsafeActCount = String.valueOf(checkTaskAppraisals.getUnsafeActCheckTaskCount());
			}else{
				unsafeConditionCount = "0";
				unsafeActCount = "0";
			}
		}else{
			unsafeConditionCount = "0";
			unsafeActCount = "0";
		}
		jsonMap.put("本月隐患任务数量", Float.valueOf(unsafeConditionCount));
		jsonMap.put("不安全行为任务数量", Float.valueOf(unsafeActCount));
		//真实检查量
		for(PersonRecord personRecord:personRecords){
			float num=0;
			String ksTime="";
			if(personRecord.getStartDateTime().toLocalDate().isBefore(LocalDate.now().withDayOfMonth(1))){
				ksTime=LocalDate.now().withDayOfMonth(1).toString();
			}else{
				ksTime=personRecord.getStartDateTime().toLocalDate().toString();
			}
			String hql="select distinct u from InconformityItem u LEFT JOIN u.checkers c where u.deleted=false"
					+ " AND u.checkDateTime between '" + ksTime + " 00:00:00'"
					+ " and '" + LocalDate.now().withDayOfMonth(LocalDate.now().getDayOfMonth()).toString() + " 23:59:59'"
					+ " AND c.personId = '" + checkerId + "'";
			List<UnsafeCondition> list=unsafeConditionService.query(hql, 1, 10000);
			for(UnsafeCondition unsafeCondition:list){
				if(unsafeCondition.getCheckers().size()>1){
					num += ((float)1/(float)unsafeCondition.getCheckers().size());
				}else{
					num += 1;
				}
			}
			if(jsonMap.containsKey(personRecord.getDepartment().getDepartmentName())){
				jsonMap.put(personRecord.getDepartment().getDepartmentName(), jsonMap.get(personRecord.getDepartment().getDepartmentName()) + num);
			}else{
				jsonMap.put(personRecord.getDepartment().getDepartmentName(), num);
			}
		}
		return LOGIN;
	}
	//加载部门类型为集团处室的部门
	public String loadDepartment() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out=response.getWriter();
        List<Department> list=departmentService.getDepartments("JTCS");
        JSONArray jsonArray=new JSONArray();
        for(Department de:list){
        	JSONObject jo=new JSONObject();
        	jo.put("id", de.getDepartmentSn());
        	jo.put("text", de.getDepartmentName());
        	jsonArray.put(jo);
        }
        out.print(jsonArray.toString());
        out.flush(); 
        out.close();  
		return SUCCESS;
	}
	//加载任务分配表数据
	public String loadData() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out = response.getWriter();
        JSONArray jsonArray = new JSONArray();
        jsonList = new ArrayList<CheckTaskAppraisals>();
        
        StringBuffer hql = new StringBuffer();
        if(checked){
        	hql.append("SELECT i FROM CheckTaskAppraisals i"
        			+ " WHERE i.department.departmentSn LIKE '" + departmentSn + "%'");
        }else{
        	hql.append("SELECT i FROM CheckTaskAppraisals i"
        			+ " WHERE i.department.departmentSn = '" + departmentSn + "'");
        			//+ " and year(yearMonth) = '" + yearTime + "' and month(yearMonth) = '" + monthTime + "' and i.checker is not null order by i.department asc");
        }//select p from Person p where (p.personId like '%"+q+"%' or p.personName like '%"+q+"%')
        if( searchContext != null && !"".equals(searchContext) ){
        	hql.append(" AND i.checker.personId IN (SELECT p.personId FROM Person p"
        			+ " WHERE p.personId LIKE '%"+searchContext+"%'"
        			+ " OR p.personName LIKE '%"+searchContext+"%')");
        }else{
        	hql.append(" AND i.checker IS NOT NULL");
        }
        hql.append(" AND YEAR(yearMonth) = '" + yearTime + "'"
        		+ " AND MONTH(yearMonth) = '" + monthTime + "'"
        		+ " AND i.checker IS NOT NULL ORDER BY i.department ASC");
		jsonList = checkTaskAppraisalsService.findByPage(hql.toString(), page, rows);
		hql = hql.replace(7, 8 , "count(*)");
		total = checkTaskAppraisalsService.countHql(hql.toString());
		for(CheckTaskAppraisals check:jsonList){
			JSONObject jo=new JSONObject();
			jo.put("id", check.getId());
			if(check.getDepartment()!=null){
				jo.put("departmentSn", check.getDepartment().getDepartmentSn());
				jo.put("departmentName", check.getDepartment().getDepartmentName());
				jo.put("implDepartmentName", check.getDepartment().getImplDepartmentName());
			}
			if(check.getChecker()!=null){
				jo.put("personId", check.getChecker().getPersonId());
				jo.put("personName", check.getChecker().getPersonName());
			}
			if(check.getCheckTaskCount() != null){				
				jo.put("checkTaskCount", check.getCheckTaskCount());
			}else{
				jo.put("checkTaskCount", 0);
			}
			if(check.getUnsafeActCheckTaskCount() != null){				
				jo.put("unsafeActCheckTaskCount", check.getUnsafeActCheckTaskCount());
			}else{
				jo.put("unsafeActCheckTaskCount", 0);
			}
			if(check.getRealCheckCount() != null){
				jo.put("realCheckCount", check.getRealCheckCount());
			}else{
				jo.put("realCheckCount", 0);
			}
			if(check.getUnsafeActRealCheckCount() != null){
				jo.put("unsafeActRealCheckCount", check.getUnsafeActRealCheckCount());
			}else{
				jo.put("unsafeActRealCheckCount", 0);
			}
			jo.put("yearMonth", check.getYearMonth());
			jsonArray.put(jo);
		}
		Map<String, Object> json = new HashMap<String, Object>();
        json.put("total", total);// total键 存放总记录数，必须的
        json.put("rows", jsonArray);// rows键 存放每页记录 list
		out.print(JSONObject.valueToString(json));
        out.flush(); 
        out.close();  
		return SUCCESS;
	}
	//页面更新任务数量的保存
	public String updateData(){
		try{
			CheckTaskAppraisals checkTaskAppraisals=checkTaskAppraisalsService.getBycheckerSn(checkerId, departmentSn,times);
			checkTaskAppraisals.setUnsafeActCheckTaskCount(unsafeActCheckTaskCount);
			checkTaskAppraisals.setCheckTaskCount(checkTaskCount);
			checkTaskAppraisalsService.update(checkTaskAppraisals);
			pag=SUCCESS;
		}catch(Exception e){
			pag=ERROR;
		}
		return SUCCESS;
	}
	//添加数据
	public String addData(){
        String hql="select c from CheckTaskAppraisals c where c.department.departmentSn='"+departmentSn+"' and year(yearMonth)='"+yearTime+"' and month(yearMonth)='"+monthTime+"' and c.checker.personId='"+checkerId+"'";
		jsonList=checkTaskAppraisalsService.getByHql(hql);
		if(jsonList.size()>0){
			pag=LOGIN;
			return SUCCESS;
		}
		try{
			CheckTaskAppraisals checkTaskAppraisals=new CheckTaskAppraisals();
			Person person=personService.getByPersonId(checkerId);
			checkTaskAppraisals.setChecker(person);
			checkTaskAppraisals.setDepartment(person.getDepartment());
			checkTaskAppraisals.setUnsafeActCheckTaskCount(unsafeActCheckTaskCount);
			checkTaskAppraisals.setCheckTaskCount(checkTaskCount);
			checkTaskAppraisals.setRealCheckCount(0f);
			checkTaskAppraisals.setNeedComputing(false);
			if(Integer.parseInt(monthTime)<10){
				monthTime="0"+monthTime;
			}
			LocalDate yearMonth = LocalDate.parse(yearTime+"-"+monthTime+"-01");
			checkTaskAppraisals.setYearMonth(yearMonth);
			checkTaskAppraisalsService.add(checkTaskAppraisals);
			pag=SUCCESS;
		}catch(Exception e){
			pag=ERROR;
		}
        return SUCCESS;		
	}
	//添加数据
	public String deleteData(){
		try{
			checkTaskAppraisalsService.delete(id);
			pag = SUCCESS;
		}catch(Exception e){
			pag = ERROR;
		}
        return SUCCESS;		
	}
	//加载考核数据
	public String loadOfficeData() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out = response.getWriter();
        net.sf.json.JSONArray jsonArray = new net.sf.json.JSONArray();
		List<Department> list = new ArrayList<Department>();
		list = departmentService.getDepartments("JTCS");
        total = list.size();
        for(Department de:list){
        	net.sf.json.JSONObject jo = new net.sf.json.JSONObject();
        	jo = this.BydepartmentSn(de);
			jsonArray.add(jo);
        }
//        //父级部门
//        Department department = departmentService.getByDepartmentSn(departmentSn);
//        net.sf.json.JSONArray ja = new net.sf.json.JSONArray();
//        net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
//        jsonObject = this.BydepartmentSn(department,department.getDepartmentName());
//        ja.add(jsonObject);
        
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("total", total);// total键 存放总记录数，必须的
        json.put("rows", jsonArray);// rows键 存放每页记录 list
//        json.put("footer", ja);// rows键 存放每页记录 list
		out.print(JSONObject.valueToString(json));
        out.flush(); 
        out.close();  
		return SUCCESS;
	}
	/**
	 * 根据部门编号获取该部门的检查任务详情
	 * @param de
	 * @return
	 */
	private net.sf.json.JSONObject BydepartmentSn(Department de){
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
        String ksDate = "";
		String jsDate = "";
		net.sf.json.JSONObject jo = new net.sf.json.JSONObject();
    	jo.put("departmentSn", de.getDepartmentSn());
    	jo.put("departmentName", de.getDepartmentName());
    	List<CheckTaskAppraisals> list2 = checkTaskAppraisalsService.getByYear(times, de.getDepartmentSn());
    	
    	for(CheckTaskAppraisals ch:list2){
    		if(ch.getRealCheckCount() == null){
    			jo.put(String.valueOf(ch.getYearMonth().getMonthValue()), "0.00");
    		}else if(ch.getCheckTaskCount() == null || ch.getCheckTaskCount() == 0){
    			jo.put(String.valueOf(ch.getYearMonth().getMonthValue()), "0.00");
    		}else if(ch.getRealCheckCount() >= ch.getCheckTaskCount()){
    			jo.put(String.valueOf(ch.getYearMonth().getMonthValue()), "100.00");
    		}else{
    			jo.put(String.valueOf(ch.getYearMonth().getMonthValue()), df.format(ch.getRealCheckCount()*100/ch.getCheckTaskCount()));
    		}
    	}
    	
//    	SystemAudit systemAudit;
    	//第一季度
    	float season1 = 0f;
    	for(int i = 1; i < 4; i++){
    		if(jo.get(String.valueOf(i)) != null){
    			season1 += jo.getDouble(String.valueOf(i));
    		}else{
    			jo.put(String.valueOf(i), "0.00");
    		}
    	}
    	season1 = season1*0.2f;
    	ksDate = times + "-01-01";
    	jsDate = times + "-03-31";
    	season1 = (float)(season1 + (100 - checkTaskAppraisalsService.countStandardIndexScore(de.getDepartmentSn(), ksDate, jsDate)) * 0.4);
    	//第一季度审核
//    	systemAudit=systemAuditService.queryScore(de.getDepartmentSn(), 1,times);
//		if(systemAudit!=null && systemAudit.getFinalScore()!=null){
//			float quarter=systemAudit.getFinalScore();
//			season1+=quarter*0.4;
//		}
		
		//第二季度
    	float season2 = 0f;
    	for(int i = 4; i < 7; i++){
    		if(jo.get(String.valueOf(i)) != null){
    			season2 += jo.getDouble(String.valueOf(i));
    		}else{
    			jo.put(String.valueOf(i), "0.00");
    		}
    	}
    	season2 = season2 * 0.2f;
    	ksDate = times + "-04-01";
		jsDate = times + "-06-30";
		season2 = (float)(season2 + (100 - checkTaskAppraisalsService.countStandardIndexScore(de.getDepartmentSn(), ksDate, jsDate)) * 0.4);
    	//第二季度审核
//    	systemAudit=systemAuditService.queryScore(de.getDepartmentSn(), 2,times);
//		if(systemAudit!=null && systemAudit.getFinalScore()!=null){
//			float quarter=systemAudit.getFinalScore();
//			season2+=quarter*0.4;
//		}
		
		//第三季度
    	float season3 = 0f;
    	for(int i = 7; i < 10; i++){
    		if(jo.get(String.valueOf(i)) != null){
    			season3 += jo.getDouble(String.valueOf(i));
    		}else{
    			jo.put(String.valueOf(i), "0.00");
    		}
    	}
    	season3 = season3 * 0.2f;
    	ksDate = times + "-07-01";
		jsDate = times + "-09-30";
		season3 = (float)(season3 + (100 - checkTaskAppraisalsService.countStandardIndexScore(de.getDepartmentSn(), ksDate, jsDate)) * 0.4);
    	//第三季度审核
//    	systemAudit=systemAuditService.queryScore(de.getDepartmentSn(), 3,times);
//		if(systemAudit!=null && systemAudit.getFinalScore()!=null){
//			float quarter=systemAudit.getFinalScore();
//			season3+=quarter*0.4;
//		}
		
		//第四季度
    	float season4 = 0f;
    	for(int i = 10; i < 13; i++){
    		if(jo.get(String.valueOf(i)) != null){
    			season4 += jo.getDouble(String.valueOf(i));
    		}else{
    			jo.put(String.valueOf(i), "0.00");
    		}
    	}
    	season4 = season4 * 0.2f;
    	ksDate = times + "-10-01";
		jsDate = times + "-12-31";
		season4 = (float)(season4 + (100 - checkTaskAppraisalsService.countStandardIndexScore(de.getDepartmentSn(), ksDate, jsDate)) * 0.4);
    	//第四季度审核
//    	systemAudit=systemAuditService.queryScore(de.getDepartmentSn(), 4,times);
//		if(systemAudit!=null && systemAudit.getFinalScore()!=null){
//			float quarter=systemAudit.getFinalScore();
//			season4+=quarter*0.4;
//		}
		
//		//下半年度
//    	float half2=0f;
//    	for(int i=7;i<13;i++){
//    		if(jo.get(String.valueOf(i))!=null){
//    			half2+=jo.getDouble(String.valueOf(i));
//    		}else{
//    			jo.put(String.valueOf(i), "0.00");
//    		}
//    	}
//    	half2=half2*0.2f;
//    	//第四季度审核
//    	systemAudit=systemAuditService.queryScore(de.getDepartmentSn(), 4,times);
//		if(systemAudit!=null && systemAudit.getFinalScore()!=null){
//			float quarter=systemAudit.getFinalScore();
//			half2+=quarter*0.4;
//		}
		
		//年度
		float year = 0f;
		int j = 0;
		//第一季度
		if(season1 > 0){
			j++;
			year += season1;
			jo.put("season1", df.format(season1));
		}else{
			jo.put("season1", "0.00");
		}
		//第二季度
		if(season2 > 0){
			j++;
			year += season2;
			jo.put("season2", df.format(season2));
		}else{
			jo.put("season2", "0.00");
		}
		//第三季度
		if(season3 > 0){
			j++;
			year += season3;
			jo.put("season3", df.format(season3));
		}else{
			jo.put("season3", "0.00");
		}
		//第四季度
		if(season4 > 0){
			j++;
			year += season4;
			jo.put("season4", df.format(season4));
		}else{
			jo.put("season4", "0.00");
		}
		//年度
		if(year > 0){
			year = year / j;
			jo.put("year", df.format(year));
		}else{
			jo.put("year", "0.00");
		}
		jo.put("rank", "");
		return jo;
	}
	//加载每个部门每个月份的详情数据
	public String loadDetailData() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out=response.getWriter();
        JSONArray jsonArray=new JSONArray();
        java.text.DecimalFormat df =new java.text.DecimalFormat("0.00");
        String hql="select i from CheckTaskAppraisals i where i.department.departmentSn='"+departmentSn+"' and year(yearMonth)='"+yearTime+"' and month(yearMonth)='"+monthTime+"' and i.checker is not null";
        jsonList=checkTaskAppraisalsService.findByPage(hql, page, rows);
        hql=hql.replaceFirst("i", "count(*)");
        total=checkTaskAppraisalsService.countHql(hql);
        for(CheckTaskAppraisals ch:jsonList){
        	JSONObject jo=new JSONObject();
        	if(ch.getChecker()!=null){
        		jo.put("personId", ch.getChecker().getPersonId());
        		jo.put("personName", ch.getChecker().getPersonName());
        	}
        	if(ch.getCheckTaskCount()==null){
        		jo.put("checkTaskCount", 0);
        	}else{
        		jo.put("checkTaskCount", ch.getCheckTaskCount());
        	}
        	if(ch.getRealCheckCount()==null){
        		jo.put("realCheckCount", 0);
        	}else{
        		jo.put("realCheckCount", ch.getRealCheckCount());
        	}
        	if(ch.getRealCheckCount()==null){
    			jo.put("rank", "0.00");
    		}else if(ch.getCheckTaskCount()==null || ch.getCheckTaskCount()==0){
    			jo.put("rank", "100.00");
    		}else if(ch.getRealCheckCount()>=ch.getCheckTaskCount()){
    			jo.put("rank", "100.00");
    		}else{
    			jo.put("rank", df.format(ch.getRealCheckCount()*100/ch.getCheckTaskCount()));
    		}
        	jsonArray.put(jo);
        }
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("total", total);// total键 存放总记录数，必须的
        json.put("rows", jsonArray);// rows键 存放每页记录 list
		out.print(JSONObject.valueToString(json));
        out.flush(); 
        out.close();  
		return SUCCESS;
	}
	public String showPersonDetail(){
		
		return SUCCESS;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSearchContext() {
		return searchContext;
	}
	public void setSearchContext(String searchContext) {
		this.searchContext = searchContext;
	}
	public Integer getUnsafeActCheckTaskCount() {
		return unsafeActCheckTaskCount;
	}
	public void setUnsafeActCheckTaskCount(Integer unsafeActCheckTaskCount) {
		this.unsafeActCheckTaskCount = unsafeActCheckTaskCount;
	}
}
