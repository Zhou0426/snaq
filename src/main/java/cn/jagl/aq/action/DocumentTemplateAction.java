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
	 * 马辉
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
	//分页查询
	public String query() throws IOException{
		//设置总记录
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
	//指标添加查询
	public String queryByQ(){
		String hql="from DocumentTemplate s WHERE documentTemplateSn LIKE '%"+q+"%' OR documentTemplateName LIKE '%"+q+"%'";
		jsonList=documentTemplateService.queryByQ(hql, 1, 15);
		return "jsonList";
	}
	//多行删除
	public void deleteByIds() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out;  
        out = response.getWriter();
		String str="{\"status\":\"ok\",\"message\":\"删除成功！\"}";
		try{
			documentTemplateService.deleteDocumentTemplateByIds(ids);
		}catch(Exception e){
			str = "{\"status\":\"nook\",\"message\":\"删除失败！\"}";
		}
		out.print(str);
        out.flush(); 
        out.close();	
	}
	//插入
	public void save() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out;  
        out = response.getWriter();
		String str="{\"status\":\"ok\",\"message\":\"添加成功！\"}";
		DocumentTemplate documentTemplate1=documentTemplateService.getByDocumentTemplateSn(documentTemplateSn);
		if(documentTemplate1==null){
			DocumentTemplate documentTemplate=new DocumentTemplate();
			documentTemplate.setDocumentTemplateSn(documentTemplateSn);
			documentTemplate.setDocumentTemplateName(documentTemplateName);
			documentTemplate.setDepartmentType(departmentTypeService.getByDepartmentTypeSn(departmentTypeSn));
			try{
				documentTemplateService.addDocumentTemplate(documentTemplate);
			}catch(Exception e){
				str = "{\"status\":\"nook\",\"message\":\"添加失败！\"}";
			}
		}else{
			str = "{\"status\":\"nook\",\"message\":\"添加失败，模板编号已存在，不可重复添加！\"}";
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
		String str="{\"status\":\"ok\",\"message\":\"修改成功！\"}";
		DocumentTemplate documentTemplate=documentTemplateService.getById(id);
		if(documentTemplate.getDocumentTemplateSn().equals(documentTemplateSn)){
			documentTemplate.setDocumentTemplateSn(documentTemplateSn);
			documentTemplate.setDocumentTemplateName(documentTemplateName);
			try{
				documentTemplateService.update(documentTemplate);
			}catch(Exception e){
				str = "{\"status\":\"nook\",\"message\":\"修改失败！\"}";
			}
		}else{
			if(documentTemplateService.getByDocumentTemplateSn(documentTemplateSn)==null){
				documentTemplate.setDocumentTemplateSn(documentTemplateSn);
				documentTemplate.setDocumentTemplateName(documentTemplateName);
				try{
					documentTemplateService.update(documentTemplate);
				}catch(Exception e){
					str = "{\"status\":\"nook\",\"message\":\"修改失败！\"}";
				}
			}else{
				str = "{\"status\":\"nook\",\"message\":\"修改失败，模板编号已存在，请换一个编号！\"}";
			}
		}
		out.print(str);
        out.flush(); 
        out.close();
	}
	//增加相关指标
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
	//删除相关指标
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
