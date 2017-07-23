package cn.jagl.util;

import java.io.File;
import java.util.UUID;
import cn.jagl.util.FileModel;
import org.apache.commons.io.FilenameUtils;
import org.aspectj.util.FileUtil;
import org.springframework.stereotype.Component;

@Component("fileUpload")
public class FileUploadUtil implements FileUpload {
	private String filePath="G:/";
	//1.获取文件扩展名
	private String getFileExt(String fileName){
		return FilenameUtils.getExtension(fileName);
	}
	//2.生成UUID随机数最为新的文件名
	private String newFileName(String fileName){
		String ext=getFileExt(fileName);
		return UUID.randomUUID().toString()+"."+ext;
	}
	//3.实现文件上传功能，返回上传后新的文件名称 
	/* (non-Javadoc)
	 * @see cn.jagl.util.FileUpload#uploadFile(cn.jagl.aq.domain.FileModel)
	 */
	@Override
	public String uploadFile(FileModel fileModel){
		//获取新的唯一文件名
		String newName=newFileName(fileModel.getFilename());
		try {
			FileUtil.copyFile(fileModel.getFile(), new File(filePath,newName));
			return newName;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
			fileModel.getFile().delete();
		}
	}
}
