package cn.jagl.aq.action;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;
import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.DepartmentType;
import cn.jagl.aq.domain.InconformityItem;
import cn.jagl.aq.domain.InconformityLevel;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.Speciality;
import cn.jagl.aq.domain.UnsafeAct;
import cn.jagl.aq.domain.UnsafeCondition;
public class UnsafeActReportAction extends BaseAction<InconformityItem>{
	private static final long serialVersionUID = 1L;
	private String departmentSn;
	private String begin;
	private String end;
	private String clickEchart;
	private String showtime;
	public String getClickEchart() {
		return clickEchart;
	}
	public void setClickEchart(String clickEchart) {
		this.clickEchart = clickEchart;
	}
	private int page;
	public HashMap<String, Object> getPager() {
		return pager;
	}
	public void setPager(HashMap<String, Object> pager) {
		this.pager = pager;
	}
	private HashMap<String, Object> pager = new HashMap<String, Object>();
	private int rows;
	private long total;//总记录数
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
	private String parentDepartmentSn;
	public String getParentDepartmentSn() {
		return parentDepartmentSn;
	}
	public void setParentDepartmentSn(String parentDepartmentSn) {
		this.parentDepartmentSn = parentDepartmentSn;
	}
	private String itemSn;
	private String xSnIndex;
	private String xtype;
	private InputStream excelStream; 
    private String excelFileName;   
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
	public String getXtype() {
		return xtype;
	}
	public void setXtype(String xtype) {
		this.xtype = xtype;
	}
	public String getBegin() {
		return begin;
	}
	public void setBegin(String begin) {
		this.begin = begin;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	private String departmentTypeSn;
	public String getDepartmentTypeSn() {
		return departmentTypeSn;
	}
	public void setDepartmentTypeSn(String departmentTypeSn) {
		this.departmentTypeSn = departmentTypeSn;
	}
	public String getDepartmentSn() {
		return departmentSn;
	}
	public void setDepartmentSn(String departmentSn) {
		this.departmentSn = departmentSn;
	}
	private String departmentSnIndex;
	private String specialitySnIndex;
	private String inconformityLevelSnIndex;
	public String getInconformityLevelSnIndex() {
		return inconformityLevelSnIndex;
	}
	public void setInconformityLevelSnIndex(String inconformityLevelSnIndex) {
		this.inconformityLevelSnIndex = inconformityLevelSnIndex;
	}
	public String getSpecialitySnIndex() {
		return specialitySnIndex;
	}
	public void setSpecialitySnIndex(String specialitySnIndex) {
		this.specialitySnIndex = specialitySnIndex;
	}
	public String getItemSn() {
		return itemSn;
	}
	public void setItemSn(String itemSn) {
		this.itemSn = itemSn;
	}
	public String getXSnIndex() {
		return xSnIndex;
	}
	public void setXSnIndex(String xSnIndex) {
		this.xSnIndex = xSnIndex;
	}
	public String getDepartmentSnIndex() {
		return departmentSnIndex;
	}
	public void setDepartmentSnIndex(String departmentSnIndex) {
		this.departmentSnIndex = departmentSnIndex;
	}
	public String getInconformityLevelIndex() {
		return inconformityLevelIndex;
	}
	public void setInconformityLevelIndex(String inconformityLevelIndex) {
		this.inconformityLevelIndex = inconformityLevelIndex;
	}
	private String inconformityLevelIndex;
	//加载部门类型
	public String type() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out;  
        out = response.getWriter();
        JSONArray data=new JSONArray();
    	 List<DepartmentType> departmentTypes=new ArrayList<>();
        departmentTypes=departmentTypeService.getImplDepartmentTypesExceptSelf(departmentSn);
		for(DepartmentType departmentType:departmentTypes){
			JSONObject jo=new JSONObject();
			jo.put("text",departmentType.getDepartmentTypeName());
			jo.put("value",departmentType.getDepartmentTypeSn());
			if(departmentType.getChildDepartmentTypes().size()>0){
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
	//查询不符合项数目报表
	public String report() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out;  
        out = response.getWriter();
	   	 List<Department> departments=new ArrayList<>();
	   	 List<Speciality> specialitys=new ArrayList<>();
		JSONObject data=new JSONObject();
		ArrayList<String> xName=new ArrayList<String>();
		ArrayList<Long> unsafeActCount=new ArrayList<Long>();
		ArrayList<String> departmentSns=new ArrayList<String>();
		ArrayList<String> specialitySns=new ArrayList<String>();
		ArrayList<String> inconformityLevelSns=new ArrayList<String>();
		ArrayList<Integer> children=new ArrayList<Integer>();
		//itemSn=0，只获取该部门编号和部门类型下的所有子部门，用来重置之后判断右键菜单
		if(itemSn.equals("0")){
	        departments=departmentService.getDepartmentsNew(departmentSnIndex, departmentTypeSn,clickEchart);
			for(Department department:departments){
				//data.put("children", department.getChildDepartments());
				children.add(department.getChildDepartments().size());
			} 
			data.put("children",children);
			xtype="0";
		}
		//itemSn=1，代表按部门
		if(itemSn.equals("1")){
	        departments=departmentService.getDepartmentsNew(departmentSnIndex, departmentTypeSn,clickEchart);
	        String str="";
	        if(departmentTypeSn!=null && !"".equals(departmentTypeSn)){
	        	str=departmentTypeService.getDownDepartmentTypeByParent(departmentTypeSn);
	        }
	        for(Department department:departments){	
				xName.add(department.getDepartmentName());
				unsafeActCount.add((Long)unsafeActService.query(department.getDepartmentSn(),str,specialitySnIndex,inconformityLevelSnIndex,begin, end));
				departmentSns.add(department.getDepartmentSn());
				children.add(department.getChildDepartments().size());
			} 
			data.put("departmentSns",departmentSns);
			data.put("children",children);
			xtype="1";
		}
		//itemSn=2，代表选择专业
		if(itemSn.equals("2")){
			specialitys=new ArrayList<Speciality>();
			List<DepartmentType> list =departmentTypeService.getImplDepartmentTypes(departmentSn);
			if(list.size()>0){
				List<Speciality> li=null;
				for(DepartmentType de:list){
					li=specialityService.getSpecialitysByDepartmentTypeSn(de.getDepartmentTypeSn());
					for(Speciality sp:li){
						if(specialitys==null||!specialitys.contains(sp)){
							specialitys.add(sp);
						}
					}
				}
			}else{
				Department dep=departmentService.getUpNearestImplDepartment(departmentSn);
				specialitys=specialityService.getSpecialitysByDepartmentTypeSn(dep.getDepartmentType().getDepartmentTypeSn());
			}
			String str="";
	        if(departmentTypeSn!=null && !"".equals(departmentTypeSn)){
	        	str=departmentTypeService.getDownDepartmentTypeByParent(departmentTypeSn);
	        }
	        for(Speciality speciality:specialitys){	
				xName.add(speciality.getSpecialityName());
				unsafeActCount.add((Long)unsafeActService.query(departmentSnIndex,str,speciality.getSpecialitySn(),inconformityLevelSnIndex,begin, end));
				specialitySns.add(speciality.getSpecialitySn());
				children.add(departmentService.getByDepartmentSn(departmentSnIndex).getChildDepartments().size());
			}
			data.put("specialitySns",specialitySns);
			data.put("children",children);
			xtype="2";
		}
		//itemSn=3，代表选择危险等级
		if(itemSn.equals("3")){
			String str="";
	        if(departmentTypeSn!=null && !"".equals(departmentTypeSn)){
	        	str=departmentTypeService.getDownDepartmentTypeByParent(departmentTypeSn);
	        }
			for(int i=0;i<3;i++){
				if(i==0){
					xName.add("A");
				}else if(i==1){
					xName.add("B");
				}else{
					xName.add("C");
				}
				unsafeActCount.add((Long)unsafeActService.query(departmentSnIndex,str,specialitySnIndex,String.valueOf(i),begin, end));
				String h=String.valueOf(i);
				inconformityLevelSns.add(h);
				children.add(departmentService.getByDepartmentSn(departmentSnIndex).getChildDepartments().size());
			}
			data.put("inconformityLevelSns",inconformityLevelSns);
			data.put("children",children);
			xtype="3";
		}
		//横坐标类型
		data.put("type",xtype);
		//横坐标名字
		data.put("departmentName",xName);
		//纵坐标
		data.put("inconformityItemCount",unsafeActCount);
		out.print(data); 
        out.flush(); 
        out.close();  
		return SUCCESS;
	}
	//查看明细
	public String detail()throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out;  
        out = response.getWriter();
        List<UnsafeAct> list=new ArrayList<UnsafeAct>();
		StringBuffer hqls = new StringBuffer();				
		StringBuffer hqlc = new StringBuffer();
//		String as=departmentService.getDownDepartmentByDepartmentType(departmentSnIndex, departmentTypeSn);
//		as=as.replaceAll("i.departmentSn", "p.checkedDepartment.departmentSn");
		String departmentTypeSns="";
		if(departmentTypeSn!=null && !"".equals(departmentTypeSn)){
			departmentTypeSns=departmentTypeService.getDownDepartmentTypeByParent(departmentTypeSn);
		}
		 hqls.append ( "select p FROM UnsafeAct p WHERE p.deleted=false and p.checkedDepartment.departmentSn like '"+departmentSnIndex+"%' and p.checkDateTime>'"+begin+"' and p.checkDateTime<'"+end+"'");
		 hqlc.append ( "select Count(*) FROM UnsafeAct p WHERE p.deleted=false and p.checkedDepartment.departmentSn like '"+departmentSnIndex+"%' and p.checkDateTime>'"+begin+"' and p.checkDateTime<'"+end+"'");
		 if(departmentTypeSn!=null && !"".equals(departmentTypeSn)){
			 hqls.append(" and p.checkedDepartment.departmentType.departmentTypeSn in "+departmentTypeSns);
			 hqlc.append(" and p.checkedDepartment.departmentType.departmentTypeSn in "+departmentTypeSns);
		 }
		 if(specialitySnIndex.length()>0){
				hqls.append(" AND p.speciality.specialitySn='"+specialitySnIndex+"'");	
				hqlc.append(" AND p.speciality.specialitySn='"+specialitySnIndex+"'");	
		 }	
		if(inconformityLevelSnIndex.length()>0){			  
			hqls.append(" AND p.inconformityLevel='"+inconformityLevelSnIndex+"'");	
			hqlc.append(" AND p.inconformityLevel="+inconformityLevelSnIndex+"'");
		}
		hqls.append(" ORDER BY p.checkDateTime desc");	
		String hql=hqls.toString();	
		String hqlcc=hqlc.toString();			
		setTotal(unsafeConditionService.countHql(hqlcc));
		list=unsafeActService.query(hql,page,rows);
		//setUnsafeActs(unsafeActService.query(hql,page,rows));		
		JSONArray array=new JSONArray();
		for(UnsafeAct unsafeAct:list){
			JSONObject jo=new JSONObject();
			
			jo.put("id", unsafeAct.getId());
			if(unsafeAct.getCheckType()!=null){
				jo.put("checkType",unsafeAct.getCheckType().toString());
			}
			if(unsafeAct.getCheckerFrom()!=null){
				jo.put("checkerFrom",unsafeAct.getCheckerFrom().toString());
			}
			jo.put("inconformityItemSn",unsafeAct.getInconformityItemSn());
			if(unsafeAct.getCheckDateTime()!=null){			
				jo.put("checkDateTime",unsafeAct.getCheckDateTime().toString());
			}
			if(unsafeAct.getCheckLocation()!=null){
				jo.put("checkLocation",unsafeAct.getCheckLocation().toString());
			}
			if(unsafeAct.getCheckedDepartment()!=null){
				jo.put("dpartmentName",unsafeAct.getCheckedDepartment().getDepartmentName());
  			  	jo.put("checkedDepartmentImplType",unsafeAct.getCheckedDepartment().getImplDepartmentName());
			}
			if(unsafeAct.getSpeciality().getSpecialityName()!=null){
				jo.put("specialityName",unsafeAct.getSpeciality() .getSpecialityName());
			}
			if(unsafeAct.getViolator()!=null){
				jo.put("violator",unsafeAct.getViolator().getPersonName());
			}
			jo.put("actDescription",unsafeAct.getActDescription());
			jo.put("unsafeActMark",unsafeAct.getUnsafeActMark());
			if(unsafeAct.getUnsafeActStandard()!=null){
				jo.put("unsafeActStandard", unsafeAct.getUnsafeActStandard().getStandardDescription());
				jo.put("UnsafeActLevel", unsafeAct.getUnsafeActStandard().getUnsafeActLevel());
			}
			if(unsafeAct.getSystemAudit()!=null){
				jo.put("systemAudit",unsafeAct.getSystemAudit().getSystemAuditType());
			}
			jo.put("specialCheck",unsafeAct.getSpecialCheck() );	
			jo.put("periodicalCheck",unsafeAct.getPeriodicalCheck() );
			//检查人
			Set<Person> set=unsafeAct.getCheckers();
			if(set.size()>0){
				  String s="";
				  for(Iterator<Person> iter=set.iterator();iter.hasNext();){
					  s+=iter.next().getPersonName()+",";
				  }
				  s=s.substring(0, s.lastIndexOf(","));
				  jo.put("checkers", s);
			}

			jo.put("attachments", unsafeAct.getAttachments().size());
			array.put(jo);			
		}
		
		String str="{\"total\":"+total+",\"rows\":"+array+"}";
		out.print(str);
        out.flush(); 
        out.close();	
		return SUCCESS;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	//在生成报表时生成文件存放于服务器上并返回下载链接
		public String exportexcel(){
			 List<Department> departments=new ArrayList<>();
		   	 List<Speciality> specialitys=new ArrayList<>();
			JSONObject data=new JSONObject();
			ArrayList<String> xName=new ArrayList<String>();
			ArrayList<Long> unsafeActCount=new ArrayList<Long>();
			ArrayList<String> departmentSns=new ArrayList<String>();
			ArrayList<String> specialitySns=new ArrayList<String>();
			ArrayList<String> inconformityLevelSns=new ArrayList<String>();
			ArrayList<Integer> children=new ArrayList<Integer>();
			if(departmentSnIndex.equals("null")){
				departmentSnIndex=null;
			}
			if(departmentTypeSn.equals("null")){
				departmentTypeSn=null;
			}
			if(specialitySnIndex.equals("null")){
				specialitySnIndex=null;
			}
			if(inconformityLevelSnIndex.equals("null")){
				inconformityLevelSnIndex=null;
			}
			List<UnsafeAct> unsafeActs=new ArrayList<UnsafeAct>();
			//如果选择的是日报，将此时包含的所有不符合项列出
			if(showtime.equals("1")){
				StringBuffer hqls = new StringBuffer();
				String departmentTypeSns="";
				if(departmentTypeSn!=null && !"".equals(departmentTypeSn)){
					departmentTypeSns=departmentTypeService.getDownDepartmentTypeByParent(departmentTypeSn);
				}
				 hqls.append ( "select p FROM UnsafeAct p WHERE p.deleted=false and p.checkedDepartment.departmentSn like '"+departmentSnIndex+"%' and p.checkDateTime>'"+begin+"' and p.checkDateTime<'"+end+"'");
				 if(departmentTypeSn!=null && !"".equals(departmentTypeSn)){
					 hqls.append(" and p.checkedDepartment.departmentType.departmentTypeSn in "+departmentTypeSns);
				 }
				 if(specialitySnIndex!=null&&specialitySnIndex.length()>0){
						hqls.append(" AND p.speciality.specialitySn='"+specialitySnIndex+"'");	
				 }	
				if(inconformityLevelSnIndex!=null&&inconformityLevelSnIndex.length()>0){			  
					hqls.append(" AND p.inconformityLevel='"+inconformityLevelSnIndex+"'");	
				}		
				//查出此时所有的明细
				unsafeActs=unsafeActService.query(hqls.toString(), 1, 10000);
			}
			//itemSn=0，只获取该部门编号和部门类型下的所有子部门，用来重置之后判断右键菜单
			if(itemSn.equals("0")){
		        departments=departmentService.getDepartmentsNew(departmentSnIndex, departmentTypeSn,clickEchart);
				for(Department department:departments){
					//data.put("children", department.getChildDepartments());
					children.add(department.getChildDepartments().size());
				} 
				data.put("children",children);
				xtype="0";
			}
			//itemSn=1，代表按部门
			if(itemSn.equals("1")){
		        departments=departmentService.getDepartmentsNew(departmentSnIndex, departmentTypeSn,clickEchart);
		        String str="";
		        if(departmentTypeSn!=null && !"".equals(departmentTypeSn)){
		        	str=departmentTypeService.getDownDepartmentTypeByParent(departmentTypeSn);
		        }
		        for(Department department:departments){	
					xName.add(department.getDepartmentName());
					unsafeActCount.add((Long)unsafeActService.query(department.getDepartmentSn(),str,specialitySnIndex,inconformityLevelSnIndex,begin, end));
					departmentSns.add(department.getDepartmentSn());
					children.add(department.getChildDepartments().size());
				} 
				data.put("departmentSns",departmentSns);
				data.put("children",children);
				xtype="1";
			}
			//itemSn=2，代表选择专业
			if(itemSn.equals("2")){
				specialitys=new ArrayList<Speciality>();
				List<DepartmentType> list =departmentTypeService.getImplDepartmentTypes(departmentSn);
				if(list.size()>0){
					List<Speciality> li=null;
					for(DepartmentType de:list){
						li=specialityService.getSpecialitysByDepartmentTypeSn(de.getDepartmentTypeSn());
						for(Speciality sp:li){
							if(specialitys==null||!specialitys.contains(sp)){
								specialitys.add(sp);
							}
						}
					}
				}else{
					Department dep=departmentService.getUpNearestImplDepartment(departmentSn);
					specialitys=specialityService.getSpecialitysByDepartmentTypeSn(dep.getDepartmentType().getDepartmentTypeSn());
				}
				String str="";
		        if(departmentTypeSn!=null && !"".equals(departmentTypeSn)){
		        	str=departmentTypeService.getDownDepartmentTypeByParent(departmentTypeSn);
		        }
		        for(Speciality speciality:specialitys){	
					xName.add(speciality.getSpecialityName());
					unsafeActCount.add((Long)unsafeActService.query(departmentSnIndex,str,speciality.getSpecialitySn(),inconformityLevelSnIndex,begin, end));
					specialitySns.add(speciality.getSpecialitySn());
					children.add(departmentService.getByDepartmentSn(departmentSnIndex).getChildDepartments().size());
				}
				data.put("specialitySns",specialitySns);
				data.put("children",children);
				xtype="2";
			}
			//itemSn=3，代表选择危险等级
			if(itemSn.equals("3")){
				String str="";
		        if(departmentTypeSn!=null && !"".equals(departmentTypeSn)){
		        	str=departmentTypeService.getDownDepartmentTypeByParent(departmentTypeSn);
		        }
				for(int i=0;i<3;i++){
					if(i==0){
						xName.add("观察项");
					}else if(i==1){
						xName.add("一般不符合项");
					}else{
						xName.add("严重不符合项");
					}
					unsafeActCount.add((Long)unsafeActService.query(departmentSnIndex,str,specialitySnIndex,String.valueOf(i),begin, end));
					String h=String.valueOf(i);
					inconformityLevelSns.add(h);
					children.add(departmentService.getByDepartmentSn(departmentSnIndex).getChildDepartments().size());
				}
				data.put("inconformityLevelSns",inconformityLevelSns);
				data.put("children",children);
				xtype="3";
			}
			
			XSSFWorkbook wb = new XSSFWorkbook(); 
			XSSFSheet sheet = null;
			String name="";
			//部门编号不可能为空
			String departmentName=departmentService.getByDepartmentSn(departmentSnIndex).getDepartmentName();
			//动态生成表名
	        if(!itemSn.equals("0")){
	        	if(itemSn.equals("1")){
	        		name=departmentName+"按部门表";
	        	}
	            if(itemSn.equals("2")){
	            	name=departmentName+"按专业表";
	            }if(itemSn.equals("3")){
	            	name=departmentName+"按危险等级表";
	            }
	        }else{
	        	name=departmentName+"";
	        }
    		sheet = wb.createSheet(name);
	        sheet.setColumnWidth(1,100*100);
	        XSSFRow row = sheet.createRow((int) 0);  
	        XSSFCellStyle style = wb.createCellStyle();  
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        XSSFCell cell = row.createCell(0) ;
	        if(itemSn.equals("1")){
	            cell.setCellValue("部门");  
	        }if(itemSn.equals("2")){
	            cell.setCellValue("专业");  
	        }if(itemSn.equals("3")){
	            cell.setCellValue("危险等级");    
	        }
	        cell.setCellStyle(style);  
	        cell = row.createCell(1);  
	        cell.setCellValue("不符合项数目");  
	        cell.setCellStyle(style);
	    
	        for (int i = 0; i < unsafeActCount.size(); i++)  
	        {  
	            row = sheet.createRow((int) i + 1);
	            row.createCell( 0).setCellValue(xName.get(i));
	            row.createCell( 1).setCellValue(unsafeActCount.get(i));
	        }
	        
	        if(showtime.equals("1")){
		        //明细汇总表
				XSSFSheet detailSheet = null;
				detailSheet = wb.createSheet(name+"明细");
				detailSheet.setColumnWidth(1,100*100);
		        XSSFRow detailRow = detailSheet.createRow((int) 0);  
		        XSSFCellStyle detailStyle = wb.createCellStyle();  
		        detailStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		        //第一行 标题       
		        XSSFCell detailCell = detailRow.createCell(0) ;
		        detailCell.setCellValue("检查类型");  
		        detailCell=detailRow.createCell(1) ;
		        detailCell.setCellValue("检查形式");  
		        detailCell=detailRow.createCell(2) ;
		        detailCell.setCellValue("不符合项编号"); 
		        detailCell=detailRow.createCell(3) ;
		        detailCell.setCellValue("检查日期"); 
		        detailCell=detailRow.createCell(4) ;
		        detailCell.setCellValue("检查地点"); 
		        detailCell=detailRow.createCell(5) ;
		        detailCell.setCellValue("不符合项性质"); 
		        detailCell=detailRow.createCell(6) ;
		        detailCell.setCellValue("不安全行为人员"); 
		        detailCell=detailRow.createCell(7) ;
		        detailCell.setCellValue("行为描述"); 
		        detailCell=detailRow.createCell(8) ;
		        detailCell.setCellValue("痕迹"); 
		        detailCell=detailRow.createCell(9) ;
		        detailCell.setCellValue("不安全行为标准"); 
		        detailCell=detailRow.createCell(10) ;
		        detailCell.setCellValue("所属于的审核"); 
		        detailCell=detailRow.createCell(16) ;
		        detailCell.setCellValue("专业"); 
		        detailCell=detailRow.createCell(17) ;
		        detailCell.setCellValue("不符合项等级"); 
		        detailCell=detailRow.createCell(19) ;
		        detailCell.setCellValue("附件数"); 
		        detailCell=detailRow.createCell(21) ;
		        detailCell.setCellValue("检查类型"); 
		        detailCell=detailRow.createCell(22) ;
		        detailCell.setCellValue("检查人"); 
		        //循环生成表格内容
		        for (int i = 0; i < unsafeActs.size(); i++)  
		        {  
		        	detailRow = detailSheet.createRow((int) i + 1);
		            UnsafeAct u=unsafeActs.get(i);
		            if(u.getCheckType()!=null){
		            	 detailRow.createCell( 0).setCellValue(u.getCheckType().toString());
					}
					if(u.getCheckerFrom()!=null){
						detailRow.createCell( 1).setCellValue(u.getCheckerFrom().toString());
					}
		        	detailRow.createCell( 2).setCellValue(u.getInconformityItemSn());
					if(u.getCheckDateTime()!=null){			
		           	   detailRow.createCell( 3).setCellValue(u.getCheckDateTime().toString());
					}
		           	  detailRow.createCell( 4).setCellValue(u.getCheckLocation());
		           	  if(u.getInconformityItemNature()!=null){
		               	 detailRow.createCell( 5).setCellValue(u.getInconformityItemNature().toString());
		           	  }
		           	  if(u.getViolator()!=null){
							detailRow.createCell( 6).setCellValue(u.getViolator().getPersonName());
				  	  }
				      detailRow.createCell( 7).setCellValue(u.getActDescription());
				      if(u.getUnsafeActMark()!=null){
			           	 	detailRow.createCell( 8).setCellValue(u.getUnsafeActMark().toString());
				      } 
				      if(u.getUnsafeActStandard()!=null){
					      detailRow.createCell( 9).setCellValue(u.getUnsafeActStandard().getStandardDescription());
				      }
					if(u.getSystemAudit()!=null){
						if(u.getSystemAudit().getSystemAuditType()!=null){
			           	 	detailRow.createCell( 10).setCellValue(u.getSystemAudit().getSystemAuditType().toString());
						}
		  		  	}
					if(u.getSpeciality()!=null){
		           	  detailRow.createCell( 16).setCellValue(u.getSpeciality().getSpecialityName());
					}
					if(u.getInconformityLevel()!=null){
			       	    detailRow.createCell( 17).setCellValue(u.getInconformityLevel().toString());
					}
					detailRow.createCell( 19).setCellValue(u.getAttachments().size());
					if(u.getPeriodicalCheck()!=null){
		           	 	detailRow.createCell( 21).setCellValue(u.getCheckType().toString());
					}
					if(u.getCheckers()!=null && u.getCheckers().size()>0){
						String str="";
						for(Person pe:u.getCheckers()){
							str+=pe.getPersonName()+";";
						}
						str=str.substring(0,str.length()-1);
		           	 	detailRow.createCell( 22).setCellValue(str);
					}
		        }
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
	            //动态生成文件名
	            excelFileName =URLEncoder.encode(name+".xlsx", "UTF-8");
	        }  
	        catch (Exception e)  
	        {  
	            e.printStackTrace();  
	        }
			return "download";
		}
		public String getShowtime() {
			return showtime;
		}
		public void setShowtime(String showtime) {
			this.showtime = showtime;
		}
	}
