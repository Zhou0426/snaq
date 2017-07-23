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
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.hibernate.exception.ConstraintViolationException;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.jagl.aq.domain.CheckTaskAppraisals;
import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.Education;
import cn.jagl.aq.domain.Gender;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.PersonRecord;
import cn.jagl.aq.domain.PostLevel;
import cn.jagl.aq.domain.Resource;
import cn.jagl.aq.domain.Role;
import cn.jagl.aq.domain.SessionInfo;
import cn.jagl.aq.domain.UnsafeAct;
import cn.jagl.aq.domain.UnsafeCondition;
import cn.jagl.util.IpMacUtil;
import cn.jagl.util.MD5Algorithm; 

public class PersonAction extends BaseAction<Person> implements ServletResponseAware {
	private static final long serialVersionUID = 1L;
	private int id;	//���ű�id
	private String departmentSn;//�������� ��ǰҳ������ѡ����
	private String personId;//��Ա���
	private String personName;//��Ա����
	private String password;//����
	private String idNumber;//���֤��
	private String cellphoneNumber;
	private java.sql.Date birthday;//��������
	private Education education;//ѧ��
	private Gender gender;//�Ա�
	private long total;//�ܼ�¼��
	private Integer rows;//ÿҳ��ʾ�ļ�¼��
	private Integer page;//��ǰҳ��
	private String departmentSn1;
	private HashMap<String,String> sess=new HashMap<String,String>(); //��Ŵ�session�ж�ȡ������
	private Boolean rememberPwd;//��ס����
	private String oldPwd;//�洢��ס�����е�oldPwd��������Ӧ���ݿ��е��������룬ֻ���𵽲鿴�ͻ��Ƿ��������������������
	private String loginIp;//��¼ʱ�ı���ip��ַ
	private String excelContentType;
	private String data;
	private String deleteornot;
	private String deptsn;//��Աcrud��Ĳ��ű��
	
