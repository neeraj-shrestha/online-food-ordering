package com.nepaliCravings.demo.service;

import java.util.List;

import com.nepaliCravings.demo.model.Subscription;

public interface SubscriptionService {
	
	void save(Subscription subscription);
	
	void update(Subscription subscription);
	
	Subscription findById(long id);
	
	
	List<Subscription> findByRestaurantId(long id);

}
