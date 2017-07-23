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
	
	//���ָ���ֶ�
	public PrintWriter out()throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out= response.getWriter();
        return out;
	}
	//��ҳ�Լ�������
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
			 //�ַ���
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
	 * ֤��ͳ��չʾ����
	 * @return
	 */
	public String showData(){
		
		jsonObject = certificationTypeService.queryStatistics(departmentSn, page, rows);
		
		return SUCCESS;
	}
	
	
	//���
	public String add() throws IOException{
		out();
		String message="";
		String hql="select c from CertificationType c";
		List<CertificationType> certificationTypes=certificationTypeService.findByPage(hql, 1, 10000);
		for(CertificationType certificationType:certificationTypes){
			if(certificationType.getCertificationTypeSn()==certificationTypeSn){
				message="���֤������ʧ�ܣ���֤�����ͱ���Ѵ��ڣ�";
				out().print(message);
		        out().flush(); 
		        out().close();
				return SUCCESS;
			}
			if(certificationType.getCertificationTypeName()==certificationTypeName){
				message="���֤������ʧ�ܣ���֤�����������Ѵ��ڣ�";
				out().print(message);
		        out().flush(); 
		        out().close();
				return SUCCESS;
			}
		}
		//�÷�ҳ������ȡ�������ͱ���ж��Ƿ��ظ�
		try{
			CertificationType certificationType=new CertificationType();
//			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");			
//			certificationTypeSn=df.format(new Date());
			certificationType.setCertificationTypeSn(certificationTypeSn);
			certificationType.setCertificationTypeName(certificationTypeName);
			certificationTypeService.add(certificationType);
			message="���֤�����ͳɹ���";
			}
		catch(Exception e){
			message="���֤������ʧ�ܣ������Ƿ����ظ����ݣ�";
		}
		out().print(message);
        out().flush(); 
        out().close();      
		return SUCCESS;
	}
	//�¹ʸ���
	public String update() throws IOException{
		out();
		CertificationType certificationType=certificationTypeService.getById(id);
		String message="";
		if(certificationType.getCertificationes().size()==0){
			certificationType.setCertificationTypeSn(certificationTypeSn);
			certificationType.setCertificationTypeName(certificationTypeName);
			certificationTypeService.update(certificationType);
			message="���³ɹ���";
		}
		else {
			certificationType.setCertificationTypeName(certificationTypeName);
			certificationTypeService.update(certificationType);
			message="����������֤�飬�����޸����ţ�";
		}
		out().print(message);
		out().flush(); 
		out().close();
		return SUCCESS;
	}
	//ɾ��
	public String delete() throws IOException{
		out();
		CertificationType certificationType=certificationTypeService.getById(id);
		String message;
		if(certificationType.getCertificationes().size()==0){
			certificationTypeService.delete(certificationType);
			 message="ɾ���ɹ���";
		}
		else{
			 message="����������֤�飬����ɾ����";
		}
		out().print(message);
		out().flush(); 
		out().close();
		return SUCCESS;
		
	}
}
