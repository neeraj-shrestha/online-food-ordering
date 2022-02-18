package com.nepaliCravings.demo.service;

import java.util.List;

import com.nepaliCravings.demo.model.Reviews;

public interface ReviewsService {
	Reviews findById(long id);
	List<Reviews> findByRestaurantId(long id);
	List<Reviews> findByUserId(long id);
	
	void save(Reviews reviews);
	List<Reviews> findAll();
	Reviews findByrestidanduserid(long restaurant_id, long user_id);
	

}
