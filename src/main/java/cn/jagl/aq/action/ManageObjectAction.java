package cn.jagl.aq.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.struts2.ServletActionContext;
import org.hibernate.exception.ConstraintViolationException;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.jagl.aq.domain.AccidentType;
import cn.jagl.aq.domain.CheckTaskAppraisals;
import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.DepartmentType;
import cn.jagl.aq.domain.Hazard;
import cn.jagl.aq.domain.ManageObject;
import cn.jagl.aq.domain.ManageObjectType;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.RiskLevel;
import cn.jagl.aq.domain.Role;
import cn.jagl.aq.domain.StandardIndex;

@SuppressWarnings("serial")
public class ManageObjectAction extends BaseAction<ManageObject>
{
	private String id;
	private int ids;
	private int getData;//��ȡsession����
	private String departmentTypeSn;//�����������ͱ��
	private String departmentSn;//���ű��
	private String manageObjectSn;//���������
	private String manageObjectName;//�����������
	private ManageObjectType manageObjectType;//�����������
	private String parentManageObjectSn;//�����������
	private File uploadExcel;//��ȡ�ϴ��ļ���
	private String uploadExcelFileName;//��ȡ�ϴ��ļ���
	private String uploadExcelContentType;//��ȡ�ļ�����
	private String hazardList;//���ӹ�������Σ��Դ��ӳ��
	private String pag;//����ַ�
	private String q;//�������͵Ĳ���
	private InputStream excelStream;//�����
	private String excelFileName;//�����ļ���
	private String standardSn;
	private String yearTime;//���
	private String monthTime;//�·�
	private boolean checked;//�Ƿ�ѡ���¼�����
	private String rowsData;//���ҿ��˵�ҳ������
	//��ȡ���ݵĶ���
	private Class<ManageObject> manageObject;
	
	
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public String getRowsData() {
		return rowsData;
	}
	public void setRowsData(String rowsData) {
		this.rowsData = rowsData;
	}
	public String getStandardSn() {
		return standardSn;
	}
	public void setStandardSn(String standardSn) {
		this.standardSn = standardSn;
	}
	public String getYearTime() {
		return yearTime;
	}
	public void setYearTime(String yearTime) {
		this.yearTime = yearTime;
	}
	public String getMonthTime() {
		return monthTime;
	}
	public void setMonthTime(String monthTime) {
		this.monthTime = monthTime;
	}
	public String getDepartmentSn() {
		return departmentSn;
	}
	public void setDepartmentSn(String departmentSn) {
		this.departmentSn = departmentSn;
	}
	public int getGetData() {
		return getData;
	}
	public void setGetData(int getData) {
		this.getData = getData;
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
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	public String getPag() {
		return pag;
	}
	public void setPag(String pag) {
		this.pag = pag;
	}
	public String getHazardList() {
		return hazardList;
	}
	public void setHazardList(String hazardList) {
		this.hazardList = hazardList;
	}
	public File getUploadExcel() {
		return uploadExcel;
	}
	public void setUploadExcel(File uploadExcel) {
		this.uploadExcel = uploadExcel;
	}
	public String getUploadExcelFileName() {
		return uploadExcelFileName;
	}
	public void setUploadExcelFileName(String uploadExcelFileName) {
		this.uploadExcelFileName = uploadExcelFileName;
	}
	public String getUploadExcelContentType() {
		return uploadExcelContentType;
	}
	public void setUploadExcelContentType(String uploadExcelContentType) {
		this.uploadExcelContentType = uploadExcelContentType;
	}

	public void setIds(int ids) {
		this.ids = ids;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDepartmentTypeSn() {
		return departmentTypeSn;
	}
	public void setDepartmentTypeSn(String departmentTypeSn) {
		this.departmentTypeSn = departmentTypeSn;
	}
	public String getManageObjectSn() {
		return manageObjectSn;
	}
	public void setManageObjectSn(String manageObjectSn) {
		this.manageObjectSn = manageObjectSn;
	}
	public String getManageObjectName() {
		return manageObjectName;
	}
	public void setManageObjectName(String manageObjectName) {
		this.manageObjectName = manageObjectName;
	}
	public ManageObjectType getManageObjectType() {
		return manageObjectType;
	}
	public void setManageObjectType(ManageObjectType manageObjectType) {
		this.manageObjectType = manageObjectType;
	}
	public String getParentManageObjectSn() {
		return parentManageObjectSn;
	}
	public void setParentManageObjectSn(String parentManageObjectSn) {
		this.parentManageObjectSn = parentManageObjectSn;
	}

	public Class<ManageObject> getManageObject() {
		return manageObject;
	}
	public void setManageObject(Class<ManageObject> manageObject) {
		this.manageObject = manageObject;
	}

	//���ݸ�����������ȡ�����Ӽ�����
	public String show() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out;  
        out = response.getWriter();
        List<ManageObject> jsonLoad=null;
		String hql="";
		if (parentManageObjectSn!=null){
			hql = "FROM ManageObject m where m.parent.manageObjectSn='"+parentManageObjectSn+"'";
		}else{
			hql = "FROM ManageObject m where 1!=1";
		}
		jsonLoad=manageObjectService.findByPage(hql,page,rows);
		long total=manageObjectService.findNum(parentManageObjectSn);
		JSONArray jsonArray=new JSONArray();
		for(ManageObject manageObject:jsonLoad){
			JSONObject jo=new JSONObject();
			jo.put("ids", manageObject.getId());
			jo.put("departmentTypeSn", manageObject.getDepartmentType().getDepartmentTypeSn());
			jo.put("departmentTypeName", manageObject.getDepartmentType().getDepartmentTypeName());
			jo.put("manageObjectSn", manageObject.getManageObjectSn());
			jo.put("manageObjectName", manageObject.getManageObjectName());
			jo.put("manageObjectType", manageObject.getManageObjectType());
			jo.put("parent",manageObject.getParent().getManageObjectSn());
			jo.put("hazardNum", manageObject.getHazards().size());
			jsonArray.put(jo);
		}
		Map<String, Object> json = new HashMap<String, Object>();
        json.put("total", total);// total�� ����ܼ�¼���������
        json.put("rows", jsonArray);// rows�� ���ÿҳ��¼ list
        String Str= JSONObject.valueToString(json);
		out.print(Str);
		out.flush();
		out.close();
		return "josnLoad";
	}
	
	//����Σ��Դ��Ų�ѯ��ع������
	public String showManageObject() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out;  
        out = response.getWriter();
		Long a=0l;
		int b=0;
		String Str=null;
		JSONArray jsonArray=new JSONArray();
		List<ManageObject> list=new ArrayList<ManageObject>();
		if(hazardList!=null&&hazardList.trim().length()>0){
			Hazard hazard=hazardService.getByHazardSn(hazardList);
			Set<ManageObject> set=hazard.getManageObjects();
			if(set.size()>0){
				for(Iterator<ManageObject> iter=set.iterator();iter.hasNext();){
					list.add(iter.next());
					a++;
				}
				for(ManageObject manageObject:list){
					b++;
					JSONObject jo=new JSONObject();
					jo.put("id", manageObject.getId());
					jo.put("departmentTypeName",manageObject.getDepartmentType().getDepartmentTypeName() );
					jo.put("manageObjectSn",manageObject.getManageObjectSn());
					jo.put("manageObjectName",manageObject.getManageObjectName());
					jo.put("manageObjectType",manageObject.getManageObjectType());
					jo.put("parentName",manageObject.getParent().getManageObjectName());
					if(b>(page-1)*rows&&b<=rows*page){
						jsonArray.put(jo);
					}
					if(b>page*rows){
						break;
					}
				}
			}
		}
		Map<String, Object> json = new HashMap<String, Object>();
        json.put("total", a);// total�� ����ܼ�¼���������
        json.put("rows", jsonArray);// rows�� ���ÿҳ��¼ list
        Str= JSONObject.valueToString(json);
		out.print(Str);
		out.flush();
		out.close();
		return "josnLoad";
	}
	//���ӹ�����������Σ��Դ
	public String addHazard() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out;  
        out = response.getWriter();
		ManageObject manageObject=manageObjectService.getByManageObjectSn(manageObjectSn);
		Hazard hazard=hazardService.getByHazardSn(hazardList);
		boolean a=manageObject.getHazards().add(hazard);
		try{
			manageObjectService.update(manageObject);
		}catch(Exception e){
			pag="ERROR";
			out.print(pag);
			out.flush();
			out.close();
			return "josnLoad";
		}
		if(a==true){
			pag="SUCCESS";
		}else{
			pag="ERROR";
		}
		out.print(pag);
		out.flush();
		out.close();
		return "josnLoad";
	}
	//ɾ��������������Σ��Դ
	public String deleteHazard() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out;  
        out = response.getWriter();
		ManageObject manageObject=manageObjectService.getByManageObjectSn(manageObjectSn);
		Hazard hazard=hazardService.getByHazardSn(hazardList);
		boolean a= manageObject.getHazards().remove(hazard);
		try{
			manageObjectService.update(manageObject);
		}catch(Exception e){
			pag="ERROR";
			out.print(pag);
			out.flush();
			out.close();
			return "josnLoad";
		}
		
		if(a==true){
			pag="SUCCESS";
		}else{
			pag="ERROR";
		}
		out.print(pag);
		out.flush();
		out.close();
		return "josnLoad";
	}
	//��������
	public String add(){
		ManageObject man=manageObjectService.getByManageObjectSn(manageObjectSn);
		if(man!=null){
			pag=LOGIN;
			return SUCCESS;
		}else{
			ManageObject manageObject=new ManageObject();
			DepartmentType departmentType=departmentTypeService.getByDepartmentTypeSn(departmentTypeSn);
			manageObject.setDepartmentType(departmentType);
			manageObject.setManageObjectSn(manageObjectSn);
			manageObject.setManageObjectName(manageObjectName);
			manageObject.setManageObjectType(manageObjectType);
			ManageObject parentManageObject=manageObjectService.getByManageObjectSn(parentManageObjectSn);
			manageObject.setParent(parentManageObject);
			try{
				manageObjectService.addManageObject(manageObject);
				pag=SUCCESS;
			}catch(Exception e){
				pag=ERROR;
			}
			return SUCCESS;
		}
	}
	//��������
	public String update(){
		ManageObject manageObject=manageObjectService.getById(ManageObject.class,ids);
		ManageObject man=manageObjectService.getByManageObjectSn(manageObjectSn);
		//�жϸ��ݱ�Ų��ҵ������Ƿ����
		if(man!=null){
			//�жϱ�Ų��ҳ��������Ƿ���ԭ����
			if(man.getId()!=manageObject.getId()){
				pag=LOGIN;
				return SUCCESS;
			}
		}
		manageObject.setManageObjectSn(manageObjectSn);
		manageObject.setManageObjectName(manageObjectName);
		manageObject.setManageObjectType(manageObjectType);
		try{
			manageObjectService.update(manageObject);
			pag=SUCCESS;
		}catch(Exception e){
			pag=ERROR;
		}
		return SUCCESS;
	}
	//ɾ������
	public String delete(){
		try{
			manageObjectService.deleteManageObject(ids);
			pag=SUCCESS;
		}catch(Exception e){
			pag=ERROR;
		}
		return SUCCESS;
	}
	//Ȩ�޹���
	public String manager(){
		String pId=(String)session.get("personId");
    	Person person=personService.getByPersonId(pId);
        //Person person=(Person)ServletActionContext.getRequest().getSession().getAttribute("person");
        String sum="";
        if(person!=null){
        	sum="(";
        	String departmentSn=person.getDepartment().getDepartmentSn();
        	Role role = roleService.getByRoleSn("jtxtgly");
        	if(person.getRoles().contains(role)){
        		departmentSn = null;
        	}
        	List<DepartmentType> departmentTypes=departmentTypeService.getImplDepartmentTypesByLeft(departmentSn);
        	if(departmentTypes.size()!=0){
        		for(DepartmentType de:departmentTypes){
        			sum+="'"+de.getDepartmentTypeSn()+"',";
        		}
        	}else{
        		Department department=departmentService.getUpNearestImplDepartment(departmentSn);
        		sum=sum+"'"+department.getDepartmentType().getDepartmentTypeSn()+"',";
        	}
        	sum=sum.substring(0, sum.length()-1);
        	sum+=")";
        }
        return sum;
	}
	//��ȡ��λ����json����
	public String load()throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        List<DepartmentType> jsonLoad=null;
        PrintWriter out;  
        out = response.getWriter();
        String hql="";
        String power=manager();
        if(power != null && !"".equals(power)){
        	hql="FROM DepartmentType d where d.isImplDepartmentType = true and d.departmentTypeSn in " + power;
        	jsonLoad=departmentTypeService.query(hql);
        	JSONArray tree=new JSONArray();
        	for(DepartmentType departmentType:jsonLoad){
        		JSONObject jo=new JSONObject();
        		jo.put("id",departmentType.getDepartmentTypeSn());
        		jo.put("text",departmentType.getDepartmentTypeName());
        		jo.put("value",departmentType.getDepartmentTypeSn() );
        		jo.put("state","closed");
        		tree.put(jo);
        	}
        	out.print(tree.toString());
        	out.flush(); 
        	out.close();  
        }
		return "josnLoad";
	}
	//��ȡ�����������json����
	public String loadParent()throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");
        response.setContentType("text/plain; charset=utf-8");
        List<ManageObject> jsonLoad=null;
        PrintWriter out;
        out = response.getWriter();
        String hql="";
        if(id!=null&&id.trim().length()>0){
        	jsonLoad=manageObjectService.getByParentSn(id,departmentTypeSn);
        }else{
        	hql = "FROM ManageObject m where m.parent is null and m.departmentType.departmentTypeSn='"+departmentTypeSn+"'";
        	jsonLoad=manageObjectService.findByPage(hql,1,10000);
        }
		JSONArray tree=new JSONArray();
		for(ManageObject manageObject:jsonLoad){
			JSONObject jo=new JSONObject();
			jo.put("id",manageObject.getManageObjectSn());
			jo.put("text",manageObject.getManageObjectName());
			if(manageObject.getChildren().size()>0){
				jo.put("state","closed");
			}else{
				jo.put("state", "open");
			}
			tree.put(jo);
		}
		out.print(tree.toString());
        out.flush(); 
        out.close();  
		return "josnLoad";
	}
	//Σ��Դ��������Ĺ�����������б�
	public String manageObjectList() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setContentType("text/plain; charset=utf-8");
		PrintWriter out=response.getWriter();
		List<ManageObject> jsonLoad=null;
		jsonLoad=manageObjectService.getByMoHuFind(q);
		JSONArray jsonArray=new JSONArray();
		for(ManageObject man:jsonLoad){
			JSONObject jo=new JSONObject();
			jo.put("id", man.getManageObjectSn());
			jo.put("text", man.getManageObjectName());
			jo.put("value", man.getManageObjectSn());
			jo.put("state", "closed");
			jsonArray.put(jo);
		}
		out.print(jsonArray.toString());
		out.flush();
		out.close();
		return "josnLoad";
		
	}
	//��ȡsession�е�progressValueֵ  
	public String findSession(){
		getData=(int)session.get("progressValue");
		return LOGIN;
	}
	//��ȡsession�е�progressValueֵ  
	public String findProValue(){
		getData=(int)session.get("proValue");
		return LOGIN;
	}
	//���session�е�ֵ
	public void clearSession(){
		session.put("proValue",0);
	}
	//������󵼳�Excel
	public String exportExcel(){
		
		String hql="FROM ManageObject m WHERE m.departmentType.departmentTypeSn='"+departmentTypeSn+"' and m.parent.manageObjectSn='"+parentManageObjectSn+"'";
		if(parentManageObjectSn==null || "".equals(parentManageObjectSn)){
			hql="FROM ManageObject m WHERE m.departmentType.departmentTypeSn='"+departmentTypeSn+"'";
		}
		if(id!=null){
			hql="FROM ManageObject m WHERE m.departmentType.departmentTypeSn='"+departmentTypeSn+"'";
		}
			List<ManageObject> list=manageObjectService.getManageObjectsByHql(hql);
	        // ��һ��������һ��workbook����Ӧһ��Excel�ļ�
	        HSSFWorkbook workbook = new HSSFWorkbook();
	        // �ڶ�������webbook�����һ��sheet,��ӦExcel�ļ��е�sheet
	        HSSFSheet hssfSheet = workbook.createSheet("�������");
	        // ����������sheet����ӱ�ͷ��0��,ע���ϰ汾poi��Excel����������������short
	        HSSFRow hssfRow = hssfSheet.createRow(0);
	        // ���Ĳ���������Ԫ�񣬲�����ֵ��ͷ ���ñ�ͷ����
	        HSSFCellStyle hssfCellStyle = workbook.createCellStyle();
	        //������ʽ
	        hssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        HSSFFont font = workbook.createFont();
	        font.setColor(HSSFColor.VIOLET.index);
	        font.setFontHeightInPoints((short) 10);
	        hssfCellStyle.setFont(font);
	        
	        HSSFCell hssfCell = hssfRow.createCell(0);//��������0��ʼ
	        hssfCell.setCellValue("�������ͱ��");//����1
	        hssfCell.setCellStyle(hssfCellStyle);//�о�����ʾ
	        hssfCell = hssfRow.createCell(1);
	        hssfCell.setCellValue("������������");//����2
	        hssfCell.setCellStyle(hssfCellStyle);
	        hssfCell = hssfRow.createCell(2);
	        hssfCell.setCellValue("���������");//����3
	        hssfCell.setCellStyle(hssfCellStyle);
	        hssfCell = hssfRow.createCell(3);
	        hssfCell.setCellValue("�����������");//����4
	        hssfCell.setCellStyle(hssfCellStyle);
	        hssfCell = hssfRow.createCell(4);
	        hssfCell.setCellValue("�����������");//����5
	        hssfCell.setCellStyle(hssfCellStyle);
	        hssfCell = hssfRow.createCell(5);
	        hssfCell.setCellValue("�������������");//����6
	        hssfCell.setCellStyle(hssfCellStyle);
	        hssfCell = hssfRow.createCell(6);
	        hssfCell.setCellValue("���������������");//����7
	        hssfCell.setCellStyle(hssfCellStyle);

	        float num=list.size();
	        // ���岽��д��ʵ������ ʵ��Ӧ������Щ���ݴ����ݿ�õ���
	        for (int i = 0; i < list.size(); i++) {
	        	session.put("proValue",(int)((float)i*100/num));
	            hssfRow = hssfSheet.createRow(i+1);
	            ManageObject manageObject = list.get(i);
	            // ��������������Ԫ�񣬲�����ֵ
	            if(manageObject.getDepartmentType()!=null){
	            	hssfRow.createCell(0).setCellValue(manageObject.getDepartmentType().getDepartmentTypeSn());
	            	hssfRow.createCell(1).setCellValue(manageObject.getDepartmentType().getDepartmentTypeName());
	            }
	            hssfRow.createCell(2).setCellValue(manageObject.getManageObjectSn());
	            hssfRow.createCell(3).setCellValue(manageObject.getManageObjectName());
	            if(manageObject.getManageObjectType()!=null){
	            	hssfRow.createCell(4).setCellValue(manageObject.getManageObjectType().name());
	            }
	            if(manageObject.getParent()!=null){
	            	hssfRow.createCell(5).setCellValue(manageObject.getParent().getManageObjectSn());
	            	hssfRow.createCell(6).setCellValue(manageObject.getParent().getManageObjectName());
	            }
	        }
	        // ���߲������ļ��浽ָ��λ��
	        try {
	        	ByteArrayOutputStream fout = new ByteArrayOutputStream();  
	        	workbook.write(fout);
	        	workbook.close();
	            fout.close();
	            byte[] fileContent = fout.toByteArray();  
	            ByteArrayInputStream is = new ByteArrayInputStream(fileContent);
	            
	            excelStream=is;
				if(id!=null){
					excelFileName=URLEncoder.encode("������󵼳�ȫ��.xls","UTF-8");
				}else{
					excelFileName=URLEncoder.encode("������󵼳�����.xls","UTF-8");
				}
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			session.put("proValue",100);
	        return SUCCESS;
	}
	//�������Σ��Դ��������
	public String exportExcelRelation(){
		String hql="FROM ManageObject m WHERE m.departmentType.departmentTypeSn='"+departmentTypeSn+"' and m.parent.manageObjectSn='"+parentManageObjectSn+"'";
		if(parentManageObjectSn==null || "".equals(parentManageObjectSn)){
			hql="FROM ManageObject m WHERE m.departmentType.departmentTypeSn='"+departmentTypeSn+"'";
		}
		if(id!=null){
			hql="FROM ManageObject m WHERE m.departmentType.departmentTypeSn='"+departmentTypeSn+"'";
		}
			List<ManageObject> list=manageObjectService.getManageObjectsByHql(hql);
	        // ��һ��������һ��workbook����Ӧһ��Excel�ļ�
	        HSSFWorkbook workbook = new HSSFWorkbook();
	        // �ڶ�������webbook�����һ��sheet,��ӦExcel�ļ��е�sheet
	        HSSFSheet hssfSheet = workbook.createSheet("�������Σ��Դ������");
	        // ����������sheet����ӱ�ͷ��0��,ע���ϰ汾poi��Excel����������������short
	        HSSFRow hssfRow = hssfSheet.createRow(0);
	        // ���Ĳ���������Ԫ�񣬲�����ֵ��ͷ ���ñ�ͷ����
	        HSSFCellStyle hssfCellStyle = workbook.createCellStyle();
	        //������ʽ
	        hssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        HSSFFont font = workbook.createFont();
	        font.setColor(HSSFColor.VIOLET.index);
	        font.setFontHeightInPoints((short) 10);
	        hssfCellStyle.setFont(font);
	        
	        HSSFCell hssfCell = hssfRow.createCell(0);//��������0��ʼ
	        hssfCell.setCellValue("���������");//����1
	        hssfCell.setCellStyle(hssfCellStyle);//�о�����ʾ
	        hssfCell = hssfRow.createCell(1);
	        hssfCell.setCellValue("�����������");//����2
	        hssfCell.setCellStyle(hssfCellStyle);
	        hssfCell = hssfRow.createCell(2);
	        hssfCell.setCellValue("Σ��Դ���");//����3
	        hssfCell.setCellStyle(hssfCellStyle);
	        hssfCell = hssfRow.createCell(3);
	        hssfCell.setCellValue("Σ��Դ����");//����4
	        hssfCell.setCellStyle(hssfCellStyle);
	        
	        // ���岽��д��ʵ������

	        int j=1;
	        float num=list.size();
	        ManageObject manageObject =null;
	        Set<Hazard> hazards=null;
	        for (int i = 0; i < list.size(); i++) {
	        	session.put("proValue",(int)((float)i*100/num));
	        	manageObject = list.get(i);//�������
	        	//������������set����
	        	hazards=manageObject.getHazards();
	        	
	        	if(hazards.size()>0){
	        		for(Hazard hazard:hazards){
	        			hssfRow = hssfSheet.createRow(j);
	        			// ��������������Ԫ�񣬲�����ֵ
	        			hssfRow.createCell(0).setCellValue(manageObject.getManageObjectSn());
	        			hssfRow.createCell(1).setCellValue(manageObject.getManageObjectName());
	        			hssfRow.createCell(2).setCellValue(hazard.getHazardSn());
	        			hssfRow.createCell(3).setCellValue(hazard.getHazardDescription());
	        			j++;
	        		}
	        	}
	        }
	        // ���߲������ļ��浽ָ��λ��
	        try {
	        	ByteArrayOutputStream fout = new ByteArrayOutputStream();  
	        	workbook.write(fout);
	        	workbook.close();
	            fout.close();
	            byte[] fileContent = fout.toByteArray();  
	            ByteArrayInputStream is = new ByteArrayInputStream(fileContent);
	            
	            excelStream=is;
				if(id!=null){
					excelFileName=URLEncoder.encode("�������Σ��Դ��������ȫ��.xls","UTF-8");
				}else{
					excelFileName=URLEncoder.encode("�������Σ��Դ������������.xls","UTF-8");
				}
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        session.put("proValue",100);
	        return SUCCESS;
	}
	//����Σ��ԴExcel
	public String exportHazardExcel(){
			List<Hazard> list=new ArrayList<Hazard>();
			String hql;
			if(id==null || id.trim().length()==0){
				if(hazardList!=null && !"".equals(hazardList)){
					hql="select h from Hazard h where h.departmentType.departmentTypeSn ='"+departmentTypeSn+"' and (h.hazardSn like '%"+hazardList+"%' or h.hazardDescription like '%"+hazardList+"%')";
					list=hazardService.getHazardsByHql(hql);
				}else{
					if(manageObjectSn!=null && !"".equals(manageObjectSn)){
						
						List<ManageObject> listManageObject=manageObjectService.getByMoHuFind(manageObjectSn);
						for(ManageObject manageObject:listManageObject){
							Set<Hazard> set=manageObject.getHazards();
							if(set.size()>0){
								for(Iterator<Hazard> iter=set.iterator();iter.hasNext();){
									list.add(iter.next());
								}
							}
						}
					}else{
						hql="select h from Hazard h where h.departmentType.departmentTypeSn ='"+departmentTypeSn+"'";
						list=hazardService.getHazardsByHql(hql);
					}
					
				}
			}else{
				hql="FROM Hazard h where h.departmentType.departmentTypeSn ='"+departmentTypeSn+"'";
				list=hazardService.getHazardsByHql(hql);
			}
			
			// ��һ��������һ��workbook����Ӧһ��Excel�ļ�
	        HSSFWorkbook workbook = new HSSFWorkbook();
	        // �ڶ�������webbook�����һ��sheet,��ӦExcel�ļ��е�sheet
	        HSSFSheet hssfSheet = workbook.createSheet("Σ��Դ");
	        // ����������sheet����ӱ�ͷ��0��,ע���ϰ汾poi��Excel����������������short
	        HSSFRow hssfRow = hssfSheet.createRow(0);
	        // ���Ĳ���������Ԫ�񣬲�����ֵ��ͷ ���ñ�ͷ����
	        HSSFCellStyle hssfCellStyle = workbook.createCellStyle();
	        //������ʽ
	        hssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        HSSFFont font = workbook.createFont();
	        font.setColor(HSSFColor.VIOLET.index);
	        font.setFontHeightInPoints((short) 10);
	        hssfCellStyle.setFont(font);
	        
	        HSSFCell hssfCell = hssfRow.createCell(0);//��������0��ʼ
	        hssfCell.setCellValue("�������ͱ��");//����1
	        hssfCell.setCellStyle(hssfCellStyle);//�о�����ʾ
	        hssfCell = hssfRow.createCell(1);
	        hssfCell.setCellValue("������������");//����2
	        hssfCell.setCellStyle(hssfCellStyle);
	        hssfCell = hssfRow.createCell(2);
	        hssfCell.setCellValue("Σ��Դ���");//����3
	        hssfCell.setCellStyle(hssfCellStyle);
	        hssfCell = hssfRow.createCell(3);
	        hssfCell.setCellValue("Σ��Դ����");//����4
	        hssfCell.setCellStyle(hssfCellStyle);
	        hssfCell = hssfRow.createCell(4);
	        hssfCell.setCellValue("Σ��Դ�������");//����5
	        hssfCell.setCellStyle(hssfCellStyle);
	        hssfCell = hssfRow.createCell(5);
	        hssfCell.setCellValue("���յȼ�");//����6
	        hssfCell.setCellStyle(hssfCellStyle);
	        hssfCell = hssfRow.createCell(6);
	        hssfCell.setCellValue("�¹����ͱ��");//����7
	        hssfCell.setCellStyle(hssfCellStyle);
	        hssfCell = hssfRow.createCell(7);
	        hssfCell.setCellValue("�¹���������");//����8
	        hssfCell.setCellStyle(hssfCellStyle);
	        
	        float num=list.size();
	        // ���岽��д��ʵ������ ʵ��Ӧ������Щ���ݴ����ݿ�õ���
	        for (int i = 0; i < list.size(); i++) {
	        	session.put("proValue",(int)((float)i*100/num));
	            hssfRow = hssfSheet.createRow(i+1);
	            Hazard hazard = list.get(i);
	            // ��������������Ԫ�񣬲�����ֵ
	            if(hazard.getDepartmentType()!=null){
	            	hssfRow.createCell(0).setCellValue(hazard.getDepartmentType().getDepartmentTypeSn());
	            	hssfRow.createCell(1).setCellValue(hazard.getDepartmentType().getDepartmentTypeName());
	            }
	            hssfRow.createCell(2).setCellValue(hazard.getHazardSn());
	            hssfRow.createCell(3).setCellValue(hazard.getHazardDescription());
	            hssfRow.createCell(4).setCellValue(hazard.getResultDescription());
	            if(hazard.getRiskLevel()!=null){
	            	hssfRow. createCell(5).setCellValue(hazard.getRiskLevel().name());
	            }
	            if(hazard.getAccidentType()!=null){
	            	hssfRow.createCell(6).setCellValue(hazard.getAccidentType().getAccidentTypeSn());
	            	hssfRow.createCell(7).setCellValue(hazard.getAccidentType().getAccidentTypeName());
	            }
	            
	        }
	        // ���߲������ļ��浽ָ��λ��
	        try {
	        	
	        	ByteArrayOutputStream fout = new ByteArrayOutputStream();  
	        	workbook.write(fout);
	        	workbook.close();
	            fout.close();
	            byte[] fileContent = fout.toByteArray();  
	            ByteArrayInputStream is = new ByteArrayInputStream(fileContent);
	            
	            excelStream=is;
				if(id!=null){
					excelFileName=URLEncoder.encode("Σ��Դ����ȫ��.xls","UTF-8");
				}else{
					excelFileName=URLEncoder.encode("Σ��Դ��������.xls","UTF-8");
				}
	        	
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    session.put("proValue",100);
		return SUCCESS;
	}
	//Σ��Դ-��������������
	public String exportHazardExcelRelation(){
		List<Hazard> list=new ArrayList<Hazard>();
		String hql;
		if(id==null || id.trim().length()==0){
			if(hazardList!=null && !"".equals(hazardList)){
				hql="select h from Hazard h where h.departmentType.departmentTypeSn ='"+departmentTypeSn+"' and (h.hazardSn like '%"+hazardList+"%' or h.hazardDescription like '%"+hazardList+"%')";
				list=hazardService.getHazardsByHql(hql);
			}else{
				if(manageObjectSn!=null && !"".equals(manageObjectSn)){
					
					List<ManageObject> listManageObject=manageObjectService.getByMoHuFind(manageObjectSn);
					for(ManageObject manageObject:listManageObject){
						Set<Hazard> set=manageObject.getHazards();
						if(set.size()>0){
							for(Iterator<Hazard> iter=set.iterator();iter.hasNext();){
								list.add(iter.next());
							}
						}
					}
				}else{
					hql="select h from Hazard h where h.departmentType.departmentTypeSn ='"+departmentTypeSn+"'";
					list=hazardService.getHazardsByHql(hql);
				}
				
			}
		}else{
			hql="FROM Hazard h where h.departmentType.departmentTypeSn ='"+departmentTypeSn+"'";
			list=hazardService.getHazardsByHql(hql);
		}
		
		// ��һ��������һ��workbook����Ӧһ��Excel�ļ�
        HSSFWorkbook workbook = new HSSFWorkbook();
        // �ڶ�������webbook�����һ��sheet,��ӦExcel�ļ��е�sheet
        HSSFSheet hssfSheet = workbook.createSheet("Σ��Դ������������");
        // ����������sheet����ӱ�ͷ��0��,ע���ϰ汾poi��Excel����������������short
        HSSFRow hssfRow = hssfSheet.createRow(0);
        // ���Ĳ���������Ԫ�񣬲�����ֵ��ͷ ���ñ�ͷ����
        HSSFCellStyle hssfCellStyle = workbook.createCellStyle();
        //������ʽ
        hssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 10);
        hssfCellStyle.setFont(font);
        
        HSSFCell hssfCell = hssfRow.createCell(0);//��������0��ʼ
        hssfCell.setCellValue("���������");//����1
        hssfCell.setCellStyle(hssfCellStyle);//�о�����ʾ
        hssfCell = hssfRow.createCell(1);
        hssfCell.setCellValue("�����������");//����2
        hssfCell.setCellStyle(hssfCellStyle);
        hssfCell = hssfRow.createCell(2);
        hssfCell.setCellValue("Σ��Դ���");//����3
        hssfCell.setCellStyle(hssfCellStyle);
        hssfCell = hssfRow.createCell(3);
        hssfCell.setCellValue("Σ��Դ����");//����4
        hssfCell.setCellStyle(hssfCellStyle);
        
        // ���岽��д��ʵ������
        int j=1;
        Hazard hazard=null;
        float num=list.size();
        Set<ManageObject> manageObjects=null;
        for (int i = 0; i < list.size(); i++) {
        	session.put("proValue",(int)((float)i*100/num));
        	hazard = list.get(i);
        	manageObjects=hazard.getManageObjects();
        	
        	if(manageObjects.size()>0){
        		for(ManageObject manageObject:manageObjects){
        			
        			// �������������С���Ԫ�񣬲�����ֵ
        			hssfRow = hssfSheet.createRow(j);
        			hssfRow.createCell(0).setCellValue(manageObject.getManageObjectSn());
        			hssfRow.createCell(1).setCellValue(manageObject.getManageObjectName());
        			hssfRow.createCell(2).setCellValue(hazard.getHazardSn());
        			hssfRow.createCell(3).setCellValue(hazard.getHazardDescription());
        			j++;
        		}
        	}
            
        }
        // ���߲������ļ��浽ָ��λ��
        try {
        	
        	ByteArrayOutputStream fout = new ByteArrayOutputStream();  
        	workbook.write(fout);
        	workbook.close();
            fout.close();
            byte[] fileContent = fout.toByteArray();  
            ByteArrayInputStream is = new ByteArrayInputStream(fileContent);
            
            excelStream=is;
			if(id!=null){
				excelFileName=URLEncoder.encode("Σ��Դ��������������ȫ��.xls","UTF-8");
			}else{
				excelFileName=URLEncoder.encode("Σ��Դ������������������.xls","UTF-8");
			}
        	
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.put("proValue",100);
        return SUCCESS;
	}
	//Σ��Դ-ָ���������1
	public String exportHazardExcelStandardIndex(){
		List<Hazard> list=new ArrayList<Hazard>();
		String hql;
		if(id==null || id.trim().length()==0){
			if(hazardList!=null && !"".equals(hazardList)){
				hql="select h from Hazard h where h.departmentType.departmentTypeSn ='"+departmentTypeSn+"' and (h.hazardSn like '%"+hazardList+"%' or h.hazardDescription like '%"+hazardList+"%')";
				list=hazardService.getHazardsByHql(hql);
			}else{
				if(manageObjectSn!=null && !"".equals(manageObjectSn)){
					
					List<ManageObject> listManageObject=manageObjectService.getByMoHuFind(manageObjectSn);
					for(ManageObject manageObject:listManageObject){
						Set<Hazard> set=manageObject.getHazards();
						if(set.size()>0){
							for(Iterator<Hazard> iter=set.iterator();iter.hasNext();){
								list.add(iter.next());
							}
						}
					}
				}else{
					hql="select h from Hazard h where h.departmentType.departmentTypeSn ='"+departmentTypeSn+"'";
					list=hazardService.getHazardsByHql(hql);
				}
				
			}
		}else{
			hql="FROM Hazard h where h.departmentType.departmentTypeSn ='"+departmentTypeSn+"'";
			list=hazardService.getHazardsByHql(hql);
		}
		
		// ��һ��������һ��workbook����Ӧһ��Excel�ļ�
        HSSFWorkbook workbook = new HSSFWorkbook();
        // �ڶ�������webbook�����һ��sheet,��ӦExcel�ļ��е�sheet
        HSSFSheet hssfSheet = workbook.createSheet("Σ��Դ-ָ�������");
        // ����������sheet����ӱ�ͷ��0��,ע���ϰ汾poi��Excel����������������short
        HSSFRow hssfRow = hssfSheet.createRow(0);
        // ���Ĳ���������Ԫ�񣬲�����ֵ��ͷ ���ñ�ͷ����
        HSSFCellStyle hssfCellStyle = workbook.createCellStyle();
        //������ʽ
        hssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 10);
        hssfCellStyle.setFont(font);
        
        HSSFCell hssfCell = hssfRow.createCell(0);//��������0��ʼ
        hssfCell.setCellValue("ָ����");//����1
        hssfCell.setCellStyle(hssfCellStyle);//�о�����ʾ
        hssfCell = hssfRow.createCell(1);
        hssfCell.setCellValue("ָ������");//����2
        hssfCell.setCellStyle(hssfCellStyle);
        hssfCell = hssfRow.createCell(2);
        hssfCell.setCellValue("Σ��Դ���");//����3
        hssfCell.setCellStyle(hssfCellStyle);
        hssfCell = hssfRow.createCell(3);
        hssfCell.setCellValue("Σ��Դ����");//����4
        hssfCell.setCellStyle(hssfCellStyle);
        
        // ���岽��д��ʵ������
        int j=1;
        Hazard hazard=null;
        float num=list.size();
        Set<StandardIndex> standardIndexes=null;
        for (int i = 0; i < list.size(); i++) {
        	session.put("proValue",(int)((float)i*100/num));
        	hazard = list.get(i);
        	standardIndexes=hazard.getStandardIndexes();
        	
        	if(standardIndexes.size()>0){
        		for(StandardIndex standardIndex:standardIndexes){
        			
        			// �������������С���Ԫ�񣬲�����ֵ
        			hssfRow = hssfSheet.createRow(j);
        			hssfRow.createCell(0).setCellValue(standardIndex.getIndexSn());
        			hssfRow.createCell(1).setCellValue(standardIndex.getIndexName());
        			hssfRow.createCell(2).setCellValue(hazard.getHazardSn());
        			hssfRow.createCell(3).setCellValue(hazard.getHazardDescription());
        			j++;
        		}
        	}
            
        }
        // ���߲������ļ��浽ָ��λ��
        try {
        	
        	ByteArrayOutputStream fout = new ByteArrayOutputStream();  
        	workbook.write(fout);
        	workbook.close();
            fout.close();
            byte[] fileContent = fout.toByteArray();  
            ByteArrayInputStream is = new ByteArrayInputStream(fileContent);
            
            excelStream=is;
			if(id!=null){
				excelFileName=URLEncoder.encode("Σ��Դ-ָ���������ȫ��.xls","UTF-8");
			}else{
				excelFileName=URLEncoder.encode("Σ��Դ-ָ�������������.xls","UTF-8");
			}
        	
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.put("proValue",100);
        return SUCCESS;
	}
	//ָ��-Σ��Դ2
	public String exportHazardExcelStandardIndex2(){
		session.put("status","unknown");
		try{
			List<StandardIndex> list=new ArrayList<StandardIndex>();
			String hql="select s from StandardIndex s WHERE s.deleted=false AND s.standard.standardSn like '"+standardSn+"'";
			list=standardindexService.getPart(hql);						
			// ��һ��������һ��workbook����Ӧһ��Excel�ļ�
	        HSSFWorkbook workbook = new HSSFWorkbook();
	        // �ڶ�������webbook�����һ��sheet,��ӦExcel�ļ��е�sheet
	        HSSFSheet hssfSheet = workbook.createSheet("Σ��Դ-ָ�������");
	        // ����������sheet����ӱ�ͷ��0��,ע���ϰ汾poi��Excel����������������short
	        HSSFRow hssfRow = hssfSheet.createRow(0);
	        // ���Ĳ���������Ԫ�񣬲�����ֵ��ͷ ���ñ�ͷ����
	        HSSFCellStyle hssfCellStyle = workbook.createCellStyle();
	        //������ʽ
	        hssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        HSSFFont font = workbook.createFont();
	        font.setColor(HSSFColor.VIOLET.index);
	        font.setFontHeightInPoints((short) 10);
	        hssfCellStyle.setFont(font);
	        
	        HSSFCell hssfCell = hssfRow.createCell(0);//��������0��ʼ
	        hssfCell.setCellValue("ָ����");//����1
	        hssfCell.setCellStyle(hssfCellStyle);//�о�����ʾ
	        hssfCell = hssfRow.createCell(1);
	        hssfCell.setCellValue("ָ������");//����2
	        hssfCell.setCellStyle(hssfCellStyle);
	        hssfCell = hssfRow.createCell(2);
	        hssfCell.setCellValue("Σ��Դ���");//����3
	        hssfCell.setCellStyle(hssfCellStyle);
	        hssfCell = hssfRow.createCell(3);
	        hssfCell.setCellValue("Σ��Դ����");//����4
	        hssfCell.setCellStyle(hssfCellStyle);
	        
	        // ���岽��д��ʵ������
	        //�����п�
	        hssfSheet.setColumnWidth(0, 50*50);
	        hssfSheet.setColumnWidth(1, 100*100);
	        hssfSheet.setColumnWidth(2, 50*50);
	        hssfSheet.setColumnWidth(3, 100*100);
	        //���ݸ�ʽ
	        HSSFCellStyle style = workbook.createCellStyle();
	        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
	        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);;
	        style.setWrapText(true);
	        HSSFCell cell;
	        int j=1;
	        StandardIndex standardIndex=null;
	        float num=list.size();
	        Set<Hazard> hazards=null;
	        for (int i = 0; i < list.size(); i++) {
	        	session.put("progressValue",(int)((float)i*100/num));
	        	standardIndex = list.get(i);
	        	hazards=standardIndex.getHazards();		        	
	        	if(hazards.size()>0){
	        		for(Hazard hazard:hazards){	        			
	        			// �������������С���Ԫ�񣬲�����ֵ
	        			hssfRow = hssfSheet.createRow(j);
	        			cell=hssfRow.createCell(0);
	        			cell.setCellValue(standardIndex.getIndexSn());
	        			cell.setCellStyle(style);
	        			cell=hssfRow.createCell(1);
	        			cell.setCellValue(standardIndex.getIndexName());
	        			cell.setCellStyle(style);
	        			cell=hssfRow.createCell(2);
	        			cell.setCellValue(hazard.getHazardSn());
	        			cell.setCellStyle(style);
	        			cell=hssfRow.createCell(3);
	        			cell.setCellValue(hazard.getHazardDescription());
	        			cell.setCellStyle(style);
	        			j++;
	        		}
	        	}
	            
	        }
	        // ���߲������ļ��浽ָ��λ��
	        try {
	        	
	        	ByteArrayOutputStream fout = new ByteArrayOutputStream();  
	        	workbook.write(fout);
	        	workbook.close();
	            fout.close();
	            byte[] fileContent = fout.toByteArray();  
	            ByteArrayInputStream is = new ByteArrayInputStream(fileContent);	            
	            excelStream=is;
				excelFileName=URLEncoder.encode("Σ��Դ-ָ���������.xls","UTF-8");
				session.put("status","ok");
	        } catch (Exception e) {
	        	session.put("status","nook");
	        	session.put("progressValue",0);
	        }
		}catch(Exception e){
			session.put("status","nook");
			session.put("progressValue",0);
		}			
        session.put("progressValue",0);
        return SUCCESS;
	}
	//������������
	public String exportCheckTask(){
		String hql="";
		List<CheckTaskAppraisals> list=new ArrayList<CheckTaskAppraisals>();
		if(id == null || id.trim().length() == 0){
			if(checked){
	        	hql = "select i from CheckTaskAppraisals i where i.department.departmentSn like '" + departmentSn + "%' and year(yearMonth) = '" + yearTime + "' and month(yearMonth) = '" + monthTime + "' and i.checker is not null order by i.department asc";
	        }else{
	        	hql = "select i from CheckTaskAppraisals i where i.department.departmentSn = '" + departmentSn + "' and year(yearMonth) = '" + yearTime + "' and month(yearMonth) = '" + monthTime + "' and i.checker is not null order by i.department asc";
	        }
			//hql="select c from CheckTaskAppraisals c where c.department.departmentSn='"+departmentSn+"' and year(yearMonth)='"+yearTime+"' and month(yearMonth)='"+monthTime+"' and c.checker is not null";
		}else{
			hql="select c from CheckTaskAppraisals c where year(yearMonth)='"+yearTime+"' and month(yearMonth)='"+monthTime+"' and c.checker is not null order by c.department asc";
		}
		list=checkTaskAppraisalsService.getByHql(hql);
		
		// ��һ��������һ��workbook����Ӧһ��Excel�ļ�
        HSSFWorkbook workbook = new HSSFWorkbook();
        // �ڶ�������webbook�����һ��sheet,��ӦExcel�ļ��е�sheet
        HSSFSheet hssfSheet = workbook.createSheet("����������");
        // ����������sheet����ӱ�ͷ��0��,ע���ϰ汾poi��Excel����������������short
        HSSFRow hssfRow = hssfSheet.createRow(0);
        // ���Ĳ���������Ԫ�񣬲�����ֵ��ͷ ���ñ�ͷ����
        HSSFCellStyle hssfCellStyle = workbook.createCellStyle();
        //������ʽ
        hssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 10);
        hssfCellStyle.setFont(font);
        
        HSSFCell hssfCell = hssfRow.createCell(0);//��������0��ʼ
        hssfCell.setCellValue("���ű��");//����1
        hssfCell.setCellStyle(hssfCellStyle);//�о�����ʾ
        hssfCell = hssfRow.createCell(1);
        hssfCell.setCellValue("��������");//����2
        hssfCell.setCellStyle(hssfCellStyle);
        hssfCell = hssfRow.createCell(2);
        hssfCell.setCellValue("��Ա���");//����3
        hssfCell.setCellStyle(hssfCellStyle);
        hssfCell = hssfRow.createCell(3);
        hssfCell.setCellValue("��Ա����");//����4
        hssfCell.setCellStyle(hssfCellStyle);
        hssfCell = hssfRow.createCell(4);
        hssfCell.setCellValue("ʱ��");//����5
        hssfCell.setCellStyle(hssfCellStyle);
        hssfCell = hssfRow.createCell(5);
        hssfCell.setCellValue("���������������");//����6
        hssfCell.setCellStyle(hssfCellStyle);
        hssfCell = hssfRow.createCell(6);
        hssfCell.setCellValue("����ȫ��Ϊ�����������");//����7
        hssfCell.setCellStyle(hssfCellStyle);
        
        // ���岽��д��ʵ������
        CheckTaskAppraisals checkTaskAppraisals;
        float num=list.size();
        for (int i = 0; i < list.size(); i++) {
        	session.put("proValue",(int)((float)i*100/num));
        	checkTaskAppraisals = list.get(i);
        	
			// �������������С���Ԫ�񣬲�����ֵ
			hssfRow = hssfSheet.createRow(i+1);
			if(checkTaskAppraisals.getDepartment()!=null){
				hssfRow.createCell(0).setCellValue(checkTaskAppraisals.getDepartment().getDepartmentSn());
				hssfRow.createCell(1).setCellValue(checkTaskAppraisals.getDepartment().getDepartmentName());
			}
			if(checkTaskAppraisals.getChecker()!=null){
				hssfRow.createCell(2).setCellValue(checkTaskAppraisals.getChecker().getPersonId());
				hssfRow.createCell(3).setCellValue(checkTaskAppraisals.getChecker().getPersonName());
			}
			if(checkTaskAppraisals.getYearMonth()!=null){
				hssfRow.createCell(4).setCellValue(checkTaskAppraisals.getYearMonth().toString());
			}else{
				hssfRow.createCell(4).setCellValue(new Date(System.currentTimeMillis()));
			}
			if(checkTaskAppraisals.getCheckTaskCount()!=null){
				hssfRow.createCell(5).setCellValue(checkTaskAppraisals.getCheckTaskCount());
			}else{
				hssfRow.createCell(5).setCellValue(1);
			}
			if(checkTaskAppraisals.getUnsafeActCheckTaskCount() != null){
				hssfRow.createCell(6).setCellValue(checkTaskAppraisals.getUnsafeActCheckTaskCount());
			}else{
				hssfRow.createCell(6).setCellValue(1);
			}
        }
        // ���߲������ļ��浽ָ��λ��
        try {
        	
        	ByteArrayOutputStream fout = new ByteArrayOutputStream();  
        	workbook.write(fout);
        	workbook.close();
            fout.close();
            byte[] fileContent = fout.toByteArray();  
            ByteArrayInputStream is = new ByteArrayInputStream(fileContent);
            
            excelStream=is;
			if(id!=null){
				excelFileName=URLEncoder.encode("����������ȫ��.xls","UTF-8");
			}else{
				excelFileName=URLEncoder.encode("��������������.xls","UTF-8");
			}
        	
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.put("proValue",100);
		return SUCCESS;
	}
	//�������ҿ���
	public String downloadOfficeExcel(){
		//System.out.println(rowsData);
		//yearTime="2016";
		net.sf.json.JSONArray array=net.sf.json.JSONArray.fromObject(rowsData);
		String name=yearTime+"�괦�ҿ������ݵ���";
		String titles="���ű��,��������,һ��,����,����,��һ����,����,����,����,�ڶ�����,����,����,����,��������,ʮ��,ʮһ��,ʮ����,���ļ���,���,����";
		String cellValue="departmentSn,departmentName,1,2,3,season1,4,5,6,season2,7,8,9,season3,10,11,12,season4,year,rank";
		
		// ��һ��������һ��workbook����Ӧһ��Excel�ļ�
        HSSFWorkbook workbook = new HSSFWorkbook();
        // �ڶ�������webbook�����һ��sheet,��ӦExcel�ļ��е�sheet
        HSSFSheet hssfSheet = workbook.createSheet(name);
        // ����������sheet����ӱ�ͷ��0��,ע���ϰ汾poi��Excel����������������short
        HSSFRow hssfRow = hssfSheet.createRow(0);
        // ���Ĳ���������Ԫ�񣬲�����ֵ��ͷ ���ñ�ͷ����
        HSSFCellStyle hssfCellStyle = workbook.createCellStyle();
        //������ʽ
        hssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 10);
        hssfCellStyle.setFont(font);
        
        HSSFCell hssfCell;
        for(int i=0;i<titles.split(",").length;i++){
        	hssfCell = hssfRow.createCell(i);
	        hssfCell.setCellValue(titles.split(",")[i]);//����2
	        hssfCell.setCellStyle(hssfCellStyle);
        }
        
        // ���岽��д��ʵ������
        for (int i = 0; i < array.size(); i++) {
        	hssfRow = hssfSheet.createRow(i+1);
        	
			// �������������С���Ԫ�񣬲�����ֵ
        	net.sf.json.JSONObject jo=array.getJSONObject(i);
        	String[] data=cellValue.split(",");
        	for(int j=0;j<data.length;j++){
        		hssfRow.createCell(j).setCellValue(jo.getString(data[j]));
        	}
        }
        // ���߲������ļ��浽ָ��λ��
        try {
        	
        	ByteArrayOutputStream fout = new ByteArrayOutputStream();  
        	workbook.write(fout);
        	workbook.close();
            fout.close();
            byte[] fileContent = fout.toByteArray();  
            ByteArrayInputStream is = new ByteArrayInputStream(fileContent);
            
            excelStream=is;
			excelFileName=URLEncoder.encode(name+".xls","UTF-8");
        	
        } catch (Exception e) {
            e.printStackTrace();
        }
		return SUCCESS;
	}
	
	//����������Excel
	@SuppressWarnings("resource")
	public String importExcel() throws IOException{
		InputStream is= new FileInputStream(uploadExcel);
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		ManageObject manageObjects=null;
		HashMap<String, String> record = new HashMap<String, String>();
        float num=0f;
		String isExist="��";
		String isError="��";
		String notEnum="��";
		String nullData="��";
		//String isUpdate="��";
		String notFindParent="��";
		String notFindDepartmentType="��";
		// ѭ��������Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            num=hssfSheet.getLastRowNum();
            // ѭ����Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
	            session.put("progressValue", (int)((float)rowNum*100/num));
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow == null) {
                	nullData+=(rowNum+1)+",";
                    continue;
                }
                manageObjects = new ManageObject();
                // ѭ����Cell
                // 0�������ͱ��  1������������  2���������  3�����������  4�����������  5�������������  6��������������� 
                try{
                	//0�������ͱ��
	                HSSFCell departmentTypeSn = hssfRow.getCell(0);
	                if (departmentTypeSn == null || departmentTypeSn.toString().trim().length() == 0) {
	                	nullData+=(rowNum+1)+",";
	                    continue;
	                }
	                DepartmentType departmentType=departmentTypeService.getByDepartmentTypeSn(departmentTypeSn.getStringCellValue());
	                if(departmentType == null){
	                	notFindDepartmentType+=(rowNum+1)+",";
	                	continue;
	                }
	                manageObjects.setDepartmentType(departmentType);
	                
	                //2���������  
	                HSSFCell manageObjectSn = hssfRow.getCell(2);
	                if (manageObjectSn == null || manageObjectSn.toString().trim().length() == 0) {
	                	nullData+=(rowNum+1)+",";
	                    continue;
	                }
	                
	                //��¼����ı��
	                if(record.containsKey(manageObjectSn.getStringCellValue())){
	                	isExist+=(rowNum+1)+"��";
	                	//record.put(manageObjectSn.getStringCellValue(),rowNum+1);
	                	continue;
	                }else{
	                	record.put(manageObjectSn.getStringCellValue(),String.valueOf(rowNum+1));
	                }
	                
	                manageObjects.setManageObjectSn(manageObjectSn.getStringCellValue());
	                
	                
	                //3�����������  
	                HSSFCell manageObjectName = hssfRow.getCell(3);
	                if (manageObjectName == null || manageObjectName.toString().trim().length() == 0) {
	                	nullData+=(rowNum+1)+",";
	                    continue;
	                }
	                manageObjects.setManageObjectName(manageObjectName.getStringCellValue());
	                
	                //4�����������  
	                HSSFCell manageObjectType = hssfRow.getCell(4);
	                if (manageObjectType == null || manageObjectType.toString().trim().length() == 0) {
	                	nullData+=(rowNum+1)+",";
	                    continue;
	                }
	                if("�˻�����".indexOf(manageObjectType.toString())==-1){
	                	notEnum+=(rowNum+1)+",";
	                    continue;
	                }
	                manageObjects.setManageObjectType(ManageObjectType.valueOf(manageObjectType.getStringCellValue()));
	                
	                //5�������������
	                HSSFCell parentManageObjectSn = hssfRow.getCell(5);
//		                if (parentManageObjectSn.toString().equals("") || parentManageObjectSn.toString() == null) {
//		                	pagNull+=(rowNum+1)+",";
//		                    continue;
//		                }
	                if(parentManageObjectSn == null || parentManageObjectSn.toString().trim().length() == 0){
	                	if("��,��,��,��".indexOf(manageObjectName.toString())==-1){
	                		notFindParent+=(rowNum+1)+",";
	                		continue;
	                	}else{
	                		manageObjects.setParent(null);
	                	}
	                }else{
	                	ManageObject parentManageObject=manageObjectService.getByManageObjectSn(parentManageObjectSn.getStringCellValue());
	                	if(parentManageObject!=null){
	                		manageObjects.setParent(parentManageObject);
	                	}else{
	                		notFindParent+=(rowNum+1)+",";
	                		continue;
	                	}
	                }
	                
	            }catch(Exception e){
	            	isError+=(rowNum+1)+",";
                	continue;
                }
                try{
                	//��׽����ظ��쳣
                	try{
                		manageObjectService.addManageObject(manageObjects);
                	}catch(ConstraintViolationException e){
                		//����ظ��쳣
                		ManageObject man=manageObjectService.getByManageObjectSn(hssfRow.getCell(2).toString());
		                DepartmentType de=departmentTypeService.getByDepartmentTypeSn(hssfRow.getCell(0).getStringCellValue());
		                man.setDepartmentType(de);
		                man.setManageObjectName(hssfRow.getCell(3).getStringCellValue());
		                man.setManageObjectType(ManageObjectType.valueOf(hssfRow.getCell(4).getStringCellValue()));
		                try{
		                	ManageObject par=manageObjectService.getByManageObjectSn(hssfRow.getCell(5).getStringCellValue());
		                	man.setParent(par);
		                	manageObjectService.update(man);
		                }catch(NullPointerException ee){
		                	man.setParent(null);
		                	manageObjectService.update(man);
		                }
                		//isUpdate+=(rowNum+1)+",";
                	}
                }catch(Exception e){
                	isError+=(rowNum+1)+",";
                }
            }
        }	  
        //nullData:������
		//isExist:����������Ѿ�����
		//isError:�����쳣
		//notFindDepartmentType:�������Ͳ�����
		//notFindParent:�Ҳ��������������
        pag="";
        String[] number;
        if(!"��".equals(nullData)){
        	nullData=nullData.substring(0, nullData.length()-1) + "���п����ݣ�" + "<br />";
        	pag+=nullData;
        	number=nullData.split(",");
        	pag=pag+"��"+number.length+"��"+ "<br /><br />";
        }
        if(!"��".equals(isExist)){
        	isExist=isExist.substring(0, isExist.length()-1) + "�����ظ���ţ�����ʧ�ܣ�" + "<br />";
        	pag+=isExist;
        	number=isExist.split(",");
        	pag=pag+"��"+number.length+"��"+ "<br /><br />";
        }
        if(!"��".equals(notEnum)){
        	notEnum=notEnum.substring(0, notEnum.length()-1) + "�й������������д����" + "<br />";
        	pag+=notEnum;
        	number=notEnum.split(",");
        	pag=pag+"��"+number.length+"��"+ "<br /><br />";
        }
        if(!"��".equals(notFindDepartmentType)){
        	notFindDepartmentType=notFindDepartmentType.substring(0, notFindDepartmentType.length()-1) + "�в������ͱ����д����" + "<br />";
        	pag+=notFindDepartmentType;
        	number=notFindDepartmentType.split(",");
        	pag=pag+"��"+number.length+"��"+ "<br /><br />";
        }
        if(!"��".equals(notFindParent)){
        	notFindParent=notFindParent.substring(0, notFindParent.length()-1) + "�и��������������д����" + "<br />";
        	pag+=notFindParent;
        	number=notFindParent.split(",");
        	pag=pag+"��"+number.length+"��"+ "<br /><br />";
        }
        if(!"��".equals(isError)){
        	isError=isError.substring(0, isError.length()-1) + "�е����쳣�������ʽ��" + "<br />";
        	pag+=isError;
        	number=isError.split(",");
        	pag=pag+"��"+number.length+"��"+ "<br /><br />";
        }
