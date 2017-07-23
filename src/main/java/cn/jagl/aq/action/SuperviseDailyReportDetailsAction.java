package cn.jagl.aq.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.util.CellRangeAddress;

import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.DepartmentType;
import cn.jagl.aq.domain.SuperviseDailyReport;
import cn.jagl.aq.domain.SuperviseDailyReportDetails;
import cn.jagl.aq.domain.SuperviseItem;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SuperviseDailyReportDetailsAction extends BaseAction<SuperviseDailyReportDetails> {
	/**
	 * @author Sakura
	 */
	private static final long serialVersionUID = 1L;
	private SuperviseItem superviseContent;
	private String departmentSn;
	private String departmentTypeSn;
	private String success;
	private String data;
	private String excelFile;
	private InputStream excelStream;
	private int id;
	private String json;
	private String superviseItemSn;
	private String reportSketch;//上报简述
	private String reportDetails;//上报明细
	private JSONObject jsonObject=new JSONObject();
	private JSONArray jsonArray=new JSONArray();
	private String excelFileName;
	private String type;
	private String order;
	private Date startDate;
	private Date endDate;
	

	public Date getEndDate() {
		return endDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	public String getOrder() {
		return order;
	}


	public void setOrder(String order) {
		this.order = order;
	}


	//单条保存
	public String saveOrUpdate() throws Exception{
		java.util.Date nDate = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sDate = sdf.format(nDate);
        Date reportDate = Date.valueOf(sDate);
        jsonObject.put("status", "ok");
        
        departmentSn=(String) session.get("departmentSn");
        Department department=departmentService.getUpNearestImplDepartment(departmentSn);
        DepartmentType departmentType = department.getDepartmentType();
        SuperviseDailyReport superviseDailyReport=superviseDailyReportService.getSuperviseDailyReportByDateandSn(reportDate, departmentType.getDepartmentTypeSn());
		if(superviseDailyReport==null){
			//jsonObject.put("status", "nook");
        	try{
        		SuperviseDailyReport newDailyReport=new SuperviseDailyReport();
            	newDailyReport.setReportDate(reportDate);
            	newDailyReport.setDepartmentType(departmentType);
            	try{
            		superviseDailyReportService.save(newDailyReport);
            	}catch(Exception e){
            		e.printStackTrace();
            	}
				SuperviseItem superviseItem=superviseItemService.getSuperviseItemBySn(superviseItemSn);
				if(superviseItem!=null){
					if((reportDetails!=null && reportDetails.trim().length()>0) || (reportSketch!=null && reportSketch.trim().length()>0)){
						SuperviseDailyReportDetails newDailyReportDetails=new SuperviseDailyReportDetails();
						newDailyReportDetails.setDepartment(departmentService.getUpNearestImplDepartment(departmentSn.trim()));
						if(reportDetails!=null && reportDetails.trim().length()>0){
							newDailyReportDetails.setReportDetails("第"+order+"项："+reportDetails);
						}
						
						newDailyReportDetails.setReportSketch(reportSketch);
						newDailyReportDetails.setSuperviseItem(superviseItem);
						newDailyReportDetails.setSuperviseDailyReport(newDailyReport);
						superviseDailyReportDetailsService.save(newDailyReportDetails);
					}
				}else{
					jsonObject.put("status", "nook");
					return "jsonObject";
				}
			}catch(Exception e){
				jsonObject.put("status", "nook");
				return "jsonObject";
			}
        	return "jsonObject";
		}else{
			try{
				SuperviseItem superviseItem=superviseItemService.getSuperviseItemBySn(superviseItemSn);
				if(superviseItem!=null){
					SuperviseDailyReportDetails superviseDailyReportDetails=superviseDailyReportDetailsService.getByMany(superviseDailyReport.getId(), department.getDepartmentSn(), superviseItem.getSuperviseItemSn()); 
					if(superviseDailyReportDetails!=null){
						if(type.equals("info")){
							superviseDailyReportDetails.setReportSketch(reportSketch);
						}else{
							if(superviseDailyReportDetails.getReportDetails()!=null && superviseDailyReportDetails.getReportDetails().length()>0){
								superviseDailyReportDetails.setReportDetails(reportDetails);
							}else{
								if(reportDetails!=null && reportDetails.trim().length()>0){
									superviseDailyReportDetails.setReportDetails("第"+order+"项："+reportDetails);
								}
							}
							
						}
						superviseDailyReportDetailsService.updateSuperviseDailyReportDetails(superviseDailyReportDetails);
					}else if((reportDetails!=null && reportDetails.trim().length()>0) || (reportSketch!=null && reportSketch.trim().length()>0)){
						SuperviseDailyReportDetails newDailyReportDetails=new SuperviseDailyReportDetails();
						newDailyReportDetails.setDepartment(departmentService.getUpNearestImplDepartment(departmentSn.trim()));
						if(reportDetails!=null && reportDetails.trim().length()>0){
							newDailyReportDetails.setReportDetails("第"+order+"项："+reportDetails);
						}
						
						newDailyReportDetails.setReportSketch(reportSketch);
						newDailyReportDetails.setSuperviseItem(superviseItem);
						newDailyReportDetails.setSuperviseDailyReport(superviseDailyReport);
						superviseDailyReportDetailsService.save(newDailyReportDetails);
					}
				}else{
					jsonObject.put("status", "nook");
					return "jsonObject";
				}
			}catch(Exception e){
				jsonObject.put("status", "nook");
				return "jsonObject";
			}	
		}
		return "jsonObject";
	}
	
	
	public String search(){
		//判断当月总天数
		//int totalDay=LocalDate.of(year, month, 1).lengthOfMonth();
		DepartmentType implDepartmentType =null;
		List<Department> departments = new ArrayList<Department>();
        if(departmentTypeSn != null){
        	implDepartmentType = departmentTypeService.getByDepartmentTypeSn(departmentTypeSn);
        }else{
        	List<DepartmentType> departmentTypes = departmentTypeService.getAllImplDepartmentTypes();
        	implDepartmentType = departmentTypes.get(0);
        }
        if(implDepartmentType!=null){
        	departments=departmentService.getAllDepartmentsByType(implDepartmentType.getDepartmentTypeSn());
        }
        int blankDays=0;
        
        Calendar aCalendar = Calendar.getInstance();

        aCalendar.setTime(startDate);
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        int year1 = aCalendar.get(Calendar.YEAR);
        
        aCalendar.setTime(endDate);
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        int year2 = aCalendar.get(Calendar.YEAR);
        
        if(year1 != year2)   //同一年
        {
            int timeDistance = 0 ;
            for(int i = year1 ; i < year2 ; i ++)
            {
                if(i%4==0 && i%100!=0 || i%400==0)    //闰年            
                {
                    timeDistance += 366;
                }
                else    //不是闰年
                {
                    timeDistance += 365;
                }
            }
            
            blankDays=timeDistance + (day2-day1) +1;
        }else{
        	blankDays=day2-day1+1;
        }
        for(Department department:departments){
            JSONObject jo = new JSONObject();
            long submitDays=superviseDailyReportService.countByMany(department.getDepartmentSn(), startDate, endDate);
            jo.put("departmentName",department.getDepartmentName());
            jo.put("submitDays", submitDays);
            jo.put("blankDays", blankDays-submitDays);
            jo.put("departmentSn", department.getDepartmentSn());
            jsonArray.add(jo);
        }
		return "jsonArray";	
	}
	
	public String export() {
		JSONArray array=JSONArray.fromObject(data);
        DepartmentType departmentType = departmentTypeService.getByDepartmentTypeSn(departmentTypeSn);
    	HSSFWorkbook wb = new HSSFWorkbook();
    	String title1=(startDate!=null?startDate.toString():"")+"-"+(endDate!=null?endDate.toString():"")+"日报统计结果";
    	String title2=departmentType.getDepartmentTypeName()+(startDate!=null?startDate.toString():"")+"-"+(endDate!=null?endDate.toString():"")+"日报统计结果";
    	String title3=departmentType.getDepartmentTypeName()+(startDate!=null?startDate.toString():"")+"-"+(endDate!=null?endDate.toString():"");
        HSSFSheet sheet = wb.createSheet(title1);
        sheet.setDefaultColumnWidth(42);
        //生成样式
        //生成第1行标题样式
        HSSFCellStyle style1 = wb.createCellStyle();
        HSSFFont font1 = wb.createFont();
        font1.setFontName("黑体");
        font1.setFontHeightInPoints((short)20);
        font1.setBold(true);
        style1.setFont(font1);
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        //生成第2行表格标题样式
        HSSFCellStyle style2 = wb.createCellStyle();
        HSSFFont font2 = wb.createFont();
        font2.setFontName("黑体");
        font2.setFontHeightInPoints((short)13);
        font2.setBold(true);
        style2.setFont(font2);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style2.setBorderBottom(BorderStyle.THIN);
        style2.setBorderLeft(BorderStyle.THIN);
        style2.setBorderTop(BorderStyle.THIN);
        style2.setBorderRight(BorderStyle.THIN);
        style2.setWrapText(true);
        //生成表格样式
        HSSFCellStyle style3 = wb.createCellStyle();
        HSSFFont font3 = wb.createFont();
        font3.setFontName("宋体");
        font3.setFontHeightInPoints((short)12);
        style3.setFont(font3);
        style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style2.setBorderBottom(BorderStyle.THIN);
        style2.setBorderLeft(BorderStyle.THIN);
        style2.setBorderTop(BorderStyle.THIN);
        style2.setBorderRight(BorderStyle.THIN);
        style3.setWrapText(true); 
        //创建大标题
        HSSFRow row = sheet.createRow(0);
        double rheight = 41.25*20;
        row.setHeight((short)rheight);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue(title2);
        cell.setCellStyle(style1);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
        //创建表格第1行
        row = sheet.createRow(2);
        rheight = 18.75*20;
        row.setHeight((short)rheight);
        cell=row.createCell(0);
        cell.setCellValue("部门名称");
        cell.setCellStyle(style2);
        cell=row.createCell(1);
        cell.setCellValue("上报天数");
        cell.setCellStyle(style2);
        cell=row.createCell(2);
        cell.setCellValue("缺报天数");
        cell.setCellStyle(style2);
        //创建表格内容
        for(int i=0; i<array.size(); i++){
        	JSONObject jo=array.getJSONObject(i);
        	row = sheet.createRow(3+i);
        	rheight = 36.75*20;
        	row.setHeight((short)rheight);
        	cell = row.createCell(0);
        	cell.setCellValue(jo.getString("departmentName"));
            cell.setCellStyle(style3);
            cell=row.createCell(1);
            cell.setCellValue(jo.getDouble("submitDays"));
            cell.setCellStyle(style3);
            cell=row.createCell(2);
            cell.setCellValue(jo.getDouble("blankDays"));
            cell.setCellStyle(style3);        	
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
            excelFileName =URLEncoder.encode(title3+".xls", "UTF-8"); 	      
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }
        return "export";
	}
	
	public SuperviseItem getSuperviseContent() {
		return superviseContent;
	}
	public void setSuperviseContent(SuperviseItem superviseContent) {
		this.superviseContent = superviseContent;
	}
	public String getDepartmentSn() {
		return departmentSn;
	}
	public void setDepartmentSn(String departmentSn) {
		this.departmentSn = departmentSn;
	}
	public String getDepartmentTypeSn() {
		return departmentTypeSn;
	}
	public void setDepartmentTypeSn(String departmentTypeSn) {
		this.departmentTypeSn = departmentTypeSn;
	}
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getExcelFile() {
		return excelFile;
	}
	public void setExcelFile(String excelFile) {
		this.excelFile = excelFile;
	}
	public InputStream getExcelStream() {
		return excelStream;
	}
	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public String getSuperviseItemSn() {
		return superviseItemSn;
	}

	public void setSuperviseItemSn(String superviseItemSn) {
		this.superviseItemSn = superviseItemSn;
	}
	public JSONObject getJsonObject() {
		return jsonObject;
	}
	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}public String getReportSketch() {
		return reportSketch;
	}
	public void setReportSketch(String reportSketch) {
		this.reportSketch = reportSketch;
	}
	public String getReportDetails() {
		return reportDetails;
	}
	public void setReportDetails(String reportDetails) {
		this.reportDetails = reportDetails;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}
	public JSONArray getJsonArray() {
		return jsonArray;
	}
	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getExcelFileName() {
		return excelFileName;
	}


	public void setExcelFileName(String excelFileName) {
		this.excelFileName = excelFileName;
	}
	public Date getStartDate() {
		return startDate;
	}


	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


}
