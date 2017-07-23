
package cn.jagl.aq.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.struts2.ServletActionContext;
import org.aspectj.util.FileUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.DepartmentType;
import cn.jagl.aq.domain.ManagementReview;
import cn.jagl.aq.domain.ManagementReviewAttachment;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.Role;
import cn.jagl.aq.domain.RoleType;
import cn.jagl.util.AwsS3Util;

@SuppressWarnings({ "serial", "rawtypes" })
public class ManagementReviewAction extends BaseAction{
	private int page;
	private int rows;//��ҳ����
	//�ֶ�
	private int id;
	private String reviewSn;//������
	private String departmentSn;//����λ
	private String reviewEmceeSn;//����������
	private String purpose;//����Ŀ��
	private String scope;//����Χ
	private String reviewBasis;//��������
	private String reviewLocation;//����ص�
	private String reviewContent;//��������
	private String reviewRequirement;//����Ҫ��
	private String reviewMethod;//���󷽷�
	private String reviewInput;//��������
	private String reviewResult;//������
	private String correctPrevention;//������Ԥ����ʩ
	private String resultConclusion;//����ܽ�
	private String reviewOutput;//�������
	private Date reviewDate;//����ʱ��
	private String personIds;
	private InputStream wordStream; 
    private String wordFileName; 
	
	//���ز���
	private String data;
	
	//��ѯ��������
	private String q;
	private Date begintime;
	private Date endtime;
	
