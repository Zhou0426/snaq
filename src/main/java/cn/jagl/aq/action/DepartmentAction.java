 package cn.jagl.aq.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.hibernate.exception.ConstraintViolationException;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.DepartmentType;
import cn.jagl.aq.domain.Person;
import cn.jagl.util.RegExUtil;

public class DepartmentAction extends BaseAction<Department>
{
	private static final long serialVersionUID = 1L;
	private String parentDepartmentSn;
	private String pdsn;
	private String resourceSn;//权限编号
	private String departmentSn;
	private Integer updateId;
	private String departmentName;
	private String departmentTypeSn;
	private Boolean deleted;
	private Integer showSequence;
	private String dutyman;
	private Class<Department> department;
	private String id;
	private long total;//总记录数
	private Integer rows;//每页显示的记录数
	private Integer page;//当前页数
	private HashMap<String, Object> pager = new HashMap<String, Object>();
	private String jsonStrDepartments;
	private String pdeptSn;
	private String principal;
	private String parentDepartmentSn1;
	private String data;
	private InputStream excelStream; 
    private String excelFileName;   
    private String deleteornot;
    private Integer changeId;
	private String standardIndexSn;//指标编号
	private String	standardSn;
	private net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
    
	
	public String getStandardIndexSn() {
		return standardIndexSn;
	}
	public void setStandardIndexSn(String standardIndexSn) {
		this.standardIndexSn = standardIndexSn;
	}
	public String getStandardSn() {
		return standardSn;
	}
	public void setStandardSn(String standardSn) {
		this.standardSn = standardSn;
	}
	public net.sf.json.JSONObject getJsonObject() {
		return jsonObject;
	}
	public void setJsonObject(net.sf.json.JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}
	public Integer getChangeId() {
		return changeId;
	}
	public void setChangeId(Integer changeId) {
		this.changeId = changeId;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getResourceSn() {
		return resourceSn;
	}
	public void setResourceSn(String resourceSn) {
		this.resourceSn = resourceSn;
	}
	public String getPdsn() {
		return pdsn;
	}
	public void setPdsn(String pdsn) {
		this.pdsn = pdsn;
	}
	public Integer getUpdateId() {
		return updateId;
	}
	public void setUpdateId(Integer updateId) {
		this.updateId = updateId;
	}
	public String getParentDepartmentSn1() {
		return parentDepartmentSn1;
	}
	public void setParentDepartmentSn1(String parentDepartmentSn1) {
		this.parentDepartmentSn1 = parentDepartmentSn1;
	}
	
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public String getPdeptSn() {
		return pdeptSn;
	}
	public void setPdeptSn(String pdeptSn) {
		this.pdeptSn = pdeptSn;
	}
	public String getJsonStrDepartments() {
		return jsonStrDepartments;
	}
	public void setJsonStrDepartments(String jsonStrDepartments) {
		this.jsonStrDepartments = jsonStrDepartments;
	}
	public HashMap<String, Object> getPager() {
		return pager;
	}
	public void setPager(HashMap<String, Object> pager) {
		this.pager = pager;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public String getParentDepartmentSn() {
		return parentDepartmentSn;
	}
	public void setParentDepartmentSn(String parentDepartmentSn) {
		this.parentDepartmentSn = parentDepartmentSn;
	}
	public String getDepartmentTypeSn() {
		return departmentTypeSn;
	}
	public void setDepartmentTypeSn(String departmentTypeSn) {
		this.departmentTypeSn = departmentTypeSn;
	}
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	public Integer getShowSequence() {
		return showSequence;
	}
	public void setShowSequence(Integer showSequence) {
		this.showSequence = showSequence;
	}
	public String getDepartmentSn() {
		return departmentSn;
	}
	public void setDepartmentSn(String departmentSn) {
		this.departmentSn = departmentSn;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}	
	public String getDutyman() {
		return dutyman;
	}
	public void setDutyman(String dutyman) {
		this.dutyman = dutyman;
	}
	public Class<Department> getDepartment() {
		return department;
	}
	public void setDepartment(Class<Department> department) {
		this.department = department;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
    public InputStream getExcelStream() {  
        return excelStream;  
    }  
    public void setExcelStream(InputStream excelStream) {  
        this.excelStream = excelStream;  
    }  
    public String getExcelFileName() {  
        return excelFileName;  
    }  
    public void setExcelFileName(String excelFileName) {  
        this.excelFileName = excelFileName;  
    }
	public String getExcelContentType() {
		return excelContentType;
	}
	public void setExcelContentType(String excelContentType) {
		this.excelContentType = excelContentType;
	}
	//增加
	public String add(){
		try{
			departmentSn=String.valueOf(parentDepartmentSn)+String.valueOf(departmentSn);
			//寻找部门 包括已删除
			Department findDepartment=departmentService.getByDepartmentSn(departmentSn);
			if(findDepartment!=null){
				data="添加重复！该部门编号已存在";
				return SUCCESS;
			}else{
				Department department=new Department();	
				department.setDepartmentSn(departmentSn);
				department.setDepartmentName(departmentName);
				department.setShowSequence(showSequence);
				Department parentDepartment=departmentService.getByDepartmentSn(parentDepartmentSn);
				department.setParentDepartment(parentDepartment);
				DepartmentType departmentType=departmentTypeService.getByDepartmentTypeSn(departmentTypeSn);
				department.setDepartmentType(departmentType);
				Person person=personService.getByPersonId(principal);
				department.setDeleted(false);	
				department.setPrincipal(person);
				departmentService.addDepartment(department);
				data="添加成功！";
			}
		}catch(Exception e){
			data="添加异常，请联系开发人员！";			
		}
		return SUCCESS;
	}
	//更新部门
	public String update() throws IOException{
		try{
			//根据id获得部门
			Department department=departmentService.getById(updateId);
			Department parentDepartment=departmentService.getByDepartmentSn(parentDepartmentSn);
			department.setParentDepartment(parentDepartment);
			department.setDepartmentName(departmentName);
			DepartmentType departmentType=departmentTypeService.getByDepartmentTypeSn(departmentTypeSn);
			department.setDepartmentType(departmentType);
			department.setShowSequence(showSequence);
			Person person=personService.getByPersonId(principal);
			department.setPrincipal(person);
			departmentService.updateDepartment(department);
			data="修改成功";
		}
		catch(Exception e){
			data="修改异常，请联系开发人员！";
		}
		return SUCCESS;	
	}
	//删除部门
	public String delete()
	{
		try{
			//通过id获得部门并进行逻辑删除
			Department department=departmentService.getById(updateId);
			department.setDeleted(true);
			departmentService.updateDepartment(department);
			data="删除成功！";
		}catch(Exception e){
			data="删除失败！";
		}
		return SUCCESS;
	}
	//取消删除部门
	public String cancelDelete(){
		try{
			Department department = departmentService.getById(changeId);
			department.setDeleted(false);
			departmentService.updateDepartment(department);
			data="恢复成功！";
		}catch(Exception e){
			data="恢复失败！";
		}
		return SUCCESS;
	}
	//输出指定字段
	public PrintWriter out()throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out= response.getWriter();
        return out;
	}
	private String  excelContentType;
	
	//查询集团处室的部门与子部门
	public String queryDepartmentByDepartmentTypeJTCS() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out = response.getWriter();
        String hql="";
    	List<Department> departments=new ArrayList<Department>();
        if(id != null && id.trim().length() > 0){
        	departments=departmentService.getDepartmentsByParentDepartmentSn(id);
        }else{
        	String personId=(String) session.get("personId");
        	if(personId!=null){
        		hql = "FROM Department d where d.departmentType.departmentTypeSn= 'JTCS' order by d.showSequence ASC";
        		departments=departmentService.findByPage(hql,1,10000);
        	}
        }		
		JSONArray data=new JSONArray();
		for(Department department:departments){
			JSONObject jo=new JSONObject();
			jo.put("id",department.getDepartmentSn());
			jo.put("text",department.getDepartmentName());
			if(department.getChildDepartments().size()>0){
				jo.put("state","closed");
			}
			else{
				jo.put("state","open");
			}			
			data.put(jo);
		}
		out.println(data.toString()); 
        out.flush(); 
        out.close();  
		return SUCCESS;
	}
	
	//根据部门编号加载数据
	public String show() throws IOException{
		JSONArray array=new JSONArray();	
		StringBuffer h=new StringBuffer();
		StringBuffer q=new StringBuffer();	
		List<Department> departments=new ArrayList<Department>();
		if (parentDepartmentSn1!=null){
			h.append("FROM Department d where d.parentDepartment.departmentSn='"+parentDepartmentSn1+"'");
			q.append("select count(*) FROM Department d where d.parentDepartment.departmentSn='"+parentDepartmentSn1+"'");
		}
		else
		{
			h.append( "FROM Department d where d.parentDepartment is null");
			q.append( "select count(*) FROM Department d where d.parentDepartment is null");
		}
		if(deleteornot!=null && deleteornot.equals("0")){
			h.append(" and d.deleted=0");
			q.append(" and d.deleted=0");
		}
		departments=departmentService.findByPage(h.toString(), page, rows);
		total=departmentService.countHql(q.toString());
		for(Department de:departments){
			 JSONObject jo=new JSONObject();
			 //字符型
			 jo.put("id", de.getId());
			 jo.put("departmentSn",de.getDepartmentSn());
			 jo.put("departmentName",de.getDepartmentName());
			 jo.put("deleted",de.getDeleted());
			 if(de.getDepartmentType()!=null){
				 jo.put("departmentType",de.getDepartmentType().getDepartmentTypeName()); 
				 jo.put("departmentTypeSn",de.getDepartmentType().getDepartmentTypeSn());
			 }
			 else{
				 jo.put("departmentType",""); 
				 jo.put("departmentTypeSn","");
			 }
					
			 jo.put("showSequence", de.getShowSequence());
			 if(de.getPrincipal()!=null){
				 jo.put("principalSn", de.getPrincipal().getPersonId());
				 jo.put("principal", de.getPrincipal().getPersonName());
			 }
			 else{
				 jo.put("principalSn", "");
				 jo.put("principal", "");
			 }
			 array.put(jo);
		}
		data="{\"total\":"+total+",\"rows\":"+array+"}";		
		return SUCCESS;
	}
	//加载树形菜单
	public String tree() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out;  
        out = response.getWriter();
        String hql="";
    	List<Department> departments=new ArrayList<Department>();
        if(id!=null&&id.trim().length()>0){
        	departments=departmentService.getDepartmentsByParentDepartmentSn(id);
        }
        else{
        	String personId=(String) session.get("personId");
        	if(personId!=null){
        		Person person=personService.getByPersonId(personId);
        		departmentSn=person.getDepartment().getDepartmentSn();
        		List<DepartmentType> departmentTypes=departmentTypeService.getImplDepartmentTypes(departmentSn);
        		if(departmentTypes.size()>0){
        			hql = "FROM Department d where d.departmentSn= '"+departmentSn+"' order by d.showSequence ASC";
        		}else{
        			
        			int roleType = (int) session.get("roleType");
        			
        			if(roleType == 1)
        			{
        				departmentSn = departmentService.getUpNerestFgs(departmentSn)
        						.getDepartmentSn();
        			}else{
        				departmentSn = departmentService.getUpNearestImplDepartment(departmentSn).getDepartmentSn();
        			}
        			hql = "FROM Department d where d.departmentSn= '" + departmentSn + "'"
        					+ " order by d.showSequence ASC";
        			
        		}
        		departments=departmentService.findByPage(hql,1,10000);
        	}
        }		
		JSONArray data=new JSONArray();
		for(Department department:departments){
			JSONObject jo=new JSONObject();
			jo.put("id",department.getDepartmentSn());
			jo.put("text",department.getDepartmentName());
			if(department.getChildDepartments().size()>0){
				jo.put("state","closed");
			}
			else{
				jo.put("state","open");
			}			
			data.put(jo);
		}
		out.println(data.toString()); 
        out.flush(); 
        out.close();  
		return SUCCESS;
	}
	//加载树形菜单--第二种根据角色类型来获取部门
	public String treeAll() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out= response.getWriter();
        List<Department> list=new ArrayList<Department>();
        if(id!=null&&id.trim().length()>0){
        	list=departmentService.getDepartmentsByParentDepartmentSnNotDeleted(id);
        }
        else{
        	String personId=(String) session.get("personId");
        	if(personId!=null){
        		list=personService.getResourcePermissionScopeNotDeleted(personId, resourceSn);
        	}
        }		
		JSONArray tree=new JSONArray();
		for(Department department:list){
			JSONObject jo=new JSONObject();
			jo.put("id",department.getDepartmentSn());
			jo.put("text",department.getDepartmentName());
			if(department.getChildDepartments().size()>0){
				jo.put("state","closed");
			}
			else{
				jo.put("state","open");
			}			
			tree.put(jo);
		}
		out.println(tree.toString()); 
        out.flush(); 
        out.close();  
		return SUCCESS;
	}
	//加载树形菜单--第二种根据角色类型来获取部门 包含已删除
	public String treeAllIncludeDeleted() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out= response.getWriter();
        List<Department> list=new ArrayList<Department>();
        if(id!=null&&id.trim().length()>0){
        	list=departmentService.getDepartmentsByParentDepartmentSn(id);
        }
        else{
        	String personId=(String) session.get("personId");
        	if(personId!=null){
        		list=personService.getResourcePermissionScope(personId, resourceSn);
        	}
        }		
		JSONArray tree=new JSONArray();
		for(Department department:list){
			JSONObject jo=new JSONObject();
			jo.put("id",department.getDepartmentSn());
			jo.put("text",department.getDepartmentName());
			if(department.getChildDepartments().size()>0){
				jo.put("state","closed");
			}
			else{
				jo.put("state","open");
			}			
			tree.put(jo);
		}
		out.println(tree.toString()); 
        out.flush(); 
        out.close();  
		return SUCCESS;
	}
	List<DepartmentType> departmentTypes;
	public List<DepartmentType> getDepartmentTypes() {
		return departmentTypes;
	}
	public void setDepartmentTypes(List<DepartmentType> departmentTypes) {
		this.departmentTypes = departmentTypes;
	}
		//加载部门类型的树形菜单
		public String treeDT() throws IOException{
			HttpServletResponse response=ServletActionContext.getResponse();  
	        response.setContentType("text/html");  
	        response.setContentType("text/plain; charset=utf-8");
	        PrintWriter out;  
	        out = response.getWriter();
	        String hql="";
	        if(id!=null&&id.trim().length()>0){
	        	departmentTypes=departmentTypeService.getDepartmentsByParentDepartmentTypeSn(id);
	        }
	        else{
	        	hql = "FROM DepartmentType d where d.parentDepartmentType is null";
	        	departmentTypes=departmentTypeService.findByPage(hql,1,10000);
	        }		
			JSONArray treeDT=new JSONArray();
			for(DepartmentType departmentType:departmentTypes){
				JSONObject jo=new JSONObject();
				jo.put("id",departmentType.getDepartmentTypeSn());
				jo.put("text",departmentType.getDepartmentTypeName());
				if(departmentType.getChildDepartmentTypes().size()>0){
					jo.put("state","closed");
				}
				else{
					jo.put("state","open");
				}
					
				treeDT.put(jo);
			}
			out.println(treeDT.toString()); 
	        out.flush(); 
	        out.close();  
			return SUCCESS;
		}
		//加载子级部门类型
		public String queryDepartmentTypeByDepartmentSn() throws IOException{
			HttpServletResponse response=ServletActionContext.getResponse();  
	        response.setContentType("text/html");  
	        response.setContentType("text/plain; charset=utf-8");
	        PrintWriter out;  
	        out = response.getWriter();
	        DepartmentType parentDepartmentType = departmentTypeService.getDepartmentSn(departmentSn);
			JSONArray treeDT=new JSONArray();
			for(DepartmentType departmentType : parentDepartmentType.getChildDepartmentTypes()){
				JSONObject jo=new JSONObject();
				jo.put("id",departmentType.getDepartmentTypeSn());
				jo.put("text",departmentType.getDepartmentTypeName());
				treeDT.put(jo);
			}
			out.println(treeDT.toString()); 
	        out.flush(); 
	        out.close();
			return SUCCESS;
		}
		//poi导出成excel
		public String export(){  
			XSSFWorkbook wb = new XSSFWorkbook();   
			XSSFSheet sheet = wb.createSheet("部门表");
			List<Department> departments=new ArrayList<Department>();

	        sheet.setColumnWidth(1,100*100);
	        XSSFRow row = sheet.createRow((int) 0);  
	        XSSFCellStyle style = wb.createCellStyle();  
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        XSSFCell cell = row.createCell(0) ;
	        cell.setCellValue("部门编号");  
	        cell.setCellStyle(style);  
	        cell = row.createCell(1);  
	        cell.setCellValue("部门名称");  
	        cell.setCellStyle(style);	 
	        cell = row.createCell(2);  
	        cell.setCellValue("部门类型编号");  
	        cell.setCellStyle(style);	       
	        cell = row.createCell(3);  
	        cell.setCellValue("部门类型名称"); 
	        cell.setCellStyle(style);	        
	        cell = row.createCell(4);  
	        cell.setCellValue("父级部门编号");  
	        cell.setCellStyle(style);
	        cell = row.createCell(5);  
	        cell.setCellValue("父级部门名称");  
	        cell.setCellStyle(style);
	        cell = row.createCell(6);  
	        cell.setCellValue("显示顺序");  
	        cell.setCellStyle(style);
	    
	        departments= departmentService.getAllDepartments();
	        for (int i = 0; i < departments.size(); i++)  
	        {  
	            row = sheet.createRow((int) i + 1);  
	            Department department = (Department) departments.get(i);  
	            row.createCell( 0).setCellValue(department.getDepartmentSn());  
	            row.createCell( 1).setCellValue(department.getDepartmentName());
	            if(department.getDepartmentType()!=null){
	            	row.createCell( 2).setCellValue( department.getDepartmentType().getDepartmentTypeSn()); 
	            	row.createCell( 3).setCellValue( department.getDepartmentType().getDepartmentTypeName());
	            }
	            if(department.getParentDepartment()!=null){
	            	row.createCell( 4).setCellValue( department.getParentDepartment().getDepartmentSn()); 
	            	row.createCell( 5).setCellValue( department.getParentDepartment().getDepartmentName());
	            }
	            row.createCell( 6).setCellValue( department.getShowSequence());
	        }
	        try  
	        {  
	        	ByteArrayOutputStream fout = new ByteArrayOutputStream();  
	            wb.write(fout);
	            wb.close();
	            fout.close();
	            byte[] fileContent = fout.toByteArray();  
	            ByteArrayInputStream is = new ByteArrayInputStream(fileContent);  
	  
	            excelStream = is;               
	            excelFileName =URLEncoder.encode("部门表.xlsx", "UTF-8");
	        }  
	        catch (Exception e)  
	        {  
	            e.printStackTrace();  
	        }
	        return "download";
	    }
		//导入部门
		@SuppressWarnings({ "resource", "unused" })
		public String importDepartment()  throws IOException{
			InputStream is0 = new FileInputStream(excel); 
		  	XSSFWorkbook wb = new XSSFWorkbook(is0);
			Department department=null;
			HashMap<String, String> record = new HashMap<String, String>();
	        float num=0f;
			String isExist="第";
			String isError="第";
			String nullData="第";
			String notFindDepartmentType="第";
			String notFindPDepartment="第";
			String dsn2="第";//部门编号只能为两位数字
			String snError="第";
            String dept="";
            String pdept="";
            int s=0;
			// 循环工作表Sheet
	        for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
	            XSSFSheet xssfSheet = wb.getSheetAt(numSheet);
	            if (xssfSheet == null) {
	                continue;
	            }
	            num=xssfSheet.getLastRowNum();
	            // 循环行Row
	            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
		            session.put("progressValue", (int)((float)rowNum*100/num));
	                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
	                if (xssfRow == null) {
	                	//nullData+=(rowNum+1)+",";
	                    continue;
	                }
	                department = new Department();
	                try{
	                	//部门编号
		                XSSFCell departmentSn = xssfRow.getCell(0);
		                if (departmentSn == null || departmentSn.toString().trim().length() == 0) {
		                	nullData+=(rowNum+1)+",";
		                    continue;
		                }
		                if(departmentSn.getCellType()==Cell.CELL_TYPE_STRING){
		                	 dept=departmentSn.toString();
		                }
		                if(departmentSn.getCellType()==Cell.CELL_TYPE_NUMERIC){
		                	BigDecimal bd = new BigDecimal(departmentSn.getNumericCellValue());  
		                	dept=bd.toPlainString();
		                }
		                if(record.containsKey(dept)){
		                	isExist+=(rowNum+1)+",";
		                	continue;
		                }
		                else{
		                	record.put(dept,String.valueOf(rowNum+1));
		                }
		                department.setDepartmentSn(dept);
		                //父级部门
		                XSSFCell pdesn = xssfRow.getCell(4);
		                if(pdesn==null|| pdesn.toString().trim().length() == 0){
		                	if(dept.equals("1")){
		                		   department.setParentDepartment(null);
		                	}else{
		                		nullData+=(rowNum+1)+",";
			                	continue;
		                	}
		                }else{
		                	if(pdesn.getCellType()==Cell.CELL_TYPE_STRING){
			                	 pdept=pdesn.toString();
			                }
			                if(pdesn.getCellType()==Cell.CELL_TYPE_NUMERIC){
			                	BigDecimal bd = new BigDecimal(pdesn.getNumericCellValue());  
			                	pdept=bd.toPlainString();
			                }
			                //判断父级编号是否包含子部门编号之内
			                if(dept.indexOf(pdept)!=0){
			                	snError+=(rowNum+1)+",";
			                	continue;
			                }
			                //判断部门编号去除父部门编号后是否是数字
			                if (RegExUtil.isNumber(dept.replace(pdept, ""))==false) {		                	 
			                	dsn2+=(rowNum+1)+",";
			                	continue;
			                }
			                Department pde=departmentService.getByDepartmentSn(pdept);
			                if(pde==null){
			                	notFindPDepartment+=(rowNum+1)+",";
			                	continue;
			                }
			                department.setParentDepartment(pde);
		                }
//		                if (pdesn == null || pdesn.toString().trim().length() == 0) {
//		                	nullData+=(rowNum+1)+",";
//		                	continue;
//		                }		                
		                
		                //部门名称
		                XSSFCell departmentName = xssfRow.getCell(1);
		                if (departmentName == null || departmentName.toString().trim().length() == 0) {
		                	nullData+=(rowNum+1)+",";
		                    continue;
		                }
		                department.setDepartmentName(departmentName.getStringCellValue());
		                //部门类型
		                XSSFCell departmentTypeSn = xssfRow.getCell(2);
		                if (departmentTypeSn == null || departmentTypeSn.toString().trim().length() == 0) {
		                	nullData+=(rowNum+1)+",";
		                    continue;
		                }
		                DepartmentType departmentType=departmentTypeService.getByDepartmentTypeSn(departmentTypeSn.toString());
		                if(departmentType==null){
		                	notFindDepartmentType+=(rowNum+1)+",";
		                	continue;
		                }
		                else
		                {
		                	department.setDepartmentType(departmentType);
		                }
		                //显示顺序
		                XSSFCell showSequence = xssfRow.getCell(6);
		                if (showSequence == null || showSequence.toString().trim().length() == 0) {
		                	nullData+=(rowNum+1)+",";
		                    continue;
		                }
		                if(showSequence.getCellType()==Cell.CELL_TYPE_STRING){
		                	 s=Integer.valueOf(showSequence.getStringCellValue());
		                }
		                if(showSequence.getCellType()==Cell.CELL_TYPE_NUMERIC){
		                	BigDecimal bd = new BigDecimal(showSequence.getNumericCellValue());  
		                	String str=bd.toPlainString();
		                	s = Integer.valueOf(str);
		                }
		               // department.setShowSequence((int)showSequence.getNumericCellValue());
		                department.setShowSequence(s);
		             }
	                catch(Exception e){
		            	isError+=(rowNum+1)+",";
	                	continue;
	                }
	                try{
	                	try
	                	{
	                		department.setDeleted(false);
	                		departmentService.addDepartment(department);
	                	}
	                	catch(ConstraintViolationException e)
	                	{
	                		XSSFCell departmentSn = xssfRow.getCell(0);
			                if(departmentSn.getCellType()==Cell.CELL_TYPE_STRING){
			                	dept=departmentSn.toString();
			                }
			                if(departmentSn.getCellType()==Cell.CELL_TYPE_NUMERIC){
			                	BigDecimal bd = new BigDecimal(departmentSn.getNumericCellValue());  
			                	dept=bd.toPlainString();
			                }
	                		Department de=departmentService.getByDepartmentSn(dept);
	                		de.setDepartmentName(xssfRow.getCell(1).getStringCellValue());
	                		DepartmentType departmentType=departmentTypeService.getByDepartmentTypeSn(xssfRow.getCell(2).getStringCellValue());
	                		de.setDepartmentType(departmentType);
	                		XSSFCell pdepartmentSn = xssfRow.getCell(4);
	                		if(dept.equals("1")){
	                			de.setParentDepartment(null);
	                		}else{
				                if(pdepartmentSn.getCellType()==Cell.CELL_TYPE_STRING){
				                	pdept=pdepartmentSn.toString();
				                }
				                if(pdepartmentSn.getCellType()==Cell.CELL_TYPE_NUMERIC){
				                	BigDecimal bd = new BigDecimal(pdepartmentSn.getNumericCellValue());  
				                	pdept=bd.toPlainString();
				                }
					            Department pde=departmentService.getByDepartmentSn(pdept);
					            de.setParentDepartment(pde);
	                		}
				            XSSFCell showSequence = xssfRow.getCell(6);
			                if(showSequence.getCellType()==Cell.CELL_TYPE_STRING){
			                	 s=Integer.valueOf(showSequence.getStringCellValue());
			                }
			                if(showSequence.getCellType()==Cell.CELL_TYPE_NUMERIC){
			                	BigDecimal bd = new BigDecimal(showSequence.getNumericCellValue());  
			                	String str=bd.toPlainString();
			                }
			                de.setShowSequence(s);
	                		//de.setShowSequence((int)(xssfRow.getCell(6).getNumericCellValue()));
	                		department.setDeleted(false);
	                		departmentService.updateDepartment(de);		                
	                	}
	                }
	                catch(Exception e){
	                	isError+=(rowNum+1)+",";
	                }
	            }
	        }
	        data="";
	        String[] number;
	        if(!"第".equals(nullData)){
	        	nullData=nullData.substring(0, nullData.length()-1) + "行有空数据！" + "<br />";
	        	data+=nullData;
	        	number=nullData.split(",");
	        	data=data+"共"+number.length+"行"+ "<br /><br />";
	        }
	        if(!"第".equals(isExist)){
	        	isExist=isExist.substring(0, isExist.length()-1) + "行是重复编号，插入失败！" + "<br />";
	        	data+=isExist;
	        	number=isExist.split(",");
	        	data=data+"共"+number.length+"行"+ "<br /><br />";
	        }
	        if(!"第".equals(notFindDepartmentType)){
	        	notFindDepartmentType=notFindDepartmentType.substring(0, notFindDepartmentType.length()-1) + "行部门类型编号不存在！" + "<br />";
	        	data+=notFindDepartmentType;
	        	number=notFindDepartmentType.split(",");
	        	data=data+"共"+number.length+"行"+ "<br /><br />";
	        }
	        if(!"第".equals(notFindPDepartment)){
	        	notFindPDepartment=notFindPDepartment.substring(0, notFindPDepartment.length()-1) + "行父部门编号不存在！" + "<br />";
	        	data+=notFindPDepartment;
	        	number=notFindPDepartment.split(",");
	        	data=data+"共"+number.length+"行"+ "<br /><br />";
	        }
	        if(!"第".equals(isError)){
	        	isError=isError.substring(0, isError.length()-1) + "行导入异常，请检查格式！" + "<br />";
	        	data+=isError;
	        	number=isError.split(",");
	        	data=data+"共"+number.length+"行"+ "<br /><br />";
	        }
	        if(!"第".equals(dsn2)){
	        	dsn2=dsn2.substring(0, dsn2.length()-1) + "行部门编号存在非数字，请检查！" + "<br />";
	        	data+=dsn2;
	        	number=dsn2.split(",");
	        	data=data+"共"+number.length+"行"+ "<br /><br />";
	        }
	        if(!"第".equals(snError)){
	        	snError=snError.substring(0, snError.length()-1) + "行部门编号和父部门编号不匹配，请检查！" + "<br />";
	        	data+=snError;
	        	number=snError.split(",");
	        	data=data+"共"+number.length+"行"+ "<br /><br />";
	        }
	        if(data.equals("")){
	        	data="导入数据成功！";
	        }else{
	        	data+="其他数据导入成功！";
	        }
		    session.put("progressValue",0);
		    return SUCCESS;
		}
		private File excel;
		
		public File getExcel() {
			return excel;
		}
		public void setExcel(File excel) {
			this.excel = excel;
		}
		//导入模板下载
		public String download() throws IOException{  
			String path =ServletActionContext.getServletContext().getRealPath("/template/departments.xlsx");
			InputStream is0 = new FileInputStream(path); 
		  	XSSFWorkbook wb = new XSSFWorkbook(is0);	        
	        try  
	        {  
	        	ByteArrayOutputStream fout = new ByteArrayOutputStream();  
	            wb.write(fout);
	            wb.close();
	            fout.close();
	            byte[] fileContent = fout.toByteArray();  
	            ByteArrayInputStream is = new ByteArrayInputStream(fileContent);  
	  
	            excelStream = is;               
	            excelFileName=URLEncoder.encode("部门导入模板.xlsx","UTF-8");
	        }  
	        catch (Exception e)  
	        {  
	            e.printStackTrace();  
	        }
	        return "download";
	    }
		public String getDeleteornot() {
			return deleteornot;
		}
		public void setDeleteornot(String deleteornot) {
			this.deleteornot = deleteornot;
		}
		/**
		 * 根据指标和部门类型查询部门，和指标在部门所发生的次数
		 * @return
		 */
		public String queryDepartmentStandardIndex(){
			jsonObject = departmentService.queryDepartmentStandardIndex(standardSn,
					standardIndexSn, page, rows);
			return "jsonObject";
		}
}  

