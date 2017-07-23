package cn.jagl.aq.action;

import java.time.LocalDateTime;

import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.SystemAuditScore;
import cn.jagl.aq.domain.SystemAuditScoreDetails;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SystemAuditScoreDetailsAction extends BaseAction<SystemAuditScoreDetails> {
	private static final long serialVersionUID = 1L;
	private String auditSn;
	private String indexSn;
	private String departmentSn;
	private JSONObject jsonObject=new JSONObject();
	private JSONArray jsonArray=new JSONArray();
	private boolean b;
	private String departmentName;
	private String parentDepartmentSn;
	private int conformDegree;
	private int id;
	private String isUpdate;
	

	//查询
	public String query(){
		jsonObject=systemAuditScoreDetailsService.query(auditSn, indexSn, departmentSn, page, rows);
		return "jsonObject";
	}
	//查询部门
	public String queryDepartment(){
		jsonArray=systemAuditScoreDetailsService.queryDepartment(departmentSn);
		return "jsonArray";
	}
	//查询是否存在
	public String isExists(){
		if(isUpdate!=null&&isUpdate.trim().length()>0){
			b=systemAuditScoreDetailsService.isExistsUpdate(auditSn, indexSn, departmentName, id);
		}else{
			b=systemAuditScoreDetailsService.isExists(auditSn, indexSn, departmentName);
		}
		return "b";
	}
	//添加
	public String save(){
		jsonObject.put("status", "ok");
		try{
			SystemAuditScoreDetails  systemAuditScoreDetails=new SystemAuditScoreDetails();
			systemAuditScoreDetails.setConformDegree(conformDegree);
			systemAuditScoreDetails.setInputDateTime(LocalDateTime.now());
			systemAuditScoreDetails.setDepartment(departmentService.getByDepartmentSn(departmentSn));
			systemAuditScoreDetails.setStandardIndex(standardindexService.getByindexSn(indexSn));
			systemAuditScoreDetails.setSystemAudit(systemAuditService.getBySn(auditSn));
			if(session.get("personId")!=null){
				systemAuditScoreDetails.setEditor(personService.getByPersonId((String) session.get("personId")));
				systemAuditScoreDetailsService.save(systemAuditScoreDetails);
			}else{
				jsonObject.put("status", "nook");
			}
		}catch(Exception e){
			jsonObject.put("status", "nook");
			return "jsonObject";
		}
		//更新打分
		int c=systemAuditScoreDetailsService.minConformDegree(auditSn, indexSn, parentDepartmentSn);
		if(c!=-1){
			SystemAuditScore systemAuditScore;
			systemAuditScore=systemAuditScoreService.getByMany(auditSn, indexSn);
			if(systemAuditScore!=null){
				systemAuditScore.setConformDegree(c);
				systemAuditScoreService.update(systemAuditScore);
			}else{
				systemAuditScore=new SystemAuditScore();
				systemAuditScore.setConformDegree(c);
				systemAuditScore.setStandardIndex(standardindexService.getByindexSn(indexSn));
				systemAuditScore.setSystemAudit(systemAuditService.getBySn(auditSn));
				systemAuditScoreService.save(systemAuditScore);
			}
		}
		return "jsonObject";
	}
	//删除
	public String delete(){
		jsonObject.put("status", "ok");
		try{
			systemAuditScoreDetailsService.delete(id);
		}catch(Exception e){
			jsonObject.put("status", "nook");
			return "jsonObject";
		}
		//更新打分
		int c=systemAuditScoreDetailsService.minConformDegree(auditSn, indexSn, departmentSn);
		if(c!=-1){
			SystemAuditScore systemAuditScore;
			systemAuditScore=systemAuditScoreService.getByMany(auditSn, indexSn);
			if(systemAuditScore!=null){
				systemAuditScore.setConformDegree(c);
				systemAuditScoreService.update(systemAuditScore);
			}else{
				systemAuditScore=new SystemAuditScore();
				systemAuditScore.setConformDegree(c);
				systemAuditScore.setStandardIndex(standardindexService.getByindexSn(indexSn));
				systemAuditScore.setSystemAudit(systemAuditService.getBySn(auditSn));
				systemAuditScoreService.save(systemAuditScore);
			}
		}else{
			SystemAuditScore systemAuditScore;
			systemAuditScore=systemAuditScoreService.getByMany(auditSn, indexSn);
			if(systemAuditScore!=null){
				systemAuditScore.setConformDegree(100);
				systemAuditScoreService.update(systemAuditScore);
			}
		}
		return "jsonObject";
	}
	//更新
	public String update(){
		jsonObject.put("status", "ok");
		try{
			SystemAuditScoreDetails systemAuditScoreDetails=systemAuditScoreDetailsService.getById(id);
			systemAuditScoreDetails.setConformDegree(conformDegree);
			systemAuditScoreDetails.setDepartment(departmentService.getByDepartmentSn(departmentSn));
			systemAuditScoreDetailsService.update(systemAuditScoreDetails);
		}catch(Exception e){
			jsonObject.put("status", "nook");
			return "jsonObject";
		}
		//更新打分
		int c=systemAuditScoreDetailsService.minConformDegree(auditSn, indexSn, parentDepartmentSn);
		if(c!=-1){
			SystemAuditScore systemAuditScore;
			systemAuditScore=systemAuditScoreService.getByMany(auditSn, indexSn);
			if(systemAuditScore!=null){
				systemAuditScore.setConformDegree(c);
				systemAuditScoreService.update(systemAuditScore);
			}else{
				systemAuditScore=new SystemAuditScore();
				systemAuditScore.setConformDegree(c);
				systemAuditScore.setStandardIndex(standardindexService.getByindexSn(indexSn));
				systemAuditScore.setSystemAudit(systemAuditService.getBySn(auditSn));
				systemAuditScoreService.save(systemAuditScore);
			}
		}else{
			SystemAuditScore systemAuditScore;
			systemAuditScore=systemAuditScoreService.getByMany(auditSn, indexSn);
			if(systemAuditScore!=null){
				systemAuditScore.setConformDegree(100);
				systemAuditScoreService.update(systemAuditScore);
			}
		}
		return "jsonObject";
	}
	public JSONObject getJsonObject() {
		return jsonObject;
	}
	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
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
	public String getIndexSn() {
		return indexSn;
	}
	public void setIndexSn(String indexSn) {
		this.indexSn = indexSn;
	}
	public String getDepartmentSn() {
		return departmentSn;
	}
	public void setDepartmentSn(String departmentSn) {
		this.departmentSn = departmentSn;
	}
	
	public boolean isB() {
		return b;
	}

	public void setB(boolean b) {
		this.b = b;
	}
	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getParentDepartmentSn() {
		return parentDepartmentSn;
	}
	public void setParentDepartmentSn(String parentDepartmentSn) {
		this.parentDepartmentSn = parentDepartmentSn;
	}
	public int getConformDegree() {
		return conformDegree;
	}
	public void setConformDegree(int conformDegree) {
		this.conformDegree = conformDegree;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIsUpdate() {
		return isUpdate;
	}
	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}
}
