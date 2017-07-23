package cn.jagl.aq.action;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;

import cn.jagl.aq.domain.CheckClass;
import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.LatentHazard;
import cn.jagl.aq.domain.LatentHazardAttachment;
import cn.jagl.aq.domain.Person;
import cn.jagl.util.AwsS3Util;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class LatentHazardAction extends BaseAction<LatentHazard> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;//��ʶ�У��Զ�����
	private String departmentSn;//���ű��
	private String pag;//�ж��ǵ�λ�Բ黹���ⲿ���
	private String latentHazardSn;//�ش�������ţ�����ʱ���Զ�����
	private CheckClass checkClass;//������
	private String checkUnit;//��鵥λ
	private String checkUnitName;//�ⲿ��鵥λ
	private String latentHazardDescription;//�ش���������
	private Timestamp happenDateTime;//����ʱ��
	private String happenLocation;//�����ص�
	private String status;//״̬��δ�ϱ������ϱ�������ˡ�������
	private boolean auditedStatus;//���״̬
	private String departmentReportTo;//ҵ����
	private String auditSuggestion;//������
	private JSONObject jsonObject = new JSONObject();
	private JSONArray jsonArray = new JSONArray();
	private File[] upload;//����
	private String[] uploadContentType;//// ��װ�ϴ��ļ�����
	private String[] uploadFileName;// ��װ�ϴ��ļ���
	
	/**
	 * ��ѯ�����ϱ���¼
	 * @return
	 */
	public String showData(){
		String personId = session.get("personId").toString();
		jsonObject = latentHazardService.showData(personId, page, rows);
		return SUCCESS;
	}
	/**
	 * ��ѯ��˼�¼
	 * @return
	 */
	public String showAuditData(){
		String personId = session.get("personId").toString();
		Person person = personService.getByPersonId(personId);
		jsonObject = latentHazardService.showAuditData(person.getDepartment().getDepartmentSn(), page, rows);
		return SUCCESS;
	}
	/**
	 * ��ѯ���ż�¼
	 * @return
	 */
	public String showCancelData(){
		String personId = session.get("personId").toString();
		Person person = personService.getByPersonId(personId);
		if( person != null && person.getDepartment() != null && person.getDepartment().getDepartmentSn().equals("1002") ){
			jsonObject = latentHazardService.showCancelData(page, rows);
		}else{
			jsonObject.put("total", 0);
			jsonObject.put("rows", new JSONArray());
		}
		return SUCCESS;
	}
	/**
	 * ��ѯ����
	 * @return
	 */
	public String queryDepartment(){
		List<Department> departments = new ArrayList<Department>();
		if( pag == null || pag.equals("��λ�Բ�") ){
			departmentSn = (String) session.get("departmentSn");
			if(departmentSn != null && departmentSn.trim().length() > 0){
				departments = departmentService.getDepartmentsByParentDepartmentSn(departmentSn);
			}
		}else if( pag.equals("���Ź�˾") ){
			String hql = "FROM Department d where d.departmentType.departmentTypeSn= 'JTCS' order by d.showSequence ASC";
    		departments=departmentService.findByPage(hql,1,10000);
		}
        for(Department department:departments){
        	JSONObject jo=new JSONObject();
        	jo.put("departmentSn",department.getDepartmentSn());
        	jo.put("departmentName",department.getDepartmentName());
        	jo.put("state","open");
        	jsonArray.add(jo);
        }
		return "jsonArray";
	}
	/**
	 * ����ش�����
	 * @return
	 */
	public String add(){
		try{
			String personId = (String) session.get("personId");
			Person person = personService.getByPersonId(personId);
			if( person != null ){
				LatentHazard latentHazard = new LatentHazard();
				latentHazard.setLatentHazardSn(String.valueOf( System.currentTimeMillis() ));
				latentHazard.setCheckClass(checkClass);
				latentHazard.setCheckUnitName(checkUnitName);
				Department department = departmentService.getByDepartmentSn(checkUnit);
				latentHazard.setCheckUnit(department);
				latentHazard.setLatentHazardDescription(latentHazardDescription);
				latentHazard.setHappenLocation(happenLocation);
				latentHazard.setHappenDateTime(happenDateTime.toLocalDateTime());
				latentHazard.setDepartmentReportTo(departmentService.getByDepartmentSn(departmentReportTo));
				if( status != null && status.equals("yes") ){
					latentHazard.setStatus("���ϱ�");
					latentHazard.setReportDateTime(LocalDateTime.now());
				}else{
					latentHazard.setStatus("δ�ϱ�");
				}
				latentHazard.setEditor(person);
				latentHazard.setDepartment(departmentService.getUpNearestImplDepartment(person.getDepartment().getDepartmentSn()));
				latentHazard.setDeleted(false);
				latentHazardService.add(latentHazard);
				//����
				if( upload != null ){
					for( int i = 0; i < upload.length; i++ ){
						LatentHazardAttachment latentHazardAttachment = new LatentHazardAttachment();
						latentHazardAttachment.setLatentHazard(latentHazard);
						latentHazardAttachment.setLogicalFileName(this.getFileName(uploadFileName[i]));
						latentHazardAttachment.setPhysicalFileName(this.uploadFile(uploadFileName[i], upload[i]));
						latentHazardAttachment.setUploadPerson(person);
						latentHazardAttachment.setUploadDateTime(new Timestamp(System.currentTimeMillis()));
						latentHazardAttachment.setDeleted(false);
						latentHazardAttachmentService.add(latentHazardAttachment);
					}
				}
				jsonObject.put("message", SUCCESS);
			}else{
				jsonObject.put("message", ERROR);
			}
		}catch(Exception e){
			jsonObject.put("message", ERROR);
		}
		return SUCCESS;
	}
	/**
	 * չʾ����
	 * @return
	 */
	public String showDataAttachment(){
		jsonObject = latentHazardAttachmentService.queryAttachmentByLatentHazardSn(latentHazardSn);
		return SUCCESS;
	}
	/**
	 * ����id�߼�ɾ������
	 * @return
	 */
	public String deleteById(){
		try{
			String personId = (String) session.get("personId");
			LatentHazardAttachment latentHazardAttachment = latentHazardAttachmentService.queryById(id);
			if( latentHazardAttachment.getUploadPerson().getPersonId().equals(personId) ){
				if( latentHazardAttachment.getLatentHazard().getStatus().equals("δ�ϱ�") ){
					latentHazardAttachmentService.deleteById(id);
					jsonObject.put("message", SUCCESS);
				}else{
					if( latentHazardAttachment.getLatentHazard().getStatus().equals("���ϱ�") ){
						jsonObject.put("status", "���ϱ�");
					}else if( latentHazardAttachment.getLatentHazard().getStatus().equals("�����") ){
						jsonObject.put("status", "�����");
					}else{
						jsonObject.put("status", "������");
					}
					jsonObject.put("message", INPUT);
				}
			}else{
				jsonObject.put("message", LOGIN);
			}
		}catch(Exception e){
			jsonObject.put("message", ERROR);
		}
		return SUCCESS;
	}
	/**
	 * �߼�ɾ���ش������ϱ���¼
	 * @return
	 */
	public String delete(){
		try{
			latentHazardService.deleteById(id);
			jsonObject.put("message", SUCCESS);
		}catch(Exception e){
			jsonObject.put("message", ERROR);
		}
		return SUCCESS;
	}
	/**
	 * �޸��ش�����
	 * @return
	 */
	public String update(){
		try{
			LatentHazard latentHazard = latentHazardService.queryById(id);
			if( latentHazard != null ){
				String personId = (String) session.get("personId");
				if( latentHazard.getEditor().getPersonId().equals(personId) ){
					latentHazard.setLatentHazardDescription(latentHazardDescription);
					latentHazard.setCheckClass(checkClass);
					latentHazard.setCheckUnitName(checkUnitName);
					Department department = departmentService.getByDepartmentSn(checkUnit);
					latentHazard.setCheckUnit(department);
					latentHazard.setHappenDateTime(happenDateTime.toLocalDateTime());
					latentHazard.setHappenLocation(happenLocation);
					latentHazard.setDepartmentReportTo(departmentService.getByDepartmentSn(departmentReportTo));
					if( status != null && status.equals("yes") ){
						latentHazard.setStatus("���ϱ�");
						latentHazard.setReportDateTime(LocalDateTime.now());
					}else{
						latentHazard.setStatus("δ�ϱ�");
					}
					latentHazard.setDeleted(false);
					latentHazardService.update(latentHazard);
					jsonObject.put("message", SUCCESS);
				}else{
					jsonObject.put("message", LOGIN);
				}
			}else{
				jsonObject.put("message", ERROR);
			}
		}catch(Exception e){
			jsonObject.put("message", ERROR);
		}
		return SUCCESS;
	}
	/**
	 * �ϱ��ش�����
	 * @return
	 */
	public String reportLatentHazard(){
		try{
			LatentHazard latentHazard = latentHazardService.queryById(id);
			if( latentHazard != null ){
				latentHazard.setStatus("���ϱ�");
				latentHazard.setReportDateTime(LocalDateTime.now());
				latentHazardService.update(latentHazard);
				jsonObject.put("message", SUCCESS);
			}else{
				jsonObject.put("message", ERROR);
			}
		}catch(Exception e){
			jsonObject.put("message", ERROR);
		}
		return SUCCESS;
	}
	/**
	 * �ش���������
	 * @return
	 */
	public String cancelLatentHazard(){
		try{
			LatentHazard latentHazard = latentHazardService.queryById(id);
			String personId = (String) session.get("personId");
			Person person = personService.getByPersonId(personId);
			if( latentHazard != null && person != null ){
				latentHazard.setStatus("������");
				latentHazard.setCancelDateTime(LocalDateTime.now());
				latentHazard.setCancelPerson(person);
				latentHazardService.update(latentHazard);
				jsonObject.put("message", SUCCESS);
			}else{
				jsonObject.put("message", ERROR);
			}
		}catch(Exception e){
			jsonObject.put("message", ERROR);
		}
		return SUCCESS;
	}
	/**
	 * ���Ӹ���
	 * @return
	 */
	public String addAttachment(){
		try{
			String personId = (String) session.get("personId");
			Person person = personService.getByPersonId(personId);
			LatentHazard latentHazard = latentHazardService.queryById(id);
			if( upload != null && upload.length > 0 && person != null && latentHazard != null ){
				for( int i = 0; i < upload.length; i++ ){
					LatentHazardAttachment latentHazardAttachment = new LatentHazardAttachment();
					latentHazardAttachment.setLatentHazard(latentHazard);
					latentHazardAttachment.setLogicalFileName(this.getFileName(uploadFileName[i]));
					latentHazardAttachment.setPhysicalFileName(this.uploadFile(uploadFileName[i], upload[i]));
					latentHazardAttachment.setUploadPerson(person);
					latentHazardAttachment.setUploadDateTime(new Timestamp(System.currentTimeMillis()));
					latentHazardAttachment.setDeleted(false);
					latentHazardAttachmentService.add(latentHazardAttachment);
				}
				jsonObject.put("message", SUCCESS);
			}else{
				jsonObject.put("message", ERROR);
			}
		}catch(Exception e){
			jsonObject.put("message", ERROR);
		}
		return SUCCESS;
	}
	/**
	 * ���
	 * @return
	 */
	public String audit(){
		try{
			String personId = (String) session.get("personId");
			Person person = personService.getByPersonId(personId);
			LatentHazard latentHazard = latentHazardService.queryById(id);
			if( upload != null && upload.length > 0 && person != null && latentHazard != null
					&& latentHazard.getDepartmentReportTo().getPrincipal().getPersonId().equals(personId) ){
				for( int i = 0; i < upload.length; i++ ){
					LatentHazardAttachment latentHazardAttachment = new LatentHazardAttachment();
					latentHazardAttachment.setLatentHazard(latentHazard);
					latentHazardAttachment.setLogicalFileName(this.getFileName(uploadFileName[i]));
					latentHazardAttachment.setPhysicalFileName(this.uploadFile(uploadFileName[i], upload[i]));
					latentHazardAttachment.setUploadPerson(person);
					latentHazardAttachment.setUploadDateTime(new Timestamp(System.currentTimeMillis()));
					latentHazardAttachment.setDeleted(false);
					latentHazardAttachmentService.add(latentHazardAttachment);
				}
				latentHazard.setAuditSuggestion(auditSuggestion);
				if( auditedStatus ){
					latentHazard.setStatus("�����");
					latentHazard.setAuditDateTime(LocalDateTime.now());
					latentHazard.setAuditor(person);
				}
				latentHazardService.update(latentHazard);
				jsonObject.put("message", SUCCESS);
			}else{
				jsonObject.put("message", ERROR);
			}
		}catch(Exception e){
			jsonObject.put("message", ERROR);
		}
		return SUCCESS;
	}
	
	//ͨ���ļ�����ȡ��չ��
	private String getFileExt(String filename){
		return FilenameUtils.getExtension(filename);
	}
	//ͨ���ļ�����ȡû����չ�����ļ���
	private String getFileName(String filename){
		return filename.replace("." + getFileExt(filename), "");
	}
	//����UUID���������Ϊ�µ��ļ���
	private String newFileName(String filename){
		String ext = getFileExt(filename);
		return UUID.randomUUID().toString()+"."+ext;
	}
	//ʵ���ļ��ϴ����ܣ������ϴ�����µ��ļ�����
	public String uploadFile(String filename,File file){
		//��ȡ�µ�Ψһ�ļ���
		String newName = newFileName(filename);
		try {
			if(!AwsS3Util.uploadFile("latentHazard/" + newName, file, session)){
				return "error";
			}
			return "latentHazard/"+newName;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
			file.delete();
		}
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLatentHazardSn() {
		return latentHazardSn;
	}
	public void setLatentHazardSn(String latentHazardSn) {
		this.latentHazardSn = latentHazardSn;
	}
	public String getLatentHazardDescription() {
		return latentHazardDescription;
	}
	public void setLatentHazardDescription(String latentHazardDescription) {
		this.latentHazardDescription = latentHazardDescription;
	}
	public JSONObject getJsonObject() {
		return jsonObject;
	}
	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}
	public Timestamp getHappenDateTime() {
		return happenDateTime;
	}
	public void setHappenDateTime(Timestamp happenDateTime) {
		this.happenDateTime = happenDateTime;
	}
	public String getHappenLocation() {
		return happenLocation;
	}
	public void setHappenLocation(String happenLocation) {
		this.happenLocation = happenLocation;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getDepartmentReportTo() {
		return departmentReportTo;
	}
	public void setDepartmentReportTo(String departmentReportTo) {
		this.departmentReportTo = departmentReportTo;
	}
	public String getAuditSuggestion() {
		return auditSuggestion;
	}
	public void setAuditSuggestion(String auditSuggestion) {
		this.auditSuggestion = auditSuggestion;
	}
	public boolean isAuditedStatus() {
		return auditedStatus;
	}
	public void setAuditedStatus(boolean auditedStatus) {
		this.auditedStatus = auditedStatus;
	}
	public CheckClass getCheckClass() {
		return checkClass;
	}
	public void setCheckClass(CheckClass checkClass) {
		this.checkClass = checkClass;
	}
	public String getCheckUnit() {
		return checkUnit;
	}
	public void setCheckUnit(String checkUnit) {
		this.checkUnit = checkUnit;
	}
	public String getCheckUnitName() {
		return checkUnitName;
	}
	public void setCheckUnitName(String checkUnitName) {
		this.checkUnitName = checkUnitName;
	}
	public String getDepartmentSn() {
		return departmentSn;
	}
	public void setDepartmentSn(String departmentSn) {
		this.departmentSn = departmentSn;
	}
	public JSONArray getJsonArray() {
		return jsonArray;
	}
	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}
	public String getPag() {
		return pag;
	}
	public void setPag(String pag) {
		this.pag = pag;
	}
	
	
	
	
	
	
	
}
