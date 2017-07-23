package cn.jagl.aq.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.struts2.ServletActionContext;
import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.DepartmentType;
import cn.jagl.aq.domain.Hazard;
import cn.jagl.aq.domain.InconformityItem;
import cn.jagl.aq.domain.ManageObject;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.Speciality;
import cn.jagl.aq.domain.Standard;
import cn.jagl.aq.domain.StandardIndex;
import cn.jagl.aq.domain.StandardIndexAuditMethod;
import cn.jagl.aq.domain.StandardType;
import cn.jagl.aq.domain.UnsafeActStandard;
import cn.jagl.util.RegExUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class InconformityItemAction extends BaseAction<InconformityItem> {
	private static final long serialVersionUID = 1L;
	private String id;
	private String departmentSn;
	private boolean advancedSearch;
	private String q;
	private int indexId;
	private String standardSn;
	private String standardType;
	private String text;
	private String newValue;
	private String oldValue;
	private String isAnImp;
	private String violatorId;//不安全行为人员
	private JSONArray json=new JSONArray();
	private JSONObject jsonjo=new JSONObject();
	private String year;
	private String month;
	private String departmentTypeSn;
	private String type;
	private String value; 
	private InputStream excelStream; 
    private String excelFileName;
	private String title;
	private String excelContentType;
	
	
	public String getUpNearestImplDepartment(){
		String dSn=(String) session.get("departmentSn");
		List<DepartmentType> departmentTypes=departmentTypeService.getImplDepartmentTypes(dSn);
		Department department=null;
    	if(departmentTypes.size()>0){
    		department=departmentService.getByDepartmentSn(dSn);
    	}else{
    		department=departmentService.getUpNearestImplDepartment(dSn);
    	}
    	JSONObject jo=new JSONObject();
		jo.put("departmentSn", department.getDepartmentSn());
		jo.put("departmentName", department.getDepartmentName());
		jo.put("departmentTypeSn", department.getDepartmentSn());
		json.add(jo);
		return "jsonList";
	}
	//根据部门编号或取专业
	public String queryByDepartmentSn(){
		List<DepartmentType> departmentTypes=departmentTypeService.getImplDepartmentTypes(departmentSn);
		Department department;
    	if(departmentTypes.size()>0){
    		department=departmentService.getByDepartmentSn(departmentSn);
    	}else{
    		department=departmentService.getUpNearestImplDepartment(departmentSn);
    	}
    	if(department.getDepartmentType()!=null){
    		for(Speciality speciality:specialityService.getSpecialitysByDepartmentTypeSn(department.getDepartmentType().getDepartmentTypeSn())){
        		JSONObject jo=new JSONObject();
        		jo.put("specialitySn", speciality.getSpecialitySn());
        		jo.put("specialityName", speciality.getSpecialityName());
        		json.add(jo);
        	}
    	}   	
		return "jsonList";
	}
	//区队考核导出
	public String export(){
		JSONArray array=JSONArray.fromObject(text);
		HSSFWorkbook wb = new HSSFWorkbook();   
        HSSFSheet sheet = wb.createSheet("处室考核");
        HSSFRow row;
        HSSFCell cell;
        HSSFRow row1 = sheet.createRow(0);
        HSSFRow row2 = sheet.createRow(1);
        HSSFRow row3 = sheet.createRow(2);
        //数据样式
        HSSFCellStyle style = wb.createCellStyle(); 
		HSSFFont font = wb.createFont();
		font.setFontName("仿宋_GB2312");
		font.setFontHeightInPoints((short)12);
		style.setFont(font);         
		style.setAlignment(CellStyle.ALIGN_CENTER);//水平居中  
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中 
		for(int i=0;i<17;i++){
			sheet.setDefaultColumnStyle(i, style);
		}
		sheet.setColumnWidth(0, 70*70);
        //部门名称
        cell= row1.createCell(0);
        cell.setCellValue("部门名称");
        //sheet.addMergedRegion(new CellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol)
        sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 0));
        //隐患
        cell= row1.createCell(1);
        cell.setCellValue("隐患");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 12));
        //不安全行为
        cell=row1.createCell(13);
        cell.setCellValue("不安全行为");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 13, 16));
        
        cell=row2.createCell(13);
        cell.setCellValue("A类");
        sheet.addMergedRegion(new CellRangeAddress(1, 2, 13, 13));
        
        cell=row2.createCell(14);
        cell.setCellValue("B类");
        sheet.addMergedRegion(new CellRangeAddress(1, 2, 14, 14));
        
        cell=row2.createCell(15);
        cell.setCellValue("C类");
        sheet.addMergedRegion(new CellRangeAddress(1, 2, 15, 15));
        
        cell=row2.createCell(16);
        cell.setCellValue("合计");
        sheet.addMergedRegion(new CellRangeAddress(1, 2, 16, 16));
        //隐患下单元格
        String name[]={"观察项","一般不符合项","严重不符合项","合计"};
        for(int i=0;i<4;i++){
        	cell=row2.createCell(3*i+1);
        	cell.setCellValue(name[i]);
        	sheet.addMergedRegion(new CellRangeAddress(1, 1, 3*i+1, 3*i+3));
        	//设置总数，超期瞒报
        	cell=row3.createCell(3*i+1);
        	cell.setCellValue("总数");
        	cell=row3.createCell(3*i+2);
        	cell.setCellValue("超期");
        	cell=row3.createCell(3*i+3);
        	cell.setCellValue("瞒报");
        }
        //循环插入数据
        for(int i=0;i<array.size();i++){
        	row=sheet.createRow(i+3);
        	JSONObject jo=array.getJSONObject(i);
        	cell=row.createCell(0);
        	cell.setCellValue(jo.getString("departmentName"));
        	//隐患
        	for(int x=0;x<4;x++){
        		for(int y=1;y<4;y++){
        			cell=row.createCell(3*x+y);
        			cell.setCellValue(jo.getInt(x+"s"+y));
        		}
        	}
        	//不安全行为
        	for(int x=0;x<4;x++){
        		cell=row.createCell(13+x);
        		cell.setCellValue(jo.getInt(String.valueOf(x)));
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
            excelFileName =URLEncoder.encode(title+".xls", "UTF-8"); 	      
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }
		return "export";
	}
	//区队考核查询详情
	public String exportDetail(){
		if(type!=null && type.length()>0){
			excelStream=inconformityItemService.exportUnsafeCondition(departmentSn, year, month,type, value);
			try {
				excelFileName=URLEncoder.encode("隐患导出表.xls","UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}else{
			excelStream=inconformityItemService.exportUnsafeAct(departmentSn, year, month, value);
			try {
				excelFileName=URLEncoder.encode("不安全行为导出表.xls","UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return "export";
	}
	//区队考核导出详情
	public String queryDetail(){
		if(type!=null && type.length()>0){
			jsonjo=inconformityItemService.queryUnsafeCondition(departmentSn, year, month,type, value, page, rows);
		}else{
			jsonjo=inconformityItemService.queryUnsafeAct(departmentSn, year, month, value, page, rows);
		}
		return "pageModel";
	}
	
	//区队考核查询个数
	public String query(){
		json=inconformityItemService.query(departmentSn, departmentTypeSn, year, month);
		return "jsonList";
	}
	//区队考核根据部门编号查询该部门下所有的贯标单位
	public String queryDepartment(){
		for(Department department:departmentService.queryDepartment(departmentSn)){
			JSONObject jo=new JSONObject();
			jo.put("departmentSn", department.getDepartmentSn());
			jo.put("departmentName", department.getDepartmentName());
			json.add(jo);
		}
		return "jsonList";
	}
	//区队考核查询贯标单位下的所有类型
	public String queryAllDepartmentType(){
		List<Object> jsonLoad=inconformityItemService.queryAllDepartmentType(departmentSn);
		for(Object o:jsonLoad){
			net.sf.json.JSONObject jo=new net.sf.json.JSONObject();
			@SuppressWarnings("unchecked")
			Map<Object, Object> m = (Map<Object, Object>)o;
			jo.put("departmentTypeSn", m.get("departmentTypeSn"));
			jo.put("departmentTypeName", m.get("departmentTypeName"));
			json.add(jo);
		}
		return "jsonList";
	}
	
	//获取安全负责人
	public void queryPrincipal() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out;  
        out = response.getWriter();
		Department department=departmentService.getByDepartmentSn(departmentSn.trim());
		Person person=department.getPrincipal();
		JSONObject jo=new JSONObject();
		if(person!=null){			
			jo.put("personId", person.getPersonId());
			jo.put("personName", person.getPersonName());
			jo.put("isnull", false);
		}else{
			jo.put("isnull", true);
		}
		out.print(jo.toString());
        out.flush(); 
        out.close();
	}
	//判断是否是同一贯标单位
	public String isAnImpl(){
		isAnImp="false";
		try{
			Standard standard1=departmentService.getImplStandards(newValue, StandardType.评分标准);
			Standard standard2=departmentService.getImplStandards(oldValue, StandardType.评分标准);
			if(standard1!=null&&standard2!=null&&standard1.getStandardSn()==standard2.getStandardSn()){
				isAnImp="true";
			}
		}catch(Exception e){
			isAnImp="false";
		}
		return "isAnImpl";
	}
	//获取机器危险源
	public String getMachine() throws IOException{
        List<ManageObject> jsonLoad=new ArrayList<ManageObject>();
        String hql="";
        if(departmentSn!=null&&departmentSn.trim().length()>0){
        	Department department=departmentService.getUpNearestImplDepartment(departmentSn);
        	String departmentTypeSn="";
        	if(department!=null){
        		departmentTypeSn=department.getDepartmentType().getDepartmentTypeSn();
        	}
        	if (id!=null){
    			hql = "FROM ManageObject m where m.parent.manageObjectSn='"+id+"'"+" AND m.manageObjectType LIKE '机' and m.departmentType.departmentTypeSn='"+departmentTypeSn+"'";
    		}else{
    			hql = "FROM ManageObject m where m.parent is null AND m.manageObjectType LIKE '机' and m.departmentType.departmentTypeSn='"+departmentTypeSn+"'";
    		}
            jsonLoad=manageObjectService.findByPage(hql, 1, 10000);
            //JSONArray tree=new JSONArray();
            for(ManageObject manageObject:jsonLoad){
            	JSONObject jo=new JSONObject();
            	jo.put("id", manageObject.getManageObjectSn());
            	jo.put("text", manageObject.getManageObjectName());
            	jo.put("value", manageObject.getManageObjectSn());
            	if(manageObject.getChildren().size()>0){
    				jo.put("state","closed");
    			}else{
    				jo.put("state", "open");
    			}
    			json.add(jo);
            }
    		return "jsonList";
        }else{
        	return "jsonList";
        }
        
	}
	//获取部门类型
	public String getDepartment(){
        List<Department> jsonLoad=new ArrayList<Department>();
        String hql="";
        if(id!=null&&id.trim().length()>0){
        	hql="FROM Department p where p.deleted=false and p.parentDepartment.departmentSn='"+id+"' order by p.showSequence";
        	jsonLoad=departmentService.findByPage(hql, 1, 10000);
        	for(Department department:jsonLoad){
    			JSONObject jo=new JSONObject();
    			jo.put("id",department.getDepartmentSn());
    			jo.put("text",department.getDepartmentName());
    			Person person=department.getPrincipal();
    			
    			if(person!=null){
    				jo.put("personId",person.getPersonId());
    				jo.put("personName",person.getPersonName());
    			}
    			
    			if(department.getChildDepartments().size()>0){
    				jo.put("state","closed");
    			}
    			else{
    				jo.put("state","open");
    			}
    				
    			json.add(jo);
    		} 
        }else{
        	//hql = "FROM Department d where d.parentDepartment is null";
        	hql = "FROM Department d where d.deleted=false and d.departmentSn='"+departmentSn+"'";
        	jsonLoad=departmentService.findByPage(hql, 1, 10000);
        	if(jsonLoad!=null && jsonLoad.size()==1){
        		for(Department department:jsonLoad){
        			JSONObject jo=new JSONObject();
        			jo.put("id",department.getDepartmentSn());
        			jo.put("text",department.getDepartmentName());
        			jo.put("state","open");
        			Person person=department.getPrincipal();
        			if(person!=null){
        				jo.put("personId",person.getPersonId());
        				jo.put("personName",person.getPersonName());
        			}
        			hql = "FROM Department d where d.deleted=false and d.parentDepartment.departmentSn='"+department.getDepartmentSn()+"'";	
        			JSONArray children=new JSONArray();
        			for(Department child:departmentService.findByPage(hql, 1, 10000)){
        				JSONObject jo2=new JSONObject();
            			jo2.put("id",child.getDepartmentSn());
            			jo2.put("text",child.getDepartmentName());
            			Person person2=child.getPrincipal();
            			if(person2!=null){
            				jo2.put("personId",person2.getPersonId());
            				jo2.put("personName",person2.getPersonName());
            			}
            			if(child.getChildDepartments().size()>0){
            				jo2.put("state","closed");
            			}
            			else{
            				jo2.put("state","open");
            			}
            			children.add(jo2);
        			}
        			jo.put("children", children);
        			json.add(jo);
        		} 
        	}else{
        		for(Department department:jsonLoad){
        			JSONObject jo=new JSONObject();
        			jo.put("id",department.getDepartmentSn());
        			jo.put("text",department.getDepartmentName());
        			Person person=department.getPrincipal();
        			if(person!=null){
        				jo.put("personId",person.getPersonId());
        				jo.put("personName",person.getPersonName());
        			}
        			if(department.getChildDepartments().size()>0){
        				jo.put("state","closed");
        			}
        			else{
        				jo.put("state","open");
        			}
        				
        			json.add(jo);
        		} 
        	}
        }	
        
		//JSONArray tree=new JSONArray();
		
		return "jsonList";
	}
	//搜索人员
	public String getPerson(){
    	String hql="FROM Person p where (p.personName LIKE '%"+q+"%' OR p.personId LIKE '%"+q+"%')";
        if(advancedSearch==true){
        	Person person=personService.getByPersonId(q);
        	JSONObject jo=new JSONObject();
        	jo.put("id",person.getId());
        	jo.put("personId",person.getPersonId());
        	jo.put("personName",person.getPersonName());
        	json.add(jo);
        }else{
        	if(departmentSn!=null){
            	hql+=" AND p.department.departmentSn LIKE '"+departmentSn+"%'";
            }
        	List<Person> jsonLoad=new ArrayList<Person>();        
            jsonLoad=personService.findByPage(hql, 1, 30);
            for(Person person:jsonLoad){
           	JSONObject jo=new JSONObject();
               jo.put("id",person.getId());
               jo.put("personId", person.getPersonId());
               jo.put("personName",person.getPersonName());
               jo.put("gender", person.getGender());
               json.add(jo);             	
            }
        }
		return "jsonList";
	}
	//根据ids获取人员
	public String getPersonByIds(){
        List<Person> jsonLoad=new ArrayList<Person>();
        jsonLoad=personService.getByPersonIds(ids);
        for(Person person:jsonLoad){
        	JSONObject jo=new JSONObject();
        	jo.put("id",person.getId());
        	jo.put("personId", person.getPersonId());
        	jo.put("personName",person.getPersonName());
        	jo.put("gender", person.getGender());
        	json.add(jo);
        }
		return "jsonList";
	}
	//查询指标树
	public String indexTree() throws IOException{
		List<StandardIndex> jsonLoad=new ArrayList<StandardIndex>();
		String hql="";
        if(standardSn!=null&&standardSn.trim().length()>0){
        	if(id!=null&&id.trim().length()>0){
    			hql="select s FROM StandardIndex s where s.deleted=false and s.parent.indexSn='"+id+"' order by showSequence";
    			jsonLoad=standardindexService.getPart(hql);
            }else{
            	hql = "select s from StandardIndex s WHERE s.deleted=false AND s.parent is null AND s.standard.standardSn='"+standardSn+"'  order by showSequence";
            	jsonLoad=standardindexService.getPart(hql);          	
            }	
        }else{
        	if(id!=null&&id.trim().length()>0){
    			hql="select s FROM StandardIndex s where s.deleted=false and s.parent.indexSn='"+id+"'  order by showSequence";
    			jsonLoad=standardindexService.getPart(hql);
            }else{
            	Standard standard=departmentService.getImplStandards(departmentSn, StandardType.评分标准);
            	if(standard!=null){
            		hql = "select s from StandardIndex s WHERE s.deleted=false AND s.parent is null AND s.standard.standardSn='"+standard.getStandardSn()+"'  order by showSequence";
            		jsonLoad=standardindexService.getPart(hql);
            	}
            	
            }	
        }
		
		for(StandardIndex standardIndex:jsonLoad){
			JSONObject jo=new JSONObject();
			jo.put("id", standardIndex.getIndexSn());
			String iSn=standardIndex.getIndexSn();
			String text="";
			if(standardIndex.getIndexName()!=null){
				if(iSn.split("-").length==2){
		        	text=iSn.split("-")[1]+standardIndex.getIndexName();
			    }else{
			    	text=iSn+standardIndex.getIndexName();
				}
			}else{
				if(iSn.split("-").length==2){
		        	text=iSn.split("-")[1];
			    }else{
			    	text=iSn;
				}				
			}
			jo.put("text", text);
			jo.put("indexId", standardIndex.getId());
			jo.put("isKeyIndex", standardIndex.getIsKeyIndex());
			if(standardIndex.getChildren().size()>0){
				jo.put("state","closed");
			}else{
				jo.put("state", "open");
			}
			//专业
			JSONObject sjo=new JSONObject();
        	int i=0;
        	String specialitySn="";
        	for(Speciality speciality:standardIndex.getSpecialities()){
        		i++;
        		specialitySn+=speciality.getSpecialitySn()+",";
        	}
        	if(i>0){
        		specialitySn=specialitySn.substring(0,specialitySn.length()-1);
        		sjo.put("specialitySn", specialitySn);
        	}
        	sjo.put("num",i);
        	jo.put("speciality",sjo);
			json.add(jo);
		}
		return "jsonList";
	}
	//根据部门类型和输入字符查询指标
	public String queryStandard(){
    	List<StandardIndex> jsonLoad=new ArrayList<StandardIndex>();
    	Standard standard=departmentService.queryStandardByDepartmentType(departmentTypeSn, StandardType.评分标准);
    	if(standard != null){
    		standardSn=standard.getStandardSn(); 	
			jsonLoad=standardindexService.queryByQ(q,standardSn,"评分标准");
    		for(StandardIndex standardIndex:jsonLoad){
    			JSONObject jo=new JSONObject();
    			jo.put("id",standardIndex.getId());
    			jo.put("indexSn", standardIndex.getIndexSn());
    			jo.put("indexName", standardIndex.getIndexName());
    			jo.put("isKeyIndex", standardIndex.getIsKeyIndex());
    			if(standardIndex.getStandard()!=null){
    				jo.put("standardName", standardIndex.getStandard().getStandardName());
    			}else{
    				jo.put("standardName","");
    			} 
    			JSONObject sjo=new JSONObject();
    			int i=0;
    			String specialitySn="";
    			for(Speciality speciality:standardIndex.getSpecialities()){
    				i++;
    				specialitySn+=speciality.getSpecialitySn()+",";
    			}
    			if(i>0){
    				specialitySn=specialitySn.substring(0,specialitySn.length()-1);
    				sjo.put("specialitySn", specialitySn);
    			}
    			sjo.put("num",i);
    			jo.put("speciality",sjo);
    			
    			
    			int j=0;
    			String methodIds="";
    			JSONObject mjo=new JSONObject();
    			for(StandardIndexAuditMethod standardIndexAuditMethod:standardIndex.getAuditMethods()){
    				j++;
    				methodIds+=standardIndexAuditMethod.getId()+",";
    			}
    			if(j>0){
    				methodIds=methodIds.substring(0,methodIds.length()-1);
    				mjo.put("methodIds", methodIds);
    			}           	
    			mjo.put("num", j);     
    			jo.put("auditMethod",mjo);
    			json.add(jo);
    		}
    	}
		return "jsonList";
	}
	//查询相关指标
	public String getStandardIndex(){    
        if(advancedSearch==true){
        	StandardIndex standardIndex=standardindexService.getById(Integer.parseInt(q));
        	JSONObject jo=new JSONObject();
        	jo.put("id",standardIndex.getId());
        	jo.put("indexSn", standardIndex.getIndexSn());
        	if(standardIndex.getIndexName()!=null){
        		jo.put("indexName", standardIndex.getIndexName());
        	}else{
        		jo.put("indexName", "");
        	}
        	
        	jo.put("isKeyIndex",standardIndex.getIsKeyIndex());
        	if(standardIndex.getStandard()!=null){
        		jo.put("standardName", standardIndex.getStandard().getStandardName());
        	}else{
        		jo.put("standardName","");
        	}
        	json.add(jo);
        }else{
        	List<StandardIndex> jsonLoad=new ArrayList<StandardIndex>();
        	if(departmentSn!=null && departmentSn.length()>0){
        		Standard standard=departmentService.getImplStandards(departmentSn, StandardType.评分标准);
        		if(standard!=null){
        			standardSn=standard.getStandardSn();
        		}
        	}        	
        	if(q!=null&&q.trim().length()>0){
        		if(RegExUtil.isSn(q)){
        			jsonLoad=standardindexService.queryByQ(q,standardSn,"评分标准");
        		}else{
        			jsonLoad=standardindexService.queryByQ(q+text,standardSn,"评分标准");
        		}
        	}else{
        		jsonLoad=standardindexService.queryByQ(text,standardSn,"评分标准");
        	}       	       	        	
        	for(StandardIndex standardIndex:jsonLoad){
        		JSONObject jo=new JSONObject();
            	jo.put("id",standardIndex.getId());
            	jo.put("indexSn", standardIndex.getIndexSn());
            	jo.put("indexName", standardIndex.getIndexName());
            	jo.put("isKeyIndex", standardIndex.getIsKeyIndex());
            	if(standardIndex.getStandard()!=null){
            		jo.put("standardName", standardIndex.getStandard().getStandardName());
            	}else{
            		jo.put("standardName","");
            	} 
            	JSONObject sjo=new JSONObject();
            	int i=0;
            	String specialitySn="";
            	for(Speciality speciality:standardIndex.getSpecialities()){
            		i++;
            		specialitySn+=speciality.getSpecialitySn()+",";
            	}
            	if(i>0){
            		specialitySn=specialitySn.substring(0,specialitySn.length()-1);
            		sjo.put("specialitySn", specialitySn);
            	}
            	sjo.put("num",i);
            	jo.put("speciality",sjo);
            	
            	
            	int j=0;
            	String methodIds="";
            	JSONObject mjo=new JSONObject();
            	for(StandardIndexAuditMethod standardIndexAuditMethod:standardIndex.getAuditMethods()){
            		j++;
            		methodIds+=standardIndexAuditMethod.getId()+",";
            	}
            	if(j>0){
            		methodIds=methodIds.substring(0,methodIds.length()-1);
            		mjo.put("methodIds", methodIds);
            	}           	
            	mjo.put("num", j);     
            	jo.put("auditMethod",mjo);
            	json.add(jo);
        	}
        }
		return "jsonList";
	}
	//获取危险源
	public String getHazard(){
		StandardIndex standardIndex=standardindexService.getById(indexId);
		Set<Hazard> set=standardIndex.getHazards();
		for(Hazard hazard:set){
			JSONObject jo=new JSONObject();
			jo.put("id", hazard.getId());
			jo.put("hazardSn", hazard.getHazardSn());
			jo.put("hazardDescription",hazard.getHazardDescription());
			jo.put("riskLevel",hazard.getRiskLevel());
			json.add(jo);
		}
		return "jsonList";
	}
	//根据Ids获取专业
	public String getSpecialityByIds(){
		for(Speciality speciality:specialityService.getByIds(ids)){
			JSONObject jo=new JSONObject();
			jo.put("specialitySn",speciality.getSpecialitySn());
			jo.put("specialityName",speciality.getSpecialityName());
			json.add(jo);
		}
		return "jsonList";
	}		
	//查询所有不安全行为标准
	public String findAllStandard() throws IOException{
        //不安全行为标准
		Person person=personService.getByPersonId(violatorId);
		Department de=departmentService.getUpNearestImplDepartment(person.getDepartment().getDepartmentSn());
		if(de != null){
			String hql="";
			if(id!=null && id.trim().length()>0){
				hql="select u from UnsafeActStandard u where u.departmentType.departmentTypeSn='"+de.getDepartmentType().getDepartmentTypeSn()+"' and u.parent.standardSn='"+id+"'";
			}else{
				hql="select u from UnsafeActStandard u where u.departmentType.departmentTypeSn='"+de.getDepartmentType().getDepartmentTypeSn()+"' and u.parent is null";
			}
			List<UnsafeActStandard> unsafeActStandardList=unsafeActStandardService.getByHql(hql);
			for(UnsafeActStandard unsafeActStandard:unsafeActStandardList){
				JSONObject jo=new JSONObject();
				jo.put("id", unsafeActStandard.getStandardSn());
				jo.put("text", unsafeActStandard.getStandardDescription());
				jo.put("level", unsafeActStandard.getUnsafeActLevel());
				Set<Speciality> set=unsafeActStandard.getSpecialities();
				if(set.size()>0){
					String a="";
					for(Speciality spec:set){
						a+=spec.getSpecialitySn()+",";
					}
					jo.put("speciality", a);
				}else{
					jo.put("speciality", "");
				}
				if(unsafeActStandard.getChildren().size()>0){
					jo.put("state","closed");
				}else{
					jo.put("state", "open");
				}
				json.add(jo);
			}
		}
		return "jsonList";
	}
	//再次请求等级和专业
	public String questLevel(){
		//不安全行为标准
		String hql="";
		if(id!=null && id.trim().length()>0){
			hql="select u from UnsafeActStandard u where u.standardSn='"+id+"'";
			List<UnsafeActStandard> unsafeActStandardList=unsafeActStandardService.getByHql(hql);
			for(UnsafeActStandard unsafeActStandard:unsafeActStandardList){
				JSONObject jo=new JSONObject();
				jo.put("level", unsafeActStandard.getUnsafeActLevel());
				Set<Speciality> set=unsafeActStandard.getSpecialities();
				if(set.size()>0){
					String a="";
					for(Speciality spec:set){
						a+=spec.getSpecialitySn()+",";
					}
					jo.put("speciality", a);
				}else{
					jo.put("speciality", "");
				}
				json.add(jo);
			}
		}
		return "jsonList";
	}
	public String getViolatorId() {
		return violatorId;
	}
	public void setViolatorId(String violatorId) {
		this.violatorId = violatorId;
	}
	public String getIsAnImp() {
		return isAnImp;
	}
	public void setIsAnImp(String isAnImp) {
		this.isAnImp = isAnImp;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	public String getOldValue() {
		return oldValue;
	}
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getStandardType() {
		return standardType;
	}
	public void setStandardType(String standardType) {
		this.standardType = standardType;
	}
	public String getStandardSn() {
		return standardSn;
	}
	public void setStandardSn(String standardSn) {
		this.standardSn = standardSn;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDepartmentSn() {
		return departmentSn;
	}
	public void setDepartmentSn(String departmentSn) {
		this.departmentSn = departmentSn;
	}
	public boolean isAdvancedSearch() {
		return advancedSearch;
	}
	public void setAdvancedSearch(boolean advancedSearch) {
		this.advancedSearch = advancedSearch;
	}
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	public int getIndexId() {
		return indexId;
	}
	public void setIndexId(int indexId) {
		this.indexId = indexId;
	}
	public JSONArray getJson() {
		return json;
	}

	public void setJson(JSONArray json) {
		this.json = json;
	}
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDepartmentTypeSn() {
		return departmentTypeSn;
	}

	public void setDepartmentTypeSn(String departmentTypeSn) {
		this.departmentTypeSn = departmentTypeSn;
	}
	public JSONObject getJsonjo() {
		return jsonjo;
	}
	public void setJsonjo(JSONObject jsonjo) {
		this.jsonjo = jsonjo;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getExcelContentType() {
		return excelContentType;
	}
	public void setExcelContentType(String excelContentType) {
		this.excelContentType = excelContentType;
	}
}
