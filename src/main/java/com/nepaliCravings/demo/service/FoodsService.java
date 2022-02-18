package com.nepaliCravings.demo.service;

import java.util.List;

import com.nepaliCravings.demo.model.Foods;

public interface FoodsService {

	List<Foods> findByRestaurantId(long id);

	void save(Foods food);

	Foods findById(long id);

	void delete(Foods foods);
	
	void update(Foods food);
}
