package cn.jagl.aq.action;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.jagl.aq.domain.DocumentTemplate;
import cn.jagl.aq.service.DepartmentTypeService;
import cn.jagl.aq.service.DocumentTemplateService;
import cn.jagl.aq.service.StandardIndexService;
import cn.jagl.util.PageModel;
@SuppressWarnings("unchecked")
public class DocumentTemplateAction extends BaseAction<DocumentTemplate> {
	/**
	 * ���
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String documentTemplateSn;
	private String documentTemplateName;
	private String departmentTypeSn;
	private String indexSn;
	private String q;
	private String message;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	public String getDepartmentTypeSn() {
		return departmentTypeSn;
	}
	public void setDepartmentTypeSn(String departmentTypeSn) {
		this.departmentTypeSn = departmentTypeSn;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDocumentTemplateName() {
		return documentTemplateName;
	}
	public void setDocumentTemplateName(String documentTemplateName) {
		this.documentTemplateName = documentTemplateName;
	}

	public String getDocumentTemplateSn() {
		return documentTemplateSn;
	}
	public void setDocumentTemplateSn(String documentTemplateSn) {
		this.documentTemplateSn = documentTemplateSn;
	}
	public String getIndexSn() {
		return indexSn;
	}
	public void setIndexSn(String indexSn) {
		this.indexSn = indexSn;
	}
	//��ҳ��ѯ
	public String query() throws IOException{
		//�����ܼ�¼
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
		PrintWriter out;  
        out = response.getWriter();
		JSONArray array=new JSONArray();
		String hql1="select d FROM DocumentTemplate d WHERE d.departmentType.departmentTypeSn LIKE '"+departmentTypeSn+"'";
		String hql2="select COUNT(d) FROM DocumentTemplate d WHERE d.departmentType.departmentTypeSn LIKE '"+departmentTypeSn+"'";
		for(DocumentTemplate documentTemplate:documentTemplateService.queryByQ(hql1,page, rows)){
			JSONObject jo=new JSONObject();
			jo.put("id", documentTemplate.getId());
			jo.put("documentTemplateSn",documentTemplate.getDocumentTemplateSn());
			jo.put("documentTemplateName", documentTemplate.getDocumentTemplateName());
			jo.put("attachments", documentTemplate.getAttachments().size());
			jo.put("standardindex", documentTemplate.getStandardIndexes().size());
			array.put(jo);
		}
		String str="{\"total\":"+documentTemplateService.count(hql2)+",\"rows\":"+array.toString()+"}";
		out.print(str);
        out.flush(); 
        out.close();
		return "jsonList";
	}
	//ָ����Ӳ�ѯ
	public String queryByQ(){
		String hql="from DocumentTemplate s WHERE documentTemplateSn LIKE '%"+q+"%' OR documentTemplateName LIKE '%"+q+"%'";
		jsonList=documentTemplateService.queryByQ(hql, 1, 15);
		return "jsonList";
	}
	//����ɾ��
	public void deleteByIds() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out;  
        out = response.getWriter();
		String str="{\"status\":\"ok\",\"message\":\"ɾ���ɹ���\"}";
		try{
			documentTemplateService.deleteDocumentTemplateByIds(ids);
		}catch(Exception e){
			str = "{\"status\":\"nook\",\"message\":\"ɾ��ʧ�ܣ�\"}";
		}
		out.print(str);
        out.flush(); 
        out.close();	
	}
	//����
	public void save() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out;  
        out = response.getWriter();
		String str="{\"status\":\"ok\",\"message\":\"��ӳɹ���\"}";
		DocumentTemplate documentTemplate1=documentTemplateService.getByDocumentTemplateSn(documentTemplateSn);
		if(documentTemplate1==null){
			DocumentTemplate documentTemplate=new DocumentTemplate();
			documentTemplate.setDocumentTemplateSn(documentTemplateSn);
			documentTemplate.setDocumentTemplateName(documentTemplateName);
			documentTemplate.setDepartmentType(departmentTypeService.getByDepartmentTypeSn(departmentTypeSn));
			try{
				documentTemplateService.addDocumentTemplate(documentTemplate);
			}catch(Exception e){
				str = "{\"status\":\"nook\",\"message\":\"���ʧ�ܣ�\"}";
			}
		}else{
			str = "{\"status\":\"nook\",\"message\":\"���ʧ�ܣ�ģ�����Ѵ��ڣ������ظ���ӣ�\"}";
		}
		out.print(str);
        out.flush(); 
        out.close();
	}
	public void update() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out;  
        out = response.getWriter();
		String str="{\"status\":\"ok\",\"message\":\"�޸ĳɹ���\"}";
		DocumentTemplate documentTemplate=documentTemplateService.getById(id);
		if(documentTemplate.getDocumentTemplateSn().equals(documentTemplateSn)){
			documentTemplate.setDocumentTemplateSn(documentTemplateSn);
			documentTemplate.setDocumentTemplateName(documentTemplateName);
			try{
				documentTemplateService.update(documentTemplate);
			}catch(Exception e){
				str = "{\"status\":\"nook\",\"message\":\"�޸�ʧ�ܣ�\"}";
			}
		}else{
			if(documentTemplateService.getByDocumentTemplateSn(documentTemplateSn)==null){
				documentTemplate.setDocumentTemplateSn(documentTemplateSn);
				documentTemplate.setDocumentTemplateName(documentTemplateName);
				try{
					documentTemplateService.update(documentTemplate);
				}catch(Exception e){
					str = "{\"status\":\"nook\",\"message\":\"�޸�ʧ�ܣ�\"}";
				}
			}else{
				str = "{\"status\":\"nook\",\"message\":\"�޸�ʧ�ܣ�ģ�����Ѵ��ڣ��뻻һ����ţ�\"}";
			}
		}
		out.print(str);
        out.flush(); 
        out.close();
	}
	//�������ָ��
	public String addStandardIndex(){
		message="ok";
		try{
			DocumentTemplate documentTemplate=documentTemplateService.getByDocumentTemplateSn(documentTemplateSn);
			documentTemplate.getStandardIndexes().add(standardindexService.getByindexSn(indexSn));
			documentTemplateService.update(documentTemplate);
		}catch(Exception e){
			message="nook";
		}		
		return "message";
	}
	//ɾ�����ָ��
	public String deleteStandardIndex(){
		message="ok";
		try{
			DocumentTemplate documentTemplate=documentTemplateService.getByDocumentTemplateSn(documentTemplateSn);
			documentTemplate.getStandardIndexes().remove(standardindexService.getByindexSn(indexSn));
			documentTemplateService.update(documentTemplate);
		}catch(Exception e){
			message="nook";
		}
		return "message";
	}
}
