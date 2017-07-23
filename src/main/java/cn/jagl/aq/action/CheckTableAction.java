package cn.jagl.aq.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import cn.jagl.aq.domain.CheckTable;
import cn.jagl.aq.domain.CheckTableChecker;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.StandardIndex;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class CheckTableAction extends BaseAction<CheckTable> {
	private static final long serialVersionUID = 1L;
	private String specialCheckSn;
	private String periodicalCheckSn;
	private int id;
	private String personIds;
	private Map<String,Object> map;
	private String indexSn;
	private String standardSn;
	private int checkTableId;
	private String str;
	private String personId;
	private String checkTableSn;
	private JSONArray jsonArray=new JSONArray();
	private JSONObject jsonObject=new JSONObject();
	private String auditSn;
	
	
	//检查表确认
	public String confirm(){
		map=new HashMap<String,Object>();
		map.put("status","ok");
		try{
			LocalDateTime today = LocalDateTime.now();
			
			CheckTableChecker checkTableChecker=checkTableCheckerService.getById(checkTableSn,personId);
			checkTableChecker.setHasCompletedCheck(true);
			checkTableChecker.setAffirmDateTime(today);
			checkTableCheckerService.update(checkTableChecker);
		}catch(Exception e){
			map.put("status","nook");
		}		
		return SUCCESS;
	}
	//检查表
	public String queryByPersonId(){
		jsonObject=checkTableService.query(personId, page, rows);
		return "jsonObject";
	}
	
	//查看检查表
	public String query(){
		jsonObject=checkTableService.queryDetails(id, periodicalCheckSn, specialCheckSn, auditSn, page, rows);
		return "jsonObject";
	}
	//生成检查表
	public String addOrRemove(){
		map=new HashMap<String,Object>();
		map.put("status","ok");
		CheckTable checkTable=checkTableService.getById(id);
		String state=str.split(",")[1];
		String isCheck=str.split(",")[2];
		List<StandardIndex> jsonLoad=null;
		if(isCheck.equals("check")){
			try{
				if(state.equals("open")){
					StandardIndex standardIndex=standardindexService.getByindexSn(str.split(",")[0]);
					checkTable.getStandardIndexes().add(standardIndex);	
				}else{
					String hql="select s FROM StandardIndex s where s.deleted=false and s.indexSn like '"+str.split(",")[0]+".%' AND s.standard.standardSn like '"+standardSn+"'";
					//String hql="select s FROM StandardIndex s LEFT JOIN s.standard d where s.parent is null and s.deleted=false and d.standardSn like '"+standardSn+"'";
					jsonLoad=standardindexService.getPart(hql);
					for(StandardIndex standardIndex:jsonLoad){
						if(standardIndex.getChildren().size()==0){
							checkTable.getStandardIndexes().add(standardIndex);
						}					
					}
				}
			}catch(Exception e){
				map.put("status","add");
			}
		}else{
			try{
				if(state.equals("open")){
					StandardIndex standardIndex=standardindexService.getByindexSn(str.split(",")[0]);
					checkTable.getStandardIndexes().remove(standardIndex);
				}else{
					String hql="select s FROM StandardIndex s where s.deleted=false and s.indexSn like '"+str.split(",")[0]+".%' AND s.standard.standardSn like '"+standardSn+"'";
					//String hql="select s FROM StandardIndex s LEFT JOIN s.standard d where s.parent is null and s.deleted=false and d.standardSn like '"+standardSn+"'";
					jsonLoad=standardindexService.getPart(hql);
					for(StandardIndex standardIndex:jsonLoad){
						if(standardIndex.getChildren().size()==0){
							checkTable.getStandardIndexes().remove(standardIndex);
						}					
					}
				}
			}catch(Exception e){
				map.put("status","remove");
			}			
		}
		try{
			checkTableService.update(checkTable);
		}catch(Exception e){
			map.put("status","update");
		}
		
		return SUCCESS;
	}
	//编辑检查表
	public void editTable() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out;  
        out = response.getWriter();
        String hql="";
        JSONArray tree=new JSONArray();
		
		List<StandardIndex> list=standardindexService.queryJoinCheckTable(checkTableId,1,5000);
		List<StandardIndex> jsonLoad=null;
		if(indexSn!=null){
			hql="select s FROM StandardIndex s left join s.standard d where s.deleted=false and s.parent.indexSn ='"+indexSn+"' AND d.standardSn ='"+standardSn+"'";
			jsonLoad=standardindexService.getPart(hql);

		}else{
			hql="select s FROM StandardIndex s LEFT JOIN s.standard d where s.parent is null and s.deleted=false and d.standardSn ='"+standardSn+"'";
			jsonLoad=standardindexService.getPart(hql);
		}
		for(StandardIndex standardIndex:jsonLoad){
			JSONObject jo=new JSONObject();
			jo.put("id", standardIndex.getId());
			jo.put("indexSn", standardIndex.getIndexSn());
			if(standardIndex.getIndexName()!=null){
				jo.put("indexName", standardIndex.getIndexName());		
			}else{
				jo.put("indexName", "");		
			}
				
			if(standardIndex.getChildren().size()>0){
				jo.put("state","closed");
				jo.put("haschildren", true);
				String hql2="select count(s) from StandardIndex s where s.deleted=false and s.standard.standardSn ='"+standardSn+"' and s.indexSn like '"+standardIndex.getIndexSn()+".%' and not EXISTS (select d from StandardIndex d where d.deleted=false and d.parent.indexSn=s.indexSn)";
				String hql3="select count(s) from StandardIndex s inner join s.checkTables t WHERE t.id="+checkTableId+" and s.deleted=false and s.indexSn like '"+standardIndex.getIndexSn()+".%' and s.standard.standardSn ='"+standardSn+"'";
				if(standardindexService.count(hql2)==standardindexService.count(hql3)){
					jo.put("checked",true);
				}else{
					jo.put("checked",false);
				}
			}else{
				jo.put("state", "open");
				jo.put("haschildren", false);
				if(list.contains(standardIndex)){
					jo.put("checked",true);
				}else{
					jo.put("checked",false);
				}
			}
			
			tree.add(jo);
		}
		out.print(tree.toString());
        out.flush(); 
        out.close();
	}
	//根据定期、专项查询
	public String queryJoinCheck() throws IOException{
		jsonArray=checkTableService.queryJoinCheck(specialCheckSn, periodicalCheckSn, auditSn);
		return "jsonArray";
	}
	//添加
	public String save(){
		map=new HashMap<String,Object>();
		map.put("status","ok");
		try{
			//检查表
			CheckTable checkTable=new CheckTable();
			if(id>0){
				CheckTable copy=checkTableService.getById(id);
				if(copy!=null){
					checkTable.getStandardIndexes().addAll(copy.getStandardIndexes());
				}
			}
			String checkTableSn=new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
			checkTable.setCheckTableSn(checkTableSn);
			checkTable.setPeriodicalCheck(periodicalCheckService.getByPeriodicalCheckSn(periodicalCheckSn));
			checkTable.setSpecialCheck(specialCheckService.getBySpecialCheckSn(specialCheckSn));
			checkTable.setSystemAudit(systemAuditService.getBySn(auditSn));
			for(int i=0;i<personIds.split(",").length;i++){
				Person person=personService.getByPersonId(personIds.split(",")[i].trim());
				if(person!=null){
					CheckTableChecker checkTableChecker=new CheckTableChecker();
					checkTableChecker.setCheckTable(checkTable);
					checkTableChecker.setChecker(person);
					checkTableChecker.setHasCompletedCheck(false);
					checkTableCheckerService.save(checkTableChecker);
				}
			}
		}catch(Exception e){
			map.put("status","nook");
		}
		return SUCCESS;
	}
	//删除
	public String delete(){
		map=new HashMap<String,Object>();
		map.put("status","ok");
		try{
			checkTableService.deleteById(id);
		}catch(Exception e){
			map.put("status","nook");
		}
		return SUCCESS;
	}
	//修改
	public String update(){
		map=new HashMap<String,Object>();
		map.put("status","ok");
		try{
			CheckTable checkTable=checkTableService.getById(id);
			//传递新的人员集合
			String[] p1=personIds.split(",");
			List<String> new1 = new ArrayList<String>(Arrays.asList(p1));
			List<String> new2= new ArrayList<String>(Arrays.asList(p1));
			//原来的集合
			List<String> old1=new ArrayList<String>();
			List<String> old2=new ArrayList<String>();
			for(CheckTableChecker checkTableChecker:checkTable.getCheckTableCheckers()){
				Person person=checkTableChecker.getChecker();
				if(person!=null){
					old1.add(person.getPersonId());
					old2.add(person.getPersonId());
				}
			}
			//新增的人员
			new1.removeAll(old2);
			//删减的人员
			old1.removeAll(new2);
			//进行人员删减
			for(int i=0;i<old1.size();i++){
				checkTableCheckerService.deleteByMany(old1.get(i).trim(), checkTable.getCheckTableSn());
			}
			//进行人员增加
			for(int i=0;i<new1.size();i++){
				Person person=personService.getByPersonId(new1.get(i).trim());
				if(person!=null){
					CheckTableChecker checkTableChecker=new CheckTableChecker();
					checkTableChecker.setCheckTable(checkTable);
					checkTableChecker.setChecker(person);
					checkTableChecker.setHasCompletedCheck(false);
					checkTableCheckerService.save(checkTableChecker);
				}
			}
		}catch(Exception e){
			map.put("status","nook");
		}
		return SUCCESS;

	}
	public String getPersonIds() {
		return personIds;
	}

	public void setPersonIds(String personIds) {
		this.personIds = personIds;
	}

	public String getSpecialCheckSn() {
		return specialCheckSn;
	}

	public void setSpecialCheckSn(String specialCheckSn) {
		this.specialCheckSn = specialCheckSn;
	}

	public String getPeriodicalCheckSn() {
		return periodicalCheckSn;
	}

	public void setPeriodicalCheckSn(String periodicalCheckSn) {
		this.periodicalCheckSn = periodicalCheckSn;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public Map<String, Object> getMap() {
		return map;
	}
	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	public String getStandardSn() {
		return standardSn;
	}
	public void setStandardSn(String standardSn) {
		this.standardSn = standardSn;
	}
	public String getIndexSn() {
		return indexSn;
	}
	public void setIndexSn(String indexSn) {
		this.indexSn = indexSn;
	}
	public int getCheckTableId() {
		return checkTableId;
	}
	public void setCheckTableId(int checkTableId) {
		this.checkTableId = checkTableId;
	}
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getCheckTableSn() {
		return checkTableSn;
	}
	public void setCheckTableSn(String checkTableSn) {
		this.checkTableSn = checkTableSn;
	}
	public JSONArray getJsonArray() {
		return jsonArray;
	}
	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}
	public String getAuditSn() {
		return auditSn;
	}
	public void setAuditSn(String auditSn) {
		this.auditSn = auditSn;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}
	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}
}