	public void test(){
		unitAppraisalsService.score();
	}
	public String getDeleteornot() {
		return deleteornot;
	}
	public void setDeleteornot(String deleteornot) {
		this.deleteornot = deleteornot;
	}
	public HashMap<String, String> getSess() {
		return sess;
	}
	public void setSess(HashMap<String, String> sess) {
		this.sess = sess;
	}
	public String getDepartmentSn1() {
		return departmentSn1;
	}
	public void setDepartmentSn1(String departmentSn1) {
		this.departmentSn1 = departmentSn1;
	}
	private HashMap<String, Object> pager = new HashMap<String, Object>();
	private Class<Person> person;
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public HashMap<String, Object> getPager() {
		return pager;
	}
	public void setPager(HashMap<String, Object> pager) {
		this.pager = pager;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getCellphoneNumber() {
		return cellphoneNumber;
	}

	public void setCellphoneNumber(String cellphoneNumber) {
		this.cellphoneNumber = cellphoneNumber;
	}

	public java.sql.Date getBirthday() {
		return birthday;
	}

	public void setBirthday(java.sql.Date birthday) {
		this.birthday = birthday;
	}

	public Class<Person> getPerson() {
		return person;
	}
	public void setPerson(Class<Person> person) {
		this.person = person;
	}

	public String getDepartmentSn() {
		return departmentSn;
	}
	public void setDepartmentSn(String departmentSn) {
		this.departmentSn = departmentSn;
	}
	public Education getEducation() {
		return education;
	}
	public void setEducation(Education education) {
		this.education = education;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}	
	//���ݲ��ż�������
	public String show() throws IOException{
			List<Person> persons=new ArrayList<Person>();
			JSONArray array=new JSONArray();	
			//�ĳ�StringBuilder
			StringBuffer h=new StringBuffer();
			StringBuffer q=new StringBuffer();	    
			if (departmentSn1!=null){
				//��ʾʱ ����ʱ�������޶������ķ�Χ��
				h.append("FROM Person d where d.department.departmentSn like '"+departmentSn1+"%'");
				q.append("select count(*) FROM Person d where d.department.departmentSn like '"+departmentSn1+"%'");								
			}
			else
			{
				//����ʱ ������������˽�ɫ ��Χ
				//�޶�������ΧΪ��ǰ�����ڲ���֮������
//				Person personnow=(Person)ServletActionContext.getRequest().getSession().getAttribute("person");
//				departmentSn1=personnow.getDepartment().getDepartmentSn();
//				h.append("FROM Person d where d.department.departmentSn like '"+departmentSn1+"%'");
//				q.append("select count(*) FROM Person d where d.department.departmentSn like '"+departmentSn1+"%'");
				//������Χ���ݽ�ɫ���ݹ�굥λ����ȫ����
			    String pId=(String)session.get("personId");
			    Person personnow=personService.getByPersonId(pId);
			    Department implDepartment=departmentService.getUpNearestImplDepartment(personnow.getDepartment().getDepartmentSn());	    
				h.append("FROM Person d where 1=1");
				q.append("select count(*) FROM Person d where 1=1");		  
			    if(implDepartment!=null){
					h.append(" and d.department.departmentSn like '"+implDepartment.getDepartmentSn()+"%'");
					q.append(" and d.department.departmentSn like '"+implDepartment.getDepartmentSn()+"%'");
			    }
			}	
			if(personId!=null){
				h.append(" and (d.personId like '" + personId + "%' or d.personName like '" + personId + "%')" );
				q.append(" and (d.personId like '" + personId + "%' or d.personName like '" + personId + "%')" );
			}
			//��Ĭ�ϲ���ʾ��ɾ��ʱ
			if( deleteornot != null && deleteornot.equals("0") ){
				h.append(" and d.deleted != true" );
				q.append(" and d.deleted != true" );
			}else{
				h.append(" and d.deleted = true" );
				q.append(" and d.deleted = true" );
			}
			h.append(" order by d.id");
			persons = personService.findByPage( h.toString(), page, rows );
			total = personService.countHql( q.toString() );
			for(Person person : persons){
				 JSONObject jo = new JSONObject();
				 //�ַ���
				 jo.put("id", person.getId());
				 jo.put("personId", person.getPersonId());
				 jo.put("personName", person.getPersonName());		
				 jo.put("cellphoneNumber", person.getCellphoneNumber());
				 jo.put("education", person.getEducation());
				 jo.put("gender", person.getGender());
				 jo.put("idNumber", person.getIdNumber());
				 if( person.getDeleted() != null ){
					 jo.put("deleted", person.getDeleted());
				 }else{
					 jo.put("deleted", false);
				 }
				 //������	 
				 jo.put("birthday", person.getBirthday());	
				 //��
				 if( person.getDepartment() != null ){
					 jo.put("implDepartmentName", person.getDepartment().getImplDepartmentName());
					 jo.put("department", person.getDepartment().getDepartmentName());
					 jo.put("departmentsn", person.getDepartment().getDepartmentSn());
				 }
				 if( person.getPostLevel() != null ){
					 jo.put("postLevell", person.getPostLevel().getPostLevelName());
					 jo.put("postLevelS", person.getPostLevel().getPostLevelSn());
				 }
				 else{
					 jo.put("postLevell","");
					 jo.put("postLevelS","");
					 
				 }
				 array.put(jo);
			}
			data="{\"total\":" + total + ",\"rows\":" + array + "}";
			return SUCCESS;		
	}
	private String message;//���ز���
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	//������Ա
	public String add() throws IOException
	{	
		try{
			//���ݱ�Ų������ݿ����Ƿ���ڴ���Ա ������ɾ��
			Person findPerson=personService.getByPersonId(personId);
			if(findPerson==null){
				Person person=new Person();
				person.setPersonId(personId.trim());
				person.setPersonName(personName.trim());
				//Department department=departmentService.getByDepartmentSn(departmentSn.trim());
				//���ſ�ѡ
				Department department=departmentService.getByDepartmentSn(deptsn);
				person.setDepartment(department);
				person.setEducation(education);
				password=MD5Algorithm.MD5(password.trim());
				person.setPassword(password);
				person.setIdNumber(idNumber.trim());
				person.setGender(gender);		
				person.setCellphoneNumber(cellphoneNumber);
				person.setBirthday(birthday);		
				PostLevel pos=postLevelService.getBySn(postLevel);
				person.setPostLevel(pos);
				person.setDeleted(false);
				personService.addPerson(person);
				data="��ӳɹ���";
			}else{
				data="����ظ�������Ա����Ѵ��ڣ�";
				return SUCCESS;
			}
		}catch(Exception e){
			data="�����Ա�쳣������ϵ������Ա��";
		}
		return SUCCESS;
	}
	//�޸���Ա
	public String update(){
		try{
			Person person=personService.getById(changeId);
			if (password.length()!=0){
				password=MD5Algorithm.MD5(password.trim());
				person.setPassword(password);
			}
			else{
				Person personnow=personService.getById(changeId);
				password=personnow.getPassword();
				person.setPassword(password);			
			}
			person.setPersonName(personName);
			//Department department=departmentService.getByDepartmentSn(departmentSn);
			//���ſ�ѡ
			Department department=departmentService.getByDepartmentSn(deptsn);
			person.setDepartment(department);
			person.setEducation(education);
			person.setIdNumber(idNumber);
			person.setGender(gender);		
			person.setCellphoneNumber(cellphoneNumber);
			person.setBirthday(birthday);
			PostLevel pos=postLevelService.getBySn(postLevel);
			person.setPostLevel(pos);
			personService.updatePerson(person);
			data="�޸���Ա��Ϣ�ɹ���";
		}catch (Exception e){
			data="�޸��쳣������ϵ������Ա��";
		}
		return SUCCESS;
	}	
	//ɾ����Ա
	public String delete()
	{
		try{
			Person person=personService.getById(changeId);
			person.setDeleted(true);
			person.setRoles(null);
			personService.updatePerson(person);
			data="ɾ���ɹ���";
		}catch(Exception e){
			data="ɾ��ʧ�ܣ�";
		}
		return SUCCESS;
	}	
	//ȡ��ɾ����Ա
	public String cancelDelete(){
		try{
			Person person=personService.getById(changeId);
			person.setDeleted(false);
			person.setRoles(null);
			personService.updatePerson(person);
			data="�ָ��ɹ���";
		}catch(Exception e){
			data="�ָ�ʧ�ܣ�";
		}
		return SUCCESS;
	}
	//�޸�����
	public String modifypwd() throws IOException{
		out();
		String data="0";
		try{
			String pId=(String)session.get("personId");
	    	Person personnow=personService.getByPersonId(pId);
	    	String personId=personnow.getPersonId();
	    	Person person=personService.getByPersonId(personId);
	    	person.setPassword(MD5Algorithm.MD5(password.trim()));
	    	person.setHasModifiedPwd(true);
	    	personService.updatePerson(person);
	    	session.clear();
	    	data="1";
		}catch(Exception e){
			
		}
    	out().print(data);
		out().flush(); 
		out().close();
		return SUCCESS;
	}
	//��֤����
	public String findpwd() throws IOException{
		out();
		String pId=(String)session.get("personId");
    	Person personnow=personService.getByPersonId(pId);
    	String p=personnow.getPassword();
    	String data = "";
    	if(p.equals(MD5Algorithm.MD5(password.trim()))){
    		data="1";
    	}
    	out().print(data);
		out().flush(); 
		out().close();
		return SUCCESS;
	}
	private Integer changeId;
	public Integer getChangeId() {
		return changeId;
	}
	public void setChangeId(Integer changeId) {
		this.changeId = changeId;
	}
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
	//poi������excel
	public String exportPerson(){  
        HSSFWorkbook wb = new HSSFWorkbook();   
        HSSFSheet sheet = wb.createSheet("��Ա��");

        sheet.setColumnWidth(1,70*70);
        HSSFRow row = sheet.createRow((int) 0);  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
  
        HSSFCell cell = row.createCell(0) ;
        cell.setCellValue("��Ա���");  
        cell.setCellStyle(style);  
        cell = row.createCell(1);  
        cell.setCellValue("��Ա����");  
        cell.setCellStyle(style);	        
        cell = row.createCell(2);  
        cell.setCellValue("����");  
        cell.setCellStyle(style);  
        cell = row.createCell(3);  
        cell.setCellValue("���֤��");  
        cell.setCellStyle(style);
        cell = row.createCell(4);  
        cell.setCellValue("�绰����");  
        cell.setCellStyle(style);
        cell = row.createCell(5);  
        cell.setCellValue("��������");  
        cell.setCellStyle(style);
        cell = row.createCell(6);  
        cell.setCellValue("ѧ��");  
        cell.setCellStyle(style);
        cell = row.createCell(7);  
        cell.setCellValue("�Ա�");  
        cell.setCellStyle(style);
        cell = row.createCell(8);  
        cell.setCellValue("���ű��");  
        cell.setCellStyle(style);
        cell = row.createCell(9);  
        cell.setCellValue("��������");  
        cell.setCellStyle(style);
        cell = row.createCell(10);  
        cell.setCellValue("��λ������");  
        cell.setCellStyle(style);
        cell = row.createCell(11);  
        cell.setCellValue("��λ��������");  
        cell.setCellStyle(style);
		List<Person> persons=new ArrayList<Person>();
		StringBuffer hql=new StringBuffer();
		hql.append("select p from Person p where p.department.departmentSn like '" + departmentSn + "%'");
		if( deleteornot != null && deleteornot.equals("0") ){
			hql.append(" and p.deleted != true" );
		}
		hql.append(" order by p.id");
        persons= personService.getPersonsByHql( hql.toString() );
        for (int i = 0; i < persons.size(); i++)  
        {  
            row = sheet.createRow((int) i + 1);  
            session.put("proValue", (int)((float)i*100/persons.size()));
            Person person = (Person) persons.get(i);  
            row.createCell( 0).setCellValue(person.getPersonId());  
            row.createCell( 1).setCellValue(person.getPersonName());  
            row.createCell( 2).setCellValue(person.getPassword()); 
            row.createCell( 3).setCellValue(person.getIdNumber());
            row.createCell( 4).setCellValue(person.getCellphoneNumber()); 
            if(person.getBirthday()!=null){
            	row.createCell( 5).setCellValue(person.getBirthday().toString());
            }
            if(person.getEducation()!=null){
                row.createCell( 6).setCellValue(person.getEducation().toString());
            }
            if(person.getGender()!=null){
                row.createCell( 7).setCellValue(person.getGender().toString());
            }
            if(person.getDepartment()!=null){
	            row.createCell( 8).setCellValue(person.getDepartment().getDepartmentSn());
	            row.createCell( 9).setCellValue(person.getDepartment().getDepartmentName());
            }
            if(person.getPostLevel()!=null){
            	row.createCell( 10).setCellValue(person.getPostLevel().getPostLevelSn());
            	row.createCell( 11).setCellValue(person.getPostLevel().getPostLevelName());
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
            excelFileName =URLEncoder.encode("��Ա��.xls", "UTF-8");   
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }
	    session.put("proValue",100);
        return "download";
	}
	
	public File getExcel() {
		return excel;
	}
	public void setExcel(File excel) {
		this.excel = excel;
	}
	private File excel;
	//��������
	@SuppressWarnings("resource")
	public String importPerson()  throws IOException{
		InputStream is0 = new FileInputStream(excel); 
	  	XSSFWorkbook wb = new XSSFWorkbook(is0);
		Person person=null;
		HashMap<String, String> record = new HashMap<String, String>();
        float num=0f;
		String isExist="��";
		String isError="��";
		String nullData="��";
		String notFindDepartment="��";
		String notFindLevel="��";
		String notPower="��";
		String notDate="��";
		String more="��";
		// ѭ��������Sheet
        for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = wb.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            num=xssfSheet.getLastRowNum();
            // ѭ����Row
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
	            session.put("progressValue", (int)((float)rowNum*100/num));
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow == null) {
                    continue;
                }
                person = new Person();
                try{
	                XSSFCell personId = xssfRow.getCell(0);
	                if (personId == null || personId.toString().trim().length() == 0) {
	                	nullData+=(rowNum+1)+",";
	                    continue;
	                }
	              //��¼����ı��
	                if(record.containsKey(StringValue(personId))){
	                	isExist+=(rowNum+1)+"��";
	                	continue;
	                }else{
	                	record.put(StringValue(personId),String.valueOf(rowNum+1));
	                }
	                person.setPersonId(StringValue(personId));
	                
	                XSSFCell personName = xssfRow.getCell(1);
	                if (personName == null || personName.toString().trim().length() == 0) {
	                	nullData+=(rowNum+1)+",";
	                    continue;
	                }
	                person.setPersonName(personName.getStringCellValue());
	                
	                XSSFCell password = xssfRow.getCell(2);
	                if (password == null || password.toString().trim().length() == 0) {
	                	nullData+=(rowNum+1)+",";
	                    continue;
	                }
	                person.setPassword(MD5Algorithm.MD5(StringValue(password).trim()));
	                
	                XSSFCell idNumber = xssfRow.getCell(3);
	                if (idNumber == null || idNumber.toString().trim().length() == 0) {
	                	person.setIdNumber(null);
	                }else{
	                	person.setIdNumber(StringValue(idNumber));
	                }
	                
	                XSSFCell cellphoneNumber = xssfRow.getCell(4);
	                if (cellphoneNumber == null || cellphoneNumber.toString().trim().length() == 0) {
	                	person.setCellphoneNumber(null);
	                }else{
	                	if(StringValue(cellphoneNumber).length()==11){
	                		person.setCellphoneNumber(StringValue(cellphoneNumber));
	                	}
	                	else{
	                		more+=(rowNum+1)+",";
	                		continue;
	                	}
	                }
	                
	                XSSFCell  birthday= xssfRow.getCell(5);
	                if (birthday == null || birthday.toString().trim().length() == 0) {
	                	person.setBirthday(null);
	                }else{
	                	if(birthday.getCellType()==Cell.CELL_TYPE_STRING){	                       	
		                	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		                	Date date = new Date();
		                	try{
		                		date = sdf.parse(birthday.getStringCellValue());
		                		java.sql.Date sdate = new java.sql.Date(date.getTime());
		                		person.setBirthday(sdate);
		                	}
		                	catch(Exception e){
		                		notDate+=(rowNum+1)+",";
		                		continue;
		                	}
	                	}
	                	else{	     
	                		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		                	Date date = new Date();
		                	try{
		                		String d = sdf.format((birthday.getDateCellValue()));
		                		date=sdf.parse(d);
		                		java.sql.Date sdate = new java.sql.Date(date.getTime());
		                		person.setBirthday(sdate);
		                	}
		                	catch(Exception e){
		                		notDate+=(rowNum+1)+",";
		                		continue;
		                	}
	                	}
	                }

	                XSSFCell  education= xssfRow.getCell(6);
	                if (education == null || education.toString().trim().length() == 0) {
		                person.setEducation(null);
	                }else{
	                	person.setEducation(Education.valueOf(education.getStringCellValue()));
	                }
	                
	                XSSFCell  gender= xssfRow.getCell(7);
	                if (gender == null || gender.toString().trim().length() == 0) {
		                person.setGender(null);

	                }else{
	                	person.setGender(Gender.valueOf(gender.getStringCellValue()));
	                }
	                
	                XSSFCell  departmentSn= xssfRow.getCell(8);
	                if (departmentSn == null || departmentSn.toString().trim().length() == 0) {
	                	nullData+=(rowNum+1)+",";
	                    continue;
	                }
	                String pId=(String)session.get("personId");
	    	    	Person personnow=personService.getByPersonId(pId);
	            	String departmentSn0="";
	        		Boolean isDwxtgly=false;
	        		for(Role role :personService.getRoles(personnow)){
	        			if(role.getRoleSn().equals("dwxtgly")){
	        				isDwxtgly=true;
	        			}
	        		}
	        		if(isDwxtgly){
	        			departmentSn0= departmentService.getUpNearestImplDepartment(personnow.getDepartment().getDepartmentSn()).getDepartmentSn();
		                if(!StringValue(departmentSn).contains(departmentSn0)){
		                	notPower+=(rowNum+1)+",";
		                	continue;
		                }
	        	    }
	                Department department=departmentService.getByDepartmentSn(StringValue(departmentSn));
	                if(department==null){
	                	notFindDepartment+=(rowNum+1)+",";
	                	continue;
	                }
	                else
	                {
	                  person.setDepartment(department);
	                }
	                
	                XSSFCell  levelSn= xssfRow.getCell(10);
	                if (levelSn == null || levelSn.toString().trim().length() == 0) {
	                	nullData+=(rowNum+1)+",";
	                    continue;
	                }
	                PostLevel level=postLevelService.getBySn(StringValue(levelSn));
	                if(level==null){
	                	notFindLevel+=(rowNum+1)+",";
	                	continue;
	                }
	                person.setPostLevel(level);
	             }
                catch(Exception e){
	            	isError+=(rowNum+1)+",";
                	continue;
                }
                try{
                	try
                	{
                		person.setDeleted(false);
                		personService.addPerson(person);
                	}
                	catch(ConstraintViolationException e)
                	{
                		Person pe=personService.getByPersonId(StringValue(xssfRow.getCell(0)));
		                pe.setPersonName(StringValue(xssfRow.getCell(1)));
		                pe.setPassword(MD5Algorithm.MD5(StringValue(xssfRow.getCell(2)).trim()));
		                
		                XSSFCell idNumber = xssfRow.getCell(3);
		                if (idNumber == null || idNumber.toString().trim().length() == 0) {
		                	person.setIdNumber(null);
		                }else{
		                	person.setIdNumber(StringValue(idNumber));
		                }
		                
		                XSSFCell cellphoneNumber = xssfRow.getCell(4);
		                if (cellphoneNumber == null || cellphoneNumber.toString().trim().length() == 0) {
		                	person.setCellphoneNumber(null);
		                }else{
		                	if(StringValue(cellphoneNumber).length()==11){
		                		person.setCellphoneNumber(StringValue(cellphoneNumber));
		                	}
		                	else{
		                		more+=(rowNum+1)+",";
		                		continue;
		                	}
		                }
		                
		                XSSFCell  birthday= xssfRow.getCell(5);
		                if (birthday == null || birthday.toString().trim().length() == 0) {
		                	person.setBirthday(null);
		                }else{
		                	if(birthday.getCellType()==Cell.CELL_TYPE_STRING){	                       	
			                	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			                	Date date = new Date();
			                	try{
			                		date = sdf.parse(birthday.getStringCellValue());
			                		java.sql.Date sdate = new java.sql.Date(date.getTime());
			                		person.setBirthday(sdate);
			                	}
			                	catch(Exception ee){
			                		notDate+=(rowNum+1)+",";
			                		continue;
			                	}
		                	}
		                	else{	     
		                		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			                	Date date = new Date();
			                	try{
			                		String d = sdf.format((birthday.getDateCellValue()));
			                		date=sdf.parse(d);
			                		java.sql.Date sdate = new java.sql.Date(date.getTime());
			                		person.setBirthday(sdate);
			                	}
			                	catch(Exception ee){
			                		notDate+=(rowNum+1)+",";
			                		continue;
			                	}
		                	}
		                }
		                XSSFCell  education= xssfRow.getCell(6);
		                if (education == null || education.toString().trim().length() == 0) {
			                person.setEducation(null);
		                }else{
		                	person.setEducation(Education.valueOf(education.getStringCellValue()));
		                }
		                
		                XSSFCell  gender= xssfRow.getCell(7);
		                if (gender == null || gender.toString().trim().length() == 0) {
			                person.setGender(null);

		                }else{
		                	person.setGender(Gender.valueOf(gender.getStringCellValue()));
		                }
		                Department de=departmentService.getByDepartmentSn(StringValue(xssfRow.getCell(8)));
		                pe.setDepartment(de);
		                PostLevel level=postLevelService.getBySn(xssfRow.getCell(10).toString());
		                person.setPostLevel(level);
                		person.setDeleted(false);
		                personService.updatePerson(pe);		                
                	}
                }
                catch(Exception e){
                	isError+=(rowNum+1)+",";
                }
            }
        }
        data="";
        String[] number;
        if(!"��".equals(nullData)){
        	nullData=nullData.substring(0, nullData.length()-1) + "���п����ݣ�" + "<br />";
        	data+=nullData;
        	number=nullData.split(",");
        	data=data+"��"+number.length+"��"+ "<br /><br />";
        }
        if(!"��".equals(isExist)){
        	isExist=isExist.substring(0, isExist.length()-1) + "�����ظ���ţ�����ʧ�ܣ�" + "<br />";
        	data+=isExist;
        	number=isExist.split(",");
        	data=data+"��"+number.length+"��"+ "<br /><br />";
        }
        if(!"��".equals(notFindDepartment)){
        	notFindDepartment=notFindDepartment.substring(0, notFindDepartment.length()-1) + "�в��ű�Ų����ڣ�" + "<br />";
        	data+=notFindDepartment;
        	number=notFindDepartment.split(",");
        	data=data+"��"+number.length+"��"+ "<br /><br />";
        }
        if(!"��".equals(notFindLevel)){
        	notFindLevel=notFindLevel.substring(0, notFindLevel.length()-1) + "�и�λ���𲻴��ڣ�" + "<br />";
        	data+=notFindLevel;
        	number=notFindLevel.split(",");
        	data=data+"��"+number.length+"��"+ "<br /><br />";
        }
        if(!"��".equals(notPower)){
        	notPower=notPower.substring(0, notPower.length()-1) + "�в��ű��Խ��������Ȩ����ò��ţ�" + "<br />";
        	data+=notPower;
        	number=notPower.split(",");
        	data=data+"��"+number.length+"��"+ "<br /><br />";
        }
        if(!"��".equals(notDate)){
        	notDate=notDate.substring(0, notDate.length()-1) + "�г������ڸ�ʽ����,��ʹ�ó����ʽ�����ڸ�ʽ��" + "<br />";
        	data+=notDate;
        	number=notDate.split(",");
        	data=data+"��"+number.length+"��"+ "<br /><br />";
        }
        if(!"��".equals(more)){
        	more=more.substring(0, more.length()-1) + "���ֻ������ʽ����,ֻ��Ϊ11λ����" + "<br />";
        	data+=more;
        	number=more.split(",");
        	data=data+"��"+number.length+"��"+ "<br /><br />";
        }
        if(!"��".equals(isError)){
        	isError=isError.substring(0, isError.length()-1) + "�е����쳣�������ʽ��" + "<br />";
        	data+=isError;
        	number=isError.split(",");
        	data=data+"��"+number.length+"��"+ "<br /><br />";
        }
        if(data.equals("")){
        	data="�������ݳɹ���";
        }else{
        	data+="�������ݵ���ɹ���";
        }
	    session.put("progressValue",0);
	    return SUCCESS;
	}	  
	//ת������ȡ�ַ�
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
    public  String getStringValue(HSSFRow row,int index){  
        String rtn = "";  
        try {  
            HSSFCell cell = row.getCell(index);  
            rtn = cell.getRichStringCellValue().getString();  
        } catch (RuntimeException e) {  
        }  
        return rtn;  
    } 
    public  Date getDateValue(HSSFRow row,int index){ 
            HSSFCell cell = row.getCell(index);  
            Date rtn = cell.getDateCellValue();
            return rtn;
    }
    //��Ա����
    @SuppressWarnings("resource")
	public String change() throws IOException{  
	    InputStream is0 = new FileInputStream(excel); 
	  	XSSFWorkbook wb = new XSSFWorkbook(is0);
		Person person=null;
		HashMap<String, String> record = new HashMap<String, String>();
        float num=0f;
		String isExist="��";
		String isError="��";
		String nullData="��";
		String notFindDepartment="��";
		String notFindPerson="��";
		//String notPower="��";
		String notPersonPower="��";
		// ѭ��������Sheet
        for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = wb.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            num=xssfSheet.getLastRowNum();
            // ѭ����Row
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
	            session.put("progressValue", (int)((float)rowNum*100/num));
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow == null) {
                	//nullData+=(rowNum+1)+",";
                    continue;
                }
                person = new Person();
                try{
	                XSSFCell personId = xssfRow.getCell(0);
	                if (personId == null || StringValue(personId).trim().length() == 0) {
	                	nullData+=(rowNum+1)+",";
	                    continue;
	                }
	                person=personService.getByPersonId(StringValue(personId));
	                if(person==null){
	                	notFindPerson+=(rowNum+1)+",";
	                	continue;
	                }
	                for(Role role:personService.getRoles(person)){
		                if(role.equals("jtxtgly")||role.equals("dwxtgly")){
		                	notPersonPower+=(rowNum+1)+",";
		                	continue;
		                }
	                }
	              //��¼����ı��
	                if(record.containsKey(StringValue(personId))){
	                	isExist+=(rowNum+1)+"��";
	                	continue;
	                }else{
	                	record.put(StringValue(personId),String.valueOf(rowNum+1));
	                }
	                
	                
	                XSSFCell  departmentSn= xssfRow.getCell(2);
	                if (departmentSn == null || StringValue(departmentSn).trim().length() == 0) {
	                	nullData+=(rowNum+1)+",";
	                    continue;
	                }
	                Department department=departmentService.getByDepartmentSn(StringValue(departmentSn));
	                if(department==null){
	                	notFindDepartment+=(rowNum+1)+",";
	                	continue;
	                }
	                person.setDepartment(department);
	                person.setDeleted(false);
	                personService.updatePerson(person);
	                
	            }catch(Exception e){
	            	isError+=(rowNum+1)+",";
                	continue;
                }
            }
        }
        data="";
        String[] number;
        if(!"��".equals(nullData)){
        	nullData=nullData.substring(0, nullData.length()-1) + "���п����ݣ�" + "<br />";
        	data+=nullData;
        	number=nullData.split(",");
        	data=data+"��"+number.length+"��"+ "<br /><br />";
        }
        if(!"��".equals(isExist)){
        	isExist=isExist.substring(0, isExist.length()-1) + "�����ظ���ţ�����ʧ�ܣ�" + "<br />";
        	data+=isExist;
        	number=isExist.split(",");
        	data=data+"��"+number.length+"��"+ "<br /><br />";
        }
        if(!"��".equals(notFindDepartment)){
        	notFindDepartment=notFindDepartment.substring(0, notFindDepartment.length()-1) + "�в��ű�Ų����ڣ�" + "<br />";
        	data+=notFindDepartment;
        	number=notFindDepartment.split(",");
        	data=data+"��"+number.length+"��"+ "<br /><br />";
        }
        if(!"��".equals(notFindPerson)){
        	notFindPerson=notFindPerson.substring(0, notFindPerson.length()-1) + "����Ա��Ų����ڣ�" + "<br />";
        	data+=notFindPerson;
        	number=notFindPerson.split(",");
        	data=data+"��"+number.length+"��"+ "<br /><br />";
        }
        if(!"��".equals(notPersonPower)){
        	notPersonPower=notPersonPower.substring(0, notPersonPower.length()-1) + "�в��ű��Խ������Ȩ�����ý�ɫ��" + "<br />";
        	data+=notPersonPower;
        	number=notPersonPower.split(",");
        	data=data+"��"+number.length+"��"+ "<br /><br />";
        }
        if(!"��".equals(isError)){
        	isError=isError.substring(0, isError.length()-1) + "�е����쳣�������ʽ��" + "<br />";
        	data+=isError;
        	number=isError.split(",");
        	data=data+"��"+number.length+"��"+ "<br /><br />";
        }
        if(data.equals("")){
        	data="������Ա�ɹ���";
        }
        else{
        	data+="������Ա�����ɹ���";
        }
	    session.put("progressValue",0);
	    return SUCCESS;
    }
    //���ص���ģ��
  	public String downloadChange() throws IOException{  
	  		String path =ServletActionContext.getServletContext().getRealPath("/template/personmove.xlsx");
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
              //excelFileName ="��Ա����ģ��.xlsx"; 	    
              excelFileName=URLEncoder.encode("��Ա����ģ��.xlsx","UTF-8");
          }  
          catch (Exception e)  
          {  
              e.printStackTrace();  
          }
          return "download";
  	}
	//���ص���ģ��
	public String importTemplate() throws IOException{

		String path =ServletActionContext.getServletContext().getRealPath("/template/persons.xlsx");
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
            excelFileName=URLEncoder.encode("��Ա����ģ��.xlsx","UTF-8");     
		}  
		catch (Exception e)  
		{  
		    e.printStackTrace();  
		}
		return "download";
	}
  	public String getMenu() throws IOException {
		String personId = session.get("personId").toString();
		List<Resource> resources=new ArrayList<Resource>();
		
		Resource parentResource=resourceService.getById(id);
		if(parentResource!=null){
			resources=personService.getMenu(personId,parentResource.getResourceSn());			
		}else{
			resources=personService.getMenu(personId, null);
		}
		//
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out;
        out = response.getWriter();
		JSONArray tree=new JSONArray();
		for(Resource resource:resources){
			JSONObject jo=new JSONObject();
			jo.put("id",resource.getId());
			jo.put("text",resource.getResourceName());
			jo.put("url",resource.getUrl());
			if(resource.getHasMenuChildren()){
				jo.put("state","closed");
			}else{
				jo.put("state", "open");
			}
			tree.put(jo);
		}
		out.print(tree.toString());
        out.flush(); 
        out.close(); 
		return SUCCESS;
	}
  	private JSONObject jsonObject = new JSONObject();
  	public JSONObject getJsonObject() {
		return jsonObject;
	}
	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}
	/**
  	 * �ж�session�Ƿ����
  	 * @return
  	 */
  	public String sessionJudge(){
  		try{
  			sess.put("message", "false");
  			sess.put("timeDiff", "30" );
  			if( session.get("sessionTime") != null ){
  				LocalDateTime dateTime = LocalDateTime.parse(String.valueOf( session.get("sessionTime") ));
//  				LocalDateTime dateTime = LocalDateTime.parse(sessionTime);
  				Long timeMin = Duration.between(LocalDateTime.now(), dateTime).toMinutes();
  				if( timeMin <= 30 ){
  					sess.put("message", "true");
  					sess.put("timeDiff", String.valueOf(30 - timeMin));
  				}
  			}
  		}catch(Exception e){
  			sess.put("message", "false");
  			sess.put("timeDiff", "30" );
  		}
  		return LOGIN;
  	}
	/**
	 * �û���¼��֤
	 * @return
	 * @throws IOException
	 */
  	public String login() throws IOException {
        response.setContentType("text/plain; charset=utf-8");
        response.addHeader("Set-Cookie", "CookieName=CookieValue; path=/;");
        PrintWriter out;
        out = response.getWriter();
        JSONObject jo=new JSONObject();
		Person person = personService.getByPersonId(personId);
		Boolean right=false;
		if(person==null){
			right=false;
		}else{
			Cookie cookies[]=ServletActionContext.getRequest().getCookies();			
			if(rememberPwd){//���ѡ���ס���룬���cookie��дcookie
				if(password.equals(oldPwd)){//����û�û���޸����룬��ʹ��cookie�еĴ洢�ļ��ܺ�������¼
					String cookiePassword="";					
					for(Cookie cookie:cookies){
						if(cookie.getName().equals("password")){
							cookiePassword=cookie.getValue();
						}
					}
					if(cookiePassword.equals(person.getPassword())){
						right=true;
					}else{
						right=false;
					}
				}else{//�û��Ѿ��޸����룬��ʹ��������������¼
					if(MD5Algorithm.MD5(password).equals(person.getPassword())){
						right=true;
					}else{
						right=false;
					}
				}
			}else{
				if(MD5Algorithm.MD5(password).equals(person.getPassword())){
					right=true;
				}else{
					right=false;
				}
			}
		}
		if(right){
			//�����û��������ߵ�SessionId
			person.setSessionId(ServletActionContext.getRequest().getSession().getId().toString());
			person.setIpAddress(loginIp);
			//�������ݿ�
			personService.updatePerson(person);	
			//��¼��½��Ϣ
			SessionInfo sessionInfo=sessionInfoService.getByJsessionId(person.getSessionId());
			if(sessionInfo==null){
				sessionInfo=new SessionInfo();
				sessionInfo.setDepartment(person.getDepartment());
				sessionInfo.setOperator(person);
				sessionInfo.setJsessionId(person.getSessionId());
				sessionInfo.setInternalIp(loginIp);
				sessionInfo.setInternetIp(IpMacUtil.getRealIPAddr(ServletActionContext.getRequest()));
				sessionInfo.setLoginDateTime(java.time.LocalDateTime.now());
				sessionInfoService.save(sessionInfo);
			}
			
			//session.put("person", person);
			session.put("pId", String.valueOf(person.getId()));
			session.put("personId", person.getPersonId());			
			session.put("personName", person.getPersonName());
			session.put("departmentSn", person.getDepartment().getDepartmentSn());
			session.put("departmentName", person.getDepartment().getDepartmentName());
			session.put("roles", personService.getRoles(personId));//��HashMap<��ɫ��ţ���ɫ����>����ʽ�洢��ɫ
			session.put("permissions", personService.getResources(personId));//��HashMap<��Դ��ţ���Դ����>����ʽ�洢��Դ
			session.put("progressValue", 0);//��������ʼֵ
			Long total = queryCorrect(personId);//����������
			Long totals = queryReview(personId);//���Ҹ�����
			float num = this.queryCheck(person.getPersonId());//���Ҽ���
			Long totales = checkTableService.countExceptTrue(personId);//�ҵļ���
			Long deferTatal = this.queryDefer(person.getPersonId());//����������
			session.put("correctCount", total);
			session.put("reviewCount", totals);
			session.put("checkTask", num);
			session.put("myCheckTable", totales);
			session.put("DeferThing", deferTatal);
			session.put("countThing", total + totals + totales + deferTatal + num);
			session.put("roleType", roleService.getRoleType(person.getPersonId()));
			//
			jo.put("result", "success");
			jo.put("personId", person.getPersonId());
			jo.put("password",person.getPassword());
			jo.put("chkRemember", rememberPwd);
		}else{
			jo.put("result", "error");
		}
		out.print(jo.toString());
        out.flush(); 
        out.close(); 
		return SUCCESS;
	}
	//���ָ���ֶ�
	public PrintWriter out()throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out= response.getWriter();
        return out;
	}
	/**
	 * ������Ա����ж�ĳ�����Ƿ����
	 * @return
	 * @throws IOException
	 */
	public String existsByPersonId() throws IOException{
		out();
		JSONObject jo=new JSONObject();
		
		Person p= personService.getByPersonId(personId);
		if(p==null){
			jo.put("exists",false);
		}else{
			jo.put("exists", true);
		}
		out().print(jo.toString());
		out().flush(); 
		out().close();		
		return SUCCESS;	
	}
	//������и�λ�ȼ�
	
	
	public String levels(){
		JSONArray array=new JSONArray();
		StringBuffer hqlentity=new StringBuffer();
		StringBuffer hqlcount=new StringBuffer();
		hqlentity.append("select p from PostLevel p");
		hqlcount.append("select count(p) from PostLevel p");
		if(q!=null && !"".equals(q)){
    		hqlentity.append(" where p.postLevelSn like '"+q+"%' or p.postLevelName like '"+q+"%'");
    		hqlcount.append(" where p.postLevelSn like '"+q+"%' or p.postLevelName like '"+q+"%'");
    	}
		List<PostLevel> postLevels=postLevelService.findByPage(hqlentity.toString(), 1, 10);
		long total=postLevelService.countHql(hqlcount.toString());
		for(PostLevel postLevel:postLevels){
			JSONObject jo=new JSONObject();
			jo.put("postLevelSn", postLevel.getPostLevelSn());
			jo.put("postLevelName", postLevel.getPostLevelName());
			array.put(jo); 
		}
		data="{\"total\":"+total+",\"rows\":"+array+"}";
		return SUCCESS;	
	}

	//��������--�������ĵ�
	public Long queryCorrect(String personId){
		String sql="select count(*) from inconformity_item i where i.inconformity_item_type like '����' and i.deleted=false and i.has_correct_confirmed=false and i.correct_principal_sn = '"+personId+"'";
		total=unsafeConditionService.count(sql);
		return total;
	}
	//��������--���Ҹ����
	public Long queryReview(String personId){
		String sql="select count(*) from (select distinct i.*  from inconformity_item i left join inconformity_item_checker c on i.inconformity_item_sn=c.inconformity_item_sn left join person p ON c.person_id = p.person_id where i.deleted=false and i.inconformity_item_type like '����' and i.has_correct_confirmed=true and i.has_reviewed=false and i.has_correct_finished=false and (p.person_id='"+personId+"' or editor_id='"+personId+"')) as a";
		total=unsafeConditionService.count(sql);
		return total;
	}
	//��������--����������
	public Long queryDefer(String personId){
		String hql="select count(distinct u.applicationSn) from UnsafeConditionDefer u LEFT JOIN u.unsafecondition.checkers c where (u.unsafecondition.editor.personId='" + personId + "' OR c.personId = '" + personId + "') AND u.passed is null";
		total = unsafeConditionDeferService.getByhql(hql);
		return total;
	}
	//��������--���Ҽ���
	public float queryCheck(String personId){
		float numUnsafeCondition = 0;
		float numUnsafeAct = 0;
		float numTotal = 0;
		PersonRecord personRecord=personRecordService.getByPersonId(personId);
		CheckTaskAppraisals checkTaskAppraisals=checkTaskAppraisalsService.getBycheckerSn(personId, personRecord.getDepartment().getDepartmentSn(), LocalDate.now().withDayOfMonth(1).toString());
		if(checkTaskAppraisals!=null){
			String ksTime="";
			if(personRecord.getStartDateTime().toLocalDate().isBefore(LocalDate.now().withDayOfMonth(1))){
				ksTime=LocalDate.now().withDayOfMonth(1).toString();
			}else{
				ksTime=personRecord.getStartDateTime().toLocalDate().toString();
			}
			//��������
			String hql="select distinct u from UnsafeCondition u LEFT JOIN u.checkers c"
					+ " where u.deleted=false AND u.checkDateTime between '"+ksTime+" 00:00:00'"
					+ " and '"+LocalDate.now().withDayOfMonth(LocalDate.now().getDayOfMonth()).toString()+" 23:59:59'"
					+ " AND c.personId = '"+personId+"'";
			List<UnsafeCondition> listUnsafeCondition = unsafeConditionService.query(hql, 0, 0);
			for(UnsafeCondition unsafeCondition : listUnsafeCondition)
			{
				if(unsafeCondition.getCheckers().size()>1)
				{
					numUnsafeCondition += ((float)1 / (float)unsafeCondition.getCheckers().size());
				}else{
					numUnsafeCondition += 1;
				}
			}
			//����ȫ��Ϊ����
			hql="select distinct u from UnsafeAct u LEFT JOIN u.checkers c"
					+ " where u.deleted=false AND u.checkDateTime between '"+ksTime+" 00:00:00'"
					+ " and '"+LocalDate.now().withDayOfMonth(LocalDate.now().getDayOfMonth()).toString()+" 23:59:59'"
					+ " AND c.personId = '"+personId+"'";
			List<UnsafeAct> listUnsafeAct = unsafeActService.query(hql, 0, 0);
			for(UnsafeAct unsafeAct : listUnsafeAct)
			{
				if(unsafeAct.getCheckers().size()>1)
				{
					numUnsafeAct += ((float)1/(float)unsafeAct.getCheckers().size());
				}else{
					numUnsafeAct += 1;
				}
			}
			
			if(checkTaskAppraisals.getCheckTaskCount() != null)
			{
				numUnsafeCondition = (float)checkTaskAppraisals.getCheckTaskCount() - numUnsafeCondition;
			}else{
				numUnsafeCondition = (float)0 - numUnsafeCondition;
			}
			if( checkTaskAppraisals.getUnsafeActCheckTaskCount() != null )
			{
				numUnsafeAct = (float)checkTaskAppraisals.getUnsafeActCheckTaskCount() - numUnsafeAct;
			}else{
				numUnsafeAct = (float)0 - numUnsafeAct;
			}
			if(numUnsafeCondition < 0)
				numUnsafeCondition = 0f;
			if(numUnsafeAct < 0)
				numUnsafeAct = 0f;
			numTotal = numUnsafeAct + numUnsafeCondition;
		}else{
			numTotal = 0f;
		}
		return numTotal;
	}
	//����PersonId��ȡ���ű�źͲ�������
	public String departmentByPersonId(){
		String personId=(String) session.get("personId");
		if(personId!=null){
			Person person=personService.getByPersonId(personId);
			sess.put("id", String.valueOf(person.getId()));
			sess.put("personId", person.getPersonId());
			sess.put("personName", person.getPersonName());
			sess.put("departmentSn", person.getDepartment().getDepartmentSn());
			sess.put("departmentName", person.getDepartment().getDepartmentName());
		}
		return LOGIN;
	}
	//��ȡsession����
	public String findSession(){
		String pId = (String)session.get("personId");
    	Person person = personService.getByPersonId(pId);
		Long total = this.queryCorrect(person.getPersonId());//����������
		Long totals = this.queryReview(person.getPersonId());//���Ҹ�����
		float num = this.queryCheck(person.getPersonId());//���Ҽ���
		Long totales = checkTableService.countExceptTrue(person.getPersonId());//�ҵļ���
		Long deferTatal = this.queryDefer(person.getPersonId());//����������
		session.put("correctCount", total);
		session.put("reviewCount", totals);
		session.put("checkTask", num);
		session.put("myCheckTable", totales);
		session.put("DeferThing", deferTatal);
		session.put("countThing", total + totals + totales + deferTatal + num);
		
		sess.put("correctCount", String.valueOf(total));
		sess.put("reviewCount", String.valueOf(totals));
		sess.put("checkTask", String.valueOf(num));
		sess.put("myCheckTable", String.valueOf(totales));
		sess.put("DeferThing", String.valueOf(deferTatal));
		sess.put("countThing", String.valueOf(total + totals + totales + deferTatal + num));
		return LOGIN;
	}
	//���session
	public String exit(){
		session.clear();
		ServletActionContext.getRequest().getSession().invalidate();
		return SUCCESS;
	}
	
	private String q;
	private String postLevel;
	private HttpServletResponse response;


	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	public String getPostLevel() {
		return postLevel;
	}
	public void setPostLevel(String postLevel) {
		this.postLevel = postLevel;
	}
	public Boolean getRememberPwd() {
		return rememberPwd;
	}
	public void setRememberPwd(Boolean rememberPwd) {
		this.rememberPwd = rememberPwd;
	}
	public String getOldPwd() {
		return oldPwd;
	}
	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}
	@Override
	public void setServletResponse(HttpServletResponse response) {
		
		this.response=response;
	}
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	public String getExcelContentType() {
		return excelContentType;
	}
	public void setExcelContentType(String excelContentType) {
		this.excelContentType = excelContentType;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getDeptsn() {
		return deptsn;
	}
	public void setDeptsn(String deptsn) {
		this.deptsn = deptsn;
	}
	

}