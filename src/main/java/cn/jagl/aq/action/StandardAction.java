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
	private StandardType standardType;//标准类型（枚举）
	private String standardSn;//标准编号
	private String standardName;//标准名称
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

	//单表查询加分页
	public String query(){
		//获取所有的子部门
		String hql1="select COUNT(s) FROM Standard s WHERE s.deleted=false AND s.departmentType.departmentTypeSn LIKE '"+departmentTypeSn+"'";
		String hql2="select s FROM Standard s WHERE s.deleted=false AND s.departmentType.departmentTypeSn LIKE '"+departmentTypeSn+"'";
		pageModel=new PageModel<Standard>();
		pageModel.setTotal(standardService.count(hql1));
		pageModel.setRows(standardService.query(hql2,page, rows));
		return "pageModel";
	}
	//更新
	public void updateStandard() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out;  
        out = response.getWriter();
		String str="{\"status\":\"ok\",\"message\":\"修改成功！\"}";
		Standard standard=standardService.getById(id);
		if(standard.getStandardSn().equals(standardSn)){
			standard.setStandardSn(standardSn);
			standard.setStandardName(standardName);
			standard.setStandardType(standardType);
			try{
				standardService.update(standard);
			}catch(Exception e){
				str="{\"status\":\"nook\",\"message\":\"修改失败！\"}";
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
					str="{\"status\":\"nook\",\"message\":\"修改失败！\"}";
				}
			}else{
				str="{\"status\":\"nook\",\"message\":\"修改失败，标准编号已经存在，请换一个编号！\"}";
			}
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
				str = "{\"status\":\"nook\",\"message\":\"添加失败！\"}";
			}
		}else{
			if(standard1.getDeleted()==false){
				str="{\"status\":\"nook\",\"message\":\"添加失败，标准编号已经存在，不可重复添加！\"}";
			}else{
				try{
					standard1.setStandardName(standardName);
					standard1.setStandardType(standardType);
					standard1.setDepartmentType(departmentTypeService.getByDepartmentTypeSn(departmentTypeSn));
					standard1.setDeleted(false);
					standardService.update(standard1);
				}catch(Exception e){
					str = "{\"status\":\"nook\",\"message\":\"添加失败！\"}";
				}				
			}			
		}
		out.print(str);
        out.flush(); 
        out.close();
	}
	//多条记录隐藏
	public void hidden() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out;  
        out = response.getWriter();
		String str="{\"status\":\"ok\",\"message\":\"删除成功！\"}";
		try{
			standardService.hidden(ids);
		}catch(Exception e){
			str="{\"status\":\"nook\",\"message\":\"删除失败！\"}";
		}
		out.print(str);
        out.flush(); 
        out.close();	
	}
	//获取所有标准
	public String getStandard(){
		int f=3;
		switch(standardType){
		case 审核指南:
			f=0;
			break;
		case 评分标准:
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
	//获取所有审核指南
	public String queryByStandardType(){
		jsonList=standardService.queryByStandardType(standardType,departmentTypeSn);
		return "jsonList";
	}
	//获取所有指标
	public String queryAll(){
		String hql="select s FROM Standard s WHERE s.deleted=false AND s.departmentType.departmentTypeSn LIKE '"+departmentTypeSn+"'";
		jsonList=standardService.query(hql, 1, 10000);
		return "jsonList";
	}
	
	//根据树形菜单选中项加载标准
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
	//根据树形菜单选中项加载标准（根据自己所在部门的部门类型）
	public String queryStandard(){
		String departmentSn = (String) session.get("departmentSn");
		List<String> departmentTypeSns = departmentTypeService.getAllImplDepartmentTypesIncloudSelf(departmentSn);
		if( departmentTypeSns.size() > 0 )
			jsonList = standardService.getStandardByDepartmentTypeSns(departmentTypeSns);
		return "jsonList";
	}
}
