package com.nepaliCravings.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nepaliCravings.demo.model.Subscribers;
import com.nepaliCravings.demo.repository.SubscribersRepository;

@Service
public class SubscribersServiceImpl implements SubscribersService{
	@Autowired
	private SubscribersRepository subscribersRepository;

	@Override
	public Subscribers findByUserId(long id) {
		// TODO Auto-generated method stub
		return subscribersRepository.findByUserId(id);
	}

	@Override
	public Subscribers findById(long id) {
		// TODO Auto-generated method stub
		return subscribersRepository.findById(id);
	}

	@Override
	public Subscribers findBySubscriptionId(long id) {
		// TODO Auto-generated method stub
		return subscribersRepository.findBySubscriptionId(id);
	}

	@Override
	public void save(Subscribers subscribers) {
		subscribersRepository.save(subscribers);
	}

	@Override
	public void delete(Subscribers subscribers) {
		subscribersRepository.delete(subscribers);
		
	}
	

}
