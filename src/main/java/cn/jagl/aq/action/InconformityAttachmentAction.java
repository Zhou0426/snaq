package cn.jagl.aq.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.apache.struts2.ServletActionContext;
import org.aspectj.util.FileUtil;

import cn.jagl.aq.domain.InconformityAttachment;
import cn.jagl.aq.domain.UnsafeAct;
import cn.jagl.aq.domain.UnsafeCondition;
import cn.jagl.aq.service.InconformityAttachmentService;
import cn.jagl.aq.service.UnsafeConditionService;
import cn.jagl.util.AwsS3Util;
import cn.jagl.util.FileModel;

/**
 * @author mahui
 *
 * @date 2016��7��8������3:06:37
 */
public class InconformityAttachmentAction extends BaseAction<InconformityAttachment> {
	private static final long serialVersionUID = 1L;
	protected FileModel fileModel;
	private String inconformityItemSn;
	private String savePath;//�ļ��ϴ�·��
	
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
	public String getInconformityItemSn() {
		return inconformityItemSn;
	}
	public void setInconformityItemSn(String inconformityItemSn) {
		this.inconformityItemSn = inconformityItemSn;
	}
	//�����ϴ�
	public void attachmentUpload(){
		InconformityAttachment inconformityAttachment=new InconformityAttachment();
		//String filePath= getSavePath();
		String fileName=getFileModel().getFilename();
		//��ȡ��׺
		String ext=FilenameUtils.getExtension(fileName);
		//�ж��ļ�����
		//BMP��JPG��JPEG��PNG��GIF
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
		default: attachmentType="�����ļ���ʽ";
		}
		//������
		String newName="unsafecondition/"+UUID.randomUUID().toString()+"."+ext;
		//FileUtil.copyFile(getFileModel().getFile(), new File(newName));
		AwsS3Util.uploadFile(newName, getFileModel().getFile(), session);
		inconformityAttachment.setAttachmentType(attachmentType);
		inconformityAttachment.setLogicalFileName(fileName);
		inconformityAttachment.setPhysicalFileName(newName);
		inconformityAttachment.setDeleted(false);
		inconformityAttachment.setInconformityItem(unsafeConditionService.getByInconformityItemSn(inconformityItemSn));
		inconformityAttachmentService.save(inconformityAttachment);
	}
	//�����ϴ�
		public void attachmentUpload_unsafeAct(){
			InconformityAttachment inconformityAttachment=new InconformityAttachment();
			//String filePath= getSavePath();
			String fileName=getFileModel().getFilename();
			//��ȡ��׺
			String ext=FilenameUtils.getExtension(fileName);
			//�ж��ļ�����
			//BMP��JPG��JPEG��PNG��GIF
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
			default: attachmentType="�����ļ���ʽ";
			}
			//������
			String newName ="unsafeact/" + UUID.randomUUID().toString() + "." + ext;
			AwsS3Util.uploadFile(newName, getFileModel().getFile(), session);
			//FileUtil.copyFile(getFileModel().getFile(), new File(newName));
			inconformityAttachment.setAttachmentType(attachmentType);
			inconformityAttachment.setLogicalFileName(fileName);
			inconformityAttachment.setPhysicalFileName(newName);
			inconformityAttachment.setDeleted(false);
			inconformityAttachment.setInconformityItem(unsafeActService.getBySn(inconformityItemSn));
			inconformityAttachmentService.save(inconformityAttachment);
		}
	//ͨ�����������Ż�ȡ��ظ�����Ϣ
	public String queryJoinInconformityItem(){
		UnsafeCondition unsafeCondition=unsafeConditionService.getByInconformityItemSn(inconformityItemSn);
		jsonList=new ArrayList<InconformityAttachment>(unsafeCondition.getAttachments());
		return "jsonList";
	}
	public String queryJoinUnsafeAct(){
		UnsafeAct unsafeAct=unsafeActService.getBySn(inconformityItemSn);
		jsonList=new ArrayList<InconformityAttachment>(unsafeAct.getAttachments());
		return "jsonList";
	}
	//ɾ��
	public void deleteByIds(){
		String fileName=inconformityAttachmentService.getById(Integer.parseInt(ids)).getPhysicalFileName();
//		File file=new File(fileName);  
//		    if(file.exists()){  
//		        file.delete();  
//		    }     
		inconformityAttachmentService.deleteByIds(ids);
	}
}