	//�����ϴ�
	private File[] upload;//����
	private String[] uploadContentType;//// ��װ�ϴ��ļ�����
	private String[] uploadFileName;// ��װ�ϴ��ļ���
	private String savePath;//�ļ��ϴ�·��
	private String name;
	private int attid; 
	private Integer reviewYear;
	
	
	public Integer getReviewYear() {
		return reviewYear;
	}
	public void setReviewYear(Integer reviewYear) {
		this.reviewYear = reviewYear;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getRows() {
		return rows;
	}
	public int getPage() {
		return page;
	}
	public File[] getUpload() {
		return upload;
	}
	public void setUpload(File[] upload) {
		this.upload = upload;
	}
	public String[] getUploadContentType() {
		return uploadContentType;
	}
	public void setUploadContentType(String[] uploadContentType) {
		this.uploadContentType = uploadContentType;
	}
	public String[] getUploadFileName() {
		return uploadFileName;
	}
	public void setUploadFileName(String[] uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	private String getSavePath() throws Exception
	{
		return ServletActionContext.getServletContext()
			.getRealPath(savePath);
	}
	public void setSavePath(String savePath) {
		this.savePath = savePath;
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
	public String getReviewEmceeSn() {
		return reviewEmceeSn;
	}
	public void setReviewEmceeSn(String reviewEmceeSn) {
		this.reviewEmceeSn = reviewEmceeSn;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getReviewBasis() {
		return reviewBasis;
	}
	public void setReviewBasis(String reviewBasis) {
		this.reviewBasis = reviewBasis;
	}
	public String getReviewLocation() {
		return reviewLocation;
	}
	public void setReviewLocation(String reviewLocation) {
		this.reviewLocation = reviewLocation;
	}
	public String getReviewContent() {
		return reviewContent;
	}
	public void setReviewContent(String reviewContent) {
		this.reviewContent = reviewContent;
	}
	public String getReviewRequirement() {
		return reviewRequirement;
	}
	public void setReviewRequirement(String reviewRequirement) {
		this.reviewRequirement = reviewRequirement;
	}
	public String getReviewMethod() {
		return reviewMethod;
	}
	public void setReviewMethod(String reviewMethod) {
		this.reviewMethod = reviewMethod;
	}
	public Date getReviewDate() {
		return reviewDate;
	}
	public void setReviewDate(Date reviewDate) {
		this.reviewDate = reviewDate;
	}
	public String getDepartmentSn() {
		return departmentSn;
	}
	public void setDepartmentSn(String departmentSn) {
		this.departmentSn = departmentSn;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public String getReviewSn() {
		return reviewSn;
	}
	public void setReviewSn(String reviewSn) {
		this.reviewSn = reviewSn;
	}
	public String getReviewInput() {
		return reviewInput;
	}
	public void setReviewInput(String reviewInput) {
		this.reviewInput = reviewInput;
	}
	public String getReviewResult() {
		return reviewResult;
	}
	public void setReviewResult(String reviewResult) {
		this.reviewResult = reviewResult;
	}
	public String getCorrectPrevention() {
		return correctPrevention;
	}
	public void setCorrectPrevention(String correctPrevention) {
		this.correctPrevention = correctPrevention;
	}
	public String getResultConclusion() {
		return resultConclusion;
	}
	public void setResultConclusion(String resultConclusion) {
		this.resultConclusion = resultConclusion;
	}
	public String getReviewOutput() {
		return reviewOutput;
	}
	public void setReviewOutput(String reviewOutput) {
		this.reviewOutput = reviewOutput;
	}
	public String getPersonIds() {
		return personIds;
	}
	public void setPersonIds(String personIds) {
		this.personIds = personIds;
	}
	public String getWordFileName() {
		return wordFileName;
	}
	public void setWordFileName(String wordFileName) {
		this.wordFileName = wordFileName;
	}
	public InputStream getWordStream() {
		return wordStream;
	}
	public void setWordStream(InputStream wordStream) {
		this.wordStream = wordStream;
	}
	public int getAttid() {
		return attid;
	}
	public void setAttid(int attid) {
		this.attid = attid;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	//ͨ���ļ�����ȡ��չ��
	private String getFileExt(String filename){
		return FilenameUtils.getExtension(filename);
	}
	//ͨ���ļ�����ȡû����չ�����ļ���
	private String getFileName(String filename){
		return filename.replace("."+getFileExt(filename), "");
	}
	//����UUID���������Ϊ�µ��ļ���
	private String newFileName(String filename){
		String ext=getFileExt(filename);
		return UUID.randomUUID().toString()+"."+ext;
	}
	//ʵ���ļ��ϴ����ܣ������ϴ�����µ��ļ�����
	public String uploadFile(String filename,File file,String path){
		//��ȡ�µ�Ψһ�ļ���
		String newName=newFileName(filename);
		try {
//			FileUtil.copyFile(file, new File(path,newName));
//			//System.out.println(path+"\\"+newName);
//			return path+"/"+newName;
			if(!AwsS3Util.uploadFile("managementreview/" + newName, file, session)){
				return "error";
			}
			//FileUtil.copyFile(file, new File(path,newName));
			return "managementreview/"+newName;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
			file.delete();
		}
	}
	//���ָ���ֶ�
	public PrintWriter out()throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out= response.getWriter();
        return out;
	}
	//��ʾ������
	public String showDepartment() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out= response.getWriter();
		//��ȡ��¼�˵�personId
		String pId=(String) session.get("personId");
		Person person=personService.getByPersonId(pId);
		
		List<Role> list = personService.getRoles(person);
		List<Department> departments = new ArrayList<Department>();
		
		Boolean bo = null;
		for ( Role role : list ){
			if(role.getRoleSn().equals("jtxtgly") && role.getRoleType().equals(RoleType.���Ž�ɫ)){
				String hql = "select d from Department d where d.departmentType.isImplDepartmentType = true order by d.departmentSn asc";
				departments = departmentService.findByPage(hql, 1, 10000);
				bo = true;
				break;
			}else if(role.getRoleType().equals(RoleType.��굥λ��ɫ)){
				bo = false;
			}
		}
		if(bo != true){
			Department department = departmentService.getUpNearestImplDepartment(person.getDepartment().getDepartmentSn());
			departments.add(department);
		}
		JSONArray tree=new JSONArray();
		for(Department department : departments){
			JSONObject jo=new JSONObject();
			jo.put("id",department.getDepartmentSn());
			jo.put("text",department.getDepartmentName());		
			tree.put(jo);
		}
		out.println(tree.toString()); 
        out.flush(); 
        out.close();
		return SUCCESS;
	}
	//��ҳ��ʾ�Լ���ѯ
	public String show(){
		JSONArray array=new JSONArray();
		StringBuffer hqlentity=new StringBuffer();
		StringBuffer hqlcount=new StringBuffer();
		if(departmentSn==null){
			//Ĭ����ʾ�����굥λ��Χ�ڵĹ�������
			String pId=(String) session.get("personId");
			Person person=personService.getByPersonId(pId);
			
			List<Role> list = personService.getRoles(person);
			Boolean bo = false;
			for ( Role role : list ){
				if(role.getRoleSn().equals("jtxtgly")){
					departmentSn = "1";
					bo = true;
					break;
				}
			}
			if(bo == false){
				departmentSn=person.getDepartment().getDepartmentSn();
			}
	    	Department department=departmentService.getUpNearestImplDepartment(departmentSn);
	    	if(department!=null){
	    		departmentSn=department.getDepartmentSn();
	    	}
	    	begintime=null;
		}
//		String pag="";
//		//��ѯʱ��ʾ�����굥λ�������еĲ���������Ĳ��ŵĹ������� �������û�а������Լ�
//		List<DepartmentType> departmentTypes=departmentTypeService.getImplDepartmentTypes(departmentSn);
//    	if(departmentTypes.size()>0){
//    		pag="(";
//    		for(DepartmentType departmentType:departmentTypes){
//    			pag+="'"+departmentType.getDepartmentTypeSn()+"',";
//    		}
//    		pag=pag.substring(0, pag.length()-1);
//    		pag+=")";
//    	}else{
//    		Department department=departmentService.getUpNearestImplDepartment(departmentSn);
//    		pag="('"+department.getDepartmentType().getDepartmentTypeSn()+"')";
//    	}
		//��ʾ�߼���δɾ������Щ��λ�����µĺ��Լ� ���ŵĹ�������
    	hqlentity.append("select m from ManagementReview m where m.deleted=0 and m.department.departmentSn like '"+departmentSn+"%'");
		hqlcount.append("select count(*) from ManagementReview m where m.deleted=0 and m.department.departmentSn like '"+departmentSn+"%'");
    	if(begintime!=null){
    		LocalDate begintimelocal = begintime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    		LocalDate endtimelocal = endtime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			hqlentity.append(" and m.reviewDate between '"+begintimelocal+"' and '"+endtimelocal+"'");
			hqlcount.append(" and m.reviewDate between '"+begintimelocal+"' and '"+endtimelocal+"'");
    	}
    	hqlentity.append(" order by m.reviewDate DESC");
		hqlcount.append(" order by m.reviewDate DESC");
		List<ManagementReview> managementReviews=managementReviewService.findByPage(hqlentity.toString(), page, rows);
		long total=managementReviewService.countHql(hqlcount.toString());
		for(ManagementReview managementReview:managementReviews){
			 if(id!=0){
				 managementReview=managementReviewService.getById(id);
			 }
			 JSONObject jo=new JSONObject();
			 jo.put("id",managementReview.getId());
			 if(managementReview.getDepartment() != null){
				 Department de = departmentService.getUpNearestImplDepartment(managementReview.getDepartment().getDepartmentSn());
				 if(de != null){
					 jo.put("departmentName", de.getDepartmentName());
				 }else{
					 jo.put("departmentName", managementReview.getDepartment().getDepartmentName());
				 }
			 }
			 jo.put("reviewSn",managementReview.getReviewSn());
			 jo.put("reviewDate",managementReview.getReviewDate());
			 if(managementReview.getReviewEmcee()!=null){
				 jo.put("reviewEmceeSn",managementReview.getReviewEmcee().getPersonId());
				 jo.put("reviewEmceeName",managementReview.getReviewEmcee().getPersonName());
			 }
			 if(managementReview.getParticipants()!=null){
				 jo.put("personNum",managementReview.getParticipants().size());
			 }
			 int aa=0;
			 if(managementReview.getManagementReviewAttachments()!=null){
				 for(ManagementReviewAttachment m:managementReview.getManagementReviewAttachments()){
					 if(m.getDeleted()==false){
						 aa++;
					 }
				 }
			 }
			 jo.put("attachmentNum",aa);
			 jo.put("detailSn",managementReview.getId());
			 jo.put("id",managementReview.getId());
			 Set<Person> set=managementReview.getParticipants();
			 if(set.size()>0){
   				  String ids="";
   				  for(Iterator<Person> iter=set.iterator();iter.hasNext();){
   					  ids+=iter.next().getId()+",";
   				  }
   				  ids=ids.substring(0, ids.lastIndexOf(","));
   				  jo.put("personIds", ids);
		  	 }
			 if(set.size()>0){
   				  String s="";
   				  for(Iterator<Person> iter=set.iterator();iter.hasNext();){
   					  s+=iter.next().getPersonName()+",";
   				  }
   				  s=s.substring(0, s.lastIndexOf(","));
   				  jo.put("persons", s);
		  	 }
//			 if(managementReview.getDepartment()!=null){
//				 jo.put("departmentName",managementReview.getDepartment().getDepartmentName());
//			 }
			 jo.put("purpose",managementReview.getPurpose());
			 jo.put("reviewBasis",managementReview.getReviewBasis());
			 jo.put("reviewContent",managementReview.getReviewContent());
			 jo.put("reviewLocation",managementReview.getReviewLocation());
			 jo.put("reviewMethod",managementReview.getReviewMethod());
			 jo.put("reviewRequirement",managementReview.getReviewRequirement());
			 jo.put("scope",managementReview.getScope());
			 jo.put("reviewInput",managementReview.getReviewInput());
			 jo.put("reviewOutput",managementReview.getReviewOutput());
			 jo.put("correctPrevention",managementReview.getCorrectPrevention());
			 jo.put("resultConclusion",managementReview.getResultConclusion());
			 jo.put("reviewResult",managementReview.getReviewResult());
			 jo.put("reviewYear", managementReview.getAuditYear());
			 Set<ManagementReviewAttachment> attachment=managementReview.getManagementReviewAttachments();
			 String a="";
			 String b="";
			 String c="";
			 if(attachment.size()>0){
				 for(ManagementReviewAttachment man:attachment){
					 if(man.getDeleted()==false){
						 a=a+man.getLogicalFileName()+"##";
						 b=b+man.getPhysicalFileName()+",";
						 c=c+man.getId()+",";
					 }
				 }
				 if(a.length()>0){
					 a=a.substring(0,a.length()-2);
				 }
				 if(b.length()>0){
					 b=b.substring(0,b.length()-1);
				 }
				 if(c.length()>0){
					 c=c.substring(0,c.length()-1);
				 }
			 }
			 jo.put("a", a);
			 jo.put("b", b);
			 jo.put("c", c);
			 array.put(jo);
			 if(id!=0){
				 break;
			 }
		 }
		data="{\"total\":"+total+",\"rows\":"+array+"}";
		return SUCCESS;
	}
	//���ʱ��Ų����ظ� �޸�ʱ���ı��
	//���
	public String add(){
		try{
			ManagementReview managementReview = new ManagementReview(); 	
			if( personIds.substring( personIds.length() - 2 ).equals(", ") ){
				personIds = personIds.substring(0,personIds.lastIndexOf(","));
			};
			Set<Person> persons=(new HashSet<Person>(personService.getByPersonIds(personIds)));
			managementReview.setParticipants(persons);
			String pId=(String) session.get("personId");
			Person person=personService.getByPersonId(pId);
			//����λ�ǵ�ǰ�˲�������Ĺ�굥λ ����Ƕ������ž��ǲ��ű���
			Department department=person.getDepartment();
			department=departmentService.getUpNearestImplDepartment(department.getDepartmentSn());
			if(department == null){
				data="�����ڲ��ŷǹ�굥λ���������������Ϣ��";
				return SUCCESS;
			}
			managementReview.setDepartment(department);
			Person reviewEmcee=personService.getByPersonId(reviewEmceeSn);
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");			
			reviewSn=df.format(new Date());
			managementReview.setReviewSn(reviewSn);
			managementReview.setAuditYear(reviewYear);
			managementReview.setReviewEmcee(reviewEmcee);
			managementReview.setPurpose(purpose);
			managementReview.setReviewBasis(reviewBasis);
			managementReview.setReviewContent(reviewContent);
    		LocalDate reviewDatelocal = reviewDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			managementReview.setReviewDate(reviewDatelocal);
			managementReview.setReviewLocation(reviewLocation);
			managementReview.setReviewMethod(reviewMethod);
			managementReview.setReviewRequirement(reviewRequirement);
			managementReview.setScope(scope);
			managementReview.setResultConclusion(resultConclusion);
			managementReview.setReviewInput(reviewInput);
			managementReview.setReviewOutput(reviewOutput);
			managementReview.setReviewResult(reviewResult);
			managementReview.setCorrectPrevention(correctPrevention);
			managementReview.setDeleted(false);
			managementReviewService.add(managementReview);
			//�ϴ���������ȡ
			if(upload==null||upload.length==0){
			
			}
			else{
				for(int i=0;i<upload.length;i++){
					ManagementReviewAttachment attachment=new ManagementReviewAttachment();
					String name=uploadFile(uploadFileName[i],upload[i],getSavePath());
					attachment.setLogicalFileName(name);
					attachment.setPhysicalFileName(getFileName(uploadFileName[i]));
					managementReview=managementReviewService.getBySn(reviewSn);
					attachment.setManagementReview(managementReview);
					attachment.setDeleted(false);
					managementReviewAttachmentService.add(attachment);
				}
			}			
			data="�������ɹ���";
			}
		catch(Exception e){
			data="�������ʧ�ܣ����������";
		}    
		return SUCCESS;
	}
	//����
	public String update(){
		try{
			ManagementReview managementReview=managementReviewService.getById(id);
			//Person person=(Person)ServletActionContext.getRequest().getSession().getAttribute("person");
			//Department department=person.getDepartment();
//			department=departmentService.getUpNearestImplDepartment(department.getDepartmentSn());
//			managementReview.setDepartment(department);
			Person reviewEmcee=personService.getByPersonId(reviewEmceeSn);
			if(personIds.substring(personIds.length()-2).equals(", ")){
				personIds=personIds.substring(0,personIds.lastIndexOf(",")-1);
			};
			Set<Person> persons=(new HashSet<Person>(personService.getByPersonIds(personIds)));
			managementReview.setParticipants(persons);
			managementReview.setReviewEmcee(reviewEmcee);
			managementReview.setPurpose(purpose);
			managementReview.setReviewBasis(reviewBasis);
			managementReview.setAuditYear(reviewYear);
			managementReview.setReviewContent(reviewContent);
    		LocalDate reviewDatelocal = reviewDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			managementReview.setReviewDate(reviewDatelocal);
			managementReview.setReviewLocation(reviewLocation);
			managementReview.setReviewMethod(reviewMethod);
			managementReview.setReviewRequirement(reviewRequirement);
			managementReview.setScope(scope);
			//Set<Person> persons=(new HashSet<Person>(personService.getByPersonIds(personIds)));
			managementReview.setReviewInput(reviewInput);
			managementReview.setReviewOutput(reviewOutput);
			managementReview.setReviewResult(reviewResult);
			managementReview.setCorrectPrevention(correctPrevention);
			//�ϴ���������ȡ
			if(upload!=null){
				for(int i=0;i<upload.length;i++){
					ManagementReviewAttachment attachment=new ManagementReviewAttachment();
					String name=uploadFile(uploadFileName[i],upload[i],getSavePath());
					attachment.setLogicalFileName(name);
					attachment.setPhysicalFileName(getFileName(uploadFileName[i]));
					managementReview=managementReviewService.getBySn(reviewSn);
					attachment.setManagementReview(managementReview);
					attachment.setDeleted(false);	
					managementReviewAttachmentService.add(attachment);
				}
			}
			managementReviewService.update(managementReview);
			data="���³ɹ���";
		}
		catch (Exception e){
			data="����ʧ�ܣ�";
		}
		return SUCCESS;
	}
	//ɾ��
	public String delete() throws IOException{
		out();
		String message="";
		ManagementReview managementReview=managementReviewService.getById(id);
		Set<ManagementReviewAttachment> atts=managementReview.getManagementReviewAttachments();
		for(ManagementReviewAttachment att:atts){
			if(att.getDeleted()==false){
				message="���������и�����������ɾ��������ɾ��������";
				out().print(message);
				out().flush(); 
				out().close();
				return SUCCESS;
			}
		}

		
		//�߼�ɾ����������
		managementReview.setDeleted(true);
		managementReviewService.update(managementReview);
		message="ɾ���ɹ���";
		out().print(message);
		out().flush(); 
		out().close();
		return SUCCESS;
		
	}
	//ɾ������
	public String deleteAttachment(){
		try{
			//ManagementReviewAttachment attachment=managementReviewAttachmentService.getByPath(name);
			ManagementReviewAttachment attachment=managementReviewAttachmentService.getById(attid);
			ManagementReview managementReview=managementReviewService.getById(id);
			//���������Ƴ�����
			managementReview.getManagementReviewAttachments().remove(attachment);
			managementReviewService.update(managementReview);
			//�߼�ɾ������
			if(attachment!=null){
				attachment.setDeleted(true);
				managementReviewAttachmentService.update(attachment);
				 data="ɾ�������ɹ���";
			}
			else{
				data="ɾ������ʧ�ܣ��ø��������ڣ�";
			}
			//ɾ���ļ�
//				File file=new File(name);
//				file.delete();
		}
		catch(Exception e){
			data="ɾ������ʧ�ܣ�";
		}
		return SUCCESS;
	}
	/**
	 * poi����wordʱ��\r\nת��Ϊ������
	 * @param text
	 * @return
	 */
	private String textAddParagraph(String text){
		String[] str = text.split("\\r\\n");
		String returnValue = "";
		if( str.length > 1 ){
			for( int i = 0; i < str.length - 1; i++ ){
				returnValue = returnValue + str[i] + (char)11;
			}
			returnValue += str[str.length - 1];
		}else{
			returnValue = text;
		}
		return returnValue;
	}
	
	//������word
	public String export() throws Exception{
		String path =ServletActionContext.getServletContext().getRealPath("/template/managementReview.doc");
		InputStream is0 = new FileInputStream(path);  
		HWPFDocument doc = new HWPFDocument(is0);
		ManagementReview managementReview = managementReviewService.getById(id);
		Range range=doc.getRange();
//		range.replaceText("${1}",managementReview.getPurpose());
//		range.replaceText("${2}",managementReview.getScope()); 
//		range.replaceText("${4}",managementReview.getReviewEmcee().getPersonName());
		range.replaceText("${0}", managementReview.getReviewSn());
		range.replaceText("${1}", managementReview.getReviewDate().toString());
		range.replaceText("${2}", managementReview.getReviewLocation()); 
		range.replaceText("${3}", this.textAddParagraph( managementReview.getPurpose() ));
		range.replaceText("${4}", managementReview.getReviewEmcee().getPersonName());
		Set<Person> persons=managementReview.getParticipants();
		if(persons.size()>0){
			String pe="";
			for(Person per:persons){
				pe+=per.getPersonName()+",";
			}
			pe=pe.substring(0,pe.length()-1);
			range.replaceText("${5}", pe);
		}
		else{
			range.replaceText("${5}", "��");
		};
		range.replaceText("${6}", this.textAddParagraph( managementReview.getScope() ));
		range.replaceText("${7}", this.textAddParagraph( managementReview.getReviewBasis() ));
		range.replaceText("${8}", this.textAddParagraph( managementReview.getReviewContent() ));
		range.replaceText("${9}", this.textAddParagraph( managementReview.getReviewRequirement() ));
		range.replaceText("${10}", this.textAddParagraph( managementReview.getReviewMethod() ));
		range.replaceText("${11}", this.textAddParagraph( managementReview.getReviewInput() ));
		range.replaceText("${12}", this.textAddParagraph( managementReview.getReviewResult() ));
		range.replaceText("${13}", this.textAddParagraph( managementReview.getCorrectPrevention() ));
		range.replaceText("${14}", this.textAddParagraph( managementReview.getResultConclusion() ));
		range.replaceText("${15}", this.textAddParagraph( managementReview.getReviewOutput() ));
		
		ByteArrayOutputStream fout = new ByteArrayOutputStream();  
        doc.write(fout);
        doc.close();
        fout.close();
        byte[] fileContent = fout.toByteArray();  
        ByteArrayInputStream is = new ByteArrayInputStream(fileContent);
      //��doc����������  
        wordStream=is;               
        wordFileName=URLEncoder.encode(managementReview.getAuditYear() + "���" + managementReview.getDepartment().getDepartmentName() + "��������.doc","UTF-8");
	    return "download";	
	}
	//��������
	public String attachment(){
		JSONArray array=new JSONArray();
		Set<ManagementReviewAttachment> attachments=managementReviewService.getById(id).getManagementReviewAttachments();
		long total=attachments.size();
		for(ManagementReviewAttachment attachment:attachments){
			 JSONObject jo=new JSONObject();
			 if(attachment.getDeleted()==true){
				 total=total-1;
				 continue;
			 }
			 jo.put("id", attachment.getId());
			 jo.put("reviewId",id);
			 jo.put("logicalFileName", attachment.getLogicalFileName());
			 jo.put("physicalFileName", attachment.getPhysicalFileName());
			 array.put(jo);
		 }
		data="{\"total\":"+total+",\"rows\":"+array+"}";
		return SUCCESS;
	} 
	public String addAttachment(){
		try{
			ManagementReview managementReview=managementReviewService.getById(id);
			//�ϴ���������ȡ
			if(upload!=null){
				for(int i=0;i<upload.length;i++){
					ManagementReviewAttachment attachment=new ManagementReviewAttachment();
					String name=uploadFile(uploadFileName[i],upload[i],getSavePath());
					attachment.setLogicalFileName(name);
					attachment.setPhysicalFileName(getFileName(uploadFileName[i]));
					attachment.setManagementReview(managementReview);
					attachment.setDeleted(false);	
					managementReviewAttachmentService.add(attachment);
				}
			}
			managementReviewService.update(managementReview);
			data="�ϴ��ɹ���";
		}
		catch (Exception e){
			data="�ϴ�ʧ�ܣ�";
		}
		return SUCCESS;
	}
}
