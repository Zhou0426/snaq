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
 * <br/>网站: <a href="http://www.crazyit.org">疯狂Java联盟</a>
 * <br/>Copyright (C), 2001-2016, Yeeku.H.Lee
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @author  Yeeku.H.Lee kongyeeku@163.com
 * @version  1.0
 */

public class S3UploadAction extends BaseAction
{
	// 封装文件标题请求参数的属性
	private String title;
	// 封装上传文件域的属性
	private File upload;
	// 封装上传文件类型的属性
	private String uploadContentType;
	// 封装上传文件名的属性
	private String uploadFileName;
	// 直接在struts.xml文件中配置的属性
	private String savePath;
	// 接受struts.xml文件配置值的方法
	public void setSavePath(String value)
	{
		this.savePath = value;
	}
	// 获取上传文件的保存位置
	private String getSavePath() throws Exception
	{
		return ServletActionContext.getServletContext()
			.getRealPath(savePath);
	}

	// title的setter和getter方法
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getTitle()
	{
		return (this.title);
	}

	// upload的setter和getter方法
	public void setUpload(File upload)
	{
		this.upload = upload;
	}
	public File getUpload()
	{
		return (this.upload);
	}

	// uploadContentType的setter和getter方法
	public void setUploadContentType(String uploadContentType)
	{
		this.uploadContentType = uploadContentType;
	}
	public String getUploadContentType()
	{
		return (this.uploadContentType);
	}

	// uploadFileName的setter和getter方法
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
        //上传文件 :含进度信息,进度信息存储在session的progressValue中
        if(AwsS3Util.uploadFile("wz-uuid.扩展名", getUpload(),session)){
        	return SUCCESS;
        }else{
        	return INPUT;
        }
        //上传文件 :不含进度信息
//        if(S3UploadUtil.uploadFile("test/"+getUploadFileName(), getUpload())){
//        	return SUCCESS;
//        }else{
//        	return INPUT;
//        }
	}
}
