package cn.jagl.aq.action;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import cn.jagl.aq.domain.DepartmentType;
import cn.jagl.aq.domain.Standard;
import cn.jagl.aq.domain.StandardType;
import cn.jagl.util.PageModel;

public class StandardAction extends BaseAction<Standard> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private StandardType standardType;//��׼���ͣ�ö�٣�
	private String standardSn;//��׼���
	private String standardName;//��׼����
	private String departmentTypeSn;
	private List<DepartmentType> jsonLoad=null;
	
	public List<DepartmentType> getJsonLoad() {
		return jsonLoad;
	}
	public void setJsonLoad(List<DepartmentType> jsonLoad) {
		this.jsonLoad = jsonLoad;
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
	public void setId(int id){
		this.id = id;
	}
	public StandardType getStandardType() {
		return standardType;
	}
	public void setStandardType(StandardType standardType) {
		this.standardType = standardType;
	}
	public String getStandardSn() {
		return standardSn;
	}
	public void setStandardSn(String standardSn) {
		this.standardSn = standardSn;
	}
	public String getStandardName() {
		return standardName;
	}
	public void setStandardName(String standardName) {
		this.standardName = standardName;
	}

	//�����ѯ�ӷ�ҳ
	public String query(){
		//��ȡ���е��Ӳ���
		String hql1="select COUNT(s) FROM Standard s WHERE s.deleted=false AND s.departmentType.departmentTypeSn LIKE '"+departmentTypeSn+"'";
		String hql2="select s FROM Standard s WHERE s.deleted=false AND s.departmentType.departmentTypeSn LIKE '"+departmentTypeSn+"'";
		pageModel=new PageModel<Standard>();
		pageModel.setTotal(standardService.count(hql1));
		pageModel.setRows(standardService.query(hql2,page, rows));
		return "pageModel";
	}
	//����
	public void updateStandard() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out;  
        out = response.getWriter();
		String str="{\"status\":\"ok\",\"message\":\"�޸ĳɹ���\"}";
		Standard standard=standardService.getById(id);
		if(standard.getStandardSn().equals(standardSn)){
			standard.setStandardSn(standardSn);
			standard.setStandardName(standardName);
			standard.setStandardType(standardType);
			try{
				standardService.update(standard);
			}catch(Exception e){
				str="{\"status\":\"nook\",\"message\":\"�޸�ʧ�ܣ�\"}";
			}
		}else{
			Standard standard1=standardService.getByStandardSn(standardSn);
			if(standard1==null){
				standard.setStandardSn(standardSn);
				standard.setStandardName(standardName);
				standard.setStandardType(standardType);
				try{
					standardService.update(standard);
				}catch(Exception e){
					str="{\"status\":\"nook\",\"message\":\"�޸�ʧ�ܣ�\"}";
				}
			}else{
				str="{\"status\":\"nook\",\"message\":\"�޸�ʧ�ܣ���׼����Ѿ����ڣ��뻻һ����ţ�\"}";
			}
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
		Standard standard1=standardService.getByStandardSn(standardSn);
		if(standard1==null){
			Standard standard=new Standard();
			standard.setStandardSn(standardSn);
			standard.setStandardName(standardName);
			standard.setStandardType(standardType);
			standard.setDepartmentType(departmentTypeService.getByDepartmentTypeSn(departmentTypeSn));
			standard.setDeleted(false);
			try{
				standardService.addStandard(standard);
			}catch(Exception e){
				str = "{\"status\":\"nook\",\"message\":\"���ʧ�ܣ�\"}";
			}
		}else{
			if(standard1.getDeleted()==false){
				str="{\"status\":\"nook\",\"message\":\"���ʧ�ܣ���׼����Ѿ����ڣ������ظ���ӣ�\"}";
			}else{
				try{
					standard1.setStandardName(standardName);
					standard1.setStandardType(standardType);
					standard1.setDepartmentType(departmentTypeService.getByDepartmentTypeSn(departmentTypeSn));
					standard1.setDeleted(false);
					standardService.update(standard1);
				}catch(Exception e){
					str = "{\"status\":\"nook\",\"message\":\"���ʧ�ܣ�\"}";
				}				
			}			
		}
		out.print(str);
        out.flush(); 
        out.close();
	}
	//������¼����
	public void hidden() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out;  
        out = response.getWriter();
		String str="{\"status\":\"ok\",\"message\":\"ɾ���ɹ���\"}";
		try{
			standardService.hidden(ids);
		}catch(Exception e){
			str="{\"status\":\"nook\",\"message\":\"ɾ��ʧ�ܣ�\"}";
		}
		out.print(str);
        out.flush(); 
        out.close();	
	}
	//��ȡ���б�׼
	public String getStandard(){
		int f=3;
		switch(standardType){
		case ���ָ��:
			f=0;
			break;
		case ���ֱ�׼:
			f=1;
			break;
		}
		String hql="select s FROM Standard s WHERE s.deleted=false AND s.standardType="+f;
		if(departmentTypeSn!=null&&departmentTypeSn.length()>0){
			hql+=" AND s.departmentType.departmentTypeSn LIKE '"+departmentTypeSn+"'";
		}				
		jsonList=standardService.query(hql, 1, 10000);
		return "standard";
	}
	//��ȡ�������ָ��
	public String queryByStandardType(){
		jsonList=standardService.queryByStandardType(standardType,departmentTypeSn);
		return "jsonList";
	}
	//��ȡ����ָ��
	public String queryAll(){
		String hql="select s FROM Standard s WHERE s.deleted=false AND s.departmentType.departmentTypeSn LIKE '"+departmentTypeSn+"'";
		jsonList=standardService.query(hql, 1, 10000);
		return "jsonList";
	}
	
	//�������β˵�ѡ������ر�׼
	public String standard(){
		String hql="";
		if(departmentTypeSn==null||departmentTypeSn.trim().length()==0){
			hql="select s FROM Standard s WHERE s.deleted=false";
		}else{
			hql="select s FROM Standard s WHERE s.deleted=false AND s.departmentType.departmentTypeSn = '"+departmentTypeSn+"'";
		}
		jsonList=standardService.query(hql, 1, 10000);
		return "jsonList";
	}
	//�������β˵�ѡ������ر�׼�������Լ����ڲ��ŵĲ������ͣ�
	public String queryStandard(){
		String departmentSn = (String) session.get("departmentSn");
		List<String> departmentTypeSns = departmentTypeService.getAllImplDepartmentTypesIncloudSelf(departmentSn);
		if( departmentTypeSns.size() > 0 )
			jsonList = standardService.getStandardByDepartmentTypeSns(departmentTypeSns);
		return "jsonList";
	}
}
