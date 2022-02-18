package com.nepaliCravings.demo.service;

import java.util.List;

import com.nepaliCravings.demo.model.SubCatagory;

public interface SubCatagoryService {

	void save(SubCatagory subCat);
	
	List<SubCatagory> findByRestaurantId(long id);

}
