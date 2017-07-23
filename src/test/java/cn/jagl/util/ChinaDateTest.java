package cn.jagl.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
public class ChinaDateTest {
	@Before
	public void beforeTest(){
		
	}
	
	@Test
	public void testToday() {
		System.out.println(ChinaDate.today());
		
//		fail("Not yet implemented");
	}
	@Test
	public void testAmazonS3List() {
		BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIAOHQAJAK6EZUBPNMQ", "AKESZBkC596BWIFxH8BjBY8+Dsq+bfJg/v/1dyBo");
		System.out.format("Objects in S3 bucket %s:\n", "snaq");
        final AmazonS3 s3 =  AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion("cn-north-1")
                .build();
        ObjectListing ol = s3.listObjects("snaq");
        List<S3ObjectSummary> objects = ol.getObjectSummaries();
        for (S3ObjectSummary os: objects)
        {
            System.out.println("* " + os.getKey());
        }
	}
	@After
	public void afterTest(){
		
	}
	
}
