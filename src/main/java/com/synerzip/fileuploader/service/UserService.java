package com.synerzip.fileuploader.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synerzip.fileuploader.domain.User;
import com.synerzip.fileuploader.respository.UserRepository;

@Service
public class UserService {

	@Autowired
	private S3Service s3Service;

	@Autowired
	private UserRepository userRepository;

	public User createUser(User user) throws IOException {
		List<String> fileNames = s3Service.uploadFiles(user.getFiles());
		user.setFileNames(fileNames);
		userRepository.createUser(user);
		return user;
	}

	public User getUser() throws IOException {
		User user = userRepository.getUser();
		List<String> tempFilePath = s3Service.downloadFiles(user.getFileNames());
		user.setFileNames(tempFilePath);
		return user;
	}

}
