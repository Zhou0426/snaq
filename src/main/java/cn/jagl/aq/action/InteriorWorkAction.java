package cn.jagl.aq.action;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.DepartmentType;
import cn.jagl.aq.domain.InteriorWork;
import cn.jagl.aq.domain.InteriorWorkAttachment;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.Standard;
import cn.jagl.aq.domain.StandardIndex;
import cn.jagl.util.AwsS3Util;


public class InteriorWorkAction extends BaseAction<InteriorWork>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    
	private int id;
	private String interiorName;//��ҵ����
	private String departmentSn;//���ű��
	private String standardSn;//��׼���
	private String standardIndexSn;//ָ����
	private String interiorWorkSn;//��ҵ���
	private String fileName;//�ļ�����
	private String attachmentPath;//�ļ�·��
	private InputStream excelStream;//�ļ�������
	
	private String pag;//���ز���
	private long total;//����
	private String title;//�ļ�����
	private File[] upload;//����
	private String[] uploadContentType;//// ��װ�ϴ��ļ�����
	private String[] uploadFileName;// ��װ�ϴ��ļ���
	private String savePath;//�ļ��ϴ�·��
	private File[] file;
	private String[] fileContentType;
	private String[] fileFileName;
	private net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();

	
	
	public File[] getFile() {
		return file;
	}
	public void setFile(File[] file) {
		this.file = file;
	}
	public String[] getFileContentType() {
		return fileContentType;
	}
	public void setFileContentType(String[] fileContentType) {
		this.fileContentType = fileContentType;
	}
	public String[] getFileFileName() {
		return fileFileName;
	}
	public void setFileFileName(String[] fileFileName) {
		this.fileFileName = fileFileName;
	}
	public net.sf.json.JSONObject getJsonObject() {
		return jsonObject;
	}
	public void setJsonObject(net.sf.json.JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}
	public String getAttachmentPath() {
		return attachmentPath;
	}
	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}
	public InputStream getExcelStream() {
		return excelStream;
	}
	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getInteriorWorkSn() {
		return interiorWorkSn;
	}
	public void setInteriorWorkSn(String interiorWorkSn) {
		this.interiorWorkSn = interiorWorkSn;
	}
	public String getTitle() {
		return (this.title);
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getInteriorName() {
		return interiorName;
	}
	public void setInteriorName(String interiorName) {
		this.interiorName = interiorName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDepartmentSn() {
		return departmentSn;
	}
	public void setDepartmentSn(String departmentSn) {
		this.departmentSn = departmentSn;
	}
	public String getStandardSn() {
		return standardSn;
	}
	public void setStandardSn(String standardSn) {
		this.standardSn = standardSn;
	}
	public String getStandardIndexSn() {
		return standardIndexSn;
	}
	public void setStandardIndexSn(String standardIndexSn) {
		this.standardIndexSn = standardIndexSn;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	

	public String getPag() {
		return pag;
	}
	public void setPag(String pag) {
		this.pag = pag;
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
	public void setSavePath(String value)
	{
		this.savePath = value;
	}
	// ��ȡ�ϴ��ļ��ı���λ��
	private String getSavePath() throws Exception
	{
		return ServletActionContext.getServletContext()
			.getRealPath(savePath);
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
			if(!AwsS3Util.uploadFile("interiorwork/" + newName, file, session)){
				return "error";
			}
			//FileUtil.copyFile(file, new File(path,newName));
			return "interiorwork/"+newName;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
			file.delete();
		}
	}
	
	//���ݲ��ű�Ų�ѯ��������,����ѯ��׼--�ϴ�
	public void standard() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out= response.getWriter();
        	JSONArray jsonArray=new JSONArray();
        	Department department=departmentService.getUpNearestImplDepartment(departmentSn);
        	if(department!=null){
        		pag=department.getDepartmentType().getDepartmentTypeSn();
        		String hql="select s FROM Standard s WHERE s.deleted=false AND s.departmentType.departmentTypeSn ='"+pag+"'";
        		List<Standard> list=standardService.query(hql, 1, 10000);
        		for(Standard standard:list){
        			JSONObject jo=new JSONObject();
        			jo.put("id", standard.getStandardSn());
        			jo.put("text",standard.getStandardName());
        			jsonArray.put(jo);
        		}
        	}
    	out.print(jsonArray.toString());
		out.flush();
		out.close();
	}
	//���ݲ��ű�Ų�ѯ��������,����ѯ��׼--��ѯ
	public void standardQuery() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out= response.getWriter();
        	JSONArray jsonArray=new JSONArray();
        	List<DepartmentType> departmentTypes=departmentTypeService.getImplDepartmentTypes(departmentSn);
        	if(departmentTypes.size()>0){
        		pag="(";
        		for(DepartmentType departmentType:departmentTypes){
        			pag+="'"+departmentType.getDepartmentTypeSn()+"',";
        		}
        		pag=pag.substring(0, pag.length()-1);
        		pag+=")";
        	}else{
        		Department department=departmentService.getUpNearestImplDepartment(departmentSn);
        		pag="('"+department.getDepartmentType().getDepartmentTypeSn()+"')";
        	}
        		String hql="select s FROM Standard s WHERE s.deleted=false AND s.departmentType.departmentTypeSn in "+pag;
        		List<Standard> list=standardService.query(hql, 1, 10000);
        		for(Standard standard:list){
        			JSONObject jo=new JSONObject();
        			jo.put("id", standard.getStandardSn());
        			jo.put("text",standard.getStandardName());
        			jsonArray.put(jo);
        		}
        	
        	out.print(jsonArray.toString());
    		out.flush();
    		out.close();
        //}
		}
	/**
	 * չʾ��ҳ���ϸ���
	 * @return
	 */
	public String showInteriorAttachment(){
		jsonObject = interiorWorkAttachmentService.queryByInteriorWorkSn(interiorWorkSn, page, rows);
		return LOGIN;
	}
	/**
	 * ɾ����ҳ���ϸ���
	 * @return
	 */
	public String deleteInteriorAttachment(){
		try{
			InteriorWorkAttachment interiorAttachment = interiorWorkAttachmentService.getById(id);
			interiorAttachment.setDeleted(true);
			interiorWorkAttachmentService.update(interiorAttachment);
			jsonObject.put("message", SUCCESS);
		}catch(Exception e){
			jsonObject.put("message", ERROR);
		}
		return LOGIN;
	}
	/**
	 * ������ҳ���ϸ���
	 * @return
	 */
	public String addInteriorAttachment(){
		
		try{
			InteriorWork interiorWork = interiorWorkService.getByInteriorWorkSn(interiorWorkSn);
			
			for( int i = 0; i < fileFileName.length; i++ ){
				this.uploadFileFunc(fileFileName[i], file[i], interiorWork);
			}
			jsonObject.put("message", SUCCESS);
		}catch(Exception e)
		{
			jsonObject.put("message", ERROR);
		}
		
		return LOGIN;
	}
	/**
	 * �ϴ������ļ�
	 * @param fileName
	 * @param interiorWork
	 */
	private void uploadFileFunc(String fileName, File file, InteriorWork interiorWork){
		InteriorWorkAttachment interiorAttachment = null;
		//��ȡ��׺
		String ext=FilenameUtils.getExtension( fileName );
		//�ж��ļ�����
		String attachmentType="";
		switch(ext.toLowerCase()){
			case "jpg":
			case "jpeg":
			case "png":
			case "gif":
			case "bmp":
			case "pcx":
			case "tiff":
			case "tga":
			case "exif":
			case "fpx":
			case "svg":
			case "psd":
			case "cdr":
			case "pcd":
			case "dxf":
			case "ufo":
			case "eps":
			case "ai":
			case "hdri":
			case "raw":			
				attachmentType="ͼƬ";
				break;
			case "mp4":
			case "avi":
			case "rmvb":
			case "rm":
			case "asf":
			case "divx":
			case "mpg":
			case "mpeg":
			case "mpe":
			case "wmv":
			case "vob":
				attachmentType="��Ƶ";
				break;
			case "doc":
			case "docx":
				attachmentType="�ĵ�";
				break;
			case "xls":
			case "xlsx":
				attachmentType="���";
				break;
			case "ppt":
			case "pptx":
				attachmentType="�õ�Ƭ";
				break;
			default: attachmentType="�����ļ���ʽ";
		}
		//������
		String newName="interiorwork/" + UUID.randomUUID().toString() + "." + ext;
		
		AwsS3Util.uploadFile(newName, file, session);
		
		interiorAttachment = new InteriorWorkAttachment();
		interiorAttachment.setAttachmentType(attachmentType);
		interiorAttachment.setDeleted(false);
		interiorAttachment.setLogicalFileName( fileName );
		interiorAttachment.setPhysicalFileName(newName);
		interiorAttachment.setInteriorWork(interiorWork);
		interiorWorkAttachmentService.add(interiorAttachment);
	}
	//��ʾ��ҵ����--�ϴ�
	public String showInterior() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
		response.setContentType("text/html");  
		response.setContentType("text/plain; charset=utf-8");
		PrintWriter out = response.getWriter();
		JSONArray jsonArray=new JSONArray();
		jsonList=new ArrayList<InteriorWork>();
		if(standardSn!=null && !"".equals(standardSn)){
			String pId=(String) session.get("personId");
			Person person=personService.getByPersonId(pId);
			if(person!=null){
				String departmentSn=person.getDepartment().getDepartmentSn();
				String hql="select i from InteriorWork i LEFT JOIN i.standardIndexes s"
						+ " where i.department.departmentSn like '"+departmentSn+"%'"
						+ " AND s.indexSn='"+standardIndexSn+"' ORDER BY i.id DESC";
				jsonList=interiorWorkService.findByPage(hql, page, rows);
				hql=hql.replaceFirst("i", "count(*)");
				total=interiorWorkService.getByHql(hql);
				for(InteriorWork interiorWork:jsonList){
					JSONObject jo=new JSONObject();
					jo.put("id", interiorWork.getId());
					jo.put("interiorWorkSn", interiorWork.getInteriorWorkSn());
					jo.put("interiorWorkNname", interiorWork.getInteriorWorkNname());
//					jo.put("attachmentPath", interiorWork.getAttachmentPath());
					if( interiorWork.getAttachments() != null ){
						int sum = 0;
						for( InteriorWorkAttachment interiorWorkAttachment :interiorWork.getAttachments() ){
							if( !interiorWorkAttachment.getDeleted() ){
								sum++;
							}
						}
						jo.put("attachmentPath", sum);
					}else{
						jo.put("attachmentPath", 0);
					}
					if(interiorWork.getUploader()!=null){
						jo.put("uploader", interiorWork.getUploader().getPersonName());
					}
					jo.put("uploadDatetime", interiorWork.getUploadDatetime());
					Set<StandardIndex> set=interiorWork.getStandardIndexes();
					if(set.size()>0){
						String str="";
						for(Iterator<StandardIndex> iter=set.iterator();iter.hasNext();){
							str=str+iter.next().getIndexName()+";<br />";
						}
						str=str.substring(0,str.length()-1);
						jo.put("standardIndexes", str);
					}
					if(interiorWork.getDepartment()!=null){
						jo.put("department", interiorWork.getDepartment().getDepartmentName());
						jo.put("implDepartmentName", interiorWork.getDepartment().getImplDepartmentName());
					}
					jsonArray.put(jo);
				}
			}
		}
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("total", total);// total�� ����ܼ�¼���������
		json.put("rows", jsonArray);// rows�� ���ÿҳ��¼ list
		out.print(JSONObject.valueToString(json));
		out.flush();
		out.close();
		return SUCCESS;
		
	}
	//��ѯ��ҵ����
	public String queryInterior() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
		response.setContentType("text/html");  
		response.setContentType("text/plain; charset=utf-8");
		PrintWriter out = response.getWriter();
		JSONArray jsonArray=new JSONArray();
		StringBuffer hqll=new StringBuffer();
		StringBuffer sqll=new StringBuffer();
		
		sqll.append("select count(distinct i.interior_work_sn) from interior_work i");
		
		if(standardSn!=null && !"".equals(standardSn))
		{
			hqll.append("select distinct i from InteriorWork i LEFT JOIN i.standardIndexes s"
					+ " where i.department.departmentSn like '"+departmentSn+"%'");
			
			sqll.append(" left join standard_index_interior_work a on i.interior_work_sn = a.interior_work_sn");
			
			if(standardIndexSn!=null && !"".equals(standardIndexSn))
			{
				sqll.append(" where a.index_sn='"+standardIndexSn+"'");
				hqll.append(" and s.indexSn='"+standardIndexSn+"'");
			}else{
				sqll.append(" left join standard_index s on a.index_sn = s.index_sn where s.standard_sn='"+standardSn+"'");
				hqll.append(" and s.standard.standardSn='"+standardSn+"'");
			}
			sqll.append(" and i.department_sn like '"+departmentSn+"%'");
			
		}else{
			sqll.append(" where i.department_sn='"+departmentSn+"'");
			hqll.append("select i from InteriorWork i where i.department.departmentSn like '"+departmentSn+"%'");
		}
		if(interiorName!=null && !"".equals(interiorName))
		{
			String sql=sqll.toString();
			sql = sql.replaceFirst("interior_work i", "interior_work i"
					+ " left join person p on i.person_id = p.person_id")+""
					+ " and (p.person_id like '%"+interiorName+"%' or p.person_name like '%"+interiorName+"%'"
						+ " or i.interior_work_name like '%"+interiorName+"%')";
			//sqll.append(" and (i.interior_work_name like '%"+interiorName+"%'");
			hqll.append(" and (i.interiorWorkNname like '%"+interiorName+"%'"
					+ " or i.uploader.personName like '%"+interiorName+"%'"
					+ " or i.uploader.personId like '%"+interiorName+"%')");
			
			total = interiorWorkService.getBySql(sql);
			
		}else{

			total = interiorWorkService.getBySql(sqll.toString());
			
		}
		hqll.append(" ORDER BY i.id DESC");
		jsonList = interiorWorkService.findByPage(hqll.toString(), page, rows);
		//String hql=hqll.toString().replaceFirst("i", "count(*)");
		for(InteriorWork interiorWork:jsonList){
			JSONObject jo=new JSONObject();
			jo.put("id", interiorWork.getId());
			jo.put("interiorWorkSn", interiorWork.getInteriorWorkSn());
			jo.put("interiorWorkNname", interiorWork.getInteriorWorkNname());
//			jo.put("attachmentPath", interiorWork.getAttachmentPath());
			if( interiorWork.getAttachments() != null ){
				int sum = 0;
				for( InteriorWorkAttachment interiorWorkAttachment :interiorWork.getAttachments() ){
					if( !interiorWorkAttachment.getDeleted() ){
						sum++;
					}
				}
				jo.put("attachmentPath", sum);
			}else{
				jo.put("attachmentPath", 0);
			}
			if(interiorWork.getUploader()!=null){
				jo.put("uploader", interiorWork.getUploader().getPersonName());
			}
			jo.put("uploadDatetime", interiorWork.getUploadDatetime());
			Set<StandardIndex> set=interiorWork.getStandardIndexes();
			if(set.size()>0){
				String str="";
				for(Iterator<StandardIndex> iter=set.iterator();iter.hasNext();){
					str=str+iter.next().getIndexName()+";<br />";
				}
				str=str.substring(0,str.length()-1);
				jo.put("standardIndexes", str);
			}
			if(interiorWork.getDepartment()!=null){
				jo.put("department", interiorWork.getDepartment().getDepartmentName());
				jo.put("implDepartmentName", interiorWork.getDepartment().getImplDepartmentName());
			}
			jsonArray.put(jo);
		}
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("total", total);// total�� ����ܼ�¼���������
		json.put("rows", jsonArray);// rows�� ���ÿҳ��¼ list
		out.print(JSONObject.valueToString(json));
		out.flush();
		out.close();
		return SUCCESS;
	}
	//������ҵ��Ż�ȡָ��ͱ�׼
	public String showStandard() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
		response.setContentType("text/html");  
		response.setContentType("text/plain; charset=utf-8");
		PrintWriter out = response.getWriter();
		JSONArray jsonArray=new JSONArray();
		InteriorWork list=interiorWorkService.getByInteriorWorkSn(interiorWorkSn);
		Set<StandardIndex> standardIndexes=list.getStandardIndexes();
		if(standardIndexes.size()>0){
			for(StandardIndex standardIndex:standardIndexes){
				JSONObject jo=new JSONObject();
				jo.put("standardName", standardIndex.getStandard().getStandardName());
				jo.put("indexName", standardIndex.getIndexName());
				jo.put("id", standardIndex.getId());
				jsonArray.put(jo);
			}
		}
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("total", standardIndexes.size());// total�� ����ܼ�¼���������
		json.put("rows", jsonArray);// rows�� ���ÿҳ��¼ list
		String showList= JSONObject.valueToString(json);
		out.print(showList);
		out.flush();
		out.close();
		return SUCCESS;
	}
	//�ϴ���ҵ����
	public String add() throws Exception{
		String pId=(String) session.get("personId");
		Person person=personService.getByPersonId(pId);
		if(person!=null){
			InteriorWork interiorWork=new InteriorWork();
			//����person��ȡ����
			String departmentSn=person.getDepartment().getDepartmentSn();
			Department department=departmentService.getByDepartmentSn(departmentSn);
			interiorWork.setDepartment(department);
			//����ID��ȡָ��
			StandardIndex standardIndex=standardindexService.getByindexSn(standardIndexSn);
			interiorWork.getStandardIndexes().add(standardIndex);
			Person pe=personService.getByPersonId(person.getPersonId());
			interiorWork.setInteriorWorkSn(UUID.randomUUID().toString());
			interiorWork.setUploader(pe);
			interiorWork.setUploadDatetime(new Timestamp(System.currentTimeMillis()));//��ǰ����
			interiorWork.setInteriorWorkNname(interiorName);
//			String name = this.uploadFile(uploadFileName, upload, getSavePath());
//			if(name.equals("error")){
//				pag=ERROR;
//				return SUCCESS;
//			}
			interiorWork.setAttachmentPath("����");
			try{
				interiorWorkService.addInteriorWork(interiorWork);
				for( int i = 0; i < uploadFileName.length; i++ ){
					this.uploadFileFunc(uploadFileName[i], upload[i], interiorWork);
				}
				pag=SUCCESS;
			}catch(Exception e){
				pag=ERROR;
			}
		}
			return SUCCESS;
	}
	//ɾ����ҵ����
	public String deleteInteriorWork(){
		try{
			interiorWorkService.deletInteriorWork(id);
			//fileName=interiorName+attachmentPath.substring(attachmentPath.lastIndexOf("."), attachmentPath.length());
			File file=new File(attachmentPath);
			if(file.exists()){
				file.delete();
			}
			pag=SUCCESS;
		}catch(Exception e){
			pag=ERROR;
		}
		return SUCCESS;
	}
	//�����ҵ���ϵı�׼
	public String addIndex(){
		try{
			InteriorWork list=interiorWorkService.getByInteriorWorkSn(interiorWorkSn);
			StandardIndex standardIndex=standardindexService.getByindexSn(standardIndexSn);
			if(list.getStandardIndexes().contains(standardIndex)){
				pag=LOGIN;
				return SUCCESS;
			}
			list.getStandardIndexes().add(standardIndex);
			interiorWorkService.update(list);
			pag=SUCCESS;
		}catch(Exception e){
			pag=ERROR;
		}
		return SUCCESS;
	}
	//ɾ����ҵ���Ϲ����ı�׼
	public String deleteIndex(){
		try{
			InteriorWork list=interiorWorkService.getByInteriorWorkSn(interiorWorkSn);
			if(list.getStandardIndexes().size()==1){
				pag=LOGIN;
				return SUCCESS;
			}else{
				StandardIndex standardIndex=standardindexService.getById(id);
				list.getStandardIndexes().remove(standardIndex);
				interiorWorkService.update(list);
				pag=SUCCESS;
			}
		}catch(Exception e){
			pag=ERROR;
		}
		return SUCCESS;
	}
	//������ҵ���ϸ���
	public String InteriorWorkFile() throws IOException{
		fileName=interiorName+attachmentPath.substring(attachmentPath.lastIndexOf("."), attachmentPath.length());
		//File file=new File(attachmentPath);
		fileName=URLEncoder.encode(fileName,"UTF-8");
		excelStream=AwsS3Util.downloadFile(attachmentPath);
		//excelStream=FileUtils.openInputStream(file);
		return SUCCESS;
	}
	//��ѯ�����Ƿ����
	public String queryInteriorWorkFile(){
		pag=ERROR;
		//File file=new File(attachmentPath);
		if(AwsS3Util.exists(attachmentPath)){
			pag=SUCCESS;
		}
		return SUCCESS;
	}


}

