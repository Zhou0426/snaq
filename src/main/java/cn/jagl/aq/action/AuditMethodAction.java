package cn.jagl.aq.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import cn.jagl.aq.domain.StandardIndex;
import cn.jagl.aq.domain.StandardIndexAuditMethod;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author mahui
 * @method 
 * @date 2016年7月21日下午8:42:34
 */
public class AuditMethodAction extends BaseAction<StandardIndexAuditMethod> {
	private static final long serialVersionUID = 1L;
	private int id;
	private String auditMethodContent;
	private String message;
	private Float deduction;//扣分
	private String indexDeductedSn;
	private JSONArray jsonArray=new JSONArray();
	
	//添加
	public String save(){
		message="ok";
		try{			
			StandardIndex standardIndex=standardindexService.getById(id);
			String auditMethodSn=standardIndex.getStandard().getStandardSn()+"-"+standardIndex.getIndexSn()+"-"+new SimpleDateFormat("yyyyMMddHHmmssSSS") .format(new Date() );		
			StandardIndexAuditMethod standardIndexAuditMethod=new StandardIndexAuditMethod();
			standardIndexAuditMethod.setAuditMethodSn(auditMethodSn);
			standardIndexAuditMethod.setAuditMethodContent(auditMethodContent);
			standardIndexAuditMethod.setStandardIndex(standardIndex);
			if(indexDeductedSn!=null && indexDeductedSn.trim().length()>0){
				StandardIndex indexDeducted=standardindexService.getByindexSn(indexDeductedSn);
				standardIndexAuditMethod.setIndexDeducted(indexDeducted);
				standardIndexAuditMethod.setDeduction(deduction);
			}
			standardIndexAuditMethodService.save(standardIndexAuditMethod);
		}catch(Exception e){
			message="nook";
		}
		return SUCCESS;
	}
	//删除
	public String delete(){
		message="ok";
		try{
			standardIndexAuditMethodService.delete(id);
		}catch(Exception e){
			message="nook";
		}
		return SUCCESS;
	}
	//查询
	public String query(){
		jsonList=standardIndexAuditMethodService.queryJoinStandardIndex(id);
		for(StandardIndexAuditMethod s:jsonList){
			JSONObject jo=new JSONObject();
			jo.put("auditMethodContent", s.getAuditMethodContent());
			jo.put("id", s.getId());
			jo.put("auditMethodSn", s.getAuditMethodSn());
			if(s.getIndexDeducted()!=null){
				String indexSn=s.getIndexDeducted().getIndexSn();
				if(indexSn.split("-").length>1){
					indexSn=indexSn.split("-")[1];
				}
				jo.put("indexDeductedSn",indexSn);
				jo.put("deduction", s.getDeduction());
			}
			jsonArray.add(jo);
		}
		return "jsonArray";
	}
	public Float getDeduction() {
		return deduction;
	}
	public void setDeduction(Float deduction) {
		this.deduction = deduction;
	}
	public String getIndexDeductedSn() {
		return indexDeductedSn;
	}
	public void setIndexDeductedSn(String indexDeductedSn) {
		this.indexDeductedSn = indexDeductedSn;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getAuditMethodContent() {
		return auditMethodContent;
	}
	public void setAuditMethodContent(String auditMethodContent) {
		this.auditMethodContent = auditMethodContent;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public JSONArray getJsonArray() {
		return jsonArray;
	}
	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}
	
}
