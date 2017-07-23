package cn.jagl.aq.action;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
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
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.Speciality;
import cn.jagl.aq.domain.UnsafeCondition;
public class UnsafeConditionReportAction extends BaseAction<InconformityItem>{
	private static final long serialVersionUID = 1L;
	private int page;
	private int rows;
	private long total;//总记录数
	private String end;
	private String xtype;
	private String begin;
	private String itemSn;
	private String xSnIndex;
	private String clickEchart;
	private String departmentSn;
	private String departmentTypeSn;
	private String departmentSnIndex;
	private String specialitySnIndex;
	private String parentDepartmentSn;
	private String inconformityLevelIndex;
	private String inconformityLevelSnIndex;
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
	public String getClickEchart() {
		return clickEchart;
	}
	public void setClickEchart(String clickEchart) {
		this.clickEchart = clickEchart;
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
	public String getParentDepartmentSn() {
		return parentDepartmentSn;
	}
	public void setParentDepartmentSn(String parentDepartmentSn) {
		this.parentDepartmentSn = parentDepartmentSn;
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
	public String getxSnIndex() {
		return xSnIndex;
	}
	public void setxSnIndex(String xSnIndex) {
		this.xSnIndex = xSnIndex;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
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
		out.print(data.toString()); 
        out.flush(); 
        out.close();  
		return SUCCESS;
	}
	//查询不符合项数目报表
	@SuppressWarnings("rawtypes")
	public String report() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out;  
        out = response.getWriter();
		JSONObject data=new JSONObject();
   	 	List<Department> departments=new ArrayList<>();
		@SuppressWarnings("unchecked")
		ArrayList<String> xName=new ArrayList();
		@SuppressWarnings("unchecked")
		ArrayList<Long> unsafeConditionCount=new ArrayList();
		@SuppressWarnings("unchecked")
		ArrayList<String> departmentSns=new ArrayList();
		@SuppressWarnings("unchecked")
		ArrayList<String> specialitySns=new ArrayList();
		@SuppressWarnings("unchecked")
		ArrayList<String> inconformityLevelSns=new ArrayList();

		ArrayList<Integer> children=new ArrayList<Integer>();
		//itemSn=0，只获取该部门编号和部门类型下的所有子部门，用来重置之后判断右键菜单
		if(itemSn.equals("0")){
	        departments=departmentService.getDepartmentsNew(departmentSnIndex, departmentTypeSn,clickEchart);
	        //departments=departmentService.getDepartments(departmentSnIndex, departmentTypeSn);
			for(Department department:departments){
				children.add(department.getChildDepartments().size());
				//unsafeConditionCount.add((long) 0);
			} 
			data.put("children",children);
			xtype="0";
		}
		//itemSn=1，代表按部门
		if(itemSn.equals("1")){
			//DepartmentType departmentType=departmentTypeService.getByDepartmentTypeSn(departmentTypeSn);
	        departments=departmentService.getDepartmentsNew(departmentSnIndex, departmentTypeSn,clickEchart);
	        String str="";
	        if(departmentTypeSn!=null && !"".equals(departmentTypeSn)){
	        	str=departmentTypeService.getDownDepartmentTypeByParent(departmentTypeSn);
	        }
			for(Department department:departments){	
				xName.add(department.getDepartmentName());
				unsafeConditionCount.add((Long)unsafeConditionService.query(department.getDepartmentSn(),str,specialitySnIndex,inconformityLevelSnIndex,begin, end));
				departmentSns.add(department.getDepartmentSn());
				children.add(department.getChildDepartments().size());
				//data.put("children", department.getChildDepartments());
			} 
			data.put("departmentSns",departmentSns);
			data.put("children",children);
			xtype="1";
		}
		//itemSn=2，代表选择专业
		if(itemSn.equals("2")){
			List<Speciality> specialitys=new ArrayList<Speciality>();
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
			//specialitys=specialityService.getSpecialitysByDepartmentTypeSn(departmentTypeSn);
			String str="";
	        if(departmentTypeSn!=null && !"".equals(departmentTypeSn)){
	        	str=departmentTypeService.getDownDepartmentTypeByParent(departmentTypeSn);
	        }
			for(Speciality speciality:specialitys){	
				xName.add(speciality.getSpecialityName());
				unsafeConditionCount.add((Long)unsafeConditionService.query(departmentSnIndex,str,speciality.getSpecialitySn(),inconformityLevelSnIndex,begin, end));
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
				unsafeConditionCount.add((Long)unsafeConditionService.query(departmentSnIndex,str,specialitySnIndex,String.valueOf(i),begin, end));
				String h=String.valueOf(i);
				inconformityLevelSns.add(h);
				children.add(departmentService.getByDepartmentSn(departmentSnIndex).getChildDepartments().size());
			}
			data.put("inconformityLevelSns",inconformityLevelSns);
			data.put("children",children);
			xtype="3";
		}
		

//		String path =ServletActionContext.getServletContext().getRealPath("/template/unsafecondition_report.xlsx"); 
//		
//		//生成excel文件供以后点击链接下载  
//        XSSFWorkbook wb = new XSSFWorkbook();   
//		String name="";
////		//动态生成表名
//        if(!itemSn.equals("0")){
//    		name = departmentSnIndex+"按部门表";
//        }
//        if(itemSn.equals("1")){
//        	name = departmentSnIndex+"按专业表";
//        }if(itemSn.equals("2")){
//        	name =departmentSnIndex+"按危险等级表";
//        } 
//		XSSFSheet sheet = wb.createSheet(name);
//        sheet.setColumnWidth(1,100*100);
//        XSSFRow row = sheet.createRow((int) 0);  
//        XSSFCellStyle style = wb.createCellStyle();  
//        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//        XSSFCell cell = row.createCell(0) ;
//        cell.setCellStyle(style);  
//        cell = row.createCell(1);  
//        cell.setCellValue("不符合项数目");  
//        cell.setCellStyle(style);	 
//        cell = row.createCell(2); 
//    
//        for (int i = 0; i < unsafeConditionCount.size(); i++)  
//        {  
//            row = sheet.createRow((int) i + 1);
//            if(!itemSn.equals("0")){
//                if(itemSn.equals("1")){
//                    cell.setCellValue("部门");  
//                    row.createCell( 0).setCellValue(departmentSns.get(i));  
//                }if(itemSn.equals("2")){
//                    cell.setCellValue("专业");  
//                    row.createCell( 0).setCellValue(specialitySns.get(i));  
//                }if(itemSn.equals("3")){
//                    cell.setCellValue("危险等级");  
//                    row.createCell( 0).setCellValue(inconformityLevelSns.get(i));  
//                }
//                row.createCell( 1).setCellValue(unsafeConditionCount.get(i));
//            }else{
//                row.createCell( 0).setCellValue("");  
//                row.createCell( 1).setCellValue("");
//            }
//        }
//        File file =new File(path);
//        FileOutputStream f= new FileOutputStream(file);
//        wb.write(f);
//        f.close();
////        ServletOutputStream sos =response.getOutputStream();
////        wb.write(sos);
////        f.close();
        
		//横坐标类型
		data.put("type",xtype);
		//横坐标名字
		data.put("departmentName",xName);
		//纵坐标
		data.put("inconformityItemCount",unsafeConditionCount);
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
        List<UnsafeCondition> list=new ArrayList<UnsafeCondition>();
		StringBuffer hqls = new StringBuffer();				
		StringBuffer hqlc = new StringBuffer();
		String str0="";
        if(departmentTypeSn!=null && !"".equals(departmentTypeSn)){
        	str0=departmentTypeService.getDownDepartmentTypeByParent(departmentTypeSn);
        	hqls.append ( "select p FROM UnsafeCondition p WHERE p.checkedDepartment.departmentSn like '"+departmentSnIndex+"%' and p.checkedDepartment.departmentType.departmentTypeSn in "+str0+"  and p.deleted=false  and p.checkDateTime>'"+begin+"' and p.checkDateTime<'"+end+"'");
        	hqlc.append ( "select Count(*) FROM UnsafeCondition p WHERE p.checkedDepartment.departmentSn like '"+departmentSnIndex+"%' and p.checkedDepartment.departmentType.departmentTypeSn in "+str0+" and p.deleted=false and p.checkDateTime>'"+begin+"' and p.checkDateTime<'"+end+"'");
        }else{
        	hqls.append ( "select p FROM UnsafeCondition p WHERE p.checkedDepartment.departmentSn like '"+departmentSnIndex+"%' and p.deleted=false  and p.checkDateTime>'"+begin+"' and p.checkDateTime<'"+end+"'");
        	hqlc.append ( "select Count(*) FROM UnsafeCondition p WHERE p.checkedDepartment.departmentSn like '"+departmentSnIndex+"%' and p.deleted=false and p.checkDateTime>'"+begin+"' and p.checkDateTime<'"+end+"'");
        }
		 if(specialitySnIndex.length()>0){
				hqls.append(" AND p.speciality.specialitySn='"+specialitySnIndex+"'");	
				hqlc.append(" AND p.speciality.specialitySn='"+specialitySnIndex+"'");	
		 }	
		if(inconformityLevelSnIndex.length()>0){			  
			hqls.append(" AND p.inconformityLevel='"+inconformityLevelSnIndex+"'");	
			hqlc.append(" AND p.inconformityLevel="+inconformityLevelSnIndex);	
		}
		hqls.append(" ORDER BY p.checkDateTime desc");	
		String hql=hqls.toString();	
		String hqlcc=hqlc.toString();			
		setTotal(unsafeConditionService.countHql(hqlcc));		
		list=unsafeConditionService.query(hql,page,rows);		
		JSONArray array=new JSONArray();
		for(UnsafeCondition unsafeCondition:list){
			JSONObject jo=new JSONObject();
						
			if(unsafeCondition.getCheckType()!=null){
				jo.put("checkType",unsafeCondition.getCheckType().toString());
			}
			if(unsafeCondition.getCheckerFrom()!=null){
				jo.put("checkerFrom",unsafeCondition.getCheckerFrom().toString());
			}
			jo.put("inconformityItemSn",unsafeCondition.getInconformityItemSn());
			if(unsafeCondition.getCheckDateTime()!=null){			
				jo.put("checkDateTime",unsafeCondition.getCheckDateTime().toString());
			}
			if(unsafeCondition.getCheckLocation()!=null){
				jo.put("checkLocation",unsafeCondition.getCheckLocation().toString());
			}
			jo.put("inconformityItemNature", unsafeCondition.getInconformityItemNature());
			if(unsafeCondition.getMachine()!=null){
				jo.put("machine", unsafeCondition.getMachine().getManageObjectName());
	  		}
			if(unsafeCondition.getCheckedDepartment()!=null){
				jo.put("dpartmentName",unsafeCondition.getCheckedDepartment().getDepartmentName());
  			  	jo.put("checkedDepartmentImplType", unsafeCondition.getCheckedDepartment().getImplDepartmentName());
			}
			if(unsafeCondition.getStandardIndex()!=null){
				jo.put("standardIndex", unsafeCondition.getStandardIndex().getIndexName());
			}
			if(unsafeCondition.getSystemAudit()!=null){
				jo.put("systemAudit",unsafeCondition.getSystemAudit().getSystemAuditType());
  		  	}
			jo.put("problemDescription",unsafeCondition.getProblemDescription());
			jo.put("deductPoints", unsafeCondition.getDeductPoints());
			jo.put("correctType", unsafeCondition.getCorrectType());
			if(unsafeCondition.getCorrectDeadline()!=null){
				jo.put("correctDeadline",unsafeCondition.getCorrectDeadline().toString());
			}
			if(unsafeCondition.getHazrd()!=null){
				jo.put("hazard",unsafeCondition.getHazrd().getHazardDescription());
			}
			if(unsafeCondition.getSpeciality()!=null){
				jo.put("specialityName",unsafeCondition.getSpeciality().getSpecialityName());
			}
			jo.put("inconformityLevel", unsafeCondition.getInconformityLevel());
			if(unsafeCondition.getCorrectPrincipal()!=null){
				jo.put("correctPrincipal",unsafeCondition.getCorrectPrincipal().getPersonName().toString());
			}
			jo.put("correctProposal",unsafeCondition.getCorrectProposal());
			jo.put("attachments",unsafeCondition.getAttachments().size());
			if(unsafeCondition.getCheckers()!=null && unsafeCondition.getCheckers().size() != 0){
				String str="";
				for(Person pe:unsafeCondition.getCheckers()){
					str+=pe.getPersonName()+";";
				}
				str=str.substring(0,str.length()-1);
				jo.put("checkers",str);
			}
			if(unsafeCondition.getHasCorrectConfirmed()!=null){
				if(unsafeCondition.getHasCorrectConfirmed()!=true){
					jo.put("hasCorrectConfirmed", "未整改确认");
				}else{
					jo.put("hasCorrectConfirmed", "已整改确认");
				}
			}
			if(unsafeCondition.getHasReviewed()!=null){
				if(unsafeCondition.getHasReviewed()!=true){
					jo.put("hasReviewed","未复查");
				}else{
					jo.put("hasReviewed","已复查");
				}
			}
			if(unsafeCondition.getHasCorrectFinished()!=null){
				if(unsafeCondition.getHasCorrectFinished()!=true){
					jo.put("hasCorrectFinished", "未整改完成");
				}else{
					jo.put("hasCorrectFinished", "已整改完成");
				}
			}
			array.put(jo);			
		}
		
		String str="{\"total\":"+total+",\"rows\":"+array+"}";
		out.print(str);
        out.flush(); 
        out.close();	
		return SUCCESS;
	}
	//在生成报表时生成文件存放于服务器上并返回下载链接
	public String exportexcel(){
   	 	List<Department> departments=new ArrayList<>();
		ArrayList<String> xName=new ArrayList<String>();
		ArrayList<Long> unsafeConditionCount=new ArrayList<Long>();
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

        List<UnsafeCondition> unsafeConditions=new ArrayList<UnsafeCondition>();
		//导出到月
		if(showtime.equals("1")||showtime.equals("2")||showtime.equals("3")){
			StringBuffer hqls = new StringBuffer();
			String str0="";
			 if(departmentTypeSn!=null && !"".equals(departmentTypeSn)){
		        	str0=departmentTypeService.getDownDepartmentTypeByParent(departmentTypeSn);
		        	hqls.append ( "select p FROM UnsafeCondition p"
		        					+ " WHERE p.checkedDepartment.departmentSn like '"+departmentSnIndex+"%'"
		        						+ " and p.checkedDepartment.departmentType.departmentTypeSn in "+str0+""
		        						+ " and p.deleted=false and p.checkDateTime>'"+begin+"'"
		        						+ " and p.checkDateTime<'"+end+"'");
		         }else{
		        	hqls.append ( "select p FROM UnsafeCondition p"
		        					+ " WHERE p.checkedDepartment.departmentSn like '"+departmentSnIndex+"%'"
		        							+ " and p.deleted=false and p.checkDateTime>'"+begin+"'"
		        							+ " and p.checkDateTime<'"+end+"'");
		         }
				 if(specialitySnIndex!=null&&specialitySnIndex.length()>0){
						hqls.append(" AND p.speciality.specialitySn='"+specialitySnIndex+"'");	
				 }	
				if(inconformityLevelSnIndex!=null&&inconformityLevelSnIndex.length()>0){			  
					hqls.append(" AND p.inconformityLevel='"+inconformityLevelSnIndex+"'");	
				}
				String hql=hqls.toString();			
				//查出此时所有的明细
				unsafeConditions=unsafeConditionService.query(hql,1,1000);
		}
		//itemSn=0，只获取该部门编号和部门类型下的所有子部门，用来重置之后判断右键菜单
		if(itemSn.equals("0")){
	        departments=departmentService.getDepartmentsNew(departmentSnIndex, departmentTypeSn,clickEchart);
			for(Department department:departments){
				children.add(department.getChildDepartments().size());
			} 
			xtype="0";
		}
		//itemSn=1，代表按部门
		if(itemSn.equals("1")){
	        departments=departmentService.getDepartmentsNew(departmentSnIndex, departmentTypeSn,clickEchart);
	        String str="";
	        if(departmentTypeSn!=null && !"".equals(departmentTypeSn) &&!departmentTypeSn.equals("null")){
	        	str=departmentTypeService.getDownDepartmentTypeByParent(departmentTypeSn);
	        }
			for(Department department:departments){	
				xName.add(department.getDepartmentName());
				unsafeConditionCount.add((Long)unsafeConditionService.query(department.getDepartmentSn(),str,specialitySnIndex,inconformityLevelSnIndex,begin, end));
				departmentSns.add(department.getDepartmentSn());
				children.add(department.getChildDepartments().size());
			} 
			xtype="1";
		}
		//itemSn=2，代表选择事故类型
		if(itemSn.equals("2")){
			List<Speciality> specialitys=new ArrayList<Speciality>();
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
				unsafeConditionCount.add((Long)unsafeConditionService.query(departmentSnIndex,str,speciality.getSpecialitySn(),inconformityLevelSnIndex,begin, end));
				specialitySns.add(speciality.getSpecialitySn());
				children.add(departmentService.getByDepartmentSn(departmentSnIndex).getChildDepartments().size());
			}
			xtype="2";
		}
		//itemSn=3，代表选择事故等级
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
				unsafeConditionCount.add((Long)unsafeConditionService.query(departmentSnIndex,str,specialitySnIndex,String.valueOf(i),begin, end));
				String h=String.valueOf(i);
				inconformityLevelSns.add(h);
				children.add(departmentService.getByDepartmentSn(departmentSnIndex).getChildDepartments().size());
			}
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
        //第一行第一个单元格
        XSSFCell cell = row.createCell(0) ;
        if(itemSn.equals("1")){
            cell.setCellValue("部门");  
        }if(itemSn.equals("2")){
            cell.setCellValue("专业");  
        }if(itemSn.equals("3")){
            cell.setCellValue("危险等级");    
        }
        cell.setCellStyle(style);  
        //第一行第二个单元格
        cell = row.createCell(1);  
        cell.setCellValue("不符合项数目");  
        cell.setCellStyle(style);
        for (int i = 0; i < unsafeConditionCount.size(); i++)  
        {  
            row = sheet.createRow((int) i + 1);
            row.createCell( 0).setCellValue(xName.get(i));
            row.createCell( 1).setCellValue(unsafeConditionCount.get(i));
        }
        if(showtime.equals("1")||showtime.equals("2")||showtime.equals("3")){
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
	        detailCell.setCellValue("机"); 
	        detailCell=detailRow.createCell(7) ;
	        detailCell.setCellValue("贯标单位"); 
	        detailCell=detailRow.createCell(8) ;
	        detailCell.setCellValue("检查部门"); 
	        detailCell=detailRow.createCell(9) ;
	        detailCell.setCellValue("指标"); 
	        detailCell=detailRow.createCell(10) ;
	        detailCell.setCellValue("所属于的审核"); 
	        detailCell=detailRow.createCell(11) ;
	        detailCell.setCellValue("问题描述"); 
	        detailCell=detailRow.createCell(12) ;
	        detailCell.setCellValue("扣分"); 
	        detailCell=detailRow.createCell(13) ;
	        detailCell.setCellValue("整改类型"); 
	        detailCell=detailRow.createCell(14) ;
	        detailCell.setCellValue("整改期限"); 
	        detailCell=detailRow.createCell(15) ;
	        detailCell.setCellValue("危险源"); 
	        detailCell=detailRow.createCell(16) ;
	        detailCell.setCellValue("专业"); 
	        detailCell=detailRow.createCell(17) ;
	        detailCell.setCellValue("不符合项等级"); 
	        detailCell=detailRow.createCell(18) ;
	        detailCell.setCellValue("整改负责人"); 
	        detailCell=detailRow.createCell(19) ;
	        detailCell.setCellValue("附件数"); 
	        detailCell=detailRow.createCell(20) ;
	        detailCell.setCellValue("整改建议"); 
	        detailCell=detailRow.createCell(21) ;
	        detailCell.setCellValue("检查类型"); 
	        detailCell=detailRow.createCell(22) ;
	        detailCell.setCellValue("检查人"); 
	        detailCell=detailRow.createCell(23) ;
	        detailCell.setCellValue("整改确认"); 
	        detailCell=detailRow.createCell(24) ;
	        detailCell.setCellValue("复查"); 
	        detailCell=detailRow.createCell(25) ;
	        detailCell.setCellValue("整改完成"); 
	        //循环生成表格内容
	        Integer countTotal = unsafeConditions.size();
	        for (int i = 0; i < countTotal; i++)  
	        {  
	        	detailRow = detailSheet.createRow((int) i + 1);
	            UnsafeCondition u=unsafeConditions.get(i);
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
				if(u.getMachine()!=null){
					detailRow.createCell( 6).setCellValue(u.getMachine().getManageObjectName());
		  		}
				if(u.getCheckedDepartment()!=null){
		           detailRow.createCell( 7).setCellValue(u.getCheckedDepartment().getImplDepartmentName());
	           	   detailRow.createCell( 8).setCellValue(u.getCheckedDepartment().getDepartmentName());
				}
				if(u.getStandardIndex()!=null){
	           	 	detailRow.createCell( 9).setCellValue(u.getStandardIndex().getIndexName());
				}
				if(u.getSystemAudit()!=null){
					if(u.getSystemAudit().getSystemAuditType()!=null){
		           	 	detailRow.createCell( 10).setCellValue(u.getSystemAudit().getSystemAuditType().toString());
					}
	  		  	}
		       	 detailRow.createCell( 11).setCellValue(u.getProblemDescription());
		    	 detailRow.createCell( 12).setCellValue(u.getDeductPoints());
				if(u.getCorrectType()!=null){
			    	 detailRow.createCell( 13).setCellValue(u.getCorrectType().toString());
				}
				if(u.getCorrectDeadline()!=null){
	           	    detailRow.createCell( 14).setCellValue(u.getCorrectDeadline().toString());
				}
				if(u.getHazrd()!=null){
	           	 	detailRow.createCell( 15).setCellValue(u.getHazrd().getHazardDescription());
				}
				if(u.getSpeciality()!=null){
	           	  detailRow.createCell( 16).setCellValue(u.getSpeciality().getSpecialityName());
				}
				if(u.getInconformityLevel()!=null){					
					detailRow.createCell( 17).setCellValue(u.getInconformityLevel().toString());
				}
				if(u.getCorrectPrincipal()!=null){
					detailRow.createCell( 18).setCellValue(u.getCorrectPrincipal().getPersonName().toString());
				}
				detailRow.createCell( 19).setCellValue(u.getAttachments().size());
				detailRow.createCell( 20).setCellValue(u.getCorrectProposal());
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
				if(u.getHasCorrectConfirmed()!=null){
					if(u.getHasCorrectConfirmed()!=true){
		            	 detailRow.createCell( 23).setCellValue("未整改确认");
					}else{
		            	 detailRow.createCell( 23).setCellValue("已整改确认");
					}
				}
				if(u.getHasReviewed()!=null){
					if(u.getHasReviewed()!=true){
		            	 detailRow.createCell( 24).setCellValue("未复查");
					}else{
		            	 detailRow.createCell( 24).setCellValue("已复查");
					}
				}
				if(u.getHasCorrectFinished()!=null){
					if(u.getHasCorrectFinished()!=true){
		            	 detailRow.createCell( 25).setCellValue("未整改完成");
					}else{
		            	 detailRow.createCell( 25).setCellValue("已整改完成");
					}
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
	private String showtime;



	public String getShowtime() {
		return showtime;
	}
	public void setShowtime(String showtime) {
		this.showtime = showtime;
	}
	
}
