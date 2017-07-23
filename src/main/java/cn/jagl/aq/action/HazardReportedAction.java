package cn.jagl.aq.action;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import cn.jagl.aq.domain.AccidentType;
import cn.jagl.aq.domain.DepartmentType;
import cn.jagl.aq.domain.HazardReported;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.RiskLevel;
import cn.jagl.aq.domain.StandardIndex;

public class HazardReportedAction extends BaseAction<HazardReported> {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> jsonMap=new HashMap<String, Object>();
	private int id;
	private String reportSn;//上报编号
	private String indexSn;//指标编号
	private String departmentTypeSn;//部门类型
	private String departmentSn;//部门类型
	private String hazardDescription;//危险源描述
	private String resultDescription;//后果描述
	private String accidentTypeSn;//事故类型
	private RiskLevel riskLevel;//风险水平
	private String hazardSn;//危险源编号
	private String auditSuggestion;//审核意见
	private Boolean auditedStatus;//审核结果
	private String pag;//后台传回前台参数
	
	//获取所有数据-根据人员Id和角色
	@SuppressWarnings("unchecked")
	public String showData(){
		String pId=(String)session.get("personId");
    	Person person=personService.getByPersonId(pId);
		//Person person=(Person) session.get("person");
		HashMap<String, String> roles=new HashMap<String, String>();
		roles=(HashMap<String, String>) session.get("roles");
		if(person!=null){
			jsonMap=hazardReportedService.queryAllData(person.getPersonId(), roles,page,rows);
		}
		return "showList";
	}
	//上报添加数据
	public String add(){
		try{
			String pId=(String)session.get("personId");
	    	Person person=personService.getByPersonId(pId);
//			Person person=(Person) session.get("person");
//			Person per=personService.getByPersonId(person.getPersonId());
			DepartmentType departmentType=departmentTypeService.getByDepartmentTypeSn(departmentTypeSn);
			AccidentType accidentType =accidentTypeService.getByAccidentTypeSn(accidentTypeSn);
			HazardReported hazardReported=new HazardReported();
			hazardReported.setReportSn(LocalDateTime.now().toString());
			hazardReported.setHazardDescription(hazardDescription);
			hazardReported.setReportPerson(person);
			hazardReported.setReportDateTime(LocalDateTime.now());
			hazardReported.setDepartmentType(departmentType);
			hazardReported.setAccidentType(accidentType);
			hazardReported.setResultDescription(resultDescription);
			hazardReported.setRiskLevel(riskLevel);
			hazardReported.setDeleted(false);
			hazardReportedService.addData(hazardReported);
			pag=SUCCESS;
		}catch(Exception e){
			pag=ERROR;
		}
		return SUCCESS;
	}
	//删除数据
	public String deleteData(){
		try{
			hazardReportedService.deleteData(id);
			pag=SUCCESS;
		}catch(Exception e){
			pag=ERROR;
		}
		return SUCCESS;
	}
	//失控统计展示
	public String showCount(){
		jsonMap=hazardReportedService.queryCount(departmentSn, departmentTypeSn, page, rows);
		return "showList";
	}
	//失控统计展示隐患
	public String showUnsafeCondition(){
		jsonMap=hazardReportedService.queryUnsafeCondition(departmentSn, hazardSn, page, rows);
		return "showList";
	}
	//审核
	public String audit(){
		try{
			String pId=(String)session.get("personId");
	    	Person person=personService.getByPersonId(pId);
			//Person person=(Person) session.get("person");
			//Person per=personService.getByPersonId(person.getPersonId());
			HazardReported hazardReported=hazardReportedService.getById(id);
			hazardReported.setAuditSuggestion(auditSuggestion);
			hazardReported.setAuditedStatus(auditedStatus);
			hazardReported.setAuditor(person);
			hazardReportedService.updateData(hazardReported);
			pag=SUCCESS;
		}catch(Exception e){
			pag=ERROR;
		}
		return SUCCESS;
	}
	
	//上报关联指标
	public String showStandardIndex(){
		jsonMap = hazardReportedService.showStandardIndex(reportSn, page, rows);
		return "showList";
	}
	
	//添加危险源上报对应的指标
	public String addStandardIndex(){
		try{
			StandardIndex standardIndex = standardindexService.getByindexSn(indexSn);
			HazardReported hazardReported = hazardReportedService.getById(id);
			hazardReported.getStandardIndexes().add(standardIndex);
			hazardReportedService.updateData(hazardReported);
			pag = SUCCESS;
		}catch(Exception e){
			pag = ERROR;
		}
		return SUCCESS;
	}
	//删除危险源上报对应的指标
	public String deleteStandardIndex(){
		try{
			StandardIndex standardIndex = standardindexService.getByindexSn(indexSn);
			HazardReported hazardReported = hazardReportedService.getById(id);
			hazardReported.getStandardIndexes().remove(standardIndex);
			hazardReportedService.updateData(hazardReported);
			pag = SUCCESS;
		}catch(Exception e){
			pag = ERROR;
		}
		return SUCCESS;
	}
	
	
	public String getIndexSn() {
		return indexSn;
	}
	public void setIndexSn(String indexSn) {
		this.indexSn = indexSn;
	}
	public String getAuditSuggestion() {
		return auditSuggestion;
	}
	public void setAuditSuggestion(String auditSuggestion) {
		this.auditSuggestion = auditSuggestion;
	}
	public Boolean getAuditedStatus() {
		return auditedStatus;
	}
	public void setAuditedStatus(Boolean auditedStatus) {
		this.auditedStatus = auditedStatus;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getResultDescription() {
		return resultDescription;
	}
	public void setResultDescription(String resultDescription) {
		this.resultDescription = resultDescription;
	}
	public String getHazardSn() {
		return hazardSn;
	}
	public void setHazardSn(String hazardSn) {
		this.hazardSn = hazardSn;
	}
	public String getPag() {
		return pag;
	}
	public void setPag(String pag) {
		this.pag = pag;
	}
	public String getDepartmentTypeSn() {
		return departmentTypeSn;
	}
	public void setDepartmentTypeSn(String departmentTypeSn) {
		this.departmentTypeSn = departmentTypeSn;
	}
	public String getReportSn() {
		return reportSn;
	}
	public void setReportSn(String reportSn) {
		this.reportSn = reportSn;
	}
	public String getHazardDescription() {
		return hazardDescription;
	}
	public void setHazardDescription(String hazardDescription) {
		this.hazardDescription = hazardDescription;
	}
	public String getAccidentTypeSn() {
		return accidentTypeSn;
	}
	public void setAccidentTypeSn(String accidentTypeSn) {
		this.accidentTypeSn = accidentTypeSn;
	}
	public RiskLevel getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(RiskLevel riskLevel) {
		this.riskLevel = riskLevel;
	}
	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}
	public void setJsonMap(Map<String, Object> jsonMap) {
		this.jsonMap = jsonMap;
	}
	public String getDepartmentSn() {
		return departmentSn;
	}
	public void setDepartmentSn(String departmentSn) {
		this.departmentSn = departmentSn;
	}

	
}
