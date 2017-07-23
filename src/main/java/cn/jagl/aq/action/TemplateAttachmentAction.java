package cn.jagl.aq.action;


import java.util.UUID;
import org.apache.commons.io.FilenameUtils;
import cn.jagl.aq.domain.TemplateAttachment;
import cn.jagl.util.FileModel;
import cn.jagl.util.FileUpload;
import net.sf.json.JSONObject;
import cn.jagl.util.AwsS3Util;
/**
 * @author mahui
 * @method 
 * @date 2016年8月12日下午3:23:06
 */
public class TemplateAttachmentAction extends BaseAction<TemplateAttachment> {

	private static final long serialVersionUID = 1L;
	protected String documentTemplateSn;
	protected FileUpload fileUpload;
	protected FileModel fileModel;
	private String fileName;
	private String path;
	private JSONObject jsonObject=new JSONObject();
	public JSONObject getJsonObject() {
		return jsonObject;
	}
	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public FileUpload getFileUpload() {
		return fileUpload;
	}
	public void setFileUpload(FileUpload fileUpload) {
		this.fileUpload = fileUpload;
	}
	public FileModel getFileModel() {
		return fileModel;
	}
	public void setFileModel(FileModel fileModel) {
		this.fileModel = fileModel;
	}
	public String getDocumentTemplateSn() {
		return documentTemplateSn;
	}
	public void setDocumentTemplateSn(String documentTemplateSn) {
		this.documentTemplateSn = documentTemplateSn;
	}
	//模板详情
	public String queryJoinAttachment(){
		jsonList=templateAttachmentService.queryJoinDocumentTemplate(documentTemplateSn);
		return "jsonList";
	}
	//删除ByIds
	public void deleteById(){	
		templateAttachmentService.deleteByIds(ids);
		/*File file=new File(path);  
	    if(file.exists()){  
	        file.delete();  
	    }*/
	}
	//上传模板
	public String attachmentUpload() throws Exception{
		jsonObject.put("status", "ok");
		TemplateAttachment templateAttachment=new TemplateAttachment();	
		String fileName=getFileModel().getFilename();
		//获取后缀
		String ext=FilenameUtils.getExtension(fileName);
		//新物理名
		String newName="template/"+UUID.randomUUID().toString()+"."+ext;
		//FileUtil.copyFile(getFileModel().getFile(), new File(newName));
		if(AwsS3Util.uploadFile(newName, getFileModel().getFile(),session)){
			templateAttachment.setAttachmentLogicFileName(fileName);
			templateAttachment.setAttachmentPhysicalFileName(newName);
			templateAttachment.setDocumentTemplate(documentTemplateService.getByDocumentTemplateSn(documentTemplateSn));
			templateAttachmentService.addTemplateAttachment(templateAttachment);
	        return "jsonObject";
	    }else{
	    	jsonObject.put("status", "nook");
	    	return "jsonObject";
	    }
	}
}
