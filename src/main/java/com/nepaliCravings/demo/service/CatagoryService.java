package com.nepaliCravings.demo.service;

import java.util.List;

import com.nepaliCravings.demo.model.Catagory;


public interface CatagoryService {
	
	List<Catagory> findByRestaurantId(long id);

}
