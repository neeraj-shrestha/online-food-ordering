package com.nepaliCravings.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nepaliCravings.demo.model.Reviews;
import com.nepaliCravings.demo.repository.ReviewsRepository;

@Service
public class ReviewsServiceImpl implements ReviewsService {
	
	@Autowired
	private ReviewsRepository reviewsRepository;

	@Override
	public Reviews findById(long id) {
		return reviewsRepository.findById(id);
	}

	@Override
	public List<Reviews> findByRestaurantId(long id) {
		return reviewsRepository.findByRestaurantId(id);
	}

	@Override
	public List<Reviews> findByUserId(long id) {
		return reviewsRepository.findByUserId(id);
	}

	@Override
	public void save(Reviews reviews) {
		reviewsRepository.save(reviews);
		
	}

	@Override
	public List<Reviews> findAll() {
		// TODO Auto-generated method stub
		return reviewsRepository.findAll();
	}

	@Override
	public Reviews findByrestidanduserid(long restaurant_id, long user_id) {
		// TODO Auto-generated method stub
		return reviewsRepository.findByrestidanduserid(restaurant_id, user_id);
	}

}
