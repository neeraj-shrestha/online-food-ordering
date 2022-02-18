package com.nepaliCravings.demo.service;

import javax.validation.Valid;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.nepaliCravings.demo.dto.AdminRegistrationDto;
import com.nepaliCravings.demo.model.Admin;

public interface AdminService extends UserDetailsService {
	
	Admin findByUserName(String userName);

	Admin findByEmail(String email);

	Admin findById(long id);

	void save(Admin admin);

	void adminSave(@Valid AdminRegistrationDto adminDto);

	void savepassword(Admin admin);

	void saveprofile(Admin admin);

}
