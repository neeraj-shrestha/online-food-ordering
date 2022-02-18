package com.nepaliCravings.demo.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nepaliCravings.demo.dto.AdminRegistrationDto;
import com.nepaliCravings.demo.model.Admin;
import com.nepaliCravings.demo.model.Roles;
import com.nepaliCravings.demo.repository.AdminRepository;

@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	private AdminRepository adminRepository;
	
	
	private PasswordEncoder encrypt=new BCryptPasswordEncoder();
	

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		Admin admin = adminRepository.findByUserName(userName);
		
		if (admin == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		
		return new org.springframework.security.core.userdetails.User(admin.getEmail(), admin.getPassword(),
				mapRolesToAuthorities(admin.getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Roles> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override
	public Admin findByEmail(String email) {
		return adminRepository.findByEmail(email);
	}

	@Override
	public Admin findById(long id) {
		return adminRepository.findById(id);
	}

	@Override
	public void save(Admin admin) {
		adminRepository.save(admin);
	}

	@Override
	public void adminSave(@Valid AdminRegistrationDto adminDto) {
		Admin admin = new Admin();
		
		admin.setFirstName(adminDto.getFirstName());
		admin.setLastName(adminDto.getLastName());
		admin.setUserName(adminDto.getUserName());
		admin.setPhone(adminDto.getPhone());
		admin.setAddress(adminDto.getAddress());
		admin.setEmail(adminDto.getEmail());
		admin.setImage(adminDto.getImage());
		admin.setPassword(encrypt.encode(adminDto.getPassword()));
		admin.setRoles(Arrays.asList(new Roles("ROLE_ADMIN")));
		adminRepository.save(admin);
	}

	@Override
	public Admin findByUserName(String userName) {
		return adminRepository.findByUserName(userName);
	}

	@Override
	public void savepassword(Admin admin) {
		// TODO Auto-generated method stub
		admin.setPassword(encrypt.encode(admin.getPassword()));
		adminRepository.save(admin);
		
	}

	@Override
	public void saveprofile(Admin admin) {
		// TODO Auto-generated method stub
		adminRepository.save(admin);
		
	}

}
