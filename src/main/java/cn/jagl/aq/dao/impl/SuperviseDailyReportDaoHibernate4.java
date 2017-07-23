package cn.jagl.aq.dao.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Date;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hibernate.Query;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Repository;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;
import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.DepartmentDao;
import cn.jagl.aq.dao.DepartmentTypeDao;
import cn.jagl.aq.dao.SuperviseDailyReportDao;
import cn.jagl.aq.dao.SuperviseDailyReportDetailsDao;
import cn.jagl.aq.dao.SuperviseItemDao;
import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.DepartmentType;
import cn.jagl.aq.domain.SuperviseDailyReport;
import cn.jagl.aq.domain.SuperviseDailyReportDetails;
import cn.jagl.aq.domain.SuperviseItem;
import cn.jagl.util.AwsS3Util;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Repository("superviseDailyReportDao")
public class SuperviseDailyReportDaoHibernate4 extends BaseDaoHibernate5<SuperviseDailyReport>
	   implements SuperviseDailyReportDao {

	@Resource(name="superviseItemDao")
	private SuperviseItemDao superviseItemDao;
	@Resource(name="superviseDailyReportDetailsDao")
	private SuperviseDailyReportDetailsDao superviseDailyReportDetailsDao;
	@Resource(name="departmentDao")
	private DepartmentDao departmentDao;
	@Resource(name="departmentTypeDao")
	private DepartmentTypeDao departmentTypeDao;
	
	
	  public static float getExcelCellAutoHeight(String str, float fontCountInline) {
	        float defaultRowHeight = 17.00f;//每一行的高度指定
	        float defaultCount = 0.00f;
	        for (int i = 0; i < str.length(); i++) {
	            float ff = getregex(str.substring(i, i + 1));
	            defaultCount = defaultCount + ff;
	        }
	        if((int) (defaultCount / fontCountInline)==(defaultCount / fontCountInline)){
	        	return ((int) (defaultCount / fontCountInline)) * defaultRowHeight;//计算
	        }else{
	        	return ((int) (defaultCount / fontCountInline) +1) * defaultRowHeight;//计算
	        }
	        //return ((int) (defaultCount / fontCountInline) + count+1) * defaultRowHeight;//计算
	    }
	
	    public static float getregex(String charStr) {
	        
	        if(charStr==" ")
	        {
	            return 0.5f;
	        }
	        // 判断是否为字母或字符
	        if (Pattern.compile("^[A-Za-z0-9]+$").matcher(charStr).matches()) {
	            return 0.5f;
	        }
	        // 判断是否为全角
	
	        if (Pattern.compile("[\u4e00-\u9fa5]+$").matcher(charStr).matches()) {
	            return 1.00f;
	        }
	        //全角符号 及中文
	        if (Pattern.compile("[^x00-xff]").matcher(charStr).matches()) {
	            return 1.00f;
	        }
	        return 0.5f;
	
	    }
	
	public Serializable createSuperviseDailyReport(DepartmentType departmentType){
		java.util.Date nDate = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sDate = sdf.format(nDate);
        Date reportDate = Date.valueOf(sDate);
        Timestamp createdTime=new Timestamp(System.currentTimeMillis());
        SuperviseDailyReport superviseDailyReport = new SuperviseDailyReport();
        superviseDailyReport.setReportDate(reportDate);
        superviseDailyReport.setCreatedTime(createdTime);
        superviseDailyReport.setDepartmentType(departmentType);
        superviseDailyReport.setFileName(departmentType.getDepartmentTypeSn()+sDate+".xls");
        return getSessionFactory().getCurrentSession().save(superviseDailyReport);
	}

	@Override
	public SuperviseDailyReport getSuperviseDailyReportByDateandSn(Date reportDate, String departmentTypeSn) {
		String hql="select p from SuperviseDailyReport p where p.reportDate=:reportDate and p.departmentType.departmentTypeSn=:departmentTypeSn";
		Query query = getSessionFactory().getCurrentSession().createQuery(hql)
				.setDate("reportDate", reportDate)
				.setString("departmentTypeSn", departmentTypeSn);
		return (SuperviseDailyReport) query.uniqueResult();
	}

	@Override
	public void updateDailyReport(SuperviseDailyReport superviseDailyReport) {
		Timestamp createdTime=new Timestamp(System.currentTimeMillis());
		superviseDailyReport.setCreatedTime(createdTime);
		getSessionFactory().getCurrentSession().saveOrUpdate(superviseDailyReport);
	}



	//分页查询
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getByPage(int page, int rows, Date startDate, Date endDate,String departmentTypeSn){
		
		String hql="select p from SuperviseDailyReport p";
		int i=0;
		if(departmentTypeSn!=null && departmentTypeSn.trim().length()>0){
			hql+=" where p.departmentType.departmentTypeSn='"+departmentTypeSn+"'";
			i++;
		}
		if(startDate!=null){
			if(i==0){
				hql+=" where p.reportDate >= '"+startDate+"'";
			}else{
				hql+=" and p.reportDate >= '"+startDate+"'";
			}
			i++;
		}
		if(endDate!=null){
			if(i==0){
				hql+=" where p.reportDate <= '"+endDate+"'";
			}else{
				hql+=" and p.reportDate <= '"+endDate+"'";
			}
		}
		hql+=" order by p.reportDate desc";
		JSONArray array=new JSONArray();
		for(SuperviseDailyReport superviseDailyReport:(List<SuperviseDailyReport>)getSessionFactory().getCurrentSession().createQuery(hql).setFirstResult((page - 1) * rows).setMaxResults(rows).list()){
			JSONObject jo=new JSONObject();
			jo.put("reportDate", superviseDailyReport.getReportDate().toString());
        	jo.put("department", superviseDailyReport.getDepartmentType().getDepartmentTypeName());
        	jo.put("fileName",superviseDailyReport.getFileName());
        	jo.put("departmentTypeSn",superviseDailyReport.getDepartmentType().getDepartmentTypeSn());
        	jo.put("pdf", superviseDailyReport.getDepartmentType().getDepartmentTypeSn()+superviseDailyReport.getReportDate().toString());
        	array.add(jo);
		}
		JSONObject jo=new JSONObject();
		jo.put("rows", array);
		jo.put("total", (long)getSessionFactory().getCurrentSession().createQuery(hql.replaceFirst("p", "count(p)")).uniqueResult());
		return jo;
	}


	//根据部门编号，年月获取个数
	@Override
	public long countByMany(String departmentSn,Date startDate,Date endDate) {
		String hql="select count(u) from SuperviseDailyReport u where u.id in (select distinct p.id from SuperviseDailyReport p inner join p.superviseDailyReportDetails s where s.department.departmentSn=:departmentSn and p.reportDate>=:startDate"
				+ " and p.reportDate<=:endDate and s.reportSketch is not null)";
		
		return (long) getSessionFactory().getCurrentSession().createQuery(hql)
				.setString("departmentSn", departmentSn)
				.setDate("startDate", startDate)
				.setDate("endDate",endDate)
				.uniqueResult();
	}

	//生成日报文件并上传
	@SuppressWarnings({ "unchecked", "resource" })
	@Override
	public void upload(){
		//获取所有贯标单位		
		List<DepartmentType> departmentTypes=(List<DepartmentType>)getSessionFactory().getCurrentSession()
				.createQuery("select p from DepartmentType p where p.isImplDepartmentType=:isImplDepartmentType")
				.setBoolean("isImplDepartmentType", true)
				.list();
		//日期
		Date date=new Date(LocalDate.now().minusDays(1).toDate().getTime());
		//循环上传
		for(DepartmentType departmentType:departmentTypes){
			try{
				int rownum=0;
				SuperviseDailyReport superviseDailyReport = getSuperviseDailyReportByDateandSn(date, departmentType.getDepartmentTypeSn());
				if(superviseDailyReport != null){
					List<SuperviseItem> list = new ArrayList<SuperviseItem>();
					List<SuperviseItem> parents = superviseItemDao.getSuperviseParentItemsByType(departmentType.getDepartmentTypeSn());
					
					HSSFWorkbook wb = new HSSFWorkbook();    
			        HSSFSheet sheet = wb.createSheet("安全日报"+date);     
			        sheet.setDefaultColumnWidth(17);
			        //样式一（标题）
			        HSSFCellStyle style1 = wb.createCellStyle();
			        HSSFFont font1 = wb.createFont();
			        font1.setFontName("黑体");
			        font1.setFontHeightInPoints((short) 18);
			        font1.setBold(true);
			        style1.setFont(font1);
			        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			        style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			        
			        //样式二（表头）
			        HSSFCellStyle style2 = wb.createCellStyle();
			        HSSFFont font2 = wb.createFont();
			        font2.setFontName("宋体");
			        font2.setFontHeightInPoints((short) 10);
			        font2.setBold(true);
			        style2.setFont(font2);
			        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			        style2.setBorderBottom(BorderStyle.THIN);
			        style2.setBorderLeft(BorderStyle.THIN);
			        style2.setBorderTop(BorderStyle.THIN);
			        style2.setBorderRight(BorderStyle.THIN);
			        style2.setWrapText(true);
			        
			        //样式三（部门）
			        HSSFCellStyle style3 = wb.createCellStyle();
			        HSSFFont font3 = wb.createFont();
			        font3.setFontName("仿宋_GB2312");
			        font3.setFontHeightInPoints((short) 12);
			        style3.setFont(font3);
			        style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			        style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			        style3.setBorderBottom(BorderStyle.THIN);
			        style3.setBorderLeft(BorderStyle.THIN);
			        style3.setBorderTop(BorderStyle.THIN);
			        style3.setBorderRight(BorderStyle.THIN);
			        style3.setWrapText(true);
			        
			        //样式四（内容）
			        HSSFCellStyle style4 = wb.createCellStyle();
			        HSSFFont font4 = wb.createFont();
			        font4.setFontName("宋体");
			        font4.setFontHeightInPoints((short) 10);
			        style4.setFont(font4);
			        style4.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			        style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			        style4.setBorderBottom(BorderStyle.THIN);
			        style4.setBorderLeft(BorderStyle.THIN);
			        style4.setBorderTop(BorderStyle.THIN);
			        style4.setBorderRight(BorderStyle.THIN);
			        style4.setWrapText(true);
			        
			      //样式六（内容2）
			        HSSFCellStyle style6 = wb.createCellStyle();
			        HSSFFont font6 = wb.createFont();
			        font6.setFontName("宋体");
			        font6.setFontHeightInPoints((short) 12);
			        style6.setFont(font6);
			        style6.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			        style6.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			        style6.setBorderBottom(BorderStyle.THIN);
			        style6.setBorderLeft(BorderStyle.THIN);
			        style6.setBorderTop(BorderStyle.THIN);
			        style6.setBorderRight(BorderStyle.THIN);
			        style6.setWrapText(true);
			        
			        HSSFCellStyle style5 = wb.createCellStyle();
			        style5.setFont(font4);
			        style5.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			        style5.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			        style5.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			        style5.setBorderBottom(BorderStyle.THIN);
			        style5.setBorderLeft(BorderStyle.THIN);
			        style5.setBorderTop(BorderStyle.THIN);
			        style5.setBorderRight(BorderStyle.THIN);
			        style5.setWrapText(true);
			        //if(!AwsS3Util.exists("superviseDailyReportFile/" + departmentType.getDepartmentTypeSn() + date + ".xls")){
			        HSSFRow row = sheet.createRow(0);
		            double rheight= 25*25 ; 
		            row.setHeight((short)rheight);
		            HSSFCell cell = row.createCell(0);
		            //父级
		            HSSFRow row3;
	            	HSSFCell cell3;
	            	//子级
	            	HSSFRow row4;
	            	HSSFCell cell4;
		            cell.setCellValue("神宁集团安全监察信息汇总表("+departmentType.getDepartmentTypeName()+")");
		            cell.setCellStyle(style1);
		            //sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, parents.size()));
		            row = sheet.createRow(1);
		            row.setHeight((short)rheight);
		            cell = row.createCell(0);
		            cell.setCellValue(date.toString()); 
		            cell.setCellStyle(style1);
		            //sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, parents.size()));           
		            row = sheet.createRow(2);
		            cell = row.createCell(0);
		            cell.setCellValue("单位\\监察内容");
		            cell.setCellStyle(style2);
		            sheet.addMergedRegion(new CellRangeAddress(2, 4, 0, 0));
		            //父级格式
		            row3=sheet.createRow(3);
		            rheight= 26*26; 
		            row3.setHeight((short)rheight);
		            cell3=row3.createCell(0);
		            cell3.setCellStyle(style2);
		            //子级格式
		            row4=sheet.createRow(4);
		            rheight= 23*23 ; 
		            row4.setHeight((short)rheight);
		            cell4=row4.createCell(0);
		            cell4.setCellStyle(style2);
		            //序号和父级
		            int colnum=0;
		            int order=0;
		            for(SuperviseItem s:parents){
		            	colnum++;
		            	int start=colnum;
		            	order++;
		            	//序号
		            	cell = row.createCell(colnum);
		            	cell.setCellValue(order);
		                cell.setCellStyle(style2);
		                //父级内容
		                cell3 = row3.createCell(colnum);
		            	cell3.setCellValue(s.getSuperviseContentName().replace("\r","").replace("\n", ""));
		                cell3.setCellStyle(style2);
		            	for(int i=1;i<s.getChild().size();i++){
		            		colnum++;		   
		            		cell = row.createCell(colnum);
		            		cell.setCellStyle(style2);
		            		cell3=row.createCell(colnum);
		            		cell3.setCellStyle(style2);
		            	}
		            	if(start<colnum){
		            		sheet.addMergedRegion(new CellRangeAddress(2, 2, start, colnum));
			            	sheet.addMergedRegion(new CellRangeAddress(3, 3, start, colnum));
		            	}
		            }
		            if(colnum>0){
		            	sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, colnum));
		            	sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, colnum));
		            }
		            //子级
		            int csize=0;
		            for(SuperviseItem s:parents){
		            	String query="select s from SuperviseItem s where s.parent.superviseItemSn=:superviseItemSn";
						List<SuperviseItem> child=(List<SuperviseItem>)getSessionFactory()
	            				.getCurrentSession()
	            				.createQuery(query)
	            				.setString("superviseItemSn", s.getSuperviseItemSn())
	            				.list();
	        			for(SuperviseItem sc:child){
	        				csize++;
	        				list.add(sc);
	        				cell4=row4.createCell(csize);
	        				cell4.setCellValue(sc.getSuperviseContentName().replace("\r","").replace("\n", ""));
	        	            cell4.setCellStyle(style2);
	        			}
		            }
		            
		            List<Department> departments = new ArrayList<Department>();
		            String hql="select p from Department p where p.departmentType.departmentTypeSn=:departmentTypeSn and p.deleted=false and (p.status is null or p.status<>0) order by p.showSequence";
		            departments = (List<Department>)getSessionFactory().getCurrentSession().createQuery(hql)
		            		.setString("departmentTypeSn", departmentType.getDepartmentTypeSn())
		            		.list();
		            rownum=departments.size()+5;
		            for(int i=0; i<departments.size(); i++){		            	
		            	row = sheet.createRow(5+i);
		            	row.setHeight((short) (25*25));
		            	HSSFRow row2 = sheet.createRow(rownum+i+1);
		            	row2.setHeightInPoints(getExcelCellAutoHeight(departments.get(i).getDepartmentName(),7f));
		            	HSSFCell cell2;
		            	cell = row.createCell(0);
		            	cell.setCellValue(departments.get(i).getDepartmentName());
		            	cell.setCellStyle(style3);
		            	cell2 = row2.createCell(0);
		            	cell2.setCellValue(departments.get(i).getDepartmentName());
		            	cell2.setCellStyle(style3);
		            	String reportDetails="";
		            	float height=0;
		            	int j=0;
		            	String lineSeparator = System.getProperty("line.separator", "\n");
		            	for(SuperviseItem superviseItem:list){
		            		String reportSketch="";
		            		j++;	            		
	        				SuperviseDailyReportDetails superviseDailyReportDetails=superviseDailyReportDetailsDao.getByMany(superviseDailyReport.getId(), departments.get(i).getDepartmentSn(), superviseItem.getSuperviseItemSn());
	        				if(superviseDailyReportDetails!=null&&superviseDailyReportDetails.getReportDetails()!=null&&superviseDailyReportDetails.getReportDetails().trim().length()>0){
	        					reportDetails+=superviseDailyReportDetails.getReportDetails().replace("\r","").replace("\n", "")+lineSeparator;
	        					height+=getExcelCellAutoHeight(superviseDailyReportDetails.getReportDetails().replace("\r","").replace("\n", ""), 70f);
	        				}
	        	
	        				if(superviseDailyReportDetails!=null&&superviseDailyReportDetails.getReportSketch()!=null&&superviseDailyReportDetails.getReportSketch().trim().length()>0){
	        					reportSketch=superviseDailyReportDetails.getReportSketch();
	        				}
		        			cell = row.createCell(j);
			            	cell.setCellValue(reportSketch);
			            	cell.setCellStyle(style5);
		        		}
		            	if(height>0){
	    					reportDetails=reportDetails.substring(0,reportDetails.length()-lineSeparator.length());
	    				}
		            	cell2 = row2.createCell(1);
		            	cell2.setCellValue(reportDetails);
		            	cell2.setCellStyle(style6);
		            	
		            	for(int t=1;t<colnum;t++){
			            	cell2 = row2.createCell(t+1);
			            	cell2.setCellStyle(style6);
			            }
		            	if(colnum>1){
		            		sheet.addMergedRegion(new CellRangeAddress(rownum+i+1, rownum+i+1, 1, colnum));	
		            	}
		            	
		            	if(height!=0&&height>row2.getHeightInPoints()){
		            		row2.setHeightInPoints(height);
		            	}
		            	
		            	/*if(reportDetails!=null&&reportDetails.trim().length()>100){
		            		float height=getExcelCellAutoHeight(reportDetails, 80f);     
			            	//根据字符串的长度设置高度
			            	row2.setHeightInPoints(height);
		            	}else if(reportDetails.contains("\r\n")){
		            		//float height=getExcelCellAutoHeight(reportDetails, reportDetails.indexOf("\r\n"));     
			            	//根据字符串的长度设置高度
			            	//row2.setHeightInPoints(height);
		            	}*/
		            }
		            row = sheet.createRow(rownum);
		            row.setHeight((short) (25*25));
		            cell = row.createCell(0);
		            cell.setCellValue("各矿当日安全生产情况说明");
		            cell.setCellStyle(style1);
		            if(colnum>0){
		            	sheet.addMergedRegion(new CellRangeAddress(rownum, rownum, 0, colnum));
		            }
		            for(int i=1;i<=colnum;i++){
		            	cell = row.createCell(i);
		            }
			        
		            sheet.setMargin(Sheet.BottomMargin,( double ) 0.5 );// 页边距（下）  
		            sheet.setMargin(Sheet.LeftMargin,( double ) 0.1 );// 页边距（左）  
		            sheet.setMargin(Sheet.RightMargin,( double ) 0.1 );// 页边距（右）  
		            sheet.setMargin(Sheet.TopMargin,( double ) 0.5 );// 页边距（上）  
		            sheet.setRowBreak(rownum-1);//分页
		            sheet.setHorizontallyCenter(true);//设置打印页面为水平居中  
		            //sheet.setVerticallyCenter(true);//设置打印页面为垂直居中
			        HSSFPrintSetup pt = sheet.getPrintSetup();    
			        pt.setLandscape(true); 
			        pt.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
			        pt.setScale((short)70);//打印缩放
			        //String path = ServletActionContext.getServletContext().getRealPath("/WEB-INF/supervisedailyreportfile");
			        FileOutputStream out = new FileOutputStream(departmentType.getDepartmentTypeSn()+date + ".xls");
			        wb.write(out);
			        out.close();
			        
			        File file = new File(departmentType.getDepartmentTypeSn()+date + ".xls");
			        
			        Workbook wb2 = new Workbook(departmentType.getDepartmentTypeSn()+date + ".xls");
			        File pdfFile = new File(departmentType.getDepartmentTypeSn()+date + ".pdf");
			        FileOutputStream fileOS = new FileOutputStream(pdfFile);
			        wb2.save(fileOS, SaveFormat.PDF);
			        fileOS.close();
			        File getPdfFile = new File(departmentType.getDepartmentTypeSn()+date + ".pdf");
			        if(getPdfFile.exists()){
			        	if(AwsS3Util.uploadFile("superviseDailyReportFile/" + departmentType.getDepartmentTypeSn()+date + ".pdf", getPdfFile)){
			        		getPdfFile.delete();
			        	}
			        }
			       
			        if(AwsS3Util.uploadFile("superviseDailyReportFile/" + departmentType.getDepartmentTypeSn()+date + ".xls", file)){
			        	file.delete();
			        	superviseDailyReport.setCreatedTime(new Timestamp(System.currentTimeMillis()));
			        	superviseDailyReport.setFileName(departmentType.getDepartmentTypeSn()+date + ".xls");
			        	update(superviseDailyReport);
			        }
				}
			}catch(Exception e){
				e.printStackTrace();
				continue;
			}
		}
	}

	@SuppressWarnings({ "unchecked"})
	@Override
	public InputStream online(Date date,String departmentTypeSn,String type) {
		try{
			DepartmentType departmentType=departmentTypeDao.getByDepartmentTypeSn(departmentTypeSn.trim());
			if(departmentType!=null){
			int rownum=0;
			SuperviseDailyReport superviseDailyReport = getSuperviseDailyReportByDateandSn(date, departmentType.getDepartmentTypeSn());
			if(superviseDailyReport != null){
				List<SuperviseItem> list = new ArrayList<SuperviseItem>();
				List<SuperviseItem> parents = superviseItemDao.getSuperviseParentItemsByType(departmentType.getDepartmentTypeSn());
				
				HSSFWorkbook wb = new HSSFWorkbook();    
		        HSSFSheet sheet = wb.createSheet("安全日报"+date);     
		        sheet.setDefaultColumnWidth(17);
		        //sheet.setColumnWidth(0, 18);
		        //样式一（标题）
		        HSSFCellStyle style1 = wb.createCellStyle();
		        HSSFFont font1 = wb.createFont();
		        font1.setFontName("黑体");
		        font1.setFontHeightInPoints((short) 18);
		        font1.setBold(true);
		        style1.setFont(font1);
		        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		        style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		        
		        //样式二（表头）
		        HSSFCellStyle style2 = wb.createCellStyle();
		        HSSFFont font2 = wb.createFont();
		        font2.setFontName("宋体");
		        font2.setFontHeightInPoints((short) 10);
		        font2.setBold(true);
		        style2.setFont(font2);
		        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		        style2.setBorderBottom(BorderStyle.THIN);
		        style2.setBorderLeft(BorderStyle.THIN);
		        style2.setBorderTop(BorderStyle.THIN);
		        style2.setBorderRight(BorderStyle.THIN);
		        style2.setWrapText(true);
		        
		        //样式三（部门）
		        HSSFCellStyle style3 = wb.createCellStyle();
		        HSSFFont font3 = wb.createFont();
		        font3.setFontName("仿宋_GB2312");
		        font3.setFontHeightInPoints((short) 12);
		        style3.setFont(font3);
		        style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		        style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		        style3.setBorderBottom(BorderStyle.THIN);
		        style3.setBorderLeft(BorderStyle.THIN);
		        style3.setBorderTop(BorderStyle.THIN);
		        style3.setBorderRight(BorderStyle.THIN);
		        style3.setWrapText(true);
		        
		        //样式四（内容1）
		        HSSFCellStyle style4 = wb.createCellStyle();
		        HSSFFont font4 = wb.createFont();
		        font4.setFontName("宋体");
		        font4.setFontHeightInPoints((short) 10);
		        style4.setFont(font4);
		        style4.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		        style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		        style4.setBorderBottom(BorderStyle.THIN);
		        style4.setBorderLeft(BorderStyle.THIN);
		        style4.setBorderTop(BorderStyle.THIN);
		        style4.setBorderRight(BorderStyle.THIN);
		        style4.setWrapText(true);
		        
		        //样式六（内容2）
		        HSSFCellStyle style6 = wb.createCellStyle();
		        HSSFFont font6 = wb.createFont();
		        font6.setFontName("宋体");
		        font6.setFontHeightInPoints((short) 12);
		        style6.setFont(font6);
		        style6.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		        style6.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		        style6.setBorderBottom(BorderStyle.THIN);
		        style6.setBorderLeft(BorderStyle.THIN);
		        style6.setBorderTop(BorderStyle.THIN);
		        style6.setBorderRight(BorderStyle.THIN);
		        style6.setWrapText(true);
		        
		        HSSFCellStyle style5 = wb.createCellStyle();
		        style5.setFont(font4);
		        style5.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		        style5.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		        style5.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		        style5.setBorderBottom(BorderStyle.THIN);
		        style5.setBorderLeft(BorderStyle.THIN);
		        style5.setBorderTop(BorderStyle.THIN);
		        style5.setBorderRight(BorderStyle.THIN);
		        style5.setWrapText(true);
		        //if(!AwsS3Util.exists("superviseDailyReportFile/" + departmentType.getDepartmentTypeSn() + date + ".xls")){
	        	HSSFRow row = sheet.createRow(0);
	            double rheight= 25*25 ; 
	            row.setHeight((short)rheight);
	            HSSFCell cell = row.createCell(0);
	            //父级
	            HSSFRow row3;
            	HSSFCell cell3;
            	//子级
            	HSSFRow row4;
            	HSSFCell cell4;
	            cell.setCellValue("神宁集团安全监察信息汇总表("+departmentType.getDepartmentTypeName()+")");
	            cell.setCellStyle(style1);
	            //sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, parents.size()));
	            row = sheet.createRow(1);
	            row.setHeight((short)rheight);
	            cell = row.createCell(0);
	            cell.setCellValue(date.toString()); 
	            cell.setCellStyle(style1);
	            //sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, parents.size()));           
	            row = sheet.createRow(2);
	            cell = row.createCell(0);
	            cell.setCellValue("单位\\监察内容");
	            cell.setCellStyle(style2);
	            sheet.addMergedRegion(new CellRangeAddress(2, 4, 0, 0));
	            //父级格式
	            row3=sheet.createRow(3);
	            rheight= 26*26; 
	            row3.setHeight((short)rheight);
	            cell3=row3.createCell(0);
	            cell3.setCellStyle(style2);
	            //子级格式
	            row4=sheet.createRow(4);
	            rheight= 23*23 ; 
	            row4.setHeight((short)rheight);
	            cell4=row4.createCell(0);
	            cell4.setCellStyle(style2);
	            //序号和父级
	            int colnum=0;
	            int order=0;
	            for(SuperviseItem s:parents){
	            	colnum++;
	            	int start=colnum;
	            	order++;
	            	//序号
	            	cell = row.createCell(colnum);
	            	cell.setCellValue(order);
	                cell.setCellStyle(style2);
	                //父级内容
	                cell3 = row3.createCell(colnum);
	            	cell3.setCellValue(s.getSuperviseContentName().replace("\r","").replace("\n", ""));
	                cell3.setCellStyle(style2);
	            	for(int i=1;i<s.getChild().size();i++){
	            		colnum++;		   
	            		cell = row.createCell(colnum);
	            		cell.setCellStyle(style2);
	            		cell3=row.createCell(colnum);
	            		cell3.setCellStyle(style2);
	            	}
	            	if(start<colnum){
	            		sheet.addMergedRegion(new CellRangeAddress(2, 2, start, colnum));
		            	sheet.addMergedRegion(new CellRangeAddress(3, 3, start, colnum));
	            	}
	            }
	            if(colnum>0){
	            	sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, colnum));
	            	sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, colnum));
	            }
	            //子级
	            int csize=0;
	            for(SuperviseItem s:parents){
	            	String query="select s from SuperviseItem s where s.parent.superviseItemSn=:superviseItemSn";
					List<SuperviseItem> child=(List<SuperviseItem>)getSessionFactory()
            				.getCurrentSession()
            				.createQuery(query)
            				.setString("superviseItemSn", s.getSuperviseItemSn())
            				.list();
        			for(SuperviseItem sc:child){
        				csize++;
        				list.add(sc);
        				cell4=row4.createCell(csize);
        				cell4.setCellValue(sc.getSuperviseContentName().replace("\r","").replace("\n", ""));
        	            cell4.setCellStyle(style2);
        			}
	            }
	            
	            List<Department> departments = new ArrayList<Department>();
	            String hql="select p from Department p where p.departmentType.departmentTypeSn=:departmentTypeSn and p.deleted=false and (p.status is null or p.status<>0) order by p.showSequence";
	            departments = (List<Department>)getSessionFactory().getCurrentSession().createQuery(hql)
	            		.setString("departmentTypeSn", departmentType.getDepartmentTypeSn())
	            		.list();
	            rownum=departments.size()+5;
	            for(int i=0; i<departments.size(); i++){		            	
	            	row = sheet.createRow(5+i);
	            	row.setHeight((short) (25*25));
	            	HSSFRow row2 = sheet.createRow(rownum+i+1);
	            	row2.setHeightInPoints(getExcelCellAutoHeight(departments.get(i).getDepartmentName(),7f));
	            	HSSFCell cell2;
	            	cell = row.createCell(0);
	            	cell.setCellValue(departments.get(i).getDepartmentName());
	            	cell.setCellStyle(style3);
	            	cell2 = row2.createCell(0);
	            	cell2.setCellValue(departments.get(i).getDepartmentName());
	            	cell2.setCellStyle(style3);
	            	String reportDetails="";
	            	float height=0;
	            	int j=0;
	            	String lineSeparator = System.getProperty("line.separator", "\n");
	            	for(SuperviseItem superviseItem:list){
	            		String reportSketch="";
	            		j++;	            		
        				SuperviseDailyReportDetails superviseDailyReportDetails=superviseDailyReportDetailsDao.getByMany(superviseDailyReport.getId(), departments.get(i).getDepartmentSn(), superviseItem.getSuperviseItemSn());
        				if(superviseDailyReportDetails!=null&&superviseDailyReportDetails.getReportDetails()!=null&&superviseDailyReportDetails.getReportDetails().trim().length()>0){
        					reportDetails+=superviseDailyReportDetails.getReportDetails().trim().replace("\r","").replace("\n", "")+lineSeparator;
        					height+=getExcelCellAutoHeight(superviseDailyReportDetails.getReportDetails().trim().replace("\r","").replace("\n", ""), 70f);
        				}
        	
        				if(superviseDailyReportDetails!=null&&superviseDailyReportDetails.getReportSketch()!=null&&superviseDailyReportDetails.getReportSketch().trim().length()>0){
        					reportSketch=superviseDailyReportDetails.getReportSketch();
        				}
	        			cell = row.createCell(j);
		            	cell.setCellValue(reportSketch);
		            	cell.setCellStyle(style5);
	        		}
	            	if(height>0){
    					reportDetails=reportDetails.substring(0,reportDetails.length()-lineSeparator.length());
    				}
	            	cell2 = row2.createCell(1);
	            	cell2.setCellValue(reportDetails);
	            	cell2.setCellStyle(style6);
	            	
	            	for(int t=1;t<colnum;t++){
		            	cell2 = row2.createCell(t+1);
		            	cell2.setCellStyle(style6);
		            }
	            	if(colnum>1){
	            		sheet.addMergedRegion(new CellRangeAddress(rownum+i+1, rownum+i+1, 1, colnum));	
	            	}
	            	
	            	if(height!=0&&height>row2.getHeightInPoints()){
	            		row2.setHeightInPoints(height);
	            	}
	            	
	            	/*if(reportDetails!=null&&reportDetails.trim().length()>100){
	            		float height=getExcelCellAutoHeight(reportDetails, 80f);     
		            	//根据字符串的长度设置高度
		            	row2.setHeightInPoints(height);
	            	}else if(reportDetails.contains("\r\n")){
	            		//float height=getExcelCellAutoHeight(reportDetails, reportDetails.indexOf("\r\n"));     
		            	//根据字符串的长度设置高度
		            	//row2.setHeightInPoints(height);
	            	}*/
	            }
	            row = sheet.createRow(rownum);
	            row.setHeight((short) (25*25));
	            cell = row.createCell(0);
	            cell.setCellValue("各矿当日安全生产情况说明");
	            cell.setCellStyle(style1);
	            if(colnum>0){
	            	sheet.addMergedRegion(new CellRangeAddress(rownum, rownum, 0, colnum));
	            }
	            for(int i=1;i<=colnum;i++){
	            	cell = row.createCell(i);
	            }
		        
	            
	            sheet.setMargin(Sheet.BottomMargin,( double ) 0.5 );// 页边距（下）  
	            sheet.setMargin(Sheet.LeftMargin,( double ) 0.1 );// 页边距（左）  
	            sheet.setMargin(Sheet.RightMargin,( double ) 0.1 );// 页边距（右）  
	            sheet.setMargin(Sheet.TopMargin,( double ) 0.5 );// 页边距（上）  
	            sheet.setRowBreak(rownum-1);//分页
	            sheet.setHorizontallyCenter(true);//设置打印页面为水平居中  
	            //sheet.setVerticallyCenter(true);//设置打印页面为垂直居中
		        HSSFPrintSetup pt = sheet.getPrintSetup();    
		        pt.setLandscape(true); 
		        pt.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
		        pt.setScale((short)70);//打印缩放
		        //String path = ServletActionContext.getServletContext().getRealPath("/WEB-INF/supervisedailyreportfile");
		        //FileOutputStream out = new FileOutputStream(departmentType.getDepartmentTypeSn()+date + ".xls");
		        //wb.write(out);
		        //out.close();
		        if(type.equals("xls")){
		        	ByteArrayOutputStream fout = new ByteArrayOutputStream();  
		            wb.write(fout);
		            wb.close();
		            fout.close();
		        	byte[] fileContent = fout.toByteArray();
		        	InputStream in = new ByteArrayInputStream(fileContent);
		        	//file.delete();
		        	return in;
		        }else{
		        	//String path = ServletActionContext.getServletContext().getRealPath("/WEB-INF/supervisedailyreportfile");
			        FileOutputStream out = new FileOutputStream(departmentType.getDepartmentTypeSn()+date + ".xls");
			        wb.write(out);
			        out.close();
		        	File file = new File(departmentType.getDepartmentTypeSn()+date + ".xls");
		        	Workbook wb2 = new Workbook(departmentType.getDepartmentTypeSn()+date + ".xls");
			        File pdfFile = new File(departmentType.getDepartmentTypeSn()+date + ".pdf");
			        FileOutputStream fileOS = new FileOutputStream(pdfFile);
			        wb2.save(fileOS, SaveFormat.PDF);
			        fileOS.close();
			        File getPdfFile = new File(departmentType.getDepartmentTypeSn()+date + ".pdf");
			        InputStream in = new FileInputStream(getPdfFile);
			        file.delete();
			        getPdfFile.delete();
			        return in;
		        }
			}else{
				return null;
			}
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return null;
	}
}
