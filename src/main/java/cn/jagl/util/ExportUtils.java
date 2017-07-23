package cn.jagl.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.struts2.ServletActionContext;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.aspose.cells.Font;

public class ExportUtils {
	/**
	 * 设置sheet表头信息
	 * @author David
	 * @param headersInfo
	 * @param sheet
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	//设置列宽
	public static void setColumnWidth(HSSFWorkbook wb,HSSFSheet sheet,String templateSn,String headername) throws JDOMException, IOException{
		//获取模板文件
		String path =ServletActionContext.getServletContext().getRealPath("/template");
		//path= path+"\\"+templateSn+".xml";
		File file=new File(path,templateSn+".xml");
		//解析xml模板
		SAXBuilder builder = new SAXBuilder();
		Document parse =  builder.build(file);
		Element root = parse.getRootElement();
		Element colgroup= root.getChild("colgroup");
		List<Element> cols = colgroup.getChildren("col");
		for (int i = 0; i < cols.size(); i++) {
			Element col = cols.get(i);
			Attribute width = col.getAttribute("width");
			String unit = width.getValue().replaceAll("[0-9,\\.]", "");
			String value = width.getValue().replaceAll(unit, "");
			int v=0;
			if(StringUtils.isBlank(unit) || "px".endsWith(unit)){
				v = Math.round(Float.parseFloat(value) * 37F);
			}else if ("em".endsWith(unit)){
				v = Math.round(Float.parseFloat(value) * 267.5F);
			}
			sheet.setColumnWidth(i, v);
		}
		//设置标题
		int rownum = 0;
		int column = 0;
		Element title = root.getChild("title");
		List<Element> trs = title.getChildren("tr");
		for (int i = 0; i < trs.size(); i++) {
			Element tr = trs.get(i);
			List<Element> tds = tr.getChildren("td");
			HSSFRow row = sheet.createRow(rownum);
			HSSFCellStyle cellStyle = wb.createCellStyle();
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			for(column = 0;column <tds.size();column ++){
				Element td = tds.get(column);
				HSSFCell cell = row.createCell(column);
				Attribute rowSpan = td.getAttribute("rowspan");
				Attribute colSpan = td.getAttribute("colspan");
				//Attribute value = td.getAttribute("value");
				//if(value !=null){
					//String val = value.getValue();
					cell.setCellValue(headername);
					int rspan = rowSpan.getIntValue() - 1;
					int cspan = colSpan.getIntValue() -1;
					
					//设置字体
					HSSFFont font = wb.createFont();
					font.setFontName("仿宋_GB2312");
					font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//字体加粗
					font.setFontHeightInPoints((short)12);
					cellStyle.setFont(font);
					cell.setCellStyle(cellStyle);
					//合并单元格居中
					sheet.addMergedRegion(new CellRangeAddress(rspan, rspan, 0, cspan));
				//}
			}
			rownum ++;
		}
		//设置表头
		//数据格式
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);;
        style.setWrapText(true);
		Element thead = root.getChild("thead");
		trs = thead.getChildren("tr");
		for (int i = 0; i < trs.size(); i++) {
			Element tr = trs.get(i);
			HSSFRow row = sheet.createRow(rownum);
			List<Element> ths = tr.getChildren("th");
			for(column = 0;column < ths.size();column++){
				Element th = ths.get(column);
				Attribute valueAttr = th.getAttribute("value");
				HSSFCell cell = row.createCell(column);
				if(valueAttr != null){
					String value =valueAttr.getValue();
					cell.setCellValue(value);
					cell.setCellStyle(style);				
				}
			}
			rownum++;
		}
	}
	
}