//	    if(!"��".equals(isUpdate)){
//	        isUpdate=isUpdate.substring(0, isUpdate.length()-1) + "�б�����ݿ����Ѵ��ڣ��Ѹ��ǣ�" + "<br />";
//	        pag+=isUpdate;
//	        number=isUpdate.split(",");
//	        pag=pag+"��"+number.length+"��"+ "<br /><br />";
//	    }
        if(pag.equals("")){
        	pag="�������ݳɹ���";
        }else{
        	pag+="�������ݵ���ɹ���";
        }
	    session.put("progressValue",0);
		return SUCCESS;
	}
		//����������-Σ��Դ����Excel
		@SuppressWarnings("resource")
		public String importExcelRelation() throws IOException{
			InputStream is= new FileInputStream(uploadExcel);
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
			ManageObject manageObject=null;
			Hazard hazard=null;
            float num=0f;
            String notManageObject="��";
            String notHazard="��";
            String isError="��";
            String nullData="��";
			// ѭ��������Sheet
	            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
	            num=hssfSheet.getLastRowNum();
	            // ѭ����Row
	            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
	                session.put("progressValue", (int)((float)rowNum*100/num));
	                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
	                if (hssfRow == null) {
	                    continue;
	                }
	                // ѭ����Cell
	                // 0���������  1�����������   2Σ��Դ��� 3Σ��Դ���� 
	                try{
		                //0���������  
		                HSSFCell manageObjectSn = hssfRow.getCell(0);
		                if (manageObjectSn == null || manageObjectSn.toString().trim().length() == 0) {
		                	nullData+=(rowNum+1)+",";
		                    continue;
		                }
		                manageObject=manageObjectService.getByManageObjectSn(manageObjectSn.getStringCellValue());
		                if (manageObject == null) {
		                	String str=manageObjectSn.getStringCellValue().replace("\\n", "").trim();
		                	manageObject=manageObjectService.getByManageObjectSn(str);
		                	if(manageObject == null){
		                		notManageObject+=(rowNum+1)+",";
		                		continue;
		                	}
		                }
		                //2Σ��Դ���
		                HSSFCell hazardSn = hssfRow.getCell(2);
		                if (hazardSn == null || hazardSn.toString().trim().length() == 0) {
		                	nullData+=(rowNum+1)+",";
		                    continue;
		                }
		                hazard=hazardService.getByHazardSn(hazardSn.getStringCellValue());
		                if (hazard == null) {
		                	String str=hazardSn.getStringCellValue().replace("\\n", "").trim();
		                	hazard=hazardService.getByHazardSn(str);
		                	if(hazard==null){
		                		notHazard+=(rowNum+1)+",";
		                		continue;
		                	}
		                }
		                manageObject.getHazards().add(hazard);
		                manageObjectService.update(manageObject);
		            }catch(Exception e){
	                	isError+=(rowNum+1)+",";
	                	continue;
	                }
            }
			pag="";
	        String[] number;
	        if(!"��".equals(nullData)){
	        	nullData=nullData.substring(0, nullData.length()-1) + "���п����ݣ�" + "<br />";
	        	pag+=nullData;
	        	number=nullData.split(",");
	        	pag=pag+"��"+number.length+"��"+ "<br /><br />";
	        }
	        if(!"��".equals(notManageObject)){
	        	notManageObject=notManageObject.substring(0, notManageObject.length()-1) + "�й�������Ų����ڣ�" + "<br />";
	        	pag+=notManageObject;
	        	number=notManageObject.split(",");
	        	pag=pag+"��"+number.length+"��"+ "<br /><br />";
	        }
	        if(!"��".equals(notHazard)){
	        	notHazard=notHazard.substring(0, notHazard.length()-1) + "��Σ��Դ��Ų����ڣ�" + "<br />";
	        	pag+=notHazard;
	        	number=notHazard.split(",");
	        	pag=pag+"��"+number.length+"��"+ "<br /><br />";
	        }
	        if(!"��".equals(isError)){
	        	isError=isError.substring(0, isError.length()-1) + "�е����쳣���������ݣ�" + "<br />";
	        	pag+=isError;
	        	number=isError.split(",");
	        	pag=pag+"��"+number.length+"��"+ "<br /><br />";
	        }
	        if(pag.equals("")){
	        	pag="�������ݳɹ���";
	        }else{
	        	pag+="�������ݵ���ɹ���";
	        }
	        session.put("progressValue",0);
	    	return SUCCESS;
		}
		//����Σ��ԴExcel
		@SuppressWarnings("resource")
		public String importHazardExcel() throws IOException{
			InputStream is= new FileInputStream(uploadExcel);
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
			Hazard hazard=null;
			HashMap<String, String> record = new HashMap<String, String>();
			float num=0f;
			String nullData="��";
			String isExist="��";
			String isError="��";
			String notEnum="��";
			String notFindDepartmentType="��";
			String notAccidentType="��";
			// ѭ��������Sheet
	        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
	            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
	            if (hssfSheet == null) {
	                continue;
	            }
	            num=hssfSheet.getLastRowNum();
	            // ѭ����Row
	            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
	            	//����session������  (int)(rowNum/hssfSheet.getLastRowNum())*100
	            	session.put("progressValue", (int)((float)rowNum*100/num));
	                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
	                if (hssfRow == null) {
	                	nullData+=(rowNum+1)+",";
	                    continue;
	                }
	                hazard = new Hazard();
	                // ѭ����Cell
	                // 0�������ͱ�� 1������������  2Σ��Դ��� 3Σ��Դ���� 4Σ��Դ������� 5���յȼ� 6�¹����ͱ�� 7�¹���������
	                try{
	                	//0�������ͱ�� 
	                	HSSFCell departmentTypeSn = hssfRow.getCell(0);
	                	if (departmentTypeSn == null || departmentTypeSn.toString().trim().length() == 0 ) {
	                		nullData+=(rowNum+1)+",";
	                		continue;
	                	}
	                	DepartmentType departmentType=departmentTypeService.getByDepartmentTypeSn(departmentTypeSn.getStringCellValue());
	                	if(departmentType == null){
		                	notFindDepartmentType+=(rowNum+1)+",";
		                	continue;
		                }
	                	hazard.setDepartmentType(departmentType);
	                	
	                	//2Σ��Դ��� 
	                	HSSFCell hazardSn = hssfRow.getCell(2);
	                	if (hazardSn == null || hazardSn.toString().trim().length() == 0) {
	                		nullData+=(rowNum+1)+",";
	                		continue;
	                	}
	                	//��¼����ı��
		                if(record.containsKey(hazardSn.getStringCellValue())){
		                	isExist+=(rowNum+1)+"��";
		                	continue;
		                }else{
		                	record.put(hazardSn.getStringCellValue(),String.valueOf(rowNum+1));
		                }
	                	hazard.setHazardSn(hazardSn.getStringCellValue());
	                	
	                	//3Σ��Դ���� 
	                	HSSFCell hazardDescription = hssfRow.getCell(3);
	                	if (hazardDescription == null || hazardDescription.toString().trim().length() == 0) {
	                		nullData+=(rowNum+1)+",";
	                		continue;
	                	}
	                	hazard.setHazardDescription(hazardDescription.getStringCellValue());
	                	
	                	//4Σ��Դ������� 
	                	HSSFCell resultDescription = hssfRow.getCell(4);
	                	if (resultDescription == null || resultDescription.toString().trim().length() == 0) {
	                		nullData+=(rowNum+1)+",";
	                		continue;
	                	}
	                	hazard.setResultDescription(resultDescription.getStringCellValue());
	                	
	                	//5���յȼ� 
	                	HSSFCell riskLevel = hssfRow.getCell(5);
	                	if (riskLevel == null || riskLevel.toString().trim().length() == 0) {
	                		nullData+=(rowNum+1)+",";
	                		continue;
	                	}else if("һ������еȷ����ش����".indexOf(riskLevel.toString())==-1){
	                		notEnum+=(rowNum+1)+",";
	                		continue;
	                	}
	                	hazard.setRiskLevel(RiskLevel.valueOf(riskLevel.getStringCellValue()));
	                	
	                	//6�¹����ͱ��
	                	HSSFCell accidentTypeSn = hssfRow.getCell(6);
	                	if (accidentTypeSn == null || accidentTypeSn.toString().trim().length() == 0) {
	                		nullData+=(rowNum+1)+",";
	                		continue;
	                	}
	                	AccidentType accidentType=accidentTypeService.getByAccidentTypeSn(accidentTypeSn.getStringCellValue());
	                	if(accidentType == null){
	                		notAccidentType+=(rowNum+1)+",";
	                		continue;
	                	}
	                	hazard.setAccidentType(accidentType);
	                }catch(Exception e){
	                	isError+=(rowNum+1)+",";
	                	continue;
	                }
	                try{
	                	//��׽����ظ��쳣
	                	try{
	                		hazardService.addHazard(hazard);
	                	}catch(ConstraintViolationException e){
	                		//����ظ��쳣
	                		Hazard haz=hazardService.getByHazardSn(hssfRow.getCell(2).toString());
	                		DepartmentType de=departmentTypeService.getByDepartmentTypeSn(hssfRow.getCell(0).getStringCellValue());
	                		haz.setDepartmentType(de);
	                		haz.setHazardDescription(hssfRow.getCell(3).getStringCellValue());
	                		haz.setResultDescription(hssfRow.getCell(4).getStringCellValue());
	                		haz.setRiskLevel(RiskLevel.valueOf(hssfRow.getCell(5).getStringCellValue()));
	                		AccidentType ac=accidentTypeService.getByAccidentTypeSn(hssfRow.getCell(6).getStringCellValue());
	                		haz.setAccidentType(ac);
	                		hazardService.updateHazard(haz);
	                	}
	                }catch(Exception e){
	                	isError+=(rowNum+1)+",";
	                }
	                
	                
	            }
	        }
	        //nullData:������
			//isExist:Σ��Դ�Ѿ�����
			//isError:����쳣
			//notEnum:����ˮƽ����
			//notFindDepartmentType:�Ҳ�����������
			//notAccidentType:�Ҳ����¹�����
	        pag="";
	        String[] number;
	        if(!"��".equals(nullData)){
	        	nullData=nullData.substring(0, nullData.length()-1) + "���п����ݣ�" + "<br />";
	        	pag+=nullData;
	        	number=nullData.split(",");
	        	pag=pag+"��"+number.length+"��"+ "<br /><br />";
	        }
	        if(!"��".equals(isExist)){
	        	isExist=isExist.substring(0, isExist.length()-1) + "��Σ��Դ����Ѿ����ڣ�" + "<br />";
	        	pag+=isExist;
	        	number=isExist.split(",");
	        	pag=pag+"��"+number.length+"��"+ "<br /><br />";
	        }
	        if(!"��".equals(notEnum)){
	        	notEnum=notEnum.substring(0, notEnum.length()-1) + "�з��յȼ���д����" + "<br />";
	        	pag+=notEnum;
	        	number=notEnum.split(",");
	        	pag=pag+"��"+number.length+"��"+ "<br /><br />";
	        }
	        if(!"��".equals(notFindDepartmentType)){
	        	notFindDepartmentType=notFindDepartmentType.substring(0, notFindDepartmentType.length()-1) + "�в������ͱ����д����" + "<br />";
	        	pag+=notFindDepartmentType;
	        	number=notFindDepartmentType.split(",");
	        	pag=pag+"��"+number.length+"��"+ "<br /><br />";
	        }
	        if(!"��".equals(notAccidentType)){
	        	notAccidentType=notAccidentType.substring(0, notAccidentType.length()-1) + "���¹����ͱ����д����" + "<br />";
	        	pag+=notAccidentType;
	        	number=notAccidentType.split(",");
	        	pag=pag+"��"+number.length+"��"+ "<br /><br />";
	        }
	        if(!"��".equals(isError)){
	        	isError=isError.substring(0, isError.length()-1) + "�е����쳣�������ʽ��" + "<br />";
	        	pag+=isError;
	        	number=isError.split(",");
	        	pag=pag+"��"+number.length+"��"+ "<br /><br />";
	        }
	        if(pag.equals("")){
	        	pag="�������ݳɹ���";
	        }else{
	        	pag+="�������ݵ���ɹ���";
	        }
	        session.put("progressValue", 0);
			return SUCCESS;
		}
		//������������
		@SuppressWarnings("resource")
		public String importCheckTask() throws IOException{
			InputStream is= new FileInputStream(uploadExcel);
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
			CheckTaskAppraisals checkTaskAppraisals = null;
			CheckTaskAppraisals check=new CheckTaskAppraisals();
			Person person=null;
            float num=0f;
            Integer unsafeConditionCheckTaskCount = 1;
            Integer unsafeActCheckTaskCount = 1;
            String notPerson="��";
            String notEquals="��";
            String isError="��";
            String timeError="��";
            String nullData="��";
            String numError="��";
			// ѭ��������Sheet
	        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
	            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
	            if (hssfSheet == null) {
	                continue;
	            }
	            num=hssfSheet.getLastRowNum();
	            // ѭ����Row
	            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
	                session.put("progressValue", (int)((float)rowNum*100/num));
	                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
	                if (hssfRow == null) {
	                    continue;
	                }
	                // ѭ����Cell
	                // 0���ű��  1��������   2��Ա��� 3��Ա���� 4ʱ�� 5��������� 
	                try{
		                //0���ű��
		                HSSFCell departmentSn = hssfRow.getCell(0);
		                if (departmentSn == null || departmentSn.toString().trim().length() == 0) {
		                	nullData+=(rowNum+1)+",";
		                    continue;
		                }
		                String deptSn = "";
		                if( departmentSn.getCellType() == Cell.CELL_TYPE_NUMERIC ){
		                	deptSn = String.valueOf((int)departmentSn.getNumericCellValue());
		                }else{
		                	deptSn = departmentSn.getStringCellValue();
		                }
		                //2��Ա���
		                HSSFCell personId = hssfRow.getCell(2);
		                if (personId == null || personId.toString().trim().length() == 0) {
		                	nullData+=(rowNum+1)+",";
		                    continue;
		                }
		                String personIdString = "";
		                if(personId.getCellType() == Cell.CELL_TYPE_NUMERIC){
		                	personIdString = String.valueOf((int)personId.getNumericCellValue());
		                }else{
		                	personIdString = personId.getStringCellValue();
		                }
		                person=personService.getByPersonId(personIdString);
		                if(person==null){
		                	notPerson+=(rowNum+1)+",";
		                    continue;
		                }
		                //�ж�Excel�в��ű���Ƿ�����Ա���Ӧ
		                if(!deptSn.equals(person.getDepartment().getDepartmentSn())){
		                	notEquals+=(rowNum+1)+",";
		                    continue;
		                }
		                //4ʱ��
		                HSSFCell yearMonth = hssfRow.getCell(4);
		                if(yearMonth==null || yearMonth.toString().trim().length()==0){
		                	nullData+=(rowNum+1)+",";
		                    continue;
		                }
		                LocalDate localDate;
		                try{
		                	if(yearMonth.getCellType()==Cell.CELL_TYPE_STRING){
		                		localDate=LocalDate.parse(yearMonth.getStringCellValue());
		                	}else{
		                		String sdf = new SimpleDateFormat("yyyy-MM-dd").format(yearMonth.getDateCellValue());
		                		localDate=LocalDate.parse(sdf);
		                	}
		                }catch(Exception e){
		                	timeError+=(rowNum+1)+",";
		                    continue;
		                }
		                
		                //5 �������������
		                try{
		                	HSSFCell checkTaskCount = hssfRow.getCell(5);
		                	if (checkTaskCount != null && checkTaskCount.toString().trim().length() != 0) {
		                		if(checkTaskCount.getCellType()==Cell.CELL_TYPE_NUMERIC){
		                			unsafeConditionCheckTaskCount = (int) checkTaskCount.getNumericCellValue();
		                		}else{
		                			unsafeConditionCheckTaskCount = Integer.parseInt(checkTaskCount.getStringCellValue());
		                		}
		                	}else{
		                		unsafeConditionCheckTaskCount = 1;
		                	}
		                }catch(Exception e){
		                	numError+=(rowNum+1)+",";
		                    continue;
		                }
		                //6 ����ȫ��Ϊ���������
		                try{
		                	HSSFCell checkTaskCount = hssfRow.getCell(5);
		                	if (checkTaskCount != null && checkTaskCount.toString().trim().length() != 0) {
		                		if(checkTaskCount.getCellType()==Cell.CELL_TYPE_NUMERIC){
		                			unsafeActCheckTaskCount = (int) checkTaskCount.getNumericCellValue();
		                		}else{
		                			unsafeActCheckTaskCount = Integer.parseInt(checkTaskCount.getStringCellValue());
		                		}
		                	}else{
		                		unsafeActCheckTaskCount = 1;
		                	}
		                }catch(Exception e){
		                	numError+=(rowNum+1)+",";
		                    continue;
		                }
		               check=checkTaskAppraisalsService.getBycheckerSn(person.getPersonId(), person.getDepartment().getDepartmentSn(),localDate.toString());
		                if(check==null){
		                	checkTaskAppraisals = new CheckTaskAppraisals();
		                	checkTaskAppraisals.setChecker(person);
		                	checkTaskAppraisals.setDepartment(person.getDepartment());
		                	checkTaskAppraisals.setCheckTaskCount(unsafeConditionCheckTaskCount);
		                	checkTaskAppraisals.setUnsafeActCheckTaskCount(unsafeActCheckTaskCount);
		                	checkTaskAppraisals.setYearMonth(localDate);
		                	checkTaskAppraisalsService.add(checkTaskAppraisals);
		                }else{
		                	check.setCheckTaskCount(unsafeConditionCheckTaskCount);
		                	check.setUnsafeActCheckTaskCount(unsafeActCheckTaskCount);
		                	checkTaskAppraisalsService.update(check);
		                }
		            }catch(Exception e){
	                	isError+=(rowNum+1)+",";
	                	continue;
	                }
            }
        }	  
