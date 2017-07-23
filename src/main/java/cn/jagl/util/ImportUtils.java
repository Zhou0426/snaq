package cn.jagl.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.struts2.ServletActionContext;
import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
public class ImportUtils{
	
	/**
	 * 马辉2016/06/29
	 */
	private JSONArray array=null;
	private JSONArray error=null;
	JSONObject er=new JSONObject();
	JSONObject erAll=new JSONObject();
	/*
	 * 文件上传处理
	 */
	public  JSONArray  upload(String templateSn,FileModel fileModel,int columnNum){
		//读取excel文件
		try {
			HSSFWorkbook wb=new HSSFWorkbook(FileUtils.openInputStream(fileModel.getFile()));			
			HSSFSheet sheet=wb.getSheetAt(0);
			//获取模板文件
			String path =ServletActionContext.getServletContext().getRealPath("/template");
			//path= path+"\\"+templateSn+".xml";
			String ss=templateSn+".xml";
			File file=new File(path,ss);
			//解析xml模板
			SAXBuilder builder = new SAXBuilder();
			Document parse =  builder.build(file);
			Element root = parse.getRootElement();
			Element tbody = root.getChild("tbody");
			Element tr = tbody.getChild("tr");
			List<Element> children = tr.getChildren("td");
			//解析excel开始行，开始列
			int firstRow = tr.getAttribute("firstrow").getIntValue();
			int firstCol = tr.getAttribute("firstcol").getIntValue();
			//获取excel最后一行行号
			int lastRowNum = sheet.getLastRowNum();
			array =new JSONArray();
			error =new JSONArray();
			//统计错误数
			int erNum=0;
			//循环每一行处理数据
			for(int i=firstRow;i<=lastRowNum;i++){
				try{
					System.out.println("第"+i+"行");
					JSONObject jo=new JSONObject();
					//读取该行
					HSSFRow row=sheet.getRow(i);
					//判断该行是否为
					if(isEmptyRow(row)){
						continue;
					}
					//如果非空行，则取所有单元格值
					for (int j=firstCol;j<columnNum;j++){
						try{
							Element td = children.get(j-firstCol);
							HSSFCell cell = row.getCell(j);
							//如果单元格为null，继续处理下一个
							System.out.println(cell);
							System.out.println(td.getAttribute("name").getValue());
							if(cell==null){
								jo.put(td.getAttribute("name").getValue(),"null");
								continue;
							}
							//获取单元格属性值
							Object value=getCellValue(cell,td);
							if(String.valueOf(value).indexOf("#000")>=0){
								erNum++;
								er.put("error"+erNum, value);
							}else if(value!=null&&String.valueOf(value).trim().length()>0){
								jo.put(td.getAttribute("name").getValue(),value);
							}else{
								jo.put(td.getAttribute("name").getValue(),"null");
							}
						}catch(Exception e){
							JSONObject er3=new JSONObject();
							int z=j+1;
							er3.put("error1", "第"+i+"行,第"+z+"列数据读取错误，程序被迫终止，请确保使用正确的单元格格式！");
							erAll.put("erNum", 1);
							error.put(er3);
							error.put(erAll);
							return error;
						}
						
					}
					array.put(jo);
				}catch(Exception e){
					JSONObject er2=new JSONObject();
					er2.put("error1", "第"+i+"行读取错误，程序被迫终止，请确保使用正确的单元格格式！");
					erAll.put("erNum", 1);
					error.put(er2);
					error.put(erAll);
					return error;
				}
							
			}
			erAll.put("erNum", erNum);
			error.put(er);
			error.put(erAll);
		} catch (Exception e) {
			JSONObject er4=new JSONObject();
			er4.put("error1", "获取文件出错，程序被迫终止，请确保使用正确模板！");
			erAll.put("erNum", 1);
			error.put(er4);
			error.put(erAll);
			return error;
		}
		if(error.getJSONObject(0).has("error1")){
			return error;			
		}
		return array;	
	}
	/*
	 * 获取单元格属性值并进行校验
	 * 
	 */
	private Object getCellValue(HSSFCell cell, Element td) {
		int i=cell.getRowIndex()+1;
		int j=cell.getColumnIndex()+1;
		//返回值为空
		Object returnValue="";		
		try {
			//获取单元格格式限制
			String type=td.getAttribute("type").getValue();
			boolean isNullAble=td.getAttribute("isnullable").getBooleanValue();
			int maxLength=9999;			
			if(td.getAttribute("maxlength")!=null){
				maxLength=td.getAttribute("maxlength").getIntValue();
			}
			Object value=null;
			//根据格式取出单元格的值
			switch(cell.getCellType()){
				case HSSFCell.CELL_TYPE_STRING:{
					value=cell.getStringCellValue();
					break;
				}
				case HSSFCell.CELL_TYPE_BOOLEAN:{
					value=cell.getBooleanCellValue();
					break;
				}
				case HSSFCell.CELL_TYPE_NUMERIC:{
					if("datetime,date".indexOf(type)>=0){
						Date date = cell.getDateCellValue();
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						value = df.format(date);
					}else if("int".indexOf(type)>=0){
						int numericCellValue =(int)cell.getNumericCellValue();
						value = numericCellValue;
					}else if("float".indexOf(type)>=0){
						float numericCellValue =(float)cell.getNumericCellValue();
						value=numericCellValue;
					}
					break;
				}
				case HSSFCell.CELL_TYPE_FORMULA: 
					 FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
					 evaluator.evaluateFormulaCell(cell);
					 CellValue cellValue = evaluator.evaluate(cell);
					 value = cellValue.getNumberValue() ;
					 break;
				default:{
					value=cell.getStringCellValue();
				}
			}
			//对非空、长度进行校验
			if(!isNullAble && StringUtils.isBlank(String.valueOf(value))){
				//错误编码,错误位置原因,单位格的值
				returnValue = "#0001,第" + i + "行第" +j +"列不能为空！";
			}else if(StringUtils.isNotBlank(String.valueOf(value)) && (String.valueOf(value).length()>maxLength)){
				returnValue = "#0002,第" + i + "行第" +j +"列长度超过最大长度！";
			}else{
				returnValue =  value;
			}
		} catch (DataConversionException e) {
			e.printStackTrace();
		}
		return returnValue;
	}
	/*
	 * 判断某行数据是否为空
	 * 一行中的前两列的值为空，则判定此行是空行！
	 */
	private boolean isEmptyRow(HSSFRow row) {
		boolean flag=true;
		for(int i=0;i<2;i++){
			HSSFCell cell=row.getCell(i);
			if(cell!=null){
				if(StringUtils.isNotBlank(cell.toString())){
					return false;
				}
			}
		}
		return flag;
	}
}
