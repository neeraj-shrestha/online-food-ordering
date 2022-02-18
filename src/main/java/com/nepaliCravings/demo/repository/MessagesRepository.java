package com.nepaliCravings.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nepaliCravings.demo.model.Messages;

public interface MessagesRepository extends JpaRepository <Messages, Long > {
	
	Messages findByRestaurantId(long id);
	Messages findById(long id);
	void delete(Messages message);
	Messages findByUserId(long id);
}
