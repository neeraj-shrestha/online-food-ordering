package com.nepaliCravings.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nepaliCravings.demo.model.Subscription;
import com.nepaliCravings.demo.repository.SubscriptionRepository;

@Service
public class SubscriptionServiceImpl implements SubscriptionService{
	@Autowired
	private SubscriptionRepository subscriptionRepository;

	@Override
	public void save(Subscription subscription) {
		subscriptionRepository.save(subscription);
		
	}

	@Override
	public void update(Subscription subscription) {
		subscriptionRepository.save(subscription);
		
	}

	@Override
	public Subscription findById(long id) {
		
		return subscriptionRepository.findById(id);
	}

	

	@Override
	public List<Subscription> findByRestaurantId(long id) {
		return subscriptionRepository.findByRestaurantId(id);
	}

}
