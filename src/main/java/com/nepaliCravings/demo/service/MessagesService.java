package com.nepaliCravings.demo.service;

import java.util.List;

import com.nepaliCravings.demo.model.Messages;

public interface MessagesService {
	void save(Messages messages);
	List<Messages> findAll();
	Messages findByRestaurantId(long id);
	Messages findByUserId(long id);
	Messages findById(long id);
	void delete(Messages message);
}
