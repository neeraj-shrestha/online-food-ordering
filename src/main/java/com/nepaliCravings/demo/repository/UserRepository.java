package com.nepaliCravings.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nepaliCravings.demo.model.User;

@Repository
public interface UserRepository extends JpaRepository <User, Long >{

	User findByEmail(String email);
	User findById(long id);
	User findByUserName(String userName);
}
