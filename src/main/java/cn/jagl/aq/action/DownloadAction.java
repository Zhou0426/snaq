package cn.jagl.aq.action;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import cn.jagl.util.AwsS3Util;

/*
 * ���2016/7/3
 * ��ͨ�ļ�����(����xmlģ��)
 */
public class DownloadAction extends ActionSupport {
	//���غ������
	private String newName;
	//��ʵ�ļ���
	private String fileName;

	public String getNewName() {
		return newName;
	}
	public void setNewName(String newname) {
		this.newName = newname;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String execute() throws Exception{
		return SUCCESS;
	}
	public InputStream getInputStream() throws IOException{
		return AwsS3Util.downloadFile(fileName);
		//return FileUtils.openInputStream(file);
	}
	public String getDownloadFileName(){
		String downloadFilename="";
		try {
			downloadFilename=URLEncoder.encode(newName,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return downloadFilename.replace("+","%20");
	}
}
