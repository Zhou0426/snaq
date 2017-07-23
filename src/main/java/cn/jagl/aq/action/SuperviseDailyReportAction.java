package cn.jagl.aq.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.HashMap;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import cn.jagl.aq.domain.SuperviseDailyReport;
import cn.jagl.util.AwsS3Util;
import cn.jagl.util.Excel2PDF;
import net.sf.json.JSONObject;

public class SuperviseDailyReportAction extends BaseAction<SuperviseDailyReport>{
	/**
	 * @author Sakura
	 */
	private static final long serialVersionUID = 1L;
	private SuperviseDailyReport superviseDailyReport;
	private String departmentSn;
	private String personId;
	private String success;
	private String inputPath;
	private String excelFile;
	private String pdfFile;
	private String pdfName;
	
	private String excelFileName;
	private InputStream excelStream;
	private InputStream pdfStream;
	private String json;
	private String data;
    private HashMap<String, Object> result = new HashMap<String, Object>();
	private JSONObject jsonObject=new JSONObject();
	private Date startDate;
	private Date endDate;
	private String departmentTypeSn;
	private Date reportDate;//日报日期
	

	public String search(){
		jsonObject=superviseDailyReportService.getByPage(page, rows, startDate, endDate, departmentTypeSn);	
		return "jsonObject";
	}
	
	public String seeOnline() throws Exception{
		if (!Excel2PDF.getLicense()) {
            return null;
        }
		if(AwsS3Util.exists("superviseDailyReportFile/" + excelFileName + ".pdf")){
			String pdf = ServletActionContext.getServletContext().getContextPath()+ "/dailyreport/"+excelFileName+"_pdf?pdfFile=superviseDailyReportFile/" + excelFileName + ".pdf";
			result.put("path", pdf);
		}else{
			String pdf = ServletActionContext.getServletContext().getContextPath()+ "/dailyreport/"+excelFileName+"_pdf?pdfFile="+excelFileName+".pdf&&departmentTypeSn="+departmentTypeSn+"&&reportDate="+reportDate;
			result.put("path", pdf);
		}
		return SUCCESS;
	}
	
	public String open() throws Exception{
		if(departmentTypeSn==null || departmentTypeSn.trim().length()==0){
			pdfStream = AwsS3Util.downloadFile(pdfFile);
			pdfName=pdfFile;
		}else{
			pdfStream=superviseDailyReportService.online(reportDate, departmentTypeSn, "pdf");
			pdfName=departmentTypeSn+reportDate+".pdf";
			if(pdfStream==null){
				pdfName="error.pdf";
				pdfStream = ServletActionContext.getServletContext().getResourceAsStream("/WEB-INF/supervisedailyreportfile/"+pdfName);
			}
		}
		return SUCCESS;
	}
	
	public String download() throws Exception{
		String path = "superviseDailyReportFile/" + excelFile;
		if(AwsS3Util.exists(path)){
			excelStream = AwsS3Util.downloadFile(path);
		}else{
			excelStream=superviseDailyReportService.online(reportDate, departmentTypeSn, "xls");
			excelFileName=URLEncoder.encode(departmentTypeSn+reportDate+".xls","UTF-8");
			if(excelStream==null){
				String path2 =ServletActionContext.getServletContext().getRealPath("/WEB-INF/supervisedailyreportfile/error.xls");
				InputStream is0 = new FileInputStream(path2); 
			  	XSSFWorkbook wb = new XSSFWorkbook(is0);	
				 try{  
		          	ByteArrayOutputStream fout = new ByteArrayOutputStream();  
		              wb.write(fout);
		              wb.close();
		              fout.close();
		              byte[] fileContent = fout.toByteArray();  
		              ByteArrayInputStream is = new ByteArrayInputStream(fileContent);  
		    
		              excelStream = is;                	    
		              excelFileName=URLEncoder.encode("error.xlsx","UTF-8");
		          }catch (Exception e){  
		              e.printStackTrace();  
		          }
			}
		}
		return "download";
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public SuperviseDailyReport getSuperviseDailyReport() {
		return superviseDailyReport;
	}
	public void setSuperviseDailyReport(SuperviseDailyReport superviseDailyReport) {
		this.superviseDailyReport = superviseDailyReport;
	}
	public String getDepartmentSn() {
		return departmentSn;
	}
	public void setDepartmentSn(String departmentSn) {
		this.departmentSn = departmentSn;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getInputPath() {
		return inputPath;
	}
	public void setInputPath(String inputPath) {
		this.inputPath = inputPath;
	}
	public String getExcelFile() {
		return excelFile;
	}
	public void setExcelFile(String excelFile) {
		this.excelFile = excelFile;
	}
	public String getPdfFile() {
		return pdfFile;
	}
	public void setPdfFile(String pdfFile) {
		this.pdfFile = pdfFile;
	}
	public String getExcelFileName() {
		return excelFileName;
	}
	public void setExcelFileName(String excelFileName) {
		this.excelFileName = excelFileName;
	}
	public InputStream getExcelStream() {
		return excelStream;
	}
	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}
	public InputStream getPdfStream() {
		return pdfStream;
	}
	public void setPdfStream(InputStream pdfStream) {
		this.pdfStream = pdfStream;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}	
	public HashMap<String, Object> getResult() {
		return result;
	}
	public void setResult(HashMap<String, Object> result) {
		this.result = result;
	}
	public JSONObject getJsonObject() {
		return jsonObject;
	}
	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getDepartmentTypeSn() {
		return departmentTypeSn;
	}

	public void setDepartmentTypeSn(String departmentTypeSn) {
		this.departmentTypeSn = departmentTypeSn;
	}
	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	public String getPdfName() {
		return pdfName;
	}

	public void setPdfName(String pdfName) {
		this.pdfName = pdfName;
	}

}
