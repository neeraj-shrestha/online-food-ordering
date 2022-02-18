package com.nepaliCravings.demo.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nepaliCravings.demo.dto.UserRegistrationDto;
import com.nepaliCravings.demo.model.Roles;
import com.nepaliCravings.demo.model.User;
import com.nepaliCravings.demo.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	

	private PasswordEncoder encrypt=new BCryptPasswordEncoder();

	
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userRepository.findByUserName(userName);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Roles> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public User findById(long id) {
		return userRepository.findById(id);
	}

	@Override
	public void delete(User user) {
		userRepository.delete(user);
	}
	public void userSave(UserRegistrationDto userDto) {
		User user = new User();
		
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setUserName(userDto.getUserName());
		user.setPhone(userDto.getPhone());
		user.setAddress(userDto.getAddress());
		user.setEmail(userDto.getEmail());
		user.setImage(userDto.getImage());
		user.setPassword(encrypt.encode(userDto.getPassword()));
		user.setRoles(Arrays.asList(new Roles("ROLE_USER")));
		userRepository.save(user);
		
	}
	public void savepassword(User user) {
		user.setPassword(encrypt.encode(user.getPassword()));
		userRepository.save(user);
	}
	@Override
	public User findByUserName(String name) {
		// TODO Auto-generated method stub
		return userRepository.findByUserName(name);
	}

	@Override
	public void saveprofile(User user) {
		// TODO Auto-generated method stub
		userRepository.save(user);
	}

	
	

}
