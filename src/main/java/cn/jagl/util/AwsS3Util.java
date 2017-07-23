package cn.jagl.util;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferProgress;
import com.amazonaws.services.s3.transfer.Upload;

public class AwsS3Util {
	private static String bucketName="snaq";
	private static String accessKey="AKIAOHQAJAK6EZUBPNMQ";
	private static String secretKey="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";
	private static String region="cn-north-1";
	public static final  AmazonS3 s3 =  AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
            .withRegion(region)
            .build();
	/**
	 * �ϴ��ļ�
	 * @param key ��ֵ
	 * @param file Ҫ�ϴ����ļ�
	 * @return
	 */
	public static Boolean uploadFile(String key, File file){
		try{
			s3.putObject(new PutObjectRequest("snaq", key, file));			
		}catch(Exception e){
			
		}finally{
			return exists(key);
		}
	}
	/**
	 * �ϴ��ļ������ȴ洢��session��progressValue��
	 * @param key ��ֵ
	 * @param file Ҫ�ϴ����ļ�
	 * @param session Map<String, Object>����
	 */
	public static Boolean uploadFile(String key, File file,Map<String, Object> session){
		Boolean complete=true;
		TransferManager tm = new TransferManager(s3);
	    Upload upload = tm.upload("snaq",key,file);
	    TransferProgress p = upload.getProgress();
	    while (upload.isDone() == false){
	        int percent =  (int)(p.getPercentTransferred());
	        session.put("progressValue",percent);
	        session.put("progressMessage","���ϴ���"+p.getBytesTransferred()+"/"+p.getTotalBytesToTransfer());	       
	        // Do work while we wait for our upload to complete...
	        try {
	            Thread.sleep(500);
	        }catch (Exception e){
	        	
	        }
	    }
	    try{
	        upload.waitForCompletion();
	    }catch (Exception e){
	        System.out.println(e.getMessage());
	        complete=false;
	    }finally {
	        tm.shutdownNow(false);//���ر�S3
	    }
	    session.put("progressMessage","���ϴ���"+p.getBytesTransferred()+"/"+p.getTotalBytesToTransfer());
	    session.put("progressValue",0);
	    return complete;
	}
	/**
	 * �ж�S3Object�Ƿ����
	 * @param key ��ֵ
	 * @return S3���Ƿ����
	 */
	public static Boolean exists(String key){	
		try{
	        s3.getObjectMetadata(bucketName, key);
	        return true;
	    }catch(AmazonS3Exception s3Exception){
	        return false;
	    }catch(Exception e){
	    	return false;
	    }
	}
	/**
	 * ��S3�����ļ��������ļ���
	 * @param key ��ֵ
	 * @return
	 */
	public static InputStream downloadFile(String key){
		try{
	        S3Object s3Object= s3.getObject(bucketName, key);
	        return s3Object.getObjectContent();
	    }catch(AmazonS3Exception s3Exception){
	        return null;
	    }catch(Exception e){
	    	return null;
	    }
	}
}
