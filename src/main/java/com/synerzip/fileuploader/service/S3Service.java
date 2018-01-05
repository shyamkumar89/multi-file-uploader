package com.synerzip.fileuploader.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class S3Service {

	private static String TEMP_UPLOAD_FOLDER = "D://";
	
	@Autowired
	private AmazonS3 s3client;

	@Value("${aws.bucket}")
	private String bucketName;

	public List<String> downloadFiles(List<String> fileNames) throws IOException {
		Iterator<String> iterator = fileNames.iterator();
		List<String> tempFilePath = new ArrayList<String>();
		while (iterator.hasNext()) {
			String fileName = iterator.next();
			downloadFile(fileName);
			tempFilePath.add(TEMP_UPLOAD_FOLDER+fileName);
		}
		return tempFilePath;
	}

	public void downloadFile(String fileName) throws IOException {
		try {
			System.out.println("Downloading an object");
			S3Object s3object = s3client.getObject(new GetObjectRequest(bucketName, fileName));
			System.out.println("Content-Type: " + s3object.getObjectMetadata().getContentType());
			InputStream inputStream = s3object.getObjectContent();
			FileOutputStream outputStream = new FileOutputStream(TEMP_UPLOAD_FOLDER+fileName);
			IOUtils.copy(inputStream, outputStream);
			log.info("===================== Import File - Done! =====================");
		} catch (AmazonServiceException ase) {
			log.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
			log.info("Error Message:    " + ase.getMessage());
			log.info("HTTP Status Code: " + ase.getStatusCode());
			log.info("AWS Error Code:   " + ase.getErrorCode());
			log.info("Error Type:       " + ase.getErrorType());
			log.info("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			log.info("Caught an AmazonClientException: ");
			log.info("Error Message: " + ace.getMessage());
		}
	}

	public List<String> uploadFiles(List<MultipartFile> files) throws IOException {
		Iterator<MultipartFile> iterator = files.iterator();
		List<String> fileNames = new ArrayList<String>();
		while (iterator.hasNext()) {
			MultipartFile file = iterator.next();
			if (!file.isEmpty()) {
				fileNames.add(uploadFile(file));
			}
		}
		return fileNames;
	}

	public String uploadFile(MultipartFile file) throws IOException {
		String fileName = null;
		try {
			InputStream in = file.getInputStream();
			ObjectMetadata metadata = new ObjectMetadata();
			fileName = file.getOriginalFilename();
			metadata.setContentLength(in.available());
			s3client.putObject(new PutObjectRequest(bucketName, fileName, in, metadata));
			log.info("===================== Upload File - Done! =====================");
		} catch (AmazonServiceException ase) {
			log.info("Caught an AmazonServiceException from PUT requests, rejected reasons:");
			log.info("Error Message:    " + ase.getMessage());
			log.info("HTTP Status Code: " + ase.getStatusCode());
			log.info("AWS Error Code:   " + ase.getErrorCode());
			log.info("Error Type:       " + ase.getErrorType());
			log.info("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			log.info("Caught an AmazonClientException: ");
			log.info("Error Message: " + ace.getMessage());
		}
		return fileName;
	}

}
