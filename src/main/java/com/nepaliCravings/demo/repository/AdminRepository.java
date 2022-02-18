package com.nepaliCravings.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nepaliCravings.demo.model.Admin;

@Repository
public interface AdminRepository extends JpaRepository <Admin, Long > {
	Admin findByEmail(String email);
	Admin findById(long id);
	Admin findByUserName(String userName);
}
