package com.nepaliCravings.demo.service;

import com.nepaliCravings.demo.model.Subscribers;

public interface SubscribersService {
	
	Subscribers findByUserId(long id);
	Subscribers findById(long id);
	Subscribers findBySubscriptionId(long id);
	void save(Subscribers subscribers);
	void delete(Subscribers subscribers);

}
