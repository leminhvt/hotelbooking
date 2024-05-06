package com.hotelbooking.service;

import java.util.List;

import com.hotelbooking.model.User;

public interface IUserService {
	User registerUser(User user);

	List<User> getUsers();

	void deleteUser(String email);

	User getUserByEmail(String email);
}
