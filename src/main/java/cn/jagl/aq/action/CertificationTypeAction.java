package cn.jagl.aq.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.jagl.aq.domain.CertificationType;

public class CertificationTypeAction extends BaseAction<CertificationType>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String q;
	private int id;
	private int page;
	private int rows;
	private boolean checked;
	private String departmentSn;
	private String certificationTypeSn;
	private String certificationTypeName;
	private net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();

	
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public String getDepartmentSn() {
		return departmentSn;
	}
	public void setDepartmentSn(String departmentSn) {
		this.departmentSn = departmentSn;
	}
	public net.sf.json.JSONObject getJsonObject() {
		return jsonObject;
	}
	public void setJsonObject(net.sf.json.JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}
	public String getCertificationTypeSn() {
		return certificationTypeSn;
	}
	public void setCertificationTypeSn(String certificationTypeSn) {
		this.certificationTypeSn = certificationTypeSn;
	}
	public String getCertificationTypeName() {
		return certificationTypeName;
	}
	public void setCertificationTypeName(String certificationTypeName) {
		this.certificationTypeName = certificationTypeName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	
	//输出指定字段
	public PrintWriter out()throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out= response.getWriter();
        return out;
	}
	//分页以及下拉框
	public String show() throws IOException{
		out();
		JSONArray array=new JSONArray();
		StringBuffer hqlentity=new StringBuffer();
		StringBuffer hqlcount=new StringBuffer();
		hqlentity.append("select c from CertificationType c");
		hqlcount.append("select count(c) from CertificationType c");
		if(q!=null && !"".equals(q)){
    		hqlentity.append(" where (c.certificationTypeSn like '"+q+"%' or c.certificationTypeName like '"+q+"%')");
    		hqlcount.append(" where (c.certificationTypeSn like '"+q+"%' or c.certificationTypeName like '"+q+"%')");
    	}
		List<CertificationType> certificationTypes=certificationTypeService.findByPage(hqlentity.toString(), 1, 10);
		long total=certificationTypeService.countHql(hqlcount.toString());
		for(CertificationType certificationType:certificationTypes){
			 JSONObject jo=new JSONObject();
			 //字符型
			 jo.put("id",certificationType.getId());
			 jo.put("certificationTypeSn",certificationType.getCertificationTypeSn());
			 jo.put("certificationTypeName",certificationType.getCertificationTypeName());
			 array.put(jo);
		}
		String str="{\"total\":"+total+",\"rows\":"+array+"}";
		out().print(str);
		out().flush(); 
		out().close();
		return SUCCESS;
	}
	
	/**
	 * 证件统计展示数据
	 * @return
	 */
	public String showData(){
		
		jsonObject = certificationTypeService.queryStatistics(departmentSn, page, rows);
		
		return SUCCESS;
	}
	
	
	//添加
	public String add() throws IOException{
		out();
		String message="";
		String hql="select c from CertificationType c";
		List<CertificationType> certificationTypes=certificationTypeService.findByPage(hql, 1, 10000);
		for(CertificationType certificationType:certificationTypes){
			if(certificationType.getCertificationTypeSn()==certificationTypeSn){
				message="添加证件类型失败，该证件类型编号已存在！";
				out().print(message);
		        out().flush(); 
		        out().close();
				return SUCCESS;
			}
			if(certificationType.getCertificationTypeName()==certificationTypeName){
				message="添加证件类型失败，该证件类型名字已存在！";
				out().print(message);
		        out().flush(); 
		        out().close();
				return SUCCESS;
			}
		}
		//用分页方法获取所有类型编号判断是否重复
		try{
			CertificationType certificationType=new CertificationType();
//			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");			
//			certificationTypeSn=df.format(new Date());
			certificationType.setCertificationTypeSn(certificationTypeSn);
			certificationType.setCertificationTypeName(certificationTypeName);
			certificationTypeService.add(certificationType);
			message="添加证件类型成功！";
			}
		catch(Exception e){
			message="添加证件类型失败，请检查是否有重复数据！";
		}
		out().print(message);
        out().flush(); 
        out().close();      
		return SUCCESS;
	}
	//事故更新
	public String update() throws IOException{
		out();
		CertificationType certificationType=certificationTypeService.getById(id);
		String message="";
		if(certificationType.getCertificationes().size()==0){
			certificationType.setCertificationTypeSn(certificationTypeSn);
			certificationType.setCertificationTypeName(certificationTypeName);
			certificationTypeService.update(certificationType);
			message="更新成功！";
		}
		else {
			certificationType.setCertificationTypeName(certificationTypeName);
			certificationTypeService.update(certificationType);
			message="该类型下有证书，不能修改其编号！";
		}
		out().print(message);
		out().flush(); 
		out().close();
		return SUCCESS;
	}
	//删除
	public String delete() throws IOException{
		out();
		CertificationType certificationType=certificationTypeService.getById(id);
		String message;
		if(certificationType.getCertificationes().size()==0){
			certificationTypeService.delete(certificationType);
			 message="删除成功！";
		}
		else{
			 message="该类型下有证书，不能删除！";
		}
		out().print(message);
		out().flush(); 
		out().close();
		return SUCCESS;
		
	}
}
