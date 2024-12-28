package com.ecom.ecommerce_backend.service;

import com.ecom.ecommerce_backend.exception.UserException;
import com.ecom.ecommerce_backend.model.User;

public interface UserService {

	public User findUserById(Long userId) throws UserException;
	
	public User findUserProfileByJwt(String jwt) throws UserException;
}
