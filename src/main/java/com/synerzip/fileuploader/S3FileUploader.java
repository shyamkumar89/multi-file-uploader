package com.synerzip.fileuploader;

import java.io.File;
import java.io.IOException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class S3FileUploader {
	private static String bucketName = "abmirror-dev";
	private static String keyName = "bodies2/image11.jpg";
	private static String uploadFileName = "C:\\Users\\shyamkumarm\\Desktop\\image1.jpg";

	public static void main(String[] args) throws IOException {
		BasicAWSCredentials credentials = new BasicAWSCredentials("AKIAJNVVGIX5CJ4K2K3Q",
				"pPk5U791ZfeFSl6GMCLpacFDBmGKqXqz5xaBK4UH");
		AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTH_1)
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
		try {
			System.out.println("Uploading a new object to S3 from a file\n");
			File file = new File(uploadFileName);
			s3client.putObject(new PutObjectRequest(bucketName, keyName, file).withCannedAcl(CannedAccessControlList.PublicRead));
			String url = s3client.getObject(new GetObjectRequest(bucketName, keyName)).getObjectContent().getHttpRequest().getURI().toString();
			System.out.println(url);
		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which " + "means your request made it "
					+ "to Amazon S3, but was rejected with an error response" + " for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which " + "means the client encountered "
					+ "an internal error while trying to " + "communicate with S3, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
	}
}