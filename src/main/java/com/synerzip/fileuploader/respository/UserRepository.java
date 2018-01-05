package com.synerzip.fileuploader.respository;

import org.springframework.stereotype.Repository;

import com.synerzip.fileuploader.domain.User;

@Repository
public class UserRepository {

	private static User userDb;

	public void createUser(User user) {
		userDb = user;
	}

	public User getUser() {
		return userDb;
	}

}
