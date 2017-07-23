package cn.jagl.aq.action;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.UnsafeCondition;
import cn.jagl.aq.domain.UnsafeConditionDefer;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class UnsafeConditionDeferAction extends BaseAction<UnsafeConditionDefer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JSONObject jsonObject=new JSONObject();
	private JSONArray jsonArray=new JSONArray();
	private int id;
	private String unsafeconditionSn;
	private Timestamp applyDeferTo;
	private String applicationSn;//延期申请编号
	private Boolean passed;//审核结果
	private String auditRemark;//审核说明
	private String reason;//申请理由
	private String pag;//前台传参
	
	//发起申请
	public String save(){
		jsonObject.put("status", "ok");
		String personId=(String) session.get("personId");
		UnsafeConditionDefer u=new UnsafeConditionDefer();
		String applicationSn=new SimpleDateFormat("yyyyMMddHHmmssSSS") .format(new Date() );
		try{
			u.setApplicationSn(applicationSn);
			u.setReason(reason);
			u.setApplyDateTime(Timestamp.valueOf(LocalDateTime.now()));
			u.setApplyDeferTo(applyDeferTo);
			u.setApplicant(personService.getByPersonId(personId));
			u.setUnsafecondition(unsafeConditionService.getByInconformityItemSn(unsafeconditionSn));
			unsafeConditionDeferService.save(u);
		}catch(Exception e){
			jsonObject.put("status", "nook");
		}
		
		return "jsonObject";
	}
	//根据隐患查询申请记录
	public String queryByUsn(){
		String hql="select u from UnsafeConditionDefer u where u.unsafecondition.inconformityItemSn="+unsafeconditionSn;
		for(UnsafeConditionDefer u:unsafeConditionDeferService.queryByPage(hql, 1, 20)){
			JSONObject jo=new JSONObject();
			jo.put("applicationSn", u.getApplicationSn());
			jo.put("reason", u.getReason());
			jo.put("applyDateTime", u.getApplyDateTime().toString());
			jo.put("applyDeferTo", u.getApplyDeferTo().toString());
			jo.put("passed", u.getPassed());
			if(u.getAuditDatetime()!=null){
				jo.put("auditRemark", u.getAuditDatetime().toString());
			}
			if(u.getAuditor()!=null){
				jo.put("auditor", u.getAuditor().getPersonName());
			}
			jsonArray.add(jo);	
		}
		return "jsonArray";
	}
	//显示审批数据
	public String showData(){
		String personId = (String) session.get("personId");
		jsonArray = unsafeConditionDeferService.getListByPersonId(personId);
		return "jsonArray";
	}
	//审批
	public String audit(){
		try{
			UnsafeConditionDefer unsafeConditionDefer = unsafeConditionDeferService.getBySn(applicationSn);
			if(passed){
				UnsafeCondition unsafeCondition = unsafeConditionService.getByInconformityItemSn(unsafeConditionDefer.getUnsafecondition().getInconformityItemSn());
				unsafeCondition.setCorrectDeadline(unsafeConditionDefer.getApplyDeferTo());
				unsafeConditionService.update(unsafeCondition);
				unsafeConditionDefer.setUnsafecondition(unsafeCondition);
			}
			String personId = (String) session.get("personId");
			Person person = personService.getByPersonId(personId);
			unsafeConditionDefer.setAuditor(person);
			unsafeConditionDefer.setPassed(passed);
			unsafeConditionDefer.setAuditRemark(auditRemark);
			unsafeConditionDefer.setAuditDatetime(new Timestamp(System.currentTimeMillis()));
			unsafeConditionDeferService.update(unsafeConditionDefer);
			pag = SUCCESS;
		}catch(Exception e){
			pag = ERROR;
		}
		return SUCCESS;
	}
	
	
	
	
	
	
	public String getPag() {
		return pag;
	}
	public void setPag(String pag) {
		this.pag = pag;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUnsafeconditionSn() {
		return unsafeconditionSn;
	}
	public void setUnsafeconditionSn(String unsafeconditionSn) {
		this.unsafeconditionSn = unsafeconditionSn;
	}
	public Timestamp getApplyDeferTo() {
		return applyDeferTo;
	}
	public void setApplyDeferTo(Timestamp applyDeferTo) {
		this.applyDeferTo = applyDeferTo;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Boolean getPassed() {
		return passed;
	}
	public void setPassed(Boolean passed) {
		this.passed = passed;
	}
	public String getAuditRemark() {
		return auditRemark;
	}
	public void setAuditRemark(String auditRemark) {
		this.auditRemark = auditRemark;
	}
	public String getApplicationSn() {
		return applicationSn;
	}
	public void setApplicationSn(String applicationSn) {
		this.applicationSn = applicationSn;
	}

}
