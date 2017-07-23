package cn.jagl.aq.action;

import java.io.InputStream;

import com.opensymphony.xwork2.ActionSupport;

import cn.jagl.util.AwsS3Util;

public class S3DownloadAction extends  ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private String key;
	private String fileName;
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}  
	//����һ������������Ϊһ���ͻ�����˵��һ���������������ڷ���������һ�� �����  
    public InputStream getDownloadFile() throws Exception  
    { 
    	setFileName("Test.java");
    	return AwsS3Util.downloadFile(key);
    }  
      
    @Override  
    public String execute() throws Exception { 
        return SUCCESS;  
    }

}
