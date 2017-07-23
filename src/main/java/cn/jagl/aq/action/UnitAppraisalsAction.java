package cn.jagl.aq.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.struts2.ServletActionContext;

import cn.jagl.aq.domain.SystemAudit;
import cn.jagl.aq.domain.UnitAppraisals;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author mahui
 * @method 
 * @date 2016年8月18日上午10:23:25
 */
@SuppressWarnings("unchecked")
public class UnitAppraisalsAction extends BaseAction<UnitAppraisals> {
	private static final long serialVersionUID = 1L;
	private String orderMonth;
	private String year;
	private String departmentTypeSn;
	private String data;
	private String departmentSn;
	private String month;
	private int quarter;
	private String titles;
	private InputStream excelStream; 
    private String excelFileName;
	private String date;
	private JSONArray jsonArray=new JSONArray();
	private String departmentTypeName;
	private String departmentName;
	private Byte type;
	
	//输出指定字段
	public PrintWriter out()throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out= response.getWriter();
        return out;
	}
	
	/**
	 * @method 安全考核隐患导出
	 * @author mahui
	 * @return String
	 */
	public String exportUnsafeCondition(){
		LocalDate local=LocalDate.parse(date);
		if(departmentSn!=null && departmentSn.trim().length()>0){
			UnitAppraisals unitAppraisals=unitAppraisalsService.queryByMany(departmentSn, local.plusDays(1),type);
			if(unitAppraisals!=null){
				excelStream=unitAppraisalsService.exportUnsafeCondition(departmentSn, local.plusDays(1));
			}		
		}            	    
        try {
			excelFileName=URLEncoder.encode(date+"隐患导出表.xls","UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "export";
	}
	
	//查看扣分详情
	public void scoreDetail() throws IOException{
		LocalDate local=LocalDate.parse(date);
		JSONArray array=new JSONArray();
		if(departmentSn!=null && departmentSn.trim().length()>0){
			UnitAppraisals unitAppraisals=unitAppraisalsService.queryByMany(departmentSn, local.plusDays(1),type);
			if(unitAppraisals!=null){
				array=unitAppraisalsService.scoreDetail(departmentSn, local.plusDays(1),year,month,type);
			}		
		}
		out().print(array);
		out().flush(); 
		out().close();
	}
	//查看某一天总得分和扣分
	public void scoreByDay() throws IOException{
		LocalDate local=LocalDate.parse(date);
		JSONObject jo=new JSONObject();
		jo=unitAppraisalsService.socoreByDay(departmentSn, local.plusDays(1),type);
		out().print(jo);
		out().flush(); 
		out().close();
	}
	//查看扣分详情--处室考核
	public void scoreDetailOffice() throws IOException{
		LocalDate local = null;
		String ksDate="";
		String jsDate="";
		if(quarter==1){
			ksDate=year+"-01-01";
			jsDate=year+"-03-31";
		}else if(quarter==2){
			ksDate=year+"-04-01";
			jsDate=year+"-06-30";
		}else if(quarter==3){
			ksDate=year+"-07-01";
			jsDate=year+"-09-30";
		}else{
			ksDate=year+"-10-01";
			jsDate=year+"-12-31";
		}
		JSONArray array=new JSONArray();
		if(departmentSn!=null && departmentSn.trim().length()>0){
			List<UnitAppraisals> unitAppraisals=unitAppraisalsService.queryByDepartmentDate(departmentSn,ksDate,jsDate);
			if(unitAppraisals.size()>0){
				array=unitAppraisalsService.scoreDetail(departmentSn, local,ksDate,jsDate,null);
			}
		}
		out().print(array);
		out().flush(); 
		out().close();
	}
	//导出部门类型下的某月考核打分说明
	public String exportMonthByDepartmentTypeSn(){
		try  
        {  
            excelStream = unitAppraisalsService.exportMonthByDepartmentTypeSn(departmentTypeSn, departmentTypeName, year, month,type);               
            String title=year+"年"+month+"月"+departmentTypeName+"月度考核打分说明";
            excelFileName =URLEncoder.encode(title+".xls", "UTF-8"); 	      
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }
		return "export";
	}
	//导出部门下的某月考核打分说明
	public String exportMonthByDepartmentSn(){
		try  
        {  
			excelStream=unitAppraisalsService.exportMonthByDepartmentSn(departmentSn, departmentName, year, month,type);
            //excelStream = unitAppraisalsService.exportMonthByDepartmentTypeSn(departmentTypeSn, departmentTypeName, year, month);               
            String title=year+"年"+month+"月"+departmentName+"月度考核打分说明";
            excelFileName =URLEncoder.encode(title+".xls", "UTF-8"); 	      
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }
		return "export";
	}
	//导出为Excel
	public  String export(){
		JSONArray array=JSONArray.fromObject(data);
		HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(type==0?"单位审核":"区队审核");
        HSSFRow row;
        //数据样式
        HSSFCellStyle style = wb.createCellStyle(); 
		HSSFFont font = wb.createFont();
		font.setFontName("仿宋_GB2312");
		font.setFontHeightInPoints((short)12);
		style.setFont(font);         
		style.setAlignment(CellStyle.ALIGN_CENTER);//水平居中  
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中 
		for(int i=0;i<titles.split(",").length;i++){
			sheet.setDefaultColumnStyle(i, style);
		}
		sheet.setColumnWidth(0, 80*80);
		if(type==0){
			sheet.setColumnWidth(22, 70*70);
		}else{
			sheet.setColumnWidth(18, 70*70);
		}
		for(int i=0;i<4;i++){
			if(type==0){
				sheet.setColumnWidth(5*i+4, 70*70);
				sheet.setColumnWidth(5*i+5, 70*70);
			}else{
				sheet.setColumnWidth(4*i+4, 70*70);
			}
			
		}
        HSSFCell cell;
        //设置标题
        row=sheet.createRow(0);
        for(int i=0;i<titles.split(",").length;i++){
        	cell=row.createCell(i);
        	cell.setCellValue(titles.split(",")[i]);
        }
        //循环插入数据
        for(int i=0;i<array.size();i++){
        	row=sheet.createRow(i+1);
        	JSONObject jo=array.getJSONObject(i);
        	if(type==0){
        		cell=row.createCell(0);
            	cell.setCellValue(jo.getString("departmentName"));
            	cell=row.createCell(1);
            	cell.setCellValue(jo.getDouble("1"));
            	cell=row.createCell(2);
            	cell.setCellValue(jo.getDouble("2"));
            	cell=row.createCell(3);
            	cell.setCellValue(jo.getDouble("3"));
            	cell=row.createCell(4);
            	cell.setCellValue(jo.getDouble("audit1"));
            	cell=row.createCell(5);
            	cell.setCellValue(jo.getDouble("season1"));
            	cell=row.createCell(6);
            	cell.setCellValue(jo.getDouble("4"));
            	cell=row.createCell(7);
            	cell.setCellValue(jo.getDouble("5"));
            	cell=row.createCell(8);
            	cell.setCellValue(jo.getDouble("6"));
            	cell=row.createCell(9);
            	cell.setCellValue(jo.getDouble("audit2"));
            	cell=row.createCell(10);
            	cell.setCellValue(jo.getDouble("season2"));
            	cell=row.createCell(11);
            	cell.setCellValue(jo.getDouble("7"));
            	cell=row.createCell(12);
            	cell.setCellValue(jo.getDouble("8"));
            	cell=row.createCell(13);
            	cell.setCellValue(jo.getDouble("9"));
            	cell=row.createCell(14);
            	cell.setCellValue(jo.getDouble("audit3"));
            	cell=row.createCell(15);
            	cell.setCellValue(jo.getDouble("season3"));
            	cell=row.createCell(16);
            	cell.setCellValue(jo.getDouble("10"));
            	cell=row.createCell(17);
            	cell.setCellValue(jo.getDouble("11"));
            	cell=row.createCell(18);
            	cell.setCellValue(jo.getDouble("12"));
            	cell=row.createCell(19);
            	cell.setCellValue(jo.getDouble("audit4"));
            	cell=row.createCell(20);
            	cell.setCellValue(jo.getDouble("season4"));
            	cell=row.createCell(21);
            	cell.setCellValue(jo.getDouble("year"));
            	cell=row.createCell(22);
            	if(jo.get("rank")!=null){
            		cell.setCellValue(jo.getInt("rank"));   
            	}
        	}else{
        		cell=row.createCell(0);
            	cell.setCellValue(jo.getString("departmentName"));
            	cell=row.createCell(1);
            	cell.setCellValue(jo.getDouble("1"));
            	cell=row.createCell(2);
            	cell.setCellValue(jo.getDouble("2"));
            	cell=row.createCell(3);
            	cell.setCellValue(jo.getDouble("3"));
            	cell=row.createCell(4);
            	cell.setCellValue(jo.getDouble("season1"));
            	cell=row.createCell(5);
            	cell.setCellValue(jo.getDouble("4"));
            	cell=row.createCell(6);
            	cell.setCellValue(jo.getDouble("5"));
            	cell=row.createCell(7);
            	cell.setCellValue(jo.getDouble("6"));
            	cell=row.createCell(8);
            	cell.setCellValue(jo.getDouble("season2"));
            	cell=row.createCell(9);
            	cell.setCellValue(jo.getDouble("7"));
            	cell=row.createCell(10);
            	cell.setCellValue(jo.getDouble("8"));
            	cell=row.createCell(11);
            	cell.setCellValue(jo.getDouble("9"));
            	cell=row.createCell(12);
            	cell.setCellValue(jo.getDouble("season3"));
            	cell=row.createCell(13);
            	cell.setCellValue(jo.getDouble("10"));
            	cell=row.createCell(14);
            	cell.setCellValue(jo.getDouble("11"));
            	cell=row.createCell(15);
            	cell.setCellValue(jo.getDouble("12"));
            	cell=row.createCell(16);
            	cell.setCellValue(jo.getDouble("season4"));
            	cell=row.createCell(17);
            	cell.setCellValue(jo.getDouble("year"));
            	cell=row.createCell(18);
            	if(jo.get("rank")!=null){
            		cell.setCellValue(jo.getInt("rank"));   
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
                excelFileName =URLEncoder.encode(type==0?"单位考核表.xls":"区队考核表.xls", "UTF-8"); 	      
            }  
            catch (Exception e)  
            {  
                e.printStackTrace();  
            }
        }
		return "export";
	}
	//获取季度对应标准季度
	public void queryQuarterDetail() throws IOException{		
		SystemAudit systemAudit=null;
		if(quarter==4){
			systemAudit=systemAuditService.queryShScore(departmentSn, year);
		}else{
			systemAudit=systemAuditService.queryScore(departmentSn, quarter, year);
		}
		JSONObject jo=new JSONObject();
		if(systemAudit!=null){
			jo.put("auditSn",systemAudit.getAuditSn());
			if(systemAudit.getFinalScore()!=null){
				jo.put("score",systemAudit.getFinalScore());
			}else{
				jo.put("score",0);
			}			
			if(systemAudit.getStandard()!=null){
				jo.put("standardSn", systemAudit.getStandard().getStandardSn());
			}else{
				jo.put("standardSn", "");
			}
		}else{
			jo.put("auditSn","");
			jo.put("score", 0);
		}
		out().print(jo.toString());
		out().flush(); 
		out().close();
	}
	//查看月份详情
	public void queryMonthDetail() throws IOException{
		List<Object> list=unitAppraisalsService.queryMonth(year,month,departmentSn,type);
		JSONObject jo=new JSONObject();
		for(int i=0;i<list.size();i++){
			Map<Object, Object> m = (Map<Object, Object>)list.get(i);
			jo.put(m.get("days").toString()+"d", m.get("score"));
		}
		out().print(jo.toString());
		out().flush(); 
		out().close();
	}
	//查询分数
	public String queryScore(){
		jsonArray=unitAppraisalsService.queryScore(year, departmentTypeSn,type);
		return "jsonArray";
	}
	
	
	public String getOrderMonth() {
		return orderMonth;
	}
	public void setOrderMonth(String orderMonth) {
		this.orderMonth = orderMonth;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getDepartmentTypeSn() {
		return departmentTypeSn;
	}
	public void setDepartmentTypeSn(String departmentTypeSn) {
		this.departmentTypeSn = departmentTypeSn;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getDepartmentSn() {
		return departmentSn;
	}
	public void setDepartmentSn(String departmentSn) {
		this.departmentSn = departmentSn;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public int getQuarter() {
		return quarter;
	}
	public void setQuarter(int quarter) {
		this.quarter = quarter;
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
	public String getTitles() {
		return titles;
	}
	public void setTitles(String titles) {
		this.titles = titles;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public JSONArray getJsonArray() {
		return jsonArray;
	}

	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}
	public String getDepartmentTypeName() {
		return departmentTypeName;
	}

	public void setDepartmentTypeName(String departmentTypeName) {
		this.departmentTypeName = departmentTypeName;
	}
	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

}