//	        String notPerson="��";
//            String notEquals="��";
//            String isError="��";
//            String nullData="��";
//            String numError="��";
			pag="";
	        String[] number;
	        if(!"��".equals(nullData)){
	        	nullData=nullData.substring(0, nullData.length()-1) + "���п����ݣ�" + "<br />";
	        	pag+=nullData;
	        	number=nullData.split(",");
	        	pag=pag+"��"+number.length+"��"+ "<br /><br />";
	        }
	        if(!"��".equals(notPerson)){
	        	notPerson=notPerson.substring(0, notPerson.length()-1) + "����Ա��Ų����ڣ�" + "<br />";
	        	pag+=notPerson;
	        	number=notPerson.split(",");
	        	pag=pag+"��"+number.length+"��"+ "<br /><br />";
	        }
	        if(!"��".equals(numError)){
	        	numError=numError.substring(0, numError.length()-1) + "�м������������ʽ����" + "<br />";
	        	pag+=numError;
	        	number=numError.split(",");
	        	pag=pag+"��"+number.length+"��"+ "<br /><br />";
	        }
	        if(!"��".equals(notEquals)){
	        	notEquals=notEquals.substring(0, notEquals.length()-1) + "�и���Ա���ڴ˲��ţ�" + "<br />";
	        	pag+=notEquals;
	        	number=notEquals.split(",");
	        	pag=pag+"��"+number.length+"��"+ "<br /><br />";
	        }
	        if(!"��".equals(timeError)){
	        	timeError=timeError.substring(0, timeError.length()-1) + "��ʱ���ʽ����" + "<br />";
	        	pag+=timeError;
	        	number=timeError.split(",");
	        	pag=pag+"��"+number.length+"��"+ "<br /><br />";
	        }
	        if(!"��".equals(isError)){
	        	isError=isError.substring(0, isError.length()-1) + "�е����쳣���������ݣ�" + "<br />";
	        	pag+=isError;
	        	number=isError.split(",");
	        	pag=pag+"��"+number.length+"��"+ "<br /><br />";
	        }
	        if(pag.equals("")){
	        	pag="�������ݳɹ���";
	        }else{
	        	pag+="�������ݵ���ɹ���";
	        }
	        session.put("progressValue",0);
	    	return SUCCESS;
		}
		
		
		//���ع��������ģ��
		public String download() 
		{
			String path=ServletActionContext.getServletContext().getRealPath("/template");//���ý����ļ���ַ
			File file=new File(path,"manageObject.xml");
			try {
				excelStream=createExcel(file);
				excelFileName=URLEncoder.encode("���������ģ��.xls","UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return SUCCESS;
		}
		
		//���ع������Σ��Դ����ģ��
		public String downloadRelation(){
			String path=ServletActionContext.getServletContext().getRealPath("/template");//���ý����ļ���ַ
			File file=new File(path,"manageObject-Hazard.xml");
			try {
				excelStream=createExcel(file);
				excelFileName=URLEncoder.encode("�������Σ��Դ��������ģ��.xls","UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return SUCCESS;
		}
		
		//����Σ��Դ����ģ��
		public String downloadHazard(){
			String path=ServletActionContext.getServletContext().getRealPath("/template");//���ý����ļ���ַ
			File file=new File(path,"hazard.xml");
			try {
				excelStream=createExcel(file);
				excelFileName=URLEncoder.encode("Σ��Դ����ģ��.xls","UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return SUCCESS;
		}
		//����Σ��Դ-ָ�굼��ģ��
		public String downloadStandardIndex(){
			String path=ServletActionContext.getServletContext().getRealPath("/template");//���ý����ļ���ַ
			File file=new File(path,"hazard-standardIndex.xml");
			try {
				excelStream=createExcel(file);
				excelFileName=URLEncoder.encode("Σ��Դ-ָ�굼��ģ��.xls","UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return SUCCESS;
		}
		//���ؼ����������ģ��
		public String checkTaskDownLoad(){
			String path = ServletActionContext.getServletContext().getRealPath("/template");//���ý����ļ���ַ
			File file = new File(path,"checkTask.xml");
			try {
				excelStream = createExcel(file);
				excelFileName = URLEncoder.encode("����������ģ��.xls","UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return SUCCESS;
		}
		//�����ϱ�ģ��
		public String reportToTemplate(){
			String path = ServletActionContext.getServletContext().getRealPath("/template");//���ý����ļ���ַ
			File file = new File(path,"reportToTemplate.xls");
			try {
				excelStream = new FileInputStream(file);
				excelFileName = URLEncoder.encode("�ش������ϱ�ģ��.xls","UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return SUCCESS;
		}
		//������������ģ��
		public String unsafeConditionTemplate(){
			String path = ServletActionContext.getServletContext().getRealPath("/template");//���ý����ļ���ַ
			File file = new File(path,"unsafeConditionTemplate.doc");
			try {
				excelStream = new FileInputStream(file);
				excelFileName = URLEncoder.encode("�ش���������ģ��.doc","UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return SUCCESS;
		}
		
		//����·������xmlģ���ļ�
		public ByteArrayInputStream createExcel(File file){
			
			ByteArrayInputStream is = null;
			SAXBuilder builder=new SAXBuilder();
			try {
				//����xml�ļ�
				Document parse=builder.build(file);
				//����Excel
				HSSFWorkbook wb=new HSSFWorkbook();
				//����sheet
				HSSFSheet sheet=wb.createSheet("Sheet0");
				//��ȡxml�ļ����ڵ�
				Element root=parse.getRootElement();
				//��ȡģ������
				//String templateName=root.getAttribute("name").getValue();
				int rownum=0;
				int column=0;
				//�����п�
				Element colgroup = root.getChild("colgroup");
				setColumnWidth(sheet,colgroup);
				
				//���ñ���
//				Element title = root.getChild("title");
//				List<Element> trs = title.getChildren("tr");
//				for (int i = 0; i < trs.size(); i++) {
//					Element tr = trs.get(i);
//					List<Element> tds = tr.getChildren("td");
//					HSSFRow row = sheet.createRow(rownum);
//					HSSFCellStyle cellStyle = wb.createCellStyle();
//					cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//					for(column = 0;column <tds.size();column ++){
//						Element td = tds.get(column);
//						HSSFCell cell = row.createCell(column);
//						Attribute rowSpan = td.getAttribute("rowspan");
//						Attribute colSpan = td.getAttribute("colspan");
//						Attribute value = td.getAttribute("value");
//						if(value !=null){
//							String val = value.getValue();
//							cell.setCellValue(val);
//							int rspan = rowSpan.getIntValue() - 1;
//							int cspan = colSpan.getIntValue() -1;
//							
//							//��������
//							HSSFFont font = wb.createFont();
//							font.setFontName("����_GB2312");
//							font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//����Ӵ�
////							font.setFontHeight((short)12);
//							font.setFontHeightInPoints((short)12);
//							cellStyle.setFont(font);
//							cell.setCellStyle(cellStyle);
//							//�ϲ���Ԫ�����
//							sheet.addMergedRegion(new CellRangeAddress(rspan, rspan, 0, cspan));
//						}
//					}
//					rownum ++;
//				}
				//���ñ�ͷ
				Element thead = root.getChild("thead");
				List<Element> trs = thead.getChildren("tr");
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
						}
					}
					rownum++;
				}
				
				//��������������ʽ
				Element tbody = root.getChild("tbody");
				Element tr = tbody.getChild("tr");
				int repeat = tr.getAttribute("repeat").getIntValue();
				
				List<Element> tds = tr.getChildren("td");
				for (int i = 0; i < repeat; i++) {
					HSSFRow row = sheet.createRow(rownum);
					for(column =0 ;column < tds.size();column++){
						Element td = tds.get(column);
						HSSFCell cell = row.createCell(column);
						setType(wb,cell,td);
					}
					rownum++;
				}
				
				//����Excel����ģ��				
				ByteArrayOutputStream fout = new ByteArrayOutputStream();  
				wb.write(fout);
				wb.close();
	            fout.close();
	            byte[] fileContent = fout.toByteArray();  
	            is = new ByteArrayInputStream(fileContent);
	            
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return is;
			
		}
		
		/**
		 * ��Ԫ����ʽ
		 * @param wb
		 * @param cell
		 * @param td
		 */
		private static void setType(HSSFWorkbook wb, HSSFCell cell, Element td) {
			Attribute typeAttr = td.getAttribute("type");
			String type = typeAttr.getValue();
			HSSFDataFormat format = wb.createDataFormat();
			HSSFCellStyle cellStyle = wb.createCellStyle();
			if("NUMERIC".equalsIgnoreCase(type)){
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				Attribute formatAttr = td.getAttribute("format");
				String formatValue = formatAttr.getValue();
				formatValue = StringUtils.isNotBlank(formatValue)? formatValue : "#,##0.00";
				cellStyle.setDataFormat(format.getFormat(formatValue));
			}else if("STRING".equalsIgnoreCase(type)){
				cell.setCellValue("");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cellStyle.setDataFormat(format.getFormat("@"));
			}else if("DATE".equalsIgnoreCase(type)){
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				cellStyle.setDataFormat(format.getFormat("yyyy-m-d"));
			}else if("ENUM".equalsIgnoreCase(type)){
				CellRangeAddressList regions = 
					new CellRangeAddressList(cell.getRowIndex(), cell.getRowIndex(), 
							cell.getColumnIndex(), cell.getColumnIndex());
				Attribute enumAttr = td.getAttribute("format");
				String enumValue = enumAttr.getValue();
				//���������б�����
				DVConstraint constraint = 
					DVConstraint.createExplicitListConstraint(enumValue.split(","));
				//������Ч�Զ���
				HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);
				wb.getSheetAt(0).addValidationData(dataValidation);
			}
			cell.setCellStyle(cellStyle);
		}

		/**
		 * �����п�
		 * @author David
		 * @param sheet
		 * @param colgroup
		 */
		private static void setColumnWidth(HSSFSheet sheet, Element colgroup) {
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
		}
}
