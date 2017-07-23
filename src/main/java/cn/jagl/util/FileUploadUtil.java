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
	//1.��ȡ�ļ���չ��
	private String getFileExt(String fileName){
		return FilenameUtils.getExtension(fileName);
	}
	//2.����UUID�������Ϊ�µ��ļ���
	private String newFileName(String fileName){
		String ext=getFileExt(fileName);
		return UUID.randomUUID().toString()+"."+ext;
	}
	//3.ʵ���ļ��ϴ����ܣ������ϴ����µ��ļ����� 
	/* (non-Javadoc)
	 * @see cn.jagl.util.FileUpload#uploadFile(cn.jagl.aq.domain.FileModel)
	 */
	@Override
	public String uploadFile(FileModel fileModel){
		//��ȡ�µ�Ψһ�ļ���
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
