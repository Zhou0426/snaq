package cn.jagl.aq.action;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import cn.jagl.util.AwsS3Util;

import org.apache.struts2.ServletActionContext;

import java.io.File;
import java.util.List;
import java.io.*;

/**
 * Description:
 * <br/>��վ: <a href="http://www.crazyit.org">���Java����</a>
 * <br/>Copyright (C), 2001-2016, Yeeku.H.Lee
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @author  Yeeku.H.Lee kongyeeku@163.com
 * @version  1.0
 */

public class S3UploadAction extends BaseAction
{
	// ��װ�ļ������������������
	private String title;
	// ��װ�ϴ��ļ��������
	private File upload;
	// ��װ�ϴ��ļ����͵�����
	private String uploadContentType;
	// ��װ�ϴ��ļ���������
	private String uploadFileName;
	// ֱ����struts.xml�ļ������õ�����
	private String savePath;
	// ����struts.xml�ļ�����ֵ�ķ���
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

	// title��setter��getter����
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getTitle()
	{
		return (this.title);
	}

	// upload��setter��getter����
	public void setUpload(File upload)
	{
		this.upload = upload;
	}
	public File getUpload()
	{
		return (this.upload);
	}

	// uploadContentType��setter��getter����
	public void setUploadContentType(String uploadContentType)
	{
		this.uploadContentType = uploadContentType;
	}
	public String getUploadContentType()
	{
		return (this.uploadContentType);
	}

	// uploadFileName��setter��getter����
	public void setUploadFileName(String uploadFileName)
	{
		this.uploadFileName = uploadFileName;
	}
	public String getUploadFileName()
	{
		return (this.uploadFileName);
	}

	@Override
	public String execute() throws Exception
	{
        //�ϴ��ļ� :��������Ϣ,������Ϣ�洢��session��progressValue��
        if(AwsS3Util.uploadFile("wz-uuid.��չ��", getUpload(),session)){
        	return SUCCESS;
        }else{
        	return INPUT;
        }
        //�ϴ��ļ� :����������Ϣ
//        if(S3UploadUtil.uploadFile("test/"+getUploadFileName(), getUpload())){
//        	return SUCCESS;
//        }else{
//        	return INPUT;
//        }
	}
}
