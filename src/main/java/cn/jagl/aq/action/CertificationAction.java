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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.hibernate.exception.ConstraintViolationException;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.jagl.aq.domain.Certification;
import cn.jagl.aq.domain.CertificationType;
import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.Person;

@SuppressWarnings({ "serial", "rawtypes" })
public class CertificationAction extends BaseAction {
	
	private String q;
	private Integer page;
	private Integer rows;//分页条件
	private String excelContentType;
	private String departmentSn;
	private String certificationTypeSn;
	private String holderSn;
	private Date begintime;
	private Date endtime;//查询条件
	private int days;
	private String order;
	//增删改
	private String certificationSn;//证件编号
	private Date issuedDate;//发证日期
	private Date validStartDate;//有效开始日期
	private Date validEndDate;//有效结束日期
	private String issuedBy;//发证单位
	private Date time;
	private Integer id;
	
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public String getDepartmentSn() {
		return departmentSn;
	}
	public void setDepartmentSn(String departmentSn) {
		this.departmentSn = departmentSn;
	}
	public String getCertificationTypeSn() {
		return certificationTypeSn;
	}
	public void setCertificationTypeSn(String certificationTypeSn) {
		this.certificationTypeSn = certificationTypeSn;
	}
	public String getHolderSn() {
		return holderSn;
	}
	public void setHolderSn(String holderSn) {
		this.holderSn = holderSn;
	}
	public Date getBegintime() {
		return begintime;
	}
	public void setBegintime(Date begintime) {
		this.begintime = begintime;
	}
	public Date getEndtime() {
		return endtime;
	}
	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
	public String getCertificationSn() {
		return certificationSn;
	}
	public void setCertificationSn(String certificationSn) {
		this.certificationSn = certificationSn;
	}
	public Date getIssuedDate() {
		return issuedDate;
	}
	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}
	public Date getValidStartDate() {
		return validStartDate;
	}
	public void setValidStartDate(Date validStartDate) {
		this.validStartDate = validStartDate;
	}
	public Date getValidEndDate() {
		return validEndDate;
	}
	public void setValidEndDate(Date validEndDate) {
		this.validEndDate = validEndDate;
	}
	public String getIssuedBy() {
		return issuedBy;
	}
	public void setIssuedBy(String issuedBy) {
		this.issuedBy = issuedBy;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
	//输出指定字段
	public PrintWriter out()throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out= response.getWriter();
        return out;
	}
	//分页显示jichaxun
	public String show() throws IOException{
		out();
		JSONArray array=new JSONArray();
		StringBuffer hqlentity=new StringBuffer();
		StringBuffer hqlcount=new StringBuffer();
		String pId=(String) session.get("personId");
		Person person=personService.getByPersonId(pId);
    	Department department=person.getDepartment();
		hqlentity.append("select c from Certification c where");
		hqlcount.append("select count(c) from Certification c where");
    	if(departmentSn==null){
    		
    		int roleType=(int) session.get("roleType");
    		departmentSn=department.getDepartmentSn();
    		if(roleType==1)
			{
				departmentSn = departmentService.getUpNerestFgs(departmentSn)
						.getDepartmentSn();
			}else{
				departmentSn = departmentService.getUpNearestImplDepartment(departmentSn)
						.getDepartmentSn();
			}
    		
    		hqlentity.append(" c.holder.department.departmentSn like '"+departmentSn+"%'");
			hqlcount.append(" c.holder.department.departmentSn like '"+departmentSn+"%'");
    	}
    	else{
    		hqlentity.append(" c.holder.department.departmentSn like '"+departmentSn+"%'");
			hqlcount.append(" c.holder.department.departmentSn like '"+departmentSn+"%'");
    	}
		if(certificationTypeSn!=null&&certificationTypeSn.length()>0){
			hqlentity.append(" and c.certificationType.certificationTypeSn='"+certificationTypeSn+"'");
			hqlcount.append(" and c.certificationType.certificationTypeSn='"+certificationTypeSn+"'");
		}
		if(certificationSn!=null&&certificationSn.length()>0){
			hqlentity.append(" and c.certificationSn='"+certificationSn+"'");
			hqlcount.append(" and c.certificationSn='"+certificationSn+"'");
		}
		if(holderSn!=null&&holderSn.length()>0){
			hqlentity.append(" and c.holder.personId='"+holderSn+"'");
			hqlcount.append(" and c.holder.personId='"+holderSn+"'");	
		}
		if(time!=null){
			LocalDate issuedDate = time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			hqlentity.append(" and c.issuedDate='"+issuedDate+"'");
			hqlcount.append(" and c.issuedDate='"+issuedDate+"'");	
		}
		if(endtime!=null){
			LocalDate endtimelocal = endtime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			hqlentity.append(" and c.validEndDate < '"+endtimelocal+"'");
			hqlcount.append(" and c.validEndDate < '"+endtimelocal+"'");	
		}
		if(issuedBy!=null&&issuedBy.length()>0){
			hqlentity.append(" and c.issuedBy='"+issuedBy+"'");
			hqlcount.append(" and c.issuedBy='"+issuedBy+"'");
		}
		if(days!=0){
			hqlentity.append(" and c.validEndDate-c.validStartDate <"+days);
			hqlcount.append(" and c.validEndDate-c.validStartDate <"+days);
		}
		if(order!=null){
			if(order.equals("ASC")){
				hqlentity.append("  order by c.validEndDate ASC");
				hqlcount.append("  order by c.validEndDate ASC");
			}
			if(order.equals("DESC")){
				hqlentity.append(" order by c.validEndDate DESC");
				hqlcount.append(" order by c.validEndDate DESC");
			}
		}
		List<Certification> certifications=certificationService.findByPage(hqlentity.toString(), page, rows);
		long total=certificationService.countHql(hqlcount.toString());
		for(Certification certification:certifications){
			 JSONObject jo=new JSONObject();
			 jo.put("id",certification.getId());
			 //字符型
			 jo.put("certificationSn",certification.getCertificationSn());
			 jo.put("issuedBy",certification.getIssuedBy());
			 //日期型	 
			 jo.put("issuedDate",certification.getIssuedDate());	
			 jo.put("validEndDate",certification.getValidEndDate());
			 jo.put("validStartDate",certification.getValidStartDate());
			 //类
			 if(certification.getCertificationType()!=null){
				 jo.put("certificationTypeName",certification.getCertificationType().getCertificationTypeName());
				 jo.put("certificationTypeSn",certification.getCertificationType().getCertificationTypeSn());
			 }
			 if(certification.getHolder()!=null){
				 jo.put("holderName",certification.getHolder().getPersonName());	 
				 jo.put("holderSn",certification.getHolder().getPersonId());	 
			 }			 
			 array.put(jo);
		}
		String str="{\"total\":"+total+",\"rows\":"+array+"}";
		out().print(str);
		out().flush(); 
		out().close();
		return SUCCESS;
	}
	
	//添加
	public String add() throws IOException{
		out();
		String message="";
		//证件编号和证件类型形成唯一索引 如何判断重复 添加不成功有可能是。。重复。格式不正确 一般都正确 选的
		try{
			Certification certification=new Certification();
			Person holder=personService.getByPersonId(holderSn);
			certification.setHolder(holder);
			certification.setCertificationSn(certificationSn);
			CertificationType certificationType=certificationTypeService.getByCertificationTypeSn(certificationTypeSn);
			certification.setCertificationType(certificationType);
			certification.setIssuedBy(issuedBy);
			LocalDate issuedDatelocal = issuedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate validStartDatelocal = validStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate validEndDatelocal = validEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();		
			certification.setIssuedDate(issuedDatelocal);
			certification.setValidStartDate(validStartDatelocal);
			certification.setValidEndDate(validEndDatelocal);
			certificationService.add(certification);
			message="添加证件成功！";
			}
		catch(Exception e){
			message="添加证件失败，请检查是否有重复数据或者数据格式不正确！";
		}
		out().print(message);
        out().flush(); 
        out().close();      
		return SUCCESS;
	}
	//更新
	public String update() throws IOException{
		out();
		String message="";
		try{
			Certification certification=certificationService.getById(id);
			Person holder=personService.getByPersonId(holderSn);
			certification.setHolder(holder);
			certification.setCertificationSn(certificationSn);
			CertificationType certificationType=certificationTypeService.getByCertificationTypeSn(certificationTypeSn);
			certification.setCertificationType(certificationType);
			certification.setIssuedBy(issuedBy);
			LocalDate issuedDatelocal = issuedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate validStartDatelocal = validStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate validEndDatelocal = validEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();		
			certification.setIssuedDate(issuedDatelocal);
			certification.setValidStartDate(validStartDatelocal);
			certification.setValidEndDate(validEndDatelocal);	
			certificationService.update(certification);
			 message="更新成功！";
		}catch(Exception e){
			message="更新失败，请检查数据是否填写完整或格式是否正确！";
		}
		out().print(message);
		out().flush(); 
		out().close();
		return SUCCESS;
	}
	//删除
	public String delete() throws IOException{
		out();
		certificationService.deleteByIds(ids);
		//Certification certification=certificationService.getById(id);
		//certificationService.delete(certification);
		String message="删除成功！";
		out().print(message);
		out().flush(); 
		out().close();
		return SUCCESS;
		
	}
	private String item;
	//当前部门下所有的人
	public String persons() throws IOException{
		out();
		JSONArray array=new JSONArray();
		if(departmentSn==null||departmentSn.trim().length()==0){
			String pId=(String) session.get("personId");
			Person personnow=personService.getByPersonId(pId);
			departmentSn=personnow.getDepartment().getDepartmentSn();
		}
		StringBuffer hqlentity=new StringBuffer();
		StringBuffer hqlcount=new StringBuffer();		
		if(item!=null){
			//该部门最近的贯标单位
			Department department=departmentService.getUpNearestImplDepartment(departmentSn);
			if(department!=null){
				departmentSn=department.getDepartmentSn();
			}
		}
		hqlentity.append("select p from Person p where p.department.departmentSn like '"+departmentSn+"%'");
		hqlcount.append("select count(p) from Person p where p.department.departmentSn like '"+departmentSn+"%'");
		if(q!=null && !"".equals(q)){
    		hqlentity.append(" AND (p.personId like '"+q.trim()+"%' or p.personName like '%"+q.trim()+"%' or p.id like '"+q.trim()+"%')");
    		hqlcount.append(" AND (p.personId like '"+q.trim()+"%' or p.personName like '%"+q.trim()+"%' or p.id like '"+q.trim()+"%')");
    	}
		List<Person> persons=personService.findByPage(hqlentity.toString(), 1, 10);
		long total=personService.countHql(hqlcount.toString());
		for(Person person:persons){
			JSONObject jo=new JSONObject();
			jo.put("id",person.getId());
			jo.put("personId", person.getPersonId());
			jo.put("personName", person.getPersonName());
			array.put(jo); 
		}
		String str="{\"total\":"+total+",\"rows\":"+array+"}";
		out().print(str);
		out().flush(); 
		out().close();		
		return SUCCESS;	
	}
	//加载改部门下的人的所有证件编号
	public String certificationSns() throws IOException{
		out();
		JSONArray array=new JSONArray();
		StringBuffer hqlentity=new StringBuffer();
		StringBuffer hqlcount=new StringBuffer();
		String pId=(String) session.get("personId");
		Person person=personService.getByPersonId(pId);
    	Department department=person.getDepartment();
		hqlentity.append("select c from Certification c where c.holder.department.departmentSn like '"+department.getDepartmentSn()+"%'");
		hqlcount.append("select count(c) from Certification c where c.holder.department.departmentSn like '"+department.getDepartmentSn()+"%'");
		if(q!=null && !"".equals(q)){
    		hqlentity.append(" AND c.certificationSn like '"+q+"%'");
    		hqlcount.append(" AND c.certificationSn like '"+q+"%'");
    	}
		List<Certification> certifications=certificationService.findByPage(hqlentity.toString(), 1, 10);
		long total=certificationService.countHql(hqlcount.toString());
		for(Certification certification:certifications){
			 JSONObject jo=new JSONObject();
			 jo.put("certificationSn",certification.getCertificationSn());	 
			 array.put(jo);
		}
		String str="{\"total\":"+total+",\"rows\":"+array+"}";
		out().print(str);
		out().flush(); 
		out().close();
		return SUCCESS;	
	}
	//获得所有发证单位
	public String issuedBys() throws IOException{
		out();
		JSONArray array=new JSONArray();
		StringBuffer hqlentity=new StringBuffer();
		StringBuffer hqlcount=new StringBuffer();
		String pId=(String) session.get("personId");
		Person person=personService.getByPersonId(pId);
    	Department department=person.getDepartment();
		hqlentity.append("select c from Certification c where c.holder.department.departmentSn like '"+department.getDepartmentSn()+"%'");
		hqlcount.append("select count(c) from Certification c where c.holder.department.departmentSn like '"+department.getDepartmentSn()+"%'");
		if(q!=null && !"".equals(q)){
    		hqlentity.append(" AND c.issuedBy like '"+q+"%'");
    		hqlcount.append(" AND c.issuedBy like '"+q+"%'");
    	}
		List<Certification> certifications=certificationService.findByPage(hqlentity.toString(), 1, 10);
		long total=certificationService.countHql(hqlcount.toString());
		for(Certification certification:certifications){
			 JSONObject jo=new JSONObject();
			 jo.put("issuedBy",certification.getIssuedBy());	 
			 array.put(jo);
		}
		String str="{\"total\":"+total+",\"rows\":"+array+"}";
		out().print(str);
		out().flush(); 
		out().close();
		return SUCCESS;
	}
	
	
	public File getExcel() {
		return excel;
	}
	public void setExcel(File excel) {
		this.excel = excel;
	}
	private File excel;
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
	//下载模板
		public String download() throws IOException{
			//String path =ServletActionContext.getServletContext().getRealPath("/template/certifications.xlsx");
			String path =ServletActionContext.getServletContext().getRealPath("/template/ce.xlsx");
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
			   // excelFileName ="persons.xlsx"; 	 
	            excelFileName=URLEncoder.encode("证件导入模板.xlsx","UTF-8");     
			}  
			catch (Exception e)  
			{  
			    e.printStackTrace();  
			}
			return SUCCESS;
		}
		private String importtip;
		public String getImporttip() {
			return importtip;
		}
		public void setImporttip(String importtip) {
			this.importtip = importtip;
		}
		//证件导入
		@SuppressWarnings({ "resource", "unchecked" })
		public String importcerbefore()  throws IOException{
			InputStream is0 = new FileInputStream(excel); 
		  	XSSFWorkbook wb = new XSSFWorkbook(is0);
			Certification cer=null;
//			HashMap<String, String> record = new HashMap<String, String>();
	        float num=0f;
//			String isExist="第";
			String isError="第";
			String nullData="第";
			String notFindHolder="第";
			String notFindDepartment="第";
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
	                cer = new Certification();
	                try{
		                XSSFCell cerSn = xssfRow.getCell(0);
		                if (cerSn == null || cerSn.toString().trim().length() == 0) {
		                	nullData+=(rowNum+1)+",";
		                    continue;
		                }
		                cer.setCertificationSn(cerSn.getStringCellValue());
		                
		                //证件类型编号
		                XSSFCell cerTSn = xssfRow.getCell(1);
		                if (cerTSn == null || cerTSn.toString().trim().length() == 0) {
		                	nullData+=(rowNum+1)+",";
		                    continue;
		                }
		                CertificationType cerT=certificationTypeService.getByCertificationTypeSn(cerTSn.toString());
		                cer.setCertificationType(cerT);
		                
		                XSSFCell holderSn = xssfRow.getCell(3);
		                if (holderSn == null || holderSn.toString().trim().length() == 0) {
		                	nullData+=(rowNum+1)+",";
		                    continue;
		                }
		                Person holder=personService.getByPersonId(holderSn.toString());
		                if(holder==null){
		                	notFindHolder+=(rowNum+1)+",";
		                }
		                cer.setHolder(holder);
		                
		                XSSFCell datecell = xssfRow.getCell(4);
		                if (datecell == null || datecell.toString().trim().length() == 0) {
		                	nullData+=(rowNum+1)+",";
		                    continue;
		                }
		                Date date=datecell.getDateCellValue();
		        		LocalDate issuedDatelocal = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		                cer.setIssuedDate(issuedDatelocal);
		                
		                XSSFCell bdatecell = xssfRow.getCell(5);
		                if (bdatecell == null || bdatecell.toString().trim().length() == 0) {
		                	nullData+=(rowNum+1)+",";
		                    continue;
		                }
		                Date bdate=bdatecell.getDateCellValue();
		        		LocalDate bDatelocal = bdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		                cer.setValidStartDate(bDatelocal);
		                
		                XSSFCell edatecell = xssfRow.getCell(6);
		                if (edatecell == null || edatecell.toString().trim().length() == 0) {
		                	nullData+=(rowNum+1)+",";
		                    continue;
		                }
		                Date edate=edatecell.getDateCellValue();
		        		LocalDate eDatelocal =edate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		                cer.setValidEndDate(eDatelocal);
		                
		                
		                XSSFCell  issuedBy= xssfRow.getCell(7);
		                if (issuedBy == null || issuedBy.toString().trim().length() == 0) {
		                	nullData+=(rowNum+1)+",";
		                    continue;
		                }
		                cer.setIssuedBy(issuedBy.toString());
		             }
	                catch(Exception e){
		            	isError+=(rowNum+1)+",";
	                	continue;
	                }
	                try{
	                	try
	                	{
	                		certificationService.add(cer);
	                	}
	                	catch(ConstraintViolationException e)
	                	{
	                		Certification ce=certificationService.getByCertificationSn(xssfRow.getCell(0).getStringCellValue());
	                		CertificationType certificationType=certificationTypeService.getByCertificationTypeSn(xssfRow.getCell(1).getStringCellValue());
			                ce.setCertificationType(certificationType);
			                Person pe=personService.getByPersonId(xssfRow.getCell(3).getStringCellValue());
			                ce.setHolder(pe);
			                Date date=(Date)xssfRow.getCell(4).getDateCellValue();
			                LocalDate issuedDatelocal = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			                ce.setIssuedDate(issuedDatelocal);
			                Date bdate=(Date)xssfRow.getCell(5).getDateCellValue();
			                LocalDate bDatelocal = bdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			                ce.setValidStartDate(bDatelocal);
			                Date edate=(Date)xssfRow.getCell(6).getDateCellValue();
			                LocalDate eDatelocal = edate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			                ce.setValidEndDate(eDatelocal);
			                ce.setIssuedBy(xssfRow.getCell(7).getStringCellValue());
			                certificationService.update(cer);               
	                	}
	                }
	                catch(Exception e){
	                	isError+=(rowNum+1)+",";
	                }
	            }
	        }
	        importtip="";
	        String[] number;
	        if(!"第".equals(nullData)){
	        	nullData=nullData.substring(0, nullData.length()-1) + "行有空数据！" + "<br />";
	        	importtip+=nullData;
	        	number=nullData.split(",");
	        	importtip=importtip+"共"+number.length+"行"+ "<br /><br />";
	        }
	        if(!"第".equals(notFindDepartment)){
	        	notFindDepartment=notFindDepartment.substring(0, notFindDepartment.length()-1) + "行部门编号不存在！" + "<br />";
	        	importtip+=notFindDepartment;
	        	number=notFindDepartment.split(",");
	        	importtip=importtip+"共"+number.length+"行"+ "<br /><br />";
	        }
	        if(!"第".equals(notFindHolder)){
	        	notFindHolder=notFindHolder.substring(0, notFindHolder.length()-1) + "行持有人不存在！" + "<br />";
	        	importtip+=notFindHolder;
	        	number=notFindHolder.split(",");
	        	importtip=importtip+"共"+number.length+"行"+ "<br /><br />";
	        }
	        if(!"第".equals(isError)){
	        	isError=isError.substring(0, isError.length()-1) + "行导入异常，请检查格式！" + "<br />";
	        	importtip+=isError;
	        	number=isError.split(",");
	        	importtip=importtip+"共"+number.length+"行"+ "<br /><br />";
	        }
	        if(importtip.equals("")){
	        	importtip="导入数据成功！";
	        }else{
	        	importtip+="其他数据导入成功！";
	        }
		    session.put("progressValue",0);
		    return SUCCESS;
		}
		//转换并读取字符
		public String StringValue(XSSFCell before){
			String after="";
	        if(before.getCellType()==Cell.CELL_TYPE_STRING){
	        	after=before.toString();
	        }
	        if(before.getCellType()==Cell.CELL_TYPE_NUMERIC){
	        	BigDecimal bd = new BigDecimal(before.getNumericCellValue());  
	        	after=bd.toPlainString();
	        }
	        return after;
		}
		//证件导入
		@SuppressWarnings({ "unchecked", "resource" })
		public String importcer()  throws IOException{
			InputStream is0 = new FileInputStream(excel); 
		  	XSSFWorkbook wb = new XSSFWorkbook(is0);
			Certification cer=null;
//			HashMap<String, String> record = new HashMap<String, String>();
	        float num=0f;
			String isExist="第";
			String isError="第";
			String nullData="第";
			String notFindHolder="第";
			String notFindDepartment="第";
			String notDate="第";
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
	                    continue;
	                }
	                cer = new Certification();
                	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                	Date d = new Date();
                	String s=null;       
	                try{
	                	//员工编号
	                	XSSFCell holderSn = xssfRow.getCell(0);
		                if (holderSn == null || holderSn.toString().trim().length() == 0) {
		                	nullData+=(rowNum+1)+",";
		                    continue;
		                }
		                Person holder=personService.getByPersonId(StringValue(holderSn));
		                if(holder==null){
		                	notFindHolder+=(rowNum+1)+",";
		                }
		                cer.setHolder(holder);
		                
		                //证件类型编号
		                XSSFCell cerTypeSn = xssfRow.getCell(3);
		                if (cerTypeSn == null || cerTypeSn.toString().trim().length() == 0) {
		                	nullData+=(rowNum+1)+",";
		                    continue;
		                }
		                CertificationType cerT=certificationTypeService.getByCertificationTypeSn(StringValue(cerTypeSn));
		                if(cerT==null){
		                	isExist+=(rowNum+1)+",";
		                	continue;
		                }
		                cer.setCertificationType(cerT);
		                
		                //证件编号
		                XSSFCell cerSn = xssfRow.getCell(4);
		                if (cerSn == null || cerSn.toString().trim().length() == 0) {
		                	nullData+=(rowNum+1)+",";
		                    continue;
		                }
		                cer.setCertificationSn(StringValue(cerSn));
		                		               
		                XSSFCell  issuedBy= xssfRow.getCell(5);
		                if (issuedBy == null || issuedBy.toString().trim().length() == 0) {
		                	nullData+=(rowNum+1)+",";
		                    continue;
		                }
		                cer.setIssuedBy(StringValue(issuedBy));
		                
		                
		                XSSFCell date = xssfRow.getCell(6);
		                if (date == null || date.toString().trim().length() == 0) {
		                	cer.setIssuedDate(null);
		                }
		                else{	
		                	try{
		                		if(date.getCellType()==Cell.CELL_TYPE_STRING){
		                			s=date.getStringCellValue();
		                		}else{
		                			s=sdf.format(date.getDateCellValue());		                			
		                		}
		                		d=sdf.parse(s);
		                		Date sdate = new Date(d.getTime());
		                		LocalDate issuedDatelocal = sdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				                cer.setIssuedDate(issuedDatelocal);
		                	}catch(Exception e){
		                		notDate+=(rowNum+1)+",";
		                		continue;
		                	}
		                }
		                
		                XSSFCell bdate = xssfRow.getCell(7);
		                if (bdate == null || bdate.toString().trim().length() == 0) {
		                	cer.setIssuedDate(null);
		                }
		                else{	
		                	try{
		                		if(bdate.getCellType()==Cell.CELL_TYPE_STRING){
		                			s=bdate.getStringCellValue();
		                		}else{
		                			s=sdf.format(bdate.getDateCellValue());		                			
		                		}
		                		d=sdf.parse(s);
		                		Date sdate = new Date(d.getTime());
		                		LocalDate validStartDate = sdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				                cer.setValidStartDate(validStartDate);
		                	}catch(Exception e){
		                		notDate+=(rowNum+1)+",";
		                		continue;
		                	}
		                }
		                
		                XSSFCell edate = xssfRow.getCell(8);
		                if (edate == null || edate.toString().trim().length() == 0) {
		                	cer.setIssuedDate(null);
		                }
		                else{	
		                	try{
		                		if(edate.getCellType()==Cell.CELL_TYPE_STRING){
		                			s=edate.getStringCellValue();
		                		}else{
		                			s=sdf.format(edate.getDateCellValue());		                			
		                		}
		                		d=sdf.parse(s);
		                		Date sdate = new Date(d.getTime());
		                		LocalDate validEndDate = sdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				                cer.setValidEndDate(validEndDate);
		                	}catch(Exception e){
		                		notDate+=(rowNum+1)+",";
		                		continue;
		                	}
		                }		               		                		                
		             }
	                catch(Exception e){
		            	isError+=(rowNum+1)+",";
	                	continue;
	                }
	                try{
	                	try
	                	{
	                		certificationService.add(cer);
	                	}
	                	catch(ConstraintViolationException e)
	                	{
	                		Certification ce=certificationService.getByCertificationSnAndTypeSn( StringValue(xssfRow.getCell(4)), StringValue(xssfRow.getCell(3)) );
			                Person pe=personService.getByPersonId(StringValue(xssfRow.getCell(0)));
			                ce.setHolder(pe);
			                Date sdate =null;
			                XSSFCell date = xssfRow.getCell(6);
			                if(date.getCellType()==Cell.CELL_TYPE_STRING){
	                			s=date.getStringCellValue();
	                		}else{
	                			s=sdf.format(date.getDateCellValue());		                			
	                		}
	                		d=sdf.parse(s);
	                		 sdate = new Date(d.getTime());
	                		LocalDate issuedDate = sdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			                ce.setIssuedDate(issuedDate);
			                
			                XSSFCell edate = xssfRow.getCell(8);
			                if(edate.getCellType()==Cell.CELL_TYPE_STRING){
	                			s=edate.getStringCellValue();
	                		}else{
	                			s=sdf.format(edate.getDateCellValue());		                			
	                		}
	                		d=sdf.parse(s);
	                		sdate = new Date(d.getTime());
	                		LocalDate validEndDate = sdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			                ce.setValidEndDate(validEndDate);
			                
			                XSSFCell bdate = xssfRow.getCell(7);
			                if(bdate.getCellType()==Cell.CELL_TYPE_STRING){
	                			s=bdate.getStringCellValue();
	                		}else{
	                			s=sdf.format(bdate.getDateCellValue());		                			
	                		}
	                		d=sdf.parse(s);
	                		sdate = new Date(d.getTime());
	                		LocalDate validStartDate = sdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			                ce.setValidStartDate(validStartDate);;
			                
			                ce.setIssuedBy(StringValue(xssfRow.getCell(5)));
			                certificationService.update(ce);               
	                	}
	                }
	                catch(Exception e){
	                	isError+=(rowNum+1)+",";
	                }
	            }
	        }
	        importtip="";
	        String[] number;
	        if(!"第".equals(nullData)){
	        	nullData=nullData.substring(0, nullData.length()-1) + "行有空数据！" + "<br />";
	        	importtip+=nullData;
	        	number=nullData.split(",");
	        	importtip=importtip+"共"+number.length+"行"+ "<br /><br />";
	        }
	        if(!"第".equals(notFindDepartment)){
	        	notFindDepartment=notFindDepartment.substring(0, notFindDepartment.length()-1) + "行部门编号不存在！" + "<br />";
	        	importtip+=notFindDepartment;
	        	number=notFindDepartment.split(",");
	        	importtip=importtip+"共"+number.length+"行"+ "<br /><br />";
	        }
	        if(!"第".equals(notFindHolder)){
	        	notFindHolder=notFindHolder.substring(0, notFindHolder.length()-1) + "行持有人不存在！" + "<br />";
	        	importtip+=notFindHolder;
	        	number=notFindHolder.split(",");
	        	importtip=importtip+"共"+number.length+"行"+ "<br /><br />";
	        }
	        if(!"第".equals(isExist)){
	        	isExist=isExist.substring(0, isExist.length()-1) + "行证件类型不存在！" + "<br />";
	        	importtip+=isExist;
	        	number=isExist.split(",");
	        	importtip=importtip+"共"+number.length+"行"+ "<br /><br />";
	        }
	        if(!"第".equals(notDate)){
	        	notDate=notDate.substring(0, notDate.length()-1) + "行日期格式错误,请使用常规格式或日期格式！" + "<br />";
	        	importtip+=notDate;
	        	number=notDate.split(",");
	        	importtip=importtip+"共"+number.length+"行"+ "<br /><br />";
	        }
	        if(!"第".equals(isError)){
	        	isError=isError.substring(0, isError.length()-1) + "行导入异常，请检查格式！" + "<br />";
	        	importtip+=isError;
	        	number=isError.split(",");
	        	importtip=importtip+"共"+number.length+"行"+ "<br /><br />";
	        }
	        if(importtip.equals("")){
	        	importtip="导入数据成功！";
	        }else{
	        	importtip+="其他数据导入成功！";
	        }
		    session.put("progressValue",0);
		    return SUCCESS;
		}
		public String getItem() {
			return item;
		}
		public void setItem(String item) {
			this.item = item;
		}
		public String getExcelContentType() {
			return excelContentType;
		}
		public void setExcelContentType(String excelContentType) {
			this.excelContentType = excelContentType;
		}	      
}

