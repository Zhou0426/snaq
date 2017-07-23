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
	 * ���2016/06/29
	 */
	private JSONArray array=null;
	private JSONArray error=null;
	JSONObject er=new JSONObject();
	JSONObject erAll=new JSONObject();
	/*
	 * �ļ��ϴ�����
	 */
	public  JSONArray  upload(String templateSn,FileModel fileModel,int columnNum){
		//��ȡexcel�ļ�
		try {
			HSSFWorkbook wb=new HSSFWorkbook(FileUtils.openInputStream(fileModel.getFile()));			
			HSSFSheet sheet=wb.getSheetAt(0);
			//��ȡģ���ļ�
			String path =ServletActionContext.getServletContext().getRealPath("/template");
			//path= path+"\\"+templateSn+".xml";
			String ss=templateSn+".xml";
			File file=new File(path,ss);
			//����xmlģ��
			SAXBuilder builder = new SAXBuilder();
			Document parse =  builder.build(file);
			Element root = parse.getRootElement();
			Element tbody = root.getChild("tbody");
			Element tr = tbody.getChild("tr");
			List<Element> children = tr.getChildren("td");
			//����excel��ʼ�У���ʼ��
			int firstRow = tr.getAttribute("firstrow").getIntValue();
			int firstCol = tr.getAttribute("firstcol").getIntValue();
			//��ȡexcel���һ���к�
			int lastRowNum = sheet.getLastRowNum();
			array =new JSONArray();
			error =new JSONArray();
			//ͳ�ƴ�����
			int erNum=0;
			//ѭ��ÿһ�д�������
			for(int i=firstRow;i<=lastRowNum;i++){
				try{
					System.out.println("��"+i+"��");
					JSONObject jo=new JSONObject();
					//��ȡ����
					HSSFRow row=sheet.getRow(i);
					//�жϸ����Ƿ�Ϊ
					if(isEmptyRow(row)){
						continue;
					}
					//����ǿ��У���ȡ���е�Ԫ��ֵ
					for (int j=firstCol;j<columnNum;j++){
						try{
							Element td = children.get(j-firstCol);
							HSSFCell cell = row.getCell(j);
							//�����Ԫ��Ϊnull������������һ��
							System.out.println(cell);
							System.out.println(td.getAttribute("name").getValue());
							if(cell==null){
								jo.put(td.getAttribute("name").getValue(),"null");
								continue;
							}
							//��ȡ��Ԫ������ֵ
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
							er3.put("error1", "��"+i+"��,��"+z+"�����ݶ�ȡ���󣬳�������ֹ����ȷ��ʹ����ȷ�ĵ�Ԫ���ʽ��");
							erAll.put("erNum", 1);
							error.put(er3);
							error.put(erAll);
							return error;
						}
						
					}
					array.put(jo);
				}catch(Exception e){
					JSONObject er2=new JSONObject();
					er2.put("error1", "��"+i+"�ж�ȡ���󣬳�������ֹ����ȷ��ʹ����ȷ�ĵ�Ԫ���ʽ��");
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
			er4.put("error1", "��ȡ�ļ�������������ֹ����ȷ��ʹ����ȷģ�壡");
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
	 * ��ȡ��Ԫ������ֵ������У��
	 * 
	 */
	private Object getCellValue(HSSFCell cell, Element td) {
		int i=cell.getRowIndex()+1;
		int j=cell.getColumnIndex()+1;
		//����ֵΪ��
		Object returnValue="";		
		try {
			//��ȡ��Ԫ���ʽ����
			String type=td.getAttribute("type").getValue();
			boolean isNullAble=td.getAttribute("isnullable").getBooleanValue();
			int maxLength=9999;			
			if(td.getAttribute("maxlength")!=null){
				maxLength=td.getAttribute("maxlength").getIntValue();
			}
			Object value=null;
			//���ݸ�ʽȡ����Ԫ���ֵ
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
			//�Էǿա����Ƚ���У��
			if(!isNullAble && StringUtils.isBlank(String.valueOf(value))){
				//�������,����λ��ԭ��,��λ���ֵ
				returnValue = "#0001,��" + i + "�е�" +j +"�в���Ϊ�գ�";
			}else if(StringUtils.isNotBlank(String.valueOf(value)) && (String.valueOf(value).length()>maxLength)){
				returnValue = "#0002,��" + i + "�е�" +j +"�г��ȳ�����󳤶ȣ�";
			}else{
				returnValue =  value;
			}
		} catch (DataConversionException e) {
			e.printStackTrace();
		}
		return returnValue;
	}
	/*
	 * �ж�ĳ�������Ƿ�Ϊ��
	 * һ���е�ǰ���е�ֵΪ�գ����ж������ǿ��У�
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
