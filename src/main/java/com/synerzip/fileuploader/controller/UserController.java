package com.synerzip.fileuploader.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.synerzip.fileuploader.domain.User;
import com.synerzip.fileuploader.service.UserService;

@RestController
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping
	public ResponseEntity<?> createPerson(@ModelAttribute User user) throws IOException {
		return ResponseEntity.ok(userService.createUser(user));
	}

	@GetMapping
	public ResponseEntity<User> getUser() throws IOException {
		return ResponseEntity.ok(userService.getUser());
	}

}
