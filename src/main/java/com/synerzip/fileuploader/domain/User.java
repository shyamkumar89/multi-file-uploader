package com.synerzip.fileuploader.domain;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class User {

	private String name;
	private int age;
	@JsonIgnore
	private List<MultipartFile> files;
	private List<String> fileNames;

}
