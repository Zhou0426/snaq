package cn.jagl.aq.action;

import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.apache.struts2.ServletActionContext;

import cn.jagl.aq.domain.SystemAuditAttachment;
import cn.jagl.util.AwsS3Util;
import cn.jagl.util.FileModel;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author ���
 * @since JDK1.8
 * @history 2017��4��18������6:32:02 ��� �½�
 */
public class SystemAuditAttachmentAction extends BaseAction<SystemAuditAttachment> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6610049537606769426L;
	
	private JSONObject jsonObject=new JSONObject();
	private JSONArray jsonArray=new JSONArray();
	@SuppressWarnings("unused")
	private String savePath;//�ļ��ϴ�·��
	protected FileModel fileModel;
	private String auditSn;
	private int id;
	
	

	//ͨ����ϵ��˱�Ż�ȡ��ظ�����Ϣ
	public String query(){
		jsonArray=systemAuditAttachmentService.query(auditSn);
		return "jsonArray";
	}

	//�����ϴ�
	public void attachmentUpload(){
		SystemAuditAttachment systemAuditAttachment=new SystemAuditAttachment();
		//String filePath= getSavePath();
		String fileName=getFileModel().getFilename();
		//��ȡ��׺
		String ext=FilenameUtils.getExtension(fileName);
		//�ж��ļ�����
		//BMP��JPG��JPEG��PNG��GIF
		String attachmentType="";
		switch(ext.toLowerCase()){
		case "doc":
		case "docx":	
			attachmentType="Word�ĵ�";
			break;
		case "xls":
		case "xlsx":	
			attachmentType="Excel�ĵ�";
			break;
		case "pdf":
			attachmentType="pdf�ĵ�";
			break;
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
		
		default: attachmentType="�����ļ���ʽ";
		}
		//������
		String newName="unsafecondition/"+UUID.randomUUID().toString()+"."+ext;
		//FileUtil.copyFile(getFileModel().getFile(), new File(newName));
		AwsS3Util.uploadFile(newName, getFileModel().getFile(), session);
		systemAuditAttachment.setAttachmentType(attachmentType);
		systemAuditAttachment.setLogicalFileName(fileName);
		systemAuditAttachment.setPhysicalFileName(newName);
		systemAuditAttachment.setDeleted(false);
		systemAuditAttachment.setSystemAudit(systemAuditService.getBySn(auditSn));
		systemAuditAttachmentService.save(systemAuditAttachment);
	}
	
	public String delete(){   
		jsonObject=systemAuditAttachmentService.delete(id);
		return "jsonObject";
	}
	public String getAuditSn() {
		return auditSn;
	}
	public void setAuditSn(String auditSn) {
		this.auditSn = auditSn;
	}
	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public JSONArray getJsonArray() {
		return jsonArray;
	}

	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}
	public String getSavePath() {
		return ServletActionContext.getServletContext()
				.getRealPath("/uploadFiles/");
	}
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
	
	public FileModel getFileModel() {
		return fileModel;
	}
	public void setFileModel(FileModel fileModel) {
		this.fileModel = fileModel;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
