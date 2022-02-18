package com.nepaliCravings.demo.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.nepaliCravings.demo.dto.UserRegistrationDto;
import com.nepaliCravings.demo.model.User;

public interface UserService extends UserDetailsService {

	User findByEmail(String email);

	List<User> findAll();

	User findById(long id);

	void delete(User user);

	void userSave(@Valid UserRegistrationDto userDto);

	User findByUserName(String name);
	
	void savepassword(User user);
	
	void saveprofile(User user);

}
