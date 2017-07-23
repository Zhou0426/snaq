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
 * @author 马辉
 * @since JDK1.8
 * @history 2017年4月18日下午6:32:02 马辉 新建
 */
public class SystemAuditAttachmentAction extends BaseAction<SystemAuditAttachment> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6610049537606769426L;
	
	private JSONObject jsonObject=new JSONObject();
	private JSONArray jsonArray=new JSONArray();
	@SuppressWarnings("unused")
	private String savePath;//文件上传路径
	protected FileModel fileModel;
	private String auditSn;
	private int id;
	
	

	//通过体系审核编号获取相关附件信息
	public String query(){
		jsonArray=systemAuditAttachmentService.query(auditSn);
		return "jsonArray";
	}

	//附件上传
	public void attachmentUpload(){
		SystemAuditAttachment systemAuditAttachment=new SystemAuditAttachment();
		//String filePath= getSavePath();
		String fileName=getFileModel().getFilename();
		//获取后缀
		String ext=FilenameUtils.getExtension(fileName);
		//判断文件类型
		//BMP、JPG、JPEG、PNG、GIF
		String attachmentType="";
		switch(ext.toLowerCase()){
		case "doc":
		case "docx":	
			attachmentType="Word文档";
			break;
		case "xls":
		case "xlsx":	
			attachmentType="Excel文档";
			break;
		case "pdf":
			attachmentType="pdf文档";
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
			attachmentType="图片";
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
			attachmentType="视频";
			break;
		
		default: attachmentType="其它文件格式";
		}
		//新名字
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
